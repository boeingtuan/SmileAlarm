package yolo.bachkhoa.com.smilealarm.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yolo.bachkhoa.com.smilealarm.Entity.AlarmImageEntity;
import yolo.bachkhoa.com.smilealarm.Model.AlarmImageModel;
import yolo.bachkhoa.com.smilealarm.Model.EventHandle;
import yolo.bachkhoa.com.smilealarm.Model.FirebaseCallback;
import yolo.bachkhoa.com.smilealarm.Object.AlarmObject;
import yolo.bachkhoa.com.smilealarm.Object.AlarmReceiver;
import yolo.bachkhoa.com.smilealarm.Object.StoreData;
import yolo.bachkhoa.com.smilealarm.R;
import yolo.bachkhoa.com.smilealarm.Service.StorageService;
import yolo.bachkhoa.com.smilealarm.Service.UserService;

import static android.content.Context.ALARM_SERVICE;

public class TimeLineFragment extends Fragment {

    private TimelineItemAdapter adapter;
    AlarmImageModel alarmImageModel = AlarmImageModel.getInstance();

    public static TimeLineFragment newInstance() {
        TimeLineFragment fragment = new TimeLineFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        ListView alarm_list = (ListView) rootView.findViewById(R.id.alarm_list);
        adapter = new TimelineItemAdapter(alarmImageModel.id_list, alarmImageModel.entity_map, this.getContext());
        alarmImageModel.addCallback(new FirebaseCallback<String>() {
            @Override
            public void onInserted(String o) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onUpdated(String o) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleted(String o) {
                adapter.notifyDataSetChanged();
            }
        });
        alarm_list.setAdapter(adapter);
        return rootView;
    }

    public class TimelineItemAdapter extends BaseAdapter {

        List<String> id_list;
        HashMap<String, AlarmImageEntity> entity_map;
        Context context;

        public TimelineItemAdapter(List<String> id_list, HashMap<String, AlarmImageEntity> entity_map, Context context) {
            this.id_list = id_list;
            this.entity_map = entity_map;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (id_list != null)
                return id_list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return entity_map.get(id_list.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(R.layout.timeline_row, parent, false);
            ImageView userImage = (ImageView) row.findViewById(R.id.userImage);
            TextView username = (TextView) row.findViewById(R.id.username);
            final ImageView alarmImage = (ImageView) row.findViewById(R.id.alarmImage);
            TextView alarmText = (TextView) row.findViewById(R.id.alarmText);

            final AlarmImageEntity item = entity_map.get(id_list.get(position));
            username.setText(UserService.getUserDisplayName() + "\n" + id_list.get(position));
            if (item.getBitmap() == null) {
                Log.d("Load Image", item.getImageName() + "");
                StorageService.getImage(item.getImageName(), new EventHandle<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap o) {
                        Log.d("Test", "load complete");
                        alarmImage.setImageBitmap(o);
                        item.setBitmap(o);
                    }

                    @Override
                    public void onError(String o) {

                    }
                });
            }
            else {
                alarmImage.setImageBitmap(item.getBitmap());
            }
            alarmText.setText(item.getText());
            Picasso.with(context).load(UserService.getUserImageUrl()).into(userImage);
            return row;
        }
    }

}
