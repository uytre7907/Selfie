package com.example.selfiemobile.selfie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private int[] colors =    {R.color.selfieDarkBlue, R.color.selfieDarkGreen, R.color.selfieDarkPurple,
            R.color.selfieGreen, R.color.selfieMagenta, R.color.selfieOrange, R.color.selfiePurple,
            R.color.selfieRed, R.color.selfieYellow};
    private Timer timer;
    private int index;

    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_test_post_second);
        layout = (RelativeLayout)(findViewById(R.id.post_second_layout));
        initializeBackgroundColor();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView welcomeMessage = (TextView) findViewById(R.id.JoinMessage);
        Typeface helvetica = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
        welcomeMessage.setTypeface(helvetica);
        welcomeMessage.setTextColor(Color.WHITE);
        welcomeMessage.setText("Thanks. Want to tell anyone?");
        authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sariassong);
        MediaPlayer yes = MediaPlayer.create(getApplicationContext(), R.raw.broccoli);
        mediaPlayer.start(); yes.start();

        timer = new Timer();
        timer.schedule(new updateBackgroundTask(), getResources().getInteger(R.integer.animation_transition_length),
                getResources().getInteger(R.integer.animation_stay_length));
    }

    private void initializeBackgroundColor() {
        index = SignUp.getColorIndex();
        layout.setBackgroundResource(colors[index]);
    }
    private void setBackgroundColor()
    {
        int newIndex;
        do
        {
            newIndex = SelfieTestSecondScreen.randInt(0, colors.length-1);
        }while (newIndex==index);
        int colorFrom = ContextCompat.getColor(getApplicationContext(), colors[index]);
        int colorTo = ContextCompat.getColor(getApplicationContext(), colors[newIndex]);

        Log.i("status ", "changing from "+colorFrom+" to "+colorTo);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(getResources().getInteger(R.integer.animation_transition_length)); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                layout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
        index = newIndex;
    }
    public void facebook(View view)
    {

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("joinselfie.com"))
                .setContentDescription("Add me at " + SignUp.getUsername() + " on Selfie!")
                .setContentTitle("Download Selfie today!")
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }
    public void twitter(View view)
    {
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Add me at " + SignUp.getUsername() + " on Selfie! #selfie #getconnected");
        builder.show();
    }
    class updateBackgroundTask extends TimerTask
    {
        public void run()
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setBackgroundColor();
                }
            });
        }
    }

}
