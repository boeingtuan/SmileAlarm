package yolo.bachkhoa.com.smilealarm.Entity;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class UserEntity {
    private String Avatar;
    private String Name;
    private HashMap<String, AlarmImageEntity> AlarmImage;

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public HashMap<String, AlarmImageEntity> getAlarmImage() {
        return AlarmImage;
    }

    public void setAlarmImage(HashMap<String, AlarmImageEntity> AlarmImage) {
        this.AlarmImage = AlarmImage;
    }
}
