package com.example.fhir_device;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "FHIR_DEVICE_CHANNEL";
    private final int NOTIFICATION_ID = 0;
    private Context context;
    private NotificationManager manager;

    public NotificationHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "FHIR-Device Notification",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.BLUE);
        channel.setDescription("Notifications from FHIR-Device application.");
        this.manager.createNotificationChannel(channel);
    }

    public void send(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("FHIR-Device Notification")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification);

        this.manager.notify(NOTIFICATION_ID, builder.build());
    }
}
