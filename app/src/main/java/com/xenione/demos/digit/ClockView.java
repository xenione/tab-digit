package com.xenione.demos.digit;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xenione.digit.CharView;

import java.util.Calendar;

/**
 * Created by Eugeni on 04/12/2016.
 */
public class ClockView extends LinearLayout implements Runnable{

    private static final char[] HOURS = new char[]{'0', '1', '2'};

    private static final char[] SEXAGISIMAL = new char[]{'0', '1', '2', '3', '4', '5'};

    private CharView mCharHighSecond;
    private CharView mCharLowSecond;
    private CharView mCharHighMinute;
    private CharView mCharLowMinute;
    private CharView mCharHighHour;
    private CharView mCharLowHour;
    private View mClock = this;

    private boolean mPause = true;

    private long elapsedTime = 0;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        setOrientation(HORIZONTAL);
        inflate(getContext(), R.layout.clock, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCharHighSecond = (CharView) findViewById(R.id.charHighSecond);
        mCharLowSecond = (CharView) findViewById(R.id.charLowSecond);
        mCharHighMinute = (CharView) findViewById(R.id.charHighMinute);
        mCharLowMinute = (CharView) findViewById(R.id.charLowMinute);
        mCharHighHour = (CharView) findViewById(R.id.charHighHour);
        mCharLowHour = (CharView) findViewById(R.id.charLowHour);

        mCharHighSecond.setTextSize(100);
        mCharHighSecond.setChars(SEXAGISIMAL);
        mCharLowSecond.setTextSize(100);

        mCharHighMinute.setTextSize(100);
        mCharHighMinute.setChars(SEXAGISIMAL);
        mCharLowMinute.setTextSize(100);

        mCharHighHour.setTextSize(100);
        mCharHighHour.setChars(HOURS);
        mCharLowHour.setTextSize(100);
    }

    public void pause() {
        mPause = true;
        mCharHighSecond.sync();
        mCharLowSecond.sync();
        mCharHighMinute.sync();
        mCharLowMinute.sync();
        mCharHighHour.sync();
        mCharLowHour.sync();
    }

    public void resume() {
        mPause = false;
        Calendar time = Calendar.getInstance();
        /* hours*/
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int highHour = hour / 10;
        mCharHighHour.setChar(highHour);

        int lowHour = (hour - highHour * 10);
        mCharLowHour.setChar(lowHour);

        /* minutes*/
        int minutes = time.get(Calendar.MINUTE);
        int highMinute = minutes / 10;
        mCharHighMinute.setChar(highMinute);

        int lowMinute = (minutes - highMinute * 10);
        mCharLowMinute.setChar(lowMinute);


        /* seconds*/
        int seconds = time.get(Calendar.SECOND);
        int highSecond = seconds / 10;
        mCharHighSecond.setChar(highSecond);

        int lowSecond = (seconds - highSecond * 10);
        mCharLowSecond.setChar(lowSecond);

        elapsedTime = lowSecond + highSecond * 10
                + lowMinute * 60 + highMinute * 600
                + lowHour * 3600 + highHour * 36000;

        ViewCompat.postOnAnimationDelayed(mClock, this, 1000);
    }

    @Override
    public void run() {
        if(mPause){
            return;
        }
        elapsedTime += 1;
        mCharLowSecond.start();
        if (elapsedTime % 10 == 0) {
            mCharHighSecond.start();
        }
        if (elapsedTime % 60 == 0) {
            mCharLowMinute.start();
        }
        if (elapsedTime % 600 == 0) {
            mCharHighMinute.start();
        }
        if (elapsedTime % 3600 == 0) {
            mCharLowHour.start();
        }
        if (elapsedTime % 36000 == 0) {
            mCharHighHour.start();
        }
        ViewCompat.postOnAnimationDelayed(mClock, this, 1000);
    }
}
