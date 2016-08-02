package com.example.selfiemobile.selfie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.SilentLogger;

public class SharingPage extends AppCompatActivity {

    private TwitterAuthConfig authConfig;
    private BackgroundAnimator backgroundAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView welcomeMessage = (TextView) findViewById(R.id.thank_message);
        Typeface helvetica = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
        welcomeMessage.setTypeface(helvetica);
        welcomeMessage.setTextColor(Color.WHITE);
        authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");

    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundAnimator.cancel();
    }
    @Override
    protected void onResume(){
        super.onResume();
        App.setAppBackgroundAnimator(new BackgroundAnimator(true));
        backgroundAnimator=App.getAppBackgroundAnimator();
        initializeBackgroundAnimator();
    }
    private void initializeBackgroundAnimator()
    {
        backgroundAnimator.setActivity(this);
        backgroundAnimator.setLayout((RelativeLayout)(findViewById(R.id.sharing_page_layout)));
        backgroundAnimator.setButton(null);
        backgroundAnimator.initializeBackgroundColor();
        backgroundAnimator.schedule(backgroundAnimator.new updateBackgroundTask(), getResources().getInteger(R.integer.animation_transition_length),
                getResources().getInteger(R.integer.animation_stay_length));
    }

    public void facebook(View view) {
        //TEJAS WE NEED TO FIX THIS

        Log.d("Facebook", "reached1");
        if (WelcomePage.getUser() != null) {
            Log.d("Facebook", "reached");
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("joinselfie.com"))
                    .setContentDescription("Add me at " + SignUp.getUsername() + " on Selfie!")
                    .setContentTitle("Download Selfie today!")
                    .build();
            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }
        else{

            Log.d("Facebook", "reached2");
            Toast t=Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT);
            t.show();
        }
    }
    public void twitter(View view) {
        if(WelcomePage.getUser()!=null) {
            Log.d("Twitter", "reached");
            Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
            TweetComposer.Builder builder = new TweetComposer.Builder(this).text("Add me at " + SignUp.getUsername() + " on Selfie! #selfie #getconnected");
            builder.show();
        }
        else{
            Toast t=Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT);
            t.show();
        }
    }
}
