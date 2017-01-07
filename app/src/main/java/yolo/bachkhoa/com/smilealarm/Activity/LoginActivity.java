package yolo.bachkhoa.com.smilealarm.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;

import yolo.bachkhoa.com.smilealarm.R;
import yolo.bachkhoa.com.smilealarm.Model.AuthenticateModel;
import yolo.bachkhoa.com.smilealarm.Model.EventHandle;
import yolo.bachkhoa.com.smilealarm.Service.UserService;

public class LoginActivity extends AppCompatActivity {
    public static String[] FACEBOOK_PERMISSIONS = {
        "public_profile",
        "email",
        "user_friends",
        "user_photos"
    };

	CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        if (AuthenticateModel.getInstance().checkUserLogin()){
            UserService.init();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions(FACEBOOK_PERMISSIONS);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("SmileLogin", "facebook:onSuccess:" + loginResult);
                AuthenticateModel.getInstance().registerHandle(loginResult.getAccessToken().getToken(), new EventHandle() {
                    @Override
                    public void onSuccess(Object o) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(String o) {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Error")
                                .setMessage((String) o)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                }, LoginActivity.this);
            }

            @Override
            public void onCancel() {
                Log.d("SmileLogin", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("SmileLogin", "facebook:onError", error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
