package yolo.bachkhoa.com.smilealarm.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yolo.bachkhoa.com.smilealarm.Object.AlarmObject;
import yolo.bachkhoa.com.smilealarm.Object.AlarmReceiver;
import yolo.bachkhoa.com.smilealarm.Object.StoreData;
import yolo.bachkhoa.com.smilealarm.R;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Tuân on 07/01/2017.
 */

public class AlarmFragment extends Fragment {

    private AlarmManager alarmManager;
    private AlarmItemAdapter adapter;
    private List<AlarmObject> alarmList;
    private StoreData storeData;
    private Map<Integer, PendingIntent> intentList;
    private Calendar myCalendar = Calendar.getInstance();
    TimePickerDialog.OnTimeSetListener setAlarm = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            AlarmObject item = new AlarmObject(selectedHour + "",
                    selectedMinute + "", false);
            alarmList.add(item);
            storeData.storeListAlarm(alarmList);
            adapter.notifyDataSetChanged();
            myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            myCalendar.set(Calendar.MINUTE, selectedMinute);
        }
    };

    public static AlarmFragment newInstance() {
        AlarmFragment fragment = new AlarmFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        ListView alarm_list = (ListView) rootView.findViewById(R.id.alarm_list);

        storeData = new StoreData(this.getContext());
        alarmList = storeData.getListAlarm();
        intentList = new HashMap();
        adapter = new AlarmItemAdapter(alarmList, this.getContext());
        alarm_list.setAdapter(adapter);

        FloatingActionButton addButton = (FloatingActionButton) rootView.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(AlarmFragment.this.getContext(), setAlarm, myCalendar.get(Calendar.HOUR_OF_DAY)
                        , myCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
                timePicker.show();
            }
        });
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        return rootView;
    }

    private class OnDeleteAlarmClickListener implements View.OnClickListener {
        int position;

        public OnDeleteAlarmClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            alarmList.remove(position);
            intentList.remove(position);
            AlarmFragment.this.adapter.notifyDataSetChanged();
            storeData.storeListAlarm(alarmList);
        }
    }

    private class OnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        LinearLayout layout;
        int position;

        public OnCheckedChangeListener(LinearLayout layout, int position) {
            this.layout = layout;
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                PendingIntent pendingIntent = intentList.get(position);
                alarmManager.cancel(pendingIntent);
                intentList.remove(position);

                layout.setBackgroundColor(Color.parseColor("#dee1e5"));
            } else {
                Intent myIntent = new Intent(AlarmFragment.this.getContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmFragment.this.getContext(), 0, myIntent, 0);
                intentList.put(position, pendingIntent);

                AlarmObject item = alarmList.get(position);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(item.getHour()));
                cal.set(Calendar.MINUTE, Integer.valueOf(item.getMinute()));
                alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pendingIntent);
                Log.d("YOLOLOG", "onCheckedChanged: " + cal.getTimeInMillis());

                layout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            AlarmFragment.this.alarmList.get(position).setOn(isChecked);
            storeData.storeListAlarm(alarmList);
            adapter.notifyDataSetChanged();
        }
    }

    public class AlarmItemAdapter extends BaseAdapter {

        List<AlarmObject> alarms;
        Context context;

        public AlarmItemAdapter(List<AlarmObject> alarms, Context context) {
            this.alarms = alarms;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (alarms != null)
                return alarms.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return alarms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(R.layout.alarm_row, parent, false);

            final TextView time = (TextView) row.findViewById(R.id.time);
            final ImageButton deleteAlarm = (ImageButton) row.findViewById(R.id.deleteAlarm);
            final Switch onOff = (Switch) row.findViewById(R.id.onOffAlarm);
            final LinearLayout itemLayout = (LinearLayout) row.findViewById(R.id.layout);

            AlarmObject item = alarms.get(position);
            time.setText(item.getHour() + ":" + item.getMinute());
            deleteAlarm.setOnClickListener(new OnDeleteAlarmClickListener(position));
            onOff.setChecked(item.isOn());
            onOff.setOnCheckedChangeListener(new OnCheckedChangeListener(itemLayout, position));

            return row;
        }
    }

}
