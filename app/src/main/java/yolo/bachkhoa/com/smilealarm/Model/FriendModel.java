package yolo.bachkhoa.com.smilealarm.Model;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import yolo.bachkhoa.com.smilealarm.Entity.UserEntity;

public class FriendModel extends Model<UserEntity>{
    private static String REFERENCE_NAME = "Friend";
    private DatabaseReference friendRef;

    private FriendModel friendModel = new FriendModel();
    private FriendModel(){
        super();
        this.friendRef = AuthenticateModel.getInstance().getUserReference().child(REFERENCE_NAME);
        this.addMainCallback();
    }
    public FriendModel getInstance(){
        return friendModel;
    }

    @Override
    public void onUserLogin() {
        friendModel = new FriendModel();
    }

    @Override
    protected void addMainCallback() {
        friendRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String friendToken = dataSnapshot.getKey();
                getFriendInfo(friendToken, new EventHandle<UserEntity>() {
                    @Override
                    public void onSuccess(UserEntity o) {
                        addObjectToMap(friendToken, o);
                    }

                    @Override
                    public void onError(String o) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Ignore
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
        friendRef.addChildEventListener(new ChildEventListener() {
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

    public void getFriendInfo(String token, EventHandle eventHandle){

    }
}
