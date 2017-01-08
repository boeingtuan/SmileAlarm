package yolo.bachkhoa.com.smilealarm.Entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class AlarmImageEntity {
    String ImageName;
    String Text;

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
}
