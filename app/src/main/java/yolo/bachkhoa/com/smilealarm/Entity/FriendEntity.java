package yolo.bachkhoa.com.smilealarm.Entity;

import android.media.Image;

import java.util.HashMap;

public class FriendEntity {
    Image profileImage;
    String name;
    HashMap<String, String> alarmImage;

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getAlarmImage() {
        return alarmImage;
    }

    public void setAlarmImage(HashMap<String, String> alarmImage) {
        this.alarmImage = alarmImage;
    }
}
