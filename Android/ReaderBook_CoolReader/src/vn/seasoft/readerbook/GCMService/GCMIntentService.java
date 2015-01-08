/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.seasoft.readerbook.GCMService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.json.JSONException;
import org.json.JSONObject;
import vn.seasoft.readerbook.MainActivity;
import vn.seasoft.readerbook.R;
import vn.seasoft.readerbook.Util.mSharedPreferences;
import vn.seasoft.readerbook.actInfoBook;
import vn.seasoft.readerbook.model.Book;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends IntentService {
    static final String TAG = "GCMIntentService";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        String messageReceived = "";
        String jsonBook = "";
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            messageReceived = extras.getString("message");
            jsonBook = extras.getString("book");
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " +
//                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                // Post notification of received message.
                if (messageReceived != null) {
                    if (!messageReceived.trim().equals("")) {
                        messageReceived = messageReceived.trim();
                        messageReceived = messageReceived.replace("{you}", "Bạn " + mSharedPreferences.getUserDisplay(getApplicationContext()));

                        Book book = null;
                        if (jsonBook != null) {
                            if (!jsonBook.equals("")) {
                                try {
                                    JSONObject getJsonBook = new JSONObject(jsonBook);
                                    JSONObject Obj = getJsonBook.getJSONObject("data");
                                    book = new Book();
                                    book.setIdbook(Obj.getInt("idbook"));
                                    book.setTitle(Obj.getString("title"));
                                    book.setAuthor(Obj.getString("author"));
                                    book.setSummary(Obj.getString("summary"));
                                    book.setIdcategory(Obj.getInt("idcategory"));
                                    book.setCountview(Obj.getInt("countview"));
                                    book.setCountdownload(Obj.getInt("countdownload"));
                                    if (!Obj.isNull("imagecover"))
                                        book.setImagecover(Obj.getString("imagecover").replace(" ", "%20"));
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    book = null;
                                }
                            }
                        }
                        sendNotification(messageReceived, book);
                    }
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, Book book) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        if (book == null) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, actInfoBook.class);
            intent.putExtra("idbook", book.getIdbook());
            intent.putExtra("titlebook", book.getTitle());
            intent.putExtra("authorbook", book.getAuthor());
            intent.putExtra("countview", book.getCountview());
            intent.putExtra("countdownload", book.getCountdownload());
            intent.putExtra("idcategory", book.getIdcategory());
            intent.putExtra("summary", book.getSummary());
            intent.putExtra("cover", book.getImagecover());
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Sách Của Tui")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
