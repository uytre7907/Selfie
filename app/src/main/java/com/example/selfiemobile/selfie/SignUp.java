package com.example.selfiemobile.selfie;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
    private ImageView availabilityImage;
    private TextView availabilityText;
    private ParseQuery<ParseUser> query;
    private BackgroundAnimator backgroundAnimator;
    private static String username;
    private static String email;
    private boolean usernameAvailable;
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
                usernameAvailable=false;
                availabilityText.setText("Username Unavailable");
                availabilityImage.setImageResource(R.drawable.unavailable);
                backgroundAnimator.deactivateButton();
                //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO AN UNAVAILABLE STATE
            }
            else{
                Log.d("searching", "searching for availability");
                usernameAvailable=false;
                availabilityText.setText("Searching");
                availabilityImage.setImageResource(R.drawable.unavailable);
                backgroundAnimator.deactivateButton();
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
                                    usernameAvailable=true;
                                    availabilityText.setText("Username Available");
                                    availabilityImage.setImageResource(R.drawable.available);
                                    backgroundAnimator.activateButton();
                                    //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO AN AVAILABLE STATE
                                }
                                else{
                                    Log.d("no error", "username does exist");
                                    usernameAvailable=false;
                                    availabilityText.setText("Username Unavailable");
                                    availabilityImage.setImageResource(R.drawable.unavailable);
                                    backgroundAnimator.deactivateButton();
                                    //HERE YOU MUST CHANGE THE IMAGE AND TEXT TO AN UNAVAILABLE STATE
                                }
                            }
                            else{
                                Log.d("error", "username does exist2");
                                usernameAvailable=false;
                                availabilityText.setText("Username Unavailable");
                                availabilityImage.setImageResource(R.drawable.unavailable);
                                backgroundAnimator.deactivateButton();
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        availabilityImage = (ImageView)(findViewById(R.id.image_availability_view));
        availabilityText = (TextView)(findViewById(R.id.username_availability_view));
        final RelativeLayout contentView = (RelativeLayout)(findViewById(R.id.sign_up_layout));

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }
                else {
                    // keyboard is closed
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
        });


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
        App.setAppBackgroundAnimator(new BackgroundAnimator(false));
        backgroundAnimator=App.getAppBackgroundAnimator();
        initializeBackgroundAnimator();
        backgroundAnimator.deactivateButton();
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
            user.put("platform", "android");
            WelcomePage.setUser(user);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null&&usernameAvailable) {
                        startActivity(new Intent(SignUp.this, SharingPage.class));
                        Log.i("Username", username);
                        Log.i("Info", "Button Tapped, Selfie Joined");
                        finish();
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Log.i("Failure", "account exists");
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
