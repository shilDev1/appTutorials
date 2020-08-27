package com.shilpimodi.socialsitelogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private TextView textInfo;
    private ImageView profile;
    private LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInfo = findViewById(R.id.info);
        profile = findViewById(R.id.profile);
        loginButton = findViewById(R.id.login);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                String userId = loginResult.getAccessToken().getUserId();
                textInfo.setText("User Id: " + userId);

                String imageUrl = "https://graph.facebook.com/" + userId + "/picture?type=normal";

                Picasso.get().load(imageUrl).into(profile);
            }

            @Override
            public void onCancel() {
                Log.d("FaceBook cancel", "In Facebook Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FaceBook Exception", error.toString());
            }
        });

        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
            }
            @Override
            public void onFailure() {
                // No access token could be retrieved for the user
            }
            @Override
            public void onError(Exception exception) {
                // An error occurred
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {

        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            // Remove the profile image if logged out
            if (currentAccessToken == null) {
                profile.setImageResource(0);
                textInfo.setText("Facebook Login Tutorial");
            }
        }
    };
}