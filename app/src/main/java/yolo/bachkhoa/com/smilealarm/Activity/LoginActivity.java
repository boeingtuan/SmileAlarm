package yolo.bachkhoa.com.smilealarm.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import yolo.bachkhoa.com.smilealarm.R;
public class LoginActivity extends AppCompatActivity {

	CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
        	@Override
        	public void onSuccess(LoginResult loginResult) {
        		Log.d(TAG, "facebook:onSuccess:" + loginResult);
        		AuthenticateService.registerHandle(loginResult.getAccessToken().getToken(), new EventHandle(){
        			public void onSuccess(Object o){
        				Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
    				}
    		
    				public void onError(Object o){
    					new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(tittle)
                            .setMessage(message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                	
                                }
                            })
                            .show();
    				}
    			});
        	}
        	@Override
        	public void onCancel() {
        		Log.d(TAG, "facebook:onCancel");
        	}
        	@Override
        	public void onError(FacebookException error) {
        		Log.d(TAG, "facebook:onError", error);
        	}
        });
    }
}
