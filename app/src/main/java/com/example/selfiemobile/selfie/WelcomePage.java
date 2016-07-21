package com.example.selfiemobile.selfie;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class WelcomePage extends AppCompatActivity {

    private static final String TWITTER_KEY = "Ix4z1rPUdVTwEaoSgo60syGKo";
    private static final String TWITTER_SECRET = "zBZA7Q7nS9XtGDrBsLwygDtlMNEZdshIn8PjJo4zuckuJDtT8m";
    private BackgroundAnimator backgroundAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView welcomeMessage = (TextView) findViewById(R.id.selfie);
        Typeface hel = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
        Typeface helvetica = hel.DEFAULT_BOLD;
        welcomeMessage.setTypeface(helvetica);
        TextView selfieMessage = (TextView) findViewById(R.id.welcome_welcome);
        selfieMessage.setTypeface(helvetica);

    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundAnimator.cancel();
    }
    @Override
    protected void onResume(){
        super.onResume();
        App.setAppBackgroundAnimator(new BackgroundAnimator());
        backgroundAnimator=App.getAppBackgroundAnimator();
        initializeBackgroundAnimator();
    }
    private void initializeBackgroundAnimator()
    {
        backgroundAnimator.setActivity(this);
        backgroundAnimator.setLayout((RelativeLayout)(findViewById(R.id.welcome_page_layout)));
        backgroundAnimator.setButton((Button)(findViewById(R.id.welcome_button)));
        backgroundAnimator.initializeBackgroundColor();
        backgroundAnimator.schedule(backgroundAnimator.new updateBackgroundTask(), getResources().getInteger(R.integer.animation_transition_length),
                getResources().getInteger(R.integer.animation_stay_length));
    }

    public void clickFunction(View view) {
        DigitsAuthConfig.Builder temp = new DigitsAuthConfig.Builder();
        temp = temp.withThemeResId(R.style.CustomDigitsTheme);
        temp = temp.withAuthCallBack(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                startActivity(new Intent(WelcomePage.this, SignUp.class));
                Toast.makeText(getApplicationContext(), "Authentication Verified", Toast.LENGTH_LONG).show();
                Log.i("Info", "Button Tapped, Selfie Joined");
                finish();


            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });
        Digits.authenticate(temp.build());
    }
    @Override
    public void onBackPressed()
    {}
}
