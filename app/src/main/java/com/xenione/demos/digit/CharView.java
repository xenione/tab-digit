package com.xenione.demos.digit;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class CharView extends View implements Runnable {

    private Folder topFolder;

    private Folder bottomFolder;

    private Folder middleFolder;

    private Matrix projectionMatrix = new Matrix();

    private int alpha = 0;

    private int mTextSize;

    private Paint mNumberPaint;

    private Rect textSize = new Rect();

    private List<Folder> folders = new ArrayList<>(3);

    private Matrix halfMatrix = new Matrix();

    private int mPadding;

    String[] chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public CharView(Context context) {
        this(context, null);
    }

    public CharView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CharView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        initPaints();
        initFolders();
        MatrixHelper.rotateX(halfMatrix, 90);
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, getResources().getDisplayMetrics());
        setDrawingCacheEnabled(true);
    }

    private void initPaints() {
        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNumberPaint.setColor(Color.WHITE);
        mNumberPaint.setTextSize(mTextSize);
    }

    private void initFolders() {
        // top folder
        topFolder = new Folder();
        topFolder.setCharacters(chars);
        topFolder.setChar(0);
        topFolder.setTextPaint(mNumberPaint);
        topFolder.rotate(180);

        folders.add(topFolder);

        // bottom folder
        bottomFolder = new Folder();
        bottomFolder.setCharacters(chars);
        bottomFolder.setChar(1);
        bottomFolder.setTextPaint(mNumberPaint);

        folders.add(bottomFolder);

        // middle folder
        middleFolder = new Folder();
        middleFolder.setCharacters(chars);
        middleFolder.setChar(0);
        middleFolder.setTextPaint(mNumberPaint);

        folders.add(middleFolder);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        calculateTextSize(textSize);

        int childWidth = textSize.width() + mPadding;
        int childHeight = textSize.height();

        measureFolders(childWidth, childHeight);

        int maxChildWidth = middleFolder.maxWith();
        int maxChildHeight = 2 * middleFolder.maxHeight();

        int resolvedWidth = resolveSize(maxChildWidth, widthMeasureSpec);
        int resolvedHeight = resolveSize(maxChildHeight, heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setupProjectionMatrix();
    }

    private void measureFolders(int width, int height) {
        for (Folder folder : folders) {
            folder.measure(width, height);
        }
    }

    private void drawFolders(Canvas canvas) {
        for (Folder folder : folders) {
            folder.draw(canvas, projectionMatrix);
        }
    }

    private void calculateTextSize(Rect rect) {
        mNumberPaint.setTextSize(mTextSize);
        mNumberPaint.getTextBounds("8", 0, 1, rect);
    }

    private void setupProjectionMatrix() {
        projectionMatrix.reset();
        int centerY = getHeight() / 2;
        int centerX = getWidth() / 2;
        MatrixHelper.translate(projectionMatrix, centerX, -centerY, 0);
    }

    public void setPadding(int padding) {
        mPadding = padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        middleFolder.rotate(alpha);
        drawFolders(canvas);
        ViewCompat.postOnAnimationDelayed(this, this, 20);
    }

    @Override
    public void run() {
        if (alpha == 90) {
            middleFolder.next();
        }
        if (alpha > 180) {
            topFolder.next();
            bottomFolder.next();
            alpha = 0;
        }
        alpha += 1;
        invalidate();
    }
}
