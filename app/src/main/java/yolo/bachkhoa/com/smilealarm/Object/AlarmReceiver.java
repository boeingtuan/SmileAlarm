package yolo.bachkhoa.com.smilealarm.Object;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Tuân on 07/01/2017.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public void onReceive(final Context context, Intent intent) {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        try {
            mediaPlayer.setDataSource(context, alarmUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);

            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}