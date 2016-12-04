package com.xenione.demos.digit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xenione.digit.CharView;

import java.util.Calendar;

/**
 * Created by Eugeni on 03/12/2016.
 */
public class ClockFragment extends Fragment implements Runnable {

    public static final String TAG = "ClockFragment";

    private static final char[] SEXAGISIMAL = new char[]{'0', '1', '2', '3', '4', '5'};

    public static Fragment newInstance() {
        return new ClockFragment();
    }

    private CharView mCharHighSecond;
    private CharView mCharLowSecond;
    private CharView mCharHighMinute;
    private CharView mCharLowMinute;
    private CharView mCharHighHour;
    private CharView mCharLowHour;
    private View mClock;

    private Calendar mDate;
    private long elapsedTime = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clock_fragment, container, false);
        mClock = view.findViewById(R.id.clock);
        mCharHighSecond = (CharView) view.findViewById(R.id.charHighSecond);
        mCharLowSecond = (CharView) view.findViewById(R.id.charLowSecond);
        mCharHighMinute = (CharView) view.findViewById(R.id.charHighMinute);
        mCharLowMinute = (CharView) view.findViewById(R.id.charLowMinute);
        mCharHighHour = (CharView) view.findViewById(R.id.charHighHour);
        mCharLowHour = (CharView) view.findViewById(R.id.charLowHour);
        init();
        return view;
    }

    private void init() {
        mCharHighSecond.setTextSize(100);
        mCharHighSecond.setChars(SEXAGISIMAL);
        mCharLowSecond.setTextSize(100);

        mCharHighMinute.setTextSize(100);
        mCharHighMinute.setChars(SEXAGISIMAL);
        mCharLowMinute.setTextSize(100);

        mCharHighHour.setTextSize(100);
        mCharHighHour.setChars(new char[]{'0', '1'});
        mCharLowHour.setTextSize(100);
    }

    @Override
    public void onResume() {
        super.onResume();
        syncDate();
    }

    @Override
    public void run() {
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

    private void syncDate() {

        /* hours*/
        int hour = mDate.get(Calendar.HOUR_OF_DAY);
        int highHour = hour / 10;
        mCharHighHour.setChar(highHour);

        int lowHour = (hour - highHour * 10);
        mCharLowHour.setChar(lowHour);

        /* minutes*/
        int minutes = mDate.get(Calendar.MINUTE);
        int highMinute = minutes / 10;
        mCharHighMinute.setChar(highMinute);

        int lowMinute = (minutes - highMinute * 10);
        mCharLowMinute.setChar(lowMinute);


        /* seconds*/
        int seconds = mDate.get(Calendar.SECOND);
        int highSecond = seconds / 10;
        mCharHighSecond.setChar(highSecond);

        int lowSecond = (seconds - highSecond * 10);
        mCharLowSecond.setChar(lowSecond);

        elapsedTime = lowSecond + highSecond * 10
                + lowMinute * 60 + highMinute * 600
                + lowHour * 3600 + highHour * 36000;

        ViewCompat.postOnAnimationDelayed(mClock, this, 1000);
    }


}
