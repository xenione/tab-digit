package com.xenione.demos.digit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Eugeni on 03/12/2016.
 */
public class CountdownFragment extends Fragment {

    public static final String TAG = "CountdownFragment";

    public static Fragment newInstance() {
        return new CountdownFragment();
    }

    private CountdownView mCountdown;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countdown_fragment, container, false);
        mCountdown = (CountdownView) view.findViewById(R.id.clock);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCountdown.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCountdown.pause();
    }
}
