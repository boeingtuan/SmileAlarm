package yolo.bachkhoa.com.smilealarm.Model;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import yolo.bachkhoa.com.smilealarm.Service.UserService;

public class AuthenticateModel {
	private static String REFERENCE_NAME = "User";

	FirebaseAuth mAuth;
    DatabaseReference mUser;
    private static AuthenticateModel authenticateModel = new AuthenticateModel();

    private AuthenticateModel(){
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference(REFERENCE_NAME);
    }

    public static AuthenticateModel getInstance(){
        return authenticateModel;
    }

    public boolean checkUserLogin(){
        return mAuth.getCurrentUser() != null;
    }

    public DatabaseReference getUserReference(){
        return mUser.child(mAuth.getCurrentUser().getUid());
    }

    public void registerHandle(String token, final EventHandle eventHandle, Activity activity){
    	loginHandle(token, new EventHandle(){
    		public void onSuccess(final Object o){
                Log.d("SmileLogin", "Test");
                UserService.init();
                final DatabaseReference user = mUser.child(mAuth.getCurrentUser().getUid());
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       if (!dataSnapshot.hasChildren()) {
                            user.child("AlarmList").setValue(0);
                            user.child("AlarmImage").setValue(0);
                            user.child("Friend").setValue(0);
                            user.child("Info").child("Name").setValue(UserService.getUserDisplayName());
                            Uri imageUri = UserService.getUserImageUrl();
                            user.child("Info").child("Avatar").setValue(imageUri.toString());
                            GraphRequest graphMeRequest = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject jsonObject,
                                                GraphResponse response) {
                                            try {
                                                Log.d("abc", jsonObject.toString());
                                                user.child("Info").child("FacebookId").setValue(jsonObject.getString("id"));
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                            graphMeRequest.executeAsync();
                        }
                        eventHandle.onSuccess(o);
                        UserService.getFacebookToken(new EventHandle<String>() {
                            @Override
                            public void onSuccess(String o) {
                                Log.d("Test", o);
                            }

                            @Override
                            public void onError(String o) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    		}
    		
    		public void onError(String o){
    			eventHandle.onError(o);
    		}
    	}, activity);
    }

    public void loginHandle(String token, final EventHandle eventHandle, Activity activity){
    	AuthCredential credential = FacebookAuthProvider.getCredential(token);
    	mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        UserService.init();
                    	eventHandle.onSuccess("Register success");
                    }
                    else {
                    	eventHandle.onError("Authentication failed.");
                    }
                }
            });
    }

    public void logoutHandle(){
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }
}
