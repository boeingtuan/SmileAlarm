package yolo.bachkhoa.com.smilealarm.Object;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuân on 22/11/2016.
 */

public class StoreData {

    private Context context;

    public StoreData(Context context) {
        this.context = context;
    }

    public void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public List<AlarmObject> getListAlarm() {
        ArrayList<String> listStringBeacon = getStringArrayPref(this.context, "yolo.bachkhoa.com.smilealarm");
        ArrayList<AlarmObject> listThing = new ArrayList<>();
        Gson gson = new Gson();
        for (String beaconItemString : listStringBeacon) {
            AlarmObject thingObject = gson.fromJson(beaconItemString, AlarmObject.class);
            listThing.add(thingObject);
        }

        return listThing;
    }

    public void storeListAlarm(List<AlarmObject> listThing) {
        ArrayList<String> listStringBeacon = new ArrayList();
        Gson gson = new Gson();
        for (AlarmObject thingObject : listThing) {
            String beaconItemString = gson.toJson(thingObject);
            listStringBeacon.add(beaconItemString);
        }
        setStringArrayPref(this.context, "yolo.bachkhoa.com.smilealarm", listStringBeacon);
    }

}
