package com.example.selfiemobile.selfie;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.twitter.sdk.android.core.AuthToken;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SignUp extends AppCompatActivity {
    ImageView available = (ImageView)findViewById(R.id.available);
    TextView availableUser = (TextView) findViewById(R.id.username_available);
    ImageView unavailable = (ImageView) findViewById(R.id.unavailable);
    TextView unavailableUser = (TextView) findViewById(R.id.username_unavailable);
    private ParseQuery<ParseUser> query;
    private TextWatcher usernameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            //TEJAS - HERE YOU NEED TO ADD A LITTLE IMAGE AND TEXT THAT CHANGE ACCORDING TO THE AVAILABILITY OF THE USERNAME AND IF THE EMALIL
            //IS VALID (CONTAINS AN @ SIGN, IS 6 CHARACTERS LONG AND HAS A .
            if(s.toString().length()<3) {
                Log.d("no error", "username DOES exist");
                unavailable.setVisibility(View.VISIBLE);
                unavailableUser.setVisibility(View.VISIBLE);
                available.setVisibility(View.GONE);
                availableUser.setVisibility(View.GONE);
                //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO AN UNAVAILABLE STATE
            }
            else{
                Log.d("searching", "searching for availability");
                unavailable.setVisibility(View.VISIBLE);
                unavailableUser.setVisibility(View.VISIBLE);
                available.setVisibility(View.GONE);
                availableUser.setVisibility(View.GONE);
                //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO A SEARCHING STATE
                if(query!=null) {
                    query.cancel();
                }
                    query = ParseUser.getQuery();
                    query.whereEqualTo("userId", s.toString());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> objects, ParseException e) {

                            if(e==null) {
                                if(objects.size()==0) {
                                    Log.d("error", "username does not exist3");
                                    available.setVisibility(View.VISIBLE);
                                    availableUser.setVisibility(View.VISIBLE);
                                    unavailableUser.setVisibility(View.GONE);
                                    unavailable.setVisibility(View.GONE);
                                    //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO AN AVAILABLE STATE
                                }
                                else{
                                    Log.d("no error", "username does exist");
                                    unavailable.setVisibility(View.VISIBLE);
                                    unavailableUser.setVisibility(View.VISIBLE);
                                    available.setVisibility(View.GONE);
                                    availableUser.setVisibility(View.GONE);
                                    //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO AN UNAVAILABLE STATE
                                }
                            }
                            else{
                                Log.d("error", "username does exist2");
                                unavailable.setVisibility(View.VISIBLE);
                                unavailableUser.setVisibility(View.VISIBLE);
                                available.setVisibility(View.GONE);
                                availableUser.setVisibility(View.GONE);
                                //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO AN UNAVAILABLE STATE
                            }
                        }
                });
            }

        }
    };
    //TEJAS HERE YOU HAVE TO CHANGE THE CONDITION CHECKS FOR EMAIL BECAUSE CURRENTLY IT IS THE SAME AS THE USERNAME
    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length()<3) {
                Log.d("no error", "username does exist");
            }
            else{
                Log.d("searching", "searching for availability");
                if(query!=null) {
                    query.cancel();
                }
                query = ParseUser.getQuery();
                query.whereEqualTo("userId", s.toString());
                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {

                        if(e==null) {
                            if(objects.size()==0) {
                                Log.d("error", "username does not exist3");
                            }
                            else{
                                Log.d("no error", "username does exist");
                            }
                        }
                        else{
                            Log.d("error", "username does exist2");
                        }
                    }
                });
            }

        }
    };
    private BackgroundAnimator backgroundAnimator;
    private static String username;
    private static String email;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        available.setVisibility(View.GONE);
        availableUser.setVisibility(View.GONE);
        unavailable.setVisibility(View.GONE);
        unavailableUser.setVisibility(View.GONE);
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
        username.addTextChangedListener(usernameTextWatcher);
    }
    public void secondClickFunction(View view) {
        username = ((EditText) findViewById(R.id.username_edit_text)).getText().toString();
        email = ((EditText) findViewById(R.id.email_edit_text)).getText().toString();
        DigitsSession session=WelcomePage.getDigitsSession();
        if(session!=null)
        {
            ParseUser user = new ParseUser();
            user.setUsername(session.getId()+"");
            String authToken = (session.getAuthToken()+"").substring(6, (session.getAuthToken()+"").indexOf(",secret="));
            String secret = (session.getAuthToken()+"").substring((session.getAuthToken()+"").indexOf("secret=")+7);
            user.setPassword(authToken);
            user.setEmail(email);

// other fields can be set just like with ParseObject
            user.put("phoneNumber", session.getPhoneNumber());
            user.put("authToken", authToken);
            user.put("authTokenSecret", secret);
            user.put("phone", session.getPhoneNumber().substring(session.getPhoneNumber().length()-7));
            user.put("userId", username);

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        startActivity(new Intent(SignUp.this, SharingPage.class));
                        Log.i("Username", username);
                        Log.i("Info", "Button Tapped, Selfie Joined");
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                    }
                }
            });
        }



    }
    public static String getUsername()
    {
        return WelcomePage.getUser().getString("userId");
    }
}
