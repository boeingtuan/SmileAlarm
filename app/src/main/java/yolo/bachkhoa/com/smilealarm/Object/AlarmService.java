package yolo.bachkhoa.com.smilealarm.Object;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import yolo.bachkhoa.com.smilealarm.Activity.MainActivity;
import yolo.bachkhoa.com.smilealarm.Activity.TurnOffActivity;
import yolo.bachkhoa.com.smilealarm.R;

/**
 * Created by Tuân on 07/01/2017.
 */

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
        takePicture();
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(this.getApplicationContext(), TurnOffActivity.class);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(takePictureIntent);
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.drawable.alarmclock)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }


}