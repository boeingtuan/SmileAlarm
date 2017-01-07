package yolo.bachkhoa.com.smilealarm.Object;

/**
 * Created by Tuân on 07/01/2017.
 */

public class AlarmObject {
    private String hour;
    private String minute;
    private boolean isOn;

    public AlarmObject(String hour, String minute, boolean isOn) {
        this.hour = hour.length() < 2 ? "0" + hour : hour;
        this.minute = minute.length() < 2 ? "0" + minute : minute;
        this.isOn = isOn;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
