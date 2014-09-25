package httpservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.FeedBack;

/**
 * Servlet implementation class TableListFeedBack
 */
@WebServlet("/TableListFeedBack")
public class TableListFeedBack extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TableListFeedBack() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		try {
			PrintWriter out = response.getWriter();
			FeedBack feedbackquery = new FeedBack();
			ArrayList<FeedBack> lstfeedbacks = new ArrayList<FeedBack>();
			lstfeedbacks = feedbackquery.getAllFeedback();
			int i = 1;
			for (FeedBack feedback : lstfeedbacks) {
//				if (i % 2 == 0) {
//					out.println("<tr class=\"alternate-row\">");
//				} else {
//					
//				}
				out.println("<tr>");
				out.println("<td>" + feedback.getIdfeedback() + "</td>");
				out.println("<td>" + feedback.getTitlebook() + "</td>");
				out.println("<td>" + feedback.getAuthorbook() + "</td>");
				out.println("<td>" + feedback.getFeedback() + "</td>");
				out.println("<td  class=\"options-width\">");

				out.println("<a title='Đã Xử Lý' class=\"icon-5 info-tooltip\" href=\"ApproveFeedBack?idfeedback="
						+ feedback.getIdfeedback() + "\"></a>");
				out.println("</td></tr>");
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
