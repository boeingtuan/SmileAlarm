package yolo.bachkhoa.com.smilealarm.Model;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

import yolo.bachkhoa.com.smilealarm.Entity.AlarmImageEntity;
import yolo.bachkhoa.com.smilealarm.Service.StorageService;

public class AlarmImageModel extends Model{
	private static String REFERENCE_NAME = "AlarmImage";
    private DatabaseReference alarmRef;

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
        alarmRef.orderByKey().limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                final AlarmImageEntity alarmImageEntity = new AlarmImageEntity();
                alarmImageEntity.setText(dataSnapshot.child("Text").getValue().toString());
                Log.d("SmileAlarm:onChildAdded", dataSnapshot.child("ImageName").getValue().toString());
                StorageService.getImage(dataSnapshot.child("ImageName").getValue().toString(), new EventHandle<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap o) {
                        alarmImageEntity.setImage(o);
                        addObjectToMap(dataSnapshot.getKey(), alarmImageEntity);
                    }

                    @Override
                    public void onError(String o) {

                    }
                });
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

    public void addCallback(final FirebaseCallback<String> callback){
        alarmRef.orderByKey().limitToLast(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                callback.onInserted(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                callback.onUpdated(dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                callback.onDeleted(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void insert(Date time, Bitmap image, final String text){
        final String name = FirebaseAuth.getInstance().getCurrentUser().getUid() + time.getTime();
        StorageService.saveImage(name, image, new EventHandle<String>() {
            @Override
            public void onSuccess(String o) {
                DatabaseReference newImage = alarmRef.push();
                newImage.child("FileName").setValue(name);
                newImage.child("Text").setValue(text);
            }

            @Override
            public void onError(String o) {
                Log.d("SmileAlarm", o);
            }
        });
    }
}