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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthConfig;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class WelcomePage extends AppCompatActivity {
    //private static final String TWITTER_KEY = "Ix4z1rPUdVTwEaoSgo60syGKo";
    //private static final String TWITTER_SECRET = "zBZA7Q7nS9XtGDrBsLwygDtlMNEZdshIn8PjJo4zuckuJDtT8m";
    private static final String TWITTER_KEY = "nNmX5YzdyJ9iYzhvoUWEFT4wb";
    private static final String TWITTER_SECRET = "KC6eLFyU6pAvgNwKh6mIViDZkeqMwI5eieISFH3mOg51FO9YCb";
    private BackgroundAnimator backgroundAnimator;
    private static DigitsSession digitsSession;
    private DigitsAuthConfig digitsAuthConfig;
    private static ParseUser user;
    private static Class nextClass;
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
        initializeDigits();
        EditText dummy = (EditText) findViewById(R.id.dummy_edit_text);
        dummy.setVisibility(View.GONE);
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
    private void initializeDigits()
    {
        DigitsAuthConfig.Builder temp = new DigitsAuthConfig.Builder();
        temp = temp.withThemeResId(R.style.CustomDigitsTheme);
        temp = temp.withAuthCallBack(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                digitsSession=session;
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("authToken", (session.getAuthToken()+"").substring(6, (session.getAuthToken()+"").indexOf(",secret=")));
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e==null) {
                            if(objects.size()==0) {
                                Log.d("query", "nothing found");
                                nextClass=SignUp.class;
                            }
                            else{
                                Log.d("query", "found");
                                user=objects.get(0);
                                nextClass=SharingPage.class;
                            }
                        }
                        else{
                            nextClass=SharingPage.class;
                            Log.d("query", "error");

                        }

                        startActivity(new Intent(WelcomePage.this, nextClass));
                    }
                });

                Log.i("Info", "Button Tapped, Selfie Joined");

            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });
        digitsAuthConfig = temp.build();
    }
    public static ParseUser getUser()
    {
        return user;
    }
    public void clickFunction(View view) {

        Digits.authenticate(digitsAuthConfig);
    }
    public static DigitsSession getDigitsSession()
    {
        return digitsSession;
    }
    @Override
    public void onBackPressed()
    {}
}
