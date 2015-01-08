/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package GCMService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.GCM;
import Others.SSUtil;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Servlet that adds a new message to all registered devices.
 * <p>
 * This servlet is used just by the browser (i.e., not device).
 */
@SuppressWarnings("serial")
public class SendAllMessagesServlet extends BaseServlet {

	private static final int MULTICAST_SIZE = 1000;

	private Sender sender;

	private static final Executor threadPool = Executors.newFixedThreadPool(5);

	static final String MESSAGE_KEY = "message";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sender = newSender(config);
	}

	/**
	 * Creates the {@link Sender} based on the servlet settings.
	 */
	protected Sender newSender(ServletConfig config) {
		String key = (String) config.getServletContext().getAttribute(
				ApiKeyInitializer.ATTRIBUTE_ACCESS_KEY);
		return new Sender(key);
	}

	/**
	 * Processes the request to add a new message.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");

		String messages = req.getParameter("message");
		if (messages.length() < 10) {
			req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, "Vui lòng nhập nhiều hơn 10 ký tự !");
			getServletContext().getRequestDispatcher("/sendnotify.jsp").forward(
					req, resp);
			return;
		}
		
		int idbook = 0;
		try {
			idbook = Integer.parseInt(req.getParameter("idbook"));
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Book book = null;
		if (idbook != 0) {
			book = new Book().getById(idbook);
			messages = messages.replace("{tuasach}", "\"" + book.getTitle() + " - "
					+ book.getAuthor() + "\"");
		}else{
			if (messages.contains("{tuasach}")) {
				req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, "Vui lòng chọn sách để sử dụng {tuasach} !");
				getServletContext().getRequestDispatcher("/sendnotify.jsp").forward(
						req, resp);
				return;
			}
		}
		String jsonBook = "";
		if (book != null) {
			jsonBook = SSUtil.convertToJson(book);
		}

		Message message = new Message.Builder().addData(MESSAGE_KEY, messages)
				.addData("book", jsonBook).build();

		List<String> devices = new ArrayList<String>();
		try {
			devices = new GCM().getAllDevices();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status;
		if (devices.isEmpty()) {
			status = "Không có thiết bị nào sử dụng !";
		} else {
			// NOTE: check below is for demonstration purposes; a real
			// application
			// could always send a multicast, even for just one recipient
			if (devices.size() == 1) {
				// send a single message using plain post
				String registrationId = devices.get(0);

				Result result = sender.send(message, registrationId, 5);
				status = "Đã gửi thành công !! Nội Dung: "
						+ message.getData().get(MESSAGE_KEY);
				;
			} else {
				// send a multicast message using JSON
				// must split in chunks of 1000 devices (GCM limit)
				int total = devices.size();
				List<String> partialDevices = new ArrayList<String>(total);
				int counter = 0;
				int tasks = 0;
				for (String device : devices) {
					counter++;
					partialDevices.add(device);
					int partialSize = partialDevices.size();
					if (partialSize == MULTICAST_SIZE || counter == total) {
						asyncSend(partialDevices, message);
						partialDevices.clear();
						tasks++;
					}
				}
				status = "Đã gửi thành công tới " + total
						+ " người dùng !! Nội Dung: "
						+ message.getData().get(MESSAGE_KEY);
			}
		}

		req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, status.toString());
		getServletContext().getRequestDispatcher("/sendnotify.jsp").forward(
				req, resp);
	}

	private void asyncSend(List<String> partialDevices, Message message) {
		// make a copy
		final List<String> devices = new ArrayList<String>(partialDevices);
		threadPool.execute(new Runnable() {

			public void run() {
				MulticastResult multicastResult;
				try {
					multicastResult = sender.send(message, devices, 5);
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error posting messages", e);
					return;
				}
				List<Result> results = multicastResult.getResults();
				// analyze the results
				for (int i = 0; i < devices.size(); i++) {
					String regId = devices.get(i);
					Result result = results.get(i);
					String messageId = result.getMessageId();
					if (messageId != null) {
						logger.fine("Succesfully sent message to device: "
								+ regId + "; messageId = " + messageId);
						String canonicalRegId = result
								.getCanonicalRegistrationId();
						if (canonicalRegId != null) {
							// same device has more than on registration id:
							// update it
							logger.info("canonicalRegId " + canonicalRegId);
							new GCM(regId).updateGCM(canonicalRegId);
						}
					} else {
						String error = result.getErrorCodeName();
						if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
							// application has been removed from device -
							// unregister it
							logger.info("Unregistered device: " + regId);
							new GCM(regId).unRegisterGCM();
						} else {
							logger.severe("Error sending message to " + regId
									+ ": " + error);
						}
					}
				}
			}
		});
	}

}
