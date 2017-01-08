package yolo.bachkhoa.com.smilealarm.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import yolo.bachkhoa.com.smilealarm.Entity.AlarmImageEntity;
import yolo.bachkhoa.com.smilealarm.Entity.UserEntity;

/**
 * Created by Tuân on 08/01/2017.
 */

public class TimelineModel {
    public static HashMap<String, AlarmImageEntity> timeline = new HashMap<>();
    public static HashMap<String, UserEntity> alarmUser = new HashMap<>();
    public static List<String> keys = new ArrayList<>();
    static List<EventHandle<String>> eventHandle = new ArrayList<>();
    public static void addCallback(EventHandle<String> a){
        eventHandle.add(a);
    }
    public static void notify(String m){
        for(EventHandle e : eventHandle){
            e.onSuccess(m);
        }
    }

    public static void addAlarm(String key, AlarmImageEntity alarmImageEntity){
        if (!keys.contains(key)) {
            keys.add(key);
            timeline.put(key, alarmImageEntity);
            Collections.sort(keys);
            notify(key);
        }
    }

    public static void addAlarm(UserEntity userEntity) {
        if (userEntity.getAlarmImage() != null){
            for (String key : userEntity.getAlarmImage().keySet()) {
                if (!keys.contains(key)) {
                    keys.add(key);
                    alarmUser.put(key, userEntity);
                    timeline.put(key, userEntity.getAlarmImage().get(key));
                }
            }
            Collections.sort(keys);
            notify(userEntity.getName());
        }
    }

    static void sort(){
        Collections.sort(keys);
    }
}
