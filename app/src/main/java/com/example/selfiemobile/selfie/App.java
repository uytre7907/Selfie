package com.example.selfiemobile.selfie;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;

/**
 * Created by uytre_000 on 7/21/2016.
 */
public class App extends Application {


    private static BackgroundAnimator backgroundAnimator=new BackgroundAnimator(true);
        @Override public void onCreate() {
            super.onCreate();

            FacebookSdk.sdkInitialize(getApplicationContext());

            AppEventsLogger.activateApp(this);
            String ApplicationID = "uQUTn8iMqVdNNyB68CpIRQhfXuDqhs1JsaNsjz1l";
            String ClientKey = "6TvHb8jGsjxaUhnhJvlxBe86icSLLd45HuIK3yzX";
            Parse.initialize(this, ApplicationID, ClientKey); // Your Application ID and Client Key are defined elsewher
        }
    public static BackgroundAnimator getAppBackgroundAnimator()
    {
        return backgroundAnimator;
    }
    public static void setAppBackgroundAnimator(BackgroundAnimator b)
    {
        backgroundAnimator=b;
    }
}