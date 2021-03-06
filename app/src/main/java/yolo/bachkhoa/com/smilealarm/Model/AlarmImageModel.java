package yolo.bachkhoa.com.smilealarm.Model;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import yolo.bachkhoa.com.smilealarm.Entity.AlarmImageEntity;
import yolo.bachkhoa.com.smilealarm.Fragment.AlarmFragment;
import yolo.bachkhoa.com.smilealarm.Service.StorageService;

public class AlarmImageModel extends Model<AlarmImageEntity>{
	private static String REFERENCE_NAME = "AlarmImage";
    private DatabaseReference alarmRef;
    public HashMap<String, Bitmap> images = new HashMap<>();

    private static AlarmImageModel alarmImageModel = new AlarmImageModel();
    private AlarmImageModel(){
        super();
    	this.alarmRef = AuthenticateModel.getInstance().getUserReference().child(REFERENCE_NAME);
        this.addMainCallback();
    }
    public static AlarmImageModel getInstance(){
    	return alarmImageModel;
    }

    @Override
    public void onUserLogin() {
        alarmImageModel = new AlarmImageModel();
    }

    @Override
    protected void addMainCallback() {
        alarmRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                final AlarmImageEntity alarmImageEntity = dataSnapshot.getValue(AlarmImageEntity.class);
                addObjectToMap(dataSnapshot.getKey(), alarmImageEntity);
                TimelineModel.addAlarm(dataSnapshot.getKey(), alarmImageEntity);
                for (FirebaseCallback<String> callback : callbackList) {
                    callback.onInserted(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                AlarmImageEntity alarmImageEntity = dataSnapshot.getValue(AlarmImageEntity.class);
                updateObjectInMap(dataSnapshot.getKey(), alarmImageEntity);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                deleteObjectInMap(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    List<FirebaseCallback<String>> callbackList = new ArrayList<>();

    public void addCallback(final FirebaseCallback<String> callback){
        callbackList.add(callback);
    }

    public void insert(final Date time, Bitmap image, final String text, final EventHandle<String> toast){
        final String name = FirebaseAuth.getInstance().getCurrentUser().getUid() + time.getTime();
        StorageService.saveImage(name, image, new EventHandle<String>() {
            @Override
            public void onSuccess(String o) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                DatabaseReference newImage = alarmRef.child(df.format(time));
                newImage.child("ImageName").setValue(name);
                newImage.child("Text").setValue(text);
                toast.onSuccess("You're successfully share you photo");
            }

            @Override
            public void onError(String o) {
                Log.d("SmileAlarm", o);
            }
        });
    }
}