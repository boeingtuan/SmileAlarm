package yolo.bachkhoa.com.smilealarm.Model;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import yolo.bachkhoa.com.smilealarm.Entity.AlarmImageEntity;

public class AlarmImageModel extends Model{
	private static String REFERENCE_NAME = "AlarmImage";
    private DatabaseReference alarmRef;

    private AlarmImageModel alarmImageModel = new AlarmImageModel();
    private AlarmImageModel(){
        super();
    	this.alarmRef = AuthenticateModel.getInstance().getUserReference().child(REFERENCE_NAME);
        this.addMainCallback();
    }
    public AlarmImageModel getInstance(){
    	return alarmImageModel;
    }

    @Override
    public void onUserLogin() {
        alarmImageModel = new AlarmImageModel();
    }

    @Override
    protected void addMainCallback() {
        alarmRef.orderByKey().limitToLast(100).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final AlarmImageEntity alarmImageEntity = dataSnapshot.getValue(AlarmImageEntity.class);
                addObjectToMap(dataSnapshot.getKey(), alarmImageEntity);
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

    public void addCallback(final FirebaseCallback callback){
        alarmRef.orderByKey().limitToLast(100).addChildEventListener(new ChildEventListener() {
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

    public void insert(String url){
        AlarmImageEntity alarmImageEntity = new AlarmImageEntity();
        alarmImageEntity.setUrl(url);
        DatabaseReference alarmImage = alarmRef.push();
        alarmImage.setValue(alarmImageEntity);
    }
}