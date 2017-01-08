package yolo.bachkhoa.com.smilealarm.Entity;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AlarmImageEntity {
    String ImageName;
    String Text;
    Bitmap bitmap;

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String ImageName) {
        this.ImageName = ImageName;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }

    @Exclude
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Exclude
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
