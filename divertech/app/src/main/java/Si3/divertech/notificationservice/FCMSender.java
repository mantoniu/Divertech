package Si3.divertech.notificationservice;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class is responsible for sending notifications to the users using Firebase Cloud Messaging
 * It uses the OkHttp library to send HTTP requests to the FCM API
 */
public class FCMSender {
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/964760790476/messages:send";

    /**
     * Sends a notification to the user with the specified token
     * @param context The context of the application
     * @param token The token of the user to send the notification to
     * @param notification The content of the notification to send
     * @throws IOException If an error occurs while sending the notification
     */
    public void sendNotification(Context context, String token,String lang, NotificationContent notification) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Create the JSON object to send to the FCM API
        JSONObject json = new JSONObject();
        JSONObject dataJson = new JSONObject();
        JSONObject notificationJson = new JSONObject();
        JSONObject androidJson = new JSONObject();
        JSONObject channelJson = new JSONObject();

        // Fill the JSON object with the necessary data
        try {
            notificationJson.put("title", String.valueOf(notification.getType().ordinal()));
            notificationJson.put("body", (lang.equals("fr") || notification.getBodyTranslation().isEmpty())?notification.getBody():notification.getBodyTranslation());
            notificationJson.put("image", notification.getImageUrl());

            dataJson.put("token", token);
            dataJson.put("notification", notificationJson);

            channelJson.put("channel_id", notification.getChannel());
            androidJson.put("notification", channelJson);
            dataJson.put("android", androidJson);

            json.put("message", dataJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the access token from the OAuth class
        String accessToken = OAuth.getAccessToken(context);

        // Create the request to send to the FCM API
        RequestBody bodyRequest = RequestBody.create(
                json.toString(), MediaType.get("application/json; charset=utf-8"));


        // Send the request to the FCM API
        Request request = new Request.Builder()
                .url(FCM_URL)
                .post(bodyRequest)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json; UTF-8")
                .build();

        // Send the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            // Handle the response from the FCM API
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("FCM", "Notification sent successfully");
                } else {
                    Log.d("FCM", "Failed to send notification"+response.body().string());
                }
            }
        });
    }
}