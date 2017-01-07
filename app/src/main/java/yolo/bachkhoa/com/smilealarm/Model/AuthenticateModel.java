package yolo.bachkhoa.com.smilealarm.Model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    		public void onSuccess(Object o){
                Log.d("SmileLogin", "Test");
    			DatabaseReference user =  mUser.child(mAuth.getCurrentUser().getUid());
    			user.child("AlarmList").setValue(0);
    		    user.child("AlarmImage").setValue(0);
    		    user.child("Friend").setValue(0);
    		    user.child("Info").setValue(0);
    		    eventHandle.onSuccess(o);
    		}
    		
    		public void onError(Object o){
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
