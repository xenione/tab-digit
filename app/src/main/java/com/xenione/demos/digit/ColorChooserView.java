package com.xenione.demos.digit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

/**
 * Created by Eugeni on 03/12/2016.
 */
public class ColorChooserView extends View {

    public interface OnColorChooserSelectListener {
        void onColorPickerViewSelected(int color);
    }

    private int mColor = 0xffffffff;

    private String mTitle;

    private Paint mBorderPaint;

    private Rect mBorderRect = new Rect();

    private OnColorChooserSelectListener mListener;

    private AlertDialog ColorChooserDialog = buildColorPicker();

    public ColorChooserView(Context context) {
        this(context, null);
    }

    public ColorChooserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorChooserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOnClickListener(mOnViewClicked);

        mBorderPaint = new Paint();
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(4);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    public void setColor(int color) {
        setBackgroundColor(color);
        mColor = color;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOnColorChooserSelectListener(OnColorChooserSelectListener listener) {
        mListener = listener;
    }

    private OnClickListener mOnViewClicked = new OnClickListener() {
        @Override
        public void onClick(View v) {
            buildColorPicker().show();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        mBorderRect.set(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(mBorderRect, mBorderPaint);
    }

    private AlertDialog buildColorPicker() {
        return ColorPickerDialogBuilder
                .with(getContext())
                .setTitle(mTitle)
                .initialColor(mColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                        setColor(i);
                        if (mListener != null) {
                            mListener.onColorPickerViewSelected(i);
                        }
                    }
                })
                .setNegativeButton("cancel", null)
                .build();
    }
}
