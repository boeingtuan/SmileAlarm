package yolo.bachkhoa.com.smilealarm.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import yolo.bachkhoa.com.smilealarm.Entity.UserEntity;
import yolo.bachkhoa.com.smilealarm.Model.EventHandle;
import yolo.bachkhoa.com.smilealarm.Model.EventHandleWithKey;

public class UserService {

    private static FirebaseUser user;
    private static DatabaseReference userRef;
    public static void init(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
    }
    public static Uri getUserImageUrl(){
        return user.getPhotoUrl();
    }

    public static String getUserDisplayName(){
        return user.getDisplayName();
    }

    public static void getFacebookToken(final EventHandle<String> eventHandle){
        user.getToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                eventHandle.onSuccess(getTokenResult.getToken());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                eventHandle.onError(e.getMessage());
            }
        });
    }

    public static void getUser(String uid, final EventHandleWithKey<String, UserEntity> eventHandle){
        userRef.orderByChild("FacebookId").equalTo(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("User", dataSnapshot.toString());
                final UserEntity userEntity = dataSnapshot.getValue(UserEntity.class);
                eventHandle.onSuccess(dataSnapshot.getKey(), userEntity);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
