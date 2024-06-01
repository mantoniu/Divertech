package Si3.divertech.notificationservice;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

public class NotificationChannel {

    private NotificationChannel() {
    }

    public static final String CHANNEL_INFO = "0";
    public static final String CHANNEL_WARNING = "1";

    public static void createNotificationChannel(Context context, String channelId, String channelName, String channelDescription, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
