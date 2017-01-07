package yolo.bachkhoa.com.smilealarm.Service;

/**
 * Created by Tuân on 07/01/2017.
 */

public class AuthenticateService {
	private static String REFERENCE_NAME = "User";

	FirebaseAuth mAuth;
    DatabaseReference mUser;
    private static AuthenticateService authenticateService = new AuthenticateService();

    private AuthenticateService(){
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference(REFERENCE_NAME);
    }

    public static AuthenticateService getInstance(){
        return authenticateService;
    }

    public boolean checkUserLogin(){
        return mAuth.getCurrentUser() != null;
    }

    public void registerHandle(String token, EventHandle eventHandle){
    	loginHandle(token, new EventHandle(){
    		public void onSuccess(Object o){
    			DatabaseReference user =  mUser.child(mAuth.getCurrentUser().getUid());
    			user.child("AlarmList").save(0);
    		    user.child("AlarmImage").save(0);
    		    user.child("Friend").save(0);
    		    user.child("Info").save(0);
    		    eventHandle.onSuccess(o);
    		}
    		
    		public void onError(Object o){
    			eventHandle.onError(o);
    		}
    	});
    }

    public boolean loginHandle(String token, EventHandle eventHandle){
    	AuthCredential credential = FacebookAuthProvider.getCredential(token);
    	mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                    	eventHandle.onSuccess("Register success");
                    }
                    else {
                    	eventHandle.onError("Authentication failed.")
                    }
                }
            });
    }

    public void logoutHandle(){
    	mAuth.signOut();
    }
}
