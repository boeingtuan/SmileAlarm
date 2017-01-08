package yolo.bachkhoa.com.smilealarm.Model;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import yolo.bachkhoa.com.smilealarm.Entity.UserEntity;
import yolo.bachkhoa.com.smilealarm.Service.UserService;

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
    List<FirebaseCallback<UserEntity>> firebaseCallbacks = new ArrayList<>();

    @Override
    protected void addMainCallback() {
        // get friend list
        id_list = new ArrayList<>();
        friendRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                id_list.add(dataSnapshot.getKey());
//                UserService.getUser(dataSnapshot.getKey(), new EventHandleWithKey<String, UserEntity>() {
//                    @Override
//                    public void onSuccess(String key, UserEntity o) {
//                        entity_map.put(key, o);
//                        for (FirebaseCallback firebaseCallback : firebaseCallbacks) {
//                            firebaseCallback.onInserted(o);
//                        }
//                    }
//
//                    @Override
//                    public void onError(String o) {
//                        Log.d("SmileFriend", o);
//                    }
//                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //ignore
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                id_list.remove(dataSnapshot.getKey());
                entity_map.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addNewCallback(FirebaseCallback<UserEntity> firebaseCallback){
        for(UserEntity u : entity_map.values()){
            firebaseCallback.onInserted(u);
        }
        firebaseCallbacks.add(firebaseCallback);
    }

    public void insertNewFriend(String uid){
        friendRef.child(uid).setValue(true);
    }
}
