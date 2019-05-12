package com.xenione.digit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class TabDigit extends View implements Runnable {

    /*
     * false: rotate upwards
     * true: rotate downwards
     */
    private boolean mReverseRotation = false;

    private Tab mTopTab;

    private Tab mBottomTab;

    private Tab mMiddleTab;

    private List<Tab> tabs = new ArrayList<>(3);

    private AbstractTabAnimation tabAnimation;

    private Matrix mProjectionMatrix = new Matrix();

    private int mCornerSize;

    private Paint mNumberPaint;

    private Paint mDividerPaint;

    private Paint mBackgroundPaint;

    private Rect mTextMeasured = new Rect();

    private int mPadding = 0;

    private char[] mChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public TabDigit(Context context) {
        this(context, null);
    }

    public TabDigit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabDigit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabDigit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        initPaints();

        int padding = -1;
        int textSize = -1;
        int cornerSize = -1;
        int textColor = 1;
        int backgroundColor = 1;
        boolean reverseRotation = false;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabDigit, 0, 0);
        final int num = ta.getIndexCount();
        for (int i = 0; i < num; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.TabDigit_textSize) {
                textSize = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_padding) {
                padding = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_cornerSize) {
                cornerSize = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_textColor) {
                textColor = ta.getColor(attr, 1);
            } else if (attr == R.styleable.TabDigit_backgroundColor) {
                backgroundColor = ta.getColor(attr, 1);
            } else if (attr == R.styleable.TabDigit_reverseRotation) {
                reverseRotation = ta.getBoolean(attr, false);
            }
        }
        ta.recycle();

        if (padding > 0) {
            mPadding = padding;
        }

        if (textSize > 0) {
            mNumberPaint.setTextSize(textSize);
        }

        if (cornerSize > 0) {
            mCornerSize = cornerSize;
        }

        if (textColor < 1) {
            mNumberPaint.setColor(textColor);
        }

        if (backgroundColor < 1) {
            mBackgroundPaint.setColor(backgroundColor);
        }

        mReverseRotation = reverseRotation;
        mReverseRotation = true;

        initTabs();
    }

    private void initPaints() {
        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNumberPaint.setColor(Color.WHITE);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDividerPaint.setColor(Color.WHITE);
        mDividerPaint.setStrokeWidth(1);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.BLACK);
    }

    private void initTabs() {
        // top Tab
        mTopTab = new Tab();
        mTopTab.rotate(180);
        tabs.add(mTopTab);

        // bottom Tab
        mBottomTab = new Tab();
        tabs.add(mBottomTab);

        // middle Tab
        mMiddleTab = new Tab();
        tabs.add(mMiddleTab);

        tabAnimation = mReverseRotation ? new TabAnimationDown(mTopTab, mBottomTab, mMiddleTab) : new TabAnimationUp(mTopTab, mBottomTab, mMiddleTab);

        tabAnimation.initMiddleTab();

        setInternalChar(0);
    }

    public void setChar(int index) {
        setInternalChar(index);
        invalidate();
    }

    private void setInternalChar(int index) {
        for (Tab tab : tabs) {
            tab.setChar(index);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        calculateTextSize(mTextMeasured);

        int childWidth = mTextMeasured.width() + mPadding;
        int childHeight = mTextMeasured.height() + mPadding;
        measureTabs(childWidth, childHeight);

        int maxChildWidth = mMiddleTab.maxWith();
        int maxChildHeight = 2 * mMiddleTab.maxHeight();
        int resolvedWidth = resolveSize(maxChildWidth, widthMeasureSpec);
        int resolvedHeight = resolveSize(maxChildHeight, heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            setupProjectionMatrix();
        }
    }

    private void setupProjectionMatrix() {
        mProjectionMatrix.reset();
        int centerY = getHeight() / 2;
        int centerX = getWidth() / 2;
        MatrixHelper.translate(mProjectionMatrix, centerX, -centerY, 0);
    }

    private void measureTabs(int width, int height) {
        for (Tab tab : tabs) {
            tab.measure(width, height);
        }
    }

    private void drawTabs(Canvas canvas) {
        for (Tab tab : tabs) {
            tab.draw(canvas);
        }
    }

    private void drawDivider(Canvas canvas) {
        canvas.save();
        canvas.concat(mProjectionMatrix);
        canvas.drawLine(-canvas.getWidth() / 2, 0, canvas.getWidth() / 2, 0, mDividerPaint);
        canvas.restore();
    }

    private void calculateTextSize(Rect rect) {
        mNumberPaint.getTextBounds("8", 0, 1, rect);
    }

    public void setTextSize(int size) {
        mNumberPaint.setTextSize(size);
        requestLayout();
    }

    public int getTextSize() {
        return (int) mNumberPaint.getTextSize();
    }

    public void setPadding(int padding) {
        mPadding = padding;
        requestLayout();
    }

    /**
     * Sets chars that are going to be displayed.
     * Note: <b>That only one digit is allow per character.</b>
     *
     * @param chars
     */
    public void setChars(char[] chars) {
        mChars = chars;
    }

    public char[]  getChars() {
        return mChars;
    }


    public void setDividerColor(int color) {
        mDividerPaint.setColor(color);
    }

    public int getPadding(){
        return mPadding;
    }

    public void setTextColor(int color) {
        mNumberPaint.setColor(color);
    }

    public int getTextColor() {
        return mNumberPaint.getColor();
    }

    public void setCornerSize(int cornerSize) {
        mCornerSize = cornerSize;
        invalidate();
    }

    public int getCornerSize() {
        return mCornerSize;
    }

    public void setBackgroundColor(int color) {
        mBackgroundPaint.setColor(color);
    }

    public int getBackgroundColor() {
        return mBackgroundPaint.getColor();
    }

    public void start() {
        tabAnimation.start();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawTabs(canvas);
        drawDivider(canvas);
        ViewCompat.postOnAnimationDelayed(this, this, 40);
    }

    @Override
    public void run() {
        tabAnimation.run();
        invalidate();
    }

    public void sync() {
        tabAnimation.sync();
        invalidate();
    }

    public class Tab {

        private final Matrix mModelViewMatrix = new Matrix();

        private final Matrix mModelViewProjectionMatrix = new Matrix();

        private final Matrix mRotationModelViewMatrix = new Matrix();

        private final RectF mStartBounds = new RectF();

        private final RectF mEndBounds = new RectF();

        private int mCurrIndex = 0;

        private int mAlpha;

        private Matrix mMeasuredMatrixHeight = new Matrix();

        private Matrix mMeasuredMatrixWidth = new Matrix();


        public void measure(int width, int height) {
            Rect area = new Rect(-width / 2, 0, width / 2, height / 2);
            mStartBounds.set(area);
            mEndBounds.set(area);
            mEndBounds.offset(0, -height / 2);
        }

        public int maxWith() {
            RectF rect = new RectF(mStartBounds);
            Matrix projectionMatrix = new Matrix();
            MatrixHelper.translate(projectionMatrix, mStartBounds.left, -mStartBounds.top, 0);
            mMeasuredMatrixWidth.reset();
            mMeasuredMatrixWidth.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_90);
            mMeasuredMatrixWidth.mapRect(rect);
            return (int) rect.width();
        }

        public int maxHeight() {
            RectF rect = new RectF(mStartBounds);
            Matrix projectionMatrix = new Matrix();
            mMeasuredMatrixHeight.reset();
            mMeasuredMatrixHeight.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_0);
            mMeasuredMatrixHeight.mapRect(rect);
            return (int) rect.height();
        }

        public void setChar(int index) {
            mCurrIndex = index > mChars.length ? 0 : index;
        }

        public void next() {
            mCurrIndex++;
            if (mCurrIndex >= mChars.length) {
                mCurrIndex = 0;
            }
        }

        public void rotate(int alpha) {
            mAlpha = alpha;
            MatrixHelper.rotateX(mRotationModelViewMatrix, alpha);
        }

        public void draw(Canvas canvas) {
            drawBackground(canvas);
            drawText(canvas);
        }

        private void drawBackground(Canvas canvas) {
            canvas.save();
            mModelViewMatrix.set(mRotationModelViewMatrix);
            applyTransformation(canvas, mModelViewMatrix);
            canvas.drawRoundRect(mStartBounds, mCornerSize, mCornerSize, mBackgroundPaint);
            canvas.restore();
        }

        private void drawText(Canvas canvas) {
            canvas.save();
            mModelViewMatrix.set(mRotationModelViewMatrix);
            RectF clip = mStartBounds;
            if (mAlpha > 90) {
                mModelViewMatrix.setConcat(mModelViewMatrix, MatrixHelper.MIRROR_X);
                clip = mEndBounds;
            }
            applyTransformation(canvas, mModelViewMatrix);
            canvas.clipRect(clip);
            canvas.drawText(Character.toString(mChars[mCurrIndex]), 0, 1, -mTextMeasured.centerX(), -mTextMeasured.centerY(), mNumberPaint);
            canvas.restore();
        }

        private void applyTransformation(Canvas canvas, Matrix matrix) {
            mModelViewProjectionMatrix.reset();
            mModelViewProjectionMatrix.setConcat(mProjectionMatrix, matrix);
            canvas.concat(mModelViewProjectionMatrix);
        }
    }

}
