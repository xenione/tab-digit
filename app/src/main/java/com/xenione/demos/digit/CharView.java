package com.xenione.demos.digit;

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
public class CharView extends View implements Runnable {

    private Folder mTopFolder;

    private Folder mBottomFolder;

    private Folder mMiddleFolder;

    private List<Folder> folders = new ArrayList<>(3);

    private Matrix mProjectionMatrix = new Matrix();

    private int mAlpha = 0;

    private int mTextSize;

    private int mCornerSize;

    private Paint mNumberPaint;

    private Paint mDividerPaint;

    private Paint mBackgroundPaint;

    private Rect mTextMeasured = new Rect();

    private int mPadding;

    private final String[] mChars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public CharView(Context context) {
        this(context, null);
    }

    public CharView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CharView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        initPaints();
        initFolders();

        int padding = -1;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CharView, 0, 0);
        final int num = ta.getIndexCount();
        for (int i = 0; i < num; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.CharView_textSize: {
                    mTextSize = ta.getDimensionPixelSize(attr, -1);
                    break;
                }
                case R.styleable.CharView_padding: {
                    padding = ta.getDimensionPixelSize(attr, -1);
                    break;
                }
                case R.styleable.CharView_textColor: {
                    mNumberPaint.setColor(ta.getColor(attr, -1));
                    break;
                }
                case R.styleable.CharView_backgroundColor: {
                    mBackgroundPaint.setColor(ta.getColor(attr, -1));
                    break;
                }
                case R.styleable.CharView_cornerSize: {
                    mCornerSize = ta.getDimensionPixelSize(attr, 5);
                    break;
                }
            }
        }

        if (padding > 0) {
            mPadding = padding;
        }
    }



    private void initPaints() {
        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNumberPaint.setColor(Color.WHITE);
        mNumberPaint.setTextSize(mTextSize);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDividerPaint.setColor(Color.WHITE);
        mDividerPaint.setStrokeWidth(1);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.BLACK);
        mBackgroundPaint.setAntiAlias(true);
    }

    private void initFolders() {
        // top folder
        mTopFolder = new Folder();
        mTopFolder.setChar(0);
        mTopFolder.rotate(180);

        folders.add(mTopFolder);

        // bottom folder
        mBottomFolder = new Folder();
        mBottomFolder.setChar(1);

        folders.add(mBottomFolder);

        // middle folder
        mMiddleFolder = new Folder();
        mMiddleFolder.setChar(0);

        folders.add(mMiddleFolder);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        calculateTextSize(mTextMeasured);

        int childWidth = mTextMeasured.width() + mPadding;
        int childHeight = mTextMeasured.height() + mPadding;

        measureFolders(childWidth, childHeight);

        int maxChildWidth = mMiddleFolder.maxWith();
        int maxChildHeight = 2 * mMiddleFolder.maxHeight();

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

    private void measureFolders(int width, int height) {
        for (Folder folder : folders) {
            folder.measure(width, height);
        }
    }

    private void drawFolders(Canvas canvas) {
        for (Folder folder : folders) {
            folder.draw(canvas);
        }
    }

    private void drawDivider(Canvas canvas) {
        canvas.save();
        canvas.concat(mProjectionMatrix);
        canvas.drawLine(-canvas.getWidth() / 2, 0, canvas.getWidth() / 2, 0, mDividerPaint);
        canvas.restore();
    }

    private void calculateTextSize(Rect rect) {
        mNumberPaint.setTextSize(mTextSize);
        mNumberPaint.getTextBounds("8", 0, 1, rect);
    }

    public void setTextSize(int size) {
        mTextSize = size;
        requestLayout();
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setPadding(int padding) {
        mPadding = padding;
        requestLayout();
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

    long time = -1;

    public void start() {
        time = System.currentTimeMillis();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFolders(canvas);
        drawDivider(canvas);
        ViewCompat.postOnAnimationDelayed(this, this, 10);
    }

    boolean middleChange = false;

    @Override
    public void run() {
        if (time == -1) {
            return;
        }
        if (mAlpha > 90 && !middleChange) {
            mMiddleFolder.next();
            middleChange = true;
        }
        if (mAlpha >= 180) {
            mTopFolder.next();
            mBottomFolder.next();
            middleChange = false;
            time = System.currentTimeMillis();
        }
        long delta = (System.currentTimeMillis() - time);
        mAlpha = (int) (180 * (1 - (1 * 1000 - delta) / (1 * 1000.0)));
        mMiddleFolder.rotate(mAlpha);
        invalidate();
    }

    public class Folder {

        private final Matrix mModelViewMatrix = new Matrix();

        private final Matrix mModelViewProjectionMatrix = new Matrix();

        private final Matrix mRotationModelViewMatrix = new Matrix();

        private final RectF startBounds = new RectF();

        private final RectF endBounds = new RectF();

        private final Rect textBounds = new Rect();

        private int currIndex = 0;

        private int mAlpha;

        private Matrix measuredMatrixHeight = new Matrix();

        private Matrix measuredMatrixWidth = new Matrix();

        public Folder() {
            initPaints();
        }

        public void measure(int width, int height) {
            Rect area = new Rect(-width / 2, 0, width / 2, height / 2);
            startBounds.set(area);
            endBounds.set(area);
            endBounds.offset(0, -height / 2);

            calculateTextSize();
        }

        public int maxWith() {
            RectF rect = new RectF(startBounds);
            Matrix projectionMatrix = new Matrix();
            MatrixHelper.translate(projectionMatrix, startBounds.left, -startBounds.top, 0);
            measuredMatrixWidth.reset();
            measuredMatrixWidth.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_90);
            measuredMatrixWidth.mapRect(rect);
            return (int) rect.width();
        }

        public int maxHeight() {
            RectF rect = new RectF(startBounds);
            Matrix projectionMatrix = new Matrix();
            measuredMatrixHeight.reset();
            measuredMatrixHeight.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_0);
            measuredMatrixHeight.mapRect(rect);
            return (int) rect.height();
        }

        private void calculateTextSize() {
            mNumberPaint.getTextBounds("8", 0, 1, textBounds);
        }

        public void setChar(int index) {
            currIndex = index > mChars.length ? 0 : index;
        }

        public void next() {
            currIndex++;
            if (currIndex >= mChars.length) {
                currIndex = 0;
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
            canvas.drawRoundRect(startBounds, mCornerSize, mCornerSize, mBackgroundPaint);
            canvas.restore();
        }

        private void drawText(Canvas canvas) {
            canvas.save();
            mModelViewMatrix.set(mRotationModelViewMatrix);
            RectF clip = startBounds;
            if (mAlpha > 90) {
                mModelViewMatrix.setConcat(mModelViewMatrix, MatrixHelper.MIRROR_X);
                clip = endBounds;
            }
            applyTransformation(canvas, mModelViewMatrix);
            canvas.clipRect(clip);
            canvas.drawText(mChars[currIndex], 0, 1, -textBounds.centerX(), -textBounds.centerY(), mNumberPaint);
            canvas.restore();
        }

        private void applyTransformation(Canvas canvas, Matrix matrix) {
            mModelViewProjectionMatrix.reset();
            mModelViewProjectionMatrix.setConcat(mProjectionMatrix, matrix);
            mModelViewProjectionMatrix.setConcat(mProjectionMatrix, mModelViewMatrix);
            canvas.concat(mModelViewProjectionMatrix);
        }
    }


}
