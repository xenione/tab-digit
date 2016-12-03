package com.xenione.demos.digit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private CharView charView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        charView1 = (CharView) findViewById(R.id.charView1);
        assert charView1 != null;
        charView1.start();

        SeekBar textSizeBar = (SeekBar) findViewById(R.id.size_bar);
        assert textSizeBar != null;
        textSizeBar.setOnSeekBarChangeListener(mTextSizeHandler);
        textSizeBar.setProgress(charView1.getTextSize());

        SeekBar paddingSizeBar = (SeekBar) findViewById(R.id.padding_size_bar);
        assert paddingSizeBar != null;
        paddingSizeBar.setOnSeekBarChangeListener(mPaddingSizeHandler);
        paddingSizeBar.setProgress(charView1.getPadding());

        SeekBar cornerSizeBar = (SeekBar) findViewById(R.id.corner_size_bar);
        assert cornerSizeBar != null;
        cornerSizeBar.setOnSeekBarChangeListener(mCornerSizeHandler);
        cornerSizeBar.setProgress(charView1.getCornerSize());

        ColorChooserView backgroundColorChooserView = (ColorChooserView) findViewById(R.id.Background_color_chooser);
        assert backgroundColorChooserView != null;
        backgroundColorChooserView.setColor(charView1.getBackgroundColor());
        backgroundColorChooserView.setTitle("Background Color Chooser");
        backgroundColorChooserView.setOnColorChooserSelectListener(mOnBackgroundColorSelectHandler);

        ColorChooserView textColorChooserView = (ColorChooserView) findViewById(R.id.text_color_chooser);
        assert textColorChooserView != null;
        textColorChooserView.setColor(charView1.getTextColor());
        textColorChooserView.setTitle("Text Color Chooser");
        textColorChooserView.setOnColorChooserSelectListener(mTextColorSelectHandler);

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



}
