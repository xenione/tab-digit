package com.xenione.demos.digit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.xenione.digit.CharView;

/**
 * Created by Eugeni on 03/12/2016.
 */
public class ConfigFragment extends Fragment implements Runnable {

    public static final String TAG = "ConfigFragment";

    public static Fragment newInstance() {
        return new ConfigFragment();
    }

    private CharView charView1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.config_fragment, container, false);
        charView1 = (CharView) view.findViewById(R.id.charView1);
        assert charView1 != null;
        ViewCompat.postOnAnimationDelayed(charView1, this, 1000);

        SeekBar textSizeBar = (SeekBar) view.findViewById(R.id.size_bar);
        assert textSizeBar != null;
        textSizeBar.setOnSeekBarChangeListener(mTextSizeHandler);
        textSizeBar.setProgress(charView1.getTextSize());

        SeekBar paddingSizeBar = (SeekBar) view.findViewById(R.id.padding_size_bar);
        assert paddingSizeBar != null;
        paddingSizeBar.setOnSeekBarChangeListener(mPaddingSizeHandler);
        paddingSizeBar.setProgress(charView1.getPadding());

        SeekBar cornerSizeBar = (SeekBar) view.findViewById(R.id.corner_size_bar);
        assert cornerSizeBar != null;
        cornerSizeBar.setOnSeekBarChangeListener(mCornerSizeHandler);
        cornerSizeBar.setProgress(charView1.getCornerSize());

        ColorChooserView backgroundColorChooserView = (ColorChooserView) view.findViewById(R.id.Background_color_chooser);
        assert backgroundColorChooserView != null;
        backgroundColorChooserView.setColor(charView1.getBackgroundColor());
        backgroundColorChooserView.setTitle("Background Color Chooser");
        backgroundColorChooserView.setOnColorChooserSelectListener(mOnBackgroundColorSelectHandler);

        ColorChooserView textColorChooserView = (ColorChooserView) view.findViewById(R.id.text_color_chooser);
        assert textColorChooserView != null;
        textColorChooserView.setColor(charView1.getTextColor());
        textColorChooserView.setTitle("Text Color Chooser");
        textColorChooserView.setOnColorChooserSelectListener(mTextColorSelectHandler);

        return view;
    }

    public ColorChooserView.OnColorChooserSelectListener mTextColorSelectHandler = new ColorChooserView.OnColorChooserSelectListener() {

        @Override
        public void onColorPickerViewSelected(int color) {
            charView1.setTextColor(color);
        }
    };

    private ColorChooserView.OnColorChooserSelectListener mOnBackgroundColorSelectHandler = new ColorChooserView.OnColorChooserSelectListener() {

        @Override
        public void onColorPickerViewSelected(int color) {
            charView1.setBackgroundColor(color);
        }
    };


    private SeekBar.OnSeekBarChangeListener mTextSizeHandler = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                charView1.setTextSize(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener mPaddingSizeHandler = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                charView1.setPadding(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener mCornerSizeHandler = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                charView1.setCornerSize((int) (progress * 0.3f));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public void run() {
        charView1.start();
        ViewCompat.postOnAnimationDelayed(charView1, this, 1000);
    }
}
