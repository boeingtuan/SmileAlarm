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
import android.graphics.drawable.BitmapDrawable;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import yolo.bachkhoa.com.smilealarm.Entity.AlarmImageEntity;
import yolo.bachkhoa.com.smilealarm.Entity.UserEntity;
import yolo.bachkhoa.com.smilealarm.Model.AlarmImageModel;
import yolo.bachkhoa.com.smilealarm.Model.EventHandle;
import yolo.bachkhoa.com.smilealarm.Model.FirebaseCallback;
import yolo.bachkhoa.com.smilealarm.Model.TimelineModel;
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
        View rootView = inflater.inflate(R.layout.fragment_time_line, container, false);
        ListView alarm_list = (ListView) rootView.findViewById(R.id.timeline_list);
//        adapter = new TimelineItemAdapter(alarmImageModel.id_list, alarmImageModel.entity_map, this.getContext());
//        alarmImageModel.addCallback(new FirebaseCallback<String>() {
//            @Override
//            public void onInserted(String o) {
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onUpdated(String o) {
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onDeleted(String o) {
//                adapter.notifyDataSetChanged();
//            }
//        });
        adapter = new TimelineItemAdapter(TimelineModel.keys, TimelineModel.timeline, TimelineModel.alarmUser, this.getContext());
        TimelineModel.addCallback(new EventHandle<String>() {
            @Override
            public void onSuccess(String o) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String o) {

            }
        });
        alarm_list.setAdapter(adapter);
        return rootView;
    }

    public class TimelineItemAdapter extends BaseAdapter {

        List<String> id_list;
        HashMap<String, AlarmImageEntity> entity_map;
        HashMap<String, UserEntity> users;
        Context context;

        public TimelineItemAdapter(List<String> id_list, HashMap<String, AlarmImageEntity> entity_map, HashMap<String, UserEntity> users, Context context) {
            this.id_list = id_list;
            this.entity_map = entity_map;
            this.users = users;
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
            final ImageView userImage = (ImageView) row.findViewById(R.id.user_image);
            TextView username = (TextView) row.findViewById(R.id.nameUser);
            TextView time = (TextView) row.findViewById(R.id.time);
            final ImageView alarmImage = (ImageView) row.findViewById(R.id.alarmImage);

            AlarmImageEntity item = entity_map.get(id_list.get(position));
            UserEntity user = users.get(id_list.get(position));
            try {
                if (user != null) {
                    username.setText(user.getName());
                    Picasso.with(context).load(user.getAvatar()).into(userImage);
                }
                else {
                    username.setText(UserService.getUserDisplayName());
                    Picasso.with(context).load(UserService.getUserImageUrl().toString()).into(userImage);
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
                Date date = format.parse(id_list.get(position));
                format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                time.setText(format.format(date));

                if (alarmImageModel.images.get(id_list.get(position)) == null) {
                    Log.d("Load Image", item.getImageName() + "");
                    StorageService.getImage(item.getImageName(), new EventHandle<Bitmap>() {
                        @Override
                        public void onSuccess(Bitmap o) {
                            Log.d("Test", "load complete");
                            alarmImage.setImageBitmap(o);
                            alarmImageModel.images.put(id_list.get(position), o);
                        }

                        @Override
                        public void onError(String o) {

                        }
                    });
//                    final String imageUrlName = UserService.getUserImageUrl().toString().replaceAll("\\.", "_");
//                    Log.d("Image", imageUrlName);
//                    Log.d("Image", UserService.avatarMap.values() + "");
//                    if (UserService.avatarMap.get(imageUrlName) == null) {
//                        Log.d("Image", "chua load :( h load ne");
//                        Picasso.with(context).load(UserService.getUserImageUrl()).into(userImage, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                                Log.d("Image", "load xong roi");
//                                Bitmap bmap = ((BitmapDrawable)userImage.getDrawable()).getBitmap();
//                                UserService.avatarMap.put(imageUrlName, bmap);
//                                Log.d("Image", "luu xong roi");
//                            }
//
//                            @Override
//                            public void onError() {
//
//                            }
//                        });
//                    }
//                    else{
//                        Log.d("Image", "Co load truoc roi ne");
//                        userImage.setImageBitmap(UserService.avatarMap.get(imageUrlName));
//                    }
                }
                else {
                    alarmImage.setImageBitmap(alarmImageModel.images.get(id_list.get(position)));
                }
            } catch (Exception e){
                Log.d("SmileTimeline", "Can't load image " + e.getMessage());
            }
            return row;
        }
    }

}
