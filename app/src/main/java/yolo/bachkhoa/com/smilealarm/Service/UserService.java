package yolo.bachkhoa.com.smilealarm.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import yolo.bachkhoa.com.smilealarm.Entity.UserEntity;
import yolo.bachkhoa.com.smilealarm.Model.EventHandle;

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

    public static void getUserList(List<String> friendUids, final EventHandle<UserEntity> eventHandle){
        for (String uid: friendUids) {
            userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final UserEntity userEntity = new UserEntity();
                    userEntity.setName(dataSnapshot.child("Info").child("Username").getValue().toString());
                    String avatarUrl = dataSnapshot.child("Info").child("Avatar").getValue().toString();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(avatarUrl).getContent());
                        userEntity.setAvatar(bitmap);
                    } catch (Exception e){
                        eventHandle.onError("Can't load image");
                    }
                    Iterator<DataSnapshot> alarmImages = dataSnapshot.child("AlarmImage").getChildren().iterator();
                    HashMap<String, String> userAlarm = new HashMap<>();
                    while(alarmImages.hasNext()){
                        DataSnapshot alarmImage = alarmImages.next();
                        userAlarm.put(alarmImage.getKey(), alarmImage.getValue().toString());
                    }
                    userEntity.setAlarmImage(userAlarm);
                    eventHandle.onSuccess(userEntity);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    eventHandle.onError(null);
                }
            });
        }
    }
}
