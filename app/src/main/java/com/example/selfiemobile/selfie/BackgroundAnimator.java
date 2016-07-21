package com.example.selfiemobile.selfie;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.selfiemobile.selfie.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by uytre_000 on 7/21/2016.
 */
public class BackgroundAnimator extends Timer
{
    private Activity activity;
    private RelativeLayout layout;
    private Button button;
    private static int[] buttonColors =    {R.color.selfieButtonDarkBlue, R.color.selfieButtonDarkGreen, R.color.selfieButtonDarkPurple,
            R.color.selfieButtonGreen, R.color.selfieButtonMagenta, R.color.selfieButtonOrange, R.color.selfieButtonPurple,
            R.color.selfieButtonRed, R.color.selfieButtonYellow};
    private static int[] colors =    {R.color.selfieDarkBlue, R.color.selfieDarkGreen, R.color.selfieDarkPurple,
            R.color.selfieGreen, R.color.selfieMagenta, R.color.selfieOrange, R.color.selfiePurple,
            R.color.selfieRed, R.color.selfieYellow};

    private static int index=randInt(0, colors.length-1);
    public BackgroundAnimator(Activity a, RelativeLayout l, Button b)
    {
        activity=a;

        layout=l;
        button=b;
        initializeBackgroundColor();
    }
    public BackgroundAnimator()
    {

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }


    public static int randInt(int low, int high)
    {
        return (int)(Math.random()*(high-low+1)+low);
    }
    public void setBackgroundColor()
    {
        int newIndex = randInt(0, colors.length-1);
        int colorFrom = ContextCompat.getColor(activity.getApplicationContext(), colors[index]);
        int colorTo = ContextCompat.getColor(activity.getApplicationContext(), colors[newIndex]);
        int buttonColorFrom = ContextCompat.getColor(activity.getApplicationContext(), buttonColors[index]);
        int buttonColorTo = ContextCompat.getColor(activity.getApplicationContext(), buttonColors[newIndex]);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(activity.getResources().getInteger(R.integer.animation_transition_length)); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                layout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        ValueAnimator buttonColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), buttonColorFrom, buttonColorTo);
        buttonColorAnimation.setDuration(activity.getResources().getInteger(R.integer.animation_transition_length)); // milliseconds
        buttonColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (button != null) {
                    GradientDrawable buttonDrawable = (GradientDrawable) ((DrawableContainer.DrawableContainerState) ((StateListDrawable)
                            (button.getBackground())).getConstantState()).getChildren()[0];
                    buttonDrawable.setColor((int) animator.getAnimatedValue());
                    buttonDrawable.setStroke(1, (int) animator.getAnimatedValue());
                }
            }
        });
        buttonColorAnimation.start();
        colorAnimation.start();
        index = newIndex;
    }

    public void initializeBackgroundColor() {
        layout.setBackgroundResource(colors[index]);
        if (button != null) {
            GradientDrawable buttonDrawable = (GradientDrawable) ((DrawableContainer.DrawableContainerState) ((StateListDrawable)
                    (button.getBackground())).getConstantState()).getChildren()[0];
            buttonDrawable.setColor(ContextCompat.getColor(activity.getApplicationContext(), buttonColors[index]));
            buttonDrawable.setStroke(1, ContextCompat.getColor(activity.getApplicationContext(), buttonColors[index]));
        }
    }
    public class updateBackgroundTask extends TimerTask {
        public void run()
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setBackgroundColor();
                }
            });
        }
    }

}
