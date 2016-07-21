package com.example.selfiemobile.selfie;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
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

import java.util.Timer;
import java.util.TimerTask;


public class SignUp extends AppCompatActivity {
    private int[] buttonColors =    {R.color.selfieButtonDarkBlue, R.color.selfieButtonDarkGreen, R.color.selfieButtonDarkPurple,
            R.color.selfieButtonGreen, R.color.selfieButtonMagenta, R.color.selfieButtonOrange, R.color.selfieButtonPurple,
            R.color.selfieButtonRed, R.color.selfieButtonYellow};
    private int[] colors =    {R.color.selfieDarkBlue, R.color.selfieDarkGreen, R.color.selfieDarkPurple,
            R.color.selfieGreen, R.color.selfieMagenta, R.color.selfieOrange, R.color.selfiePurple,
            R.color.selfieRed, R.color.selfieYellow};
    private BackgroundAnimator backgroundAnimator;
    public static String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initializeText();
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
        backgroundAnimator.setLayout((RelativeLayout)(findViewById(R.id.sign_up_layout)));
        backgroundAnimator.setButton((Button)(findViewById(R.id.sign_up_button)));
        backgroundAnimator.initializeBackgroundColor();
        backgroundAnimator.schedule(backgroundAnimator.new updateBackgroundTask(), getResources().getInteger(R.integer.animation_transition_length),
                getResources().getInteger(R.integer.animation_stay_length));
    }
    private void initializeText()
    {
        TextView welcomeMessage = (TextView) findViewById(R.id.see_you_soon);
        TextView selfieName = (TextView) findViewById(R.id.selfie_name);
        EditText email = (EditText) findViewById(R.id.email_edit_text);
        EditText username = (EditText) findViewById(R.id.username_edit_text);
        Typeface helvetica = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
        welcomeMessage.setTypeface(helvetica);
        email.setTypeface(helvetica);
        username.setTypeface(helvetica);
        selfieName.setTypeface(helvetica);
    }
    public void secondClickFunction(View view) {
        EditText user = (EditText) findViewById(R.id.username_edit_text);
        EditText email = (EditText) findViewById(R.id.email_edit_text);
        int count = 0;
        for(int i = 0; i<email.length(); i++)
        {
            if(email.getText().toString().charAt(i)=='@')
            {
                count++;
            }
        }
        if (user.length()>16||user.length()<3 && count!=1)
        {
            Toast.makeText(getApplicationContext(), "Neither the usename nor email are eligible!! Try again.", Toast.LENGTH_LONG).show();
        }
        else if(count!=1)
        {
            Toast.makeText(getApplicationContext(), "That email is not real!! Try another one.", Toast.LENGTH_LONG).show();

        }
        else if(user.length()>16||user.length()<3)
        {
            Toast.makeText(getApplicationContext(), "That username is not eligible!! Try another one within the character limit.", Toast.LENGTH_LONG).show();
        }

        else
        {
            startActivity(new Intent(SignUp.this, SharingPage.class));

            username = user.getText().toString();
            Log.i("Username", username);
            Log.i("Info", "Button Tapped, Selfie Joined");
        }
    }
    public static String getUsername()
    {
        return username;
    }
}
