package yolo.bachkhoa.com.smilealarm.Entity;

import android.graphics.Bitmap;

public class AlarmImageEntity {
    Bitmap image;
    String text;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
