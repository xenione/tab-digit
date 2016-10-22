package com.xenione.demos.digit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class Folder {

    private final Matrix mModelViewMatrix = new Matrix();

    private final Matrix mModelViewProjectionMatrix = new Matrix();

    private final Matrix mRotationModelViewMatrix = new Matrix();

    private Paint mBackgroundPaint;

    private Paint mNumberPaint;

    private final Rect startBounds = new Rect();

    private final Rect endBounds = new Rect();

    private final Rect textBounds = new Rect();

    private int currIndex = 0;

    private String[] mChars;

    private int mAlpha;

    private Matrix halfMatrix = new Matrix();

    private Matrix quarterMatrix = new Matrix();

    private Matrix mirrorMatrix = new Matrix();

    private Matrix measuredMatrixHeight = new Matrix();

    private Matrix measuredMatrixWidth = new Matrix();

    public Folder() {
        initPaints();
        MatrixHelper.rotateX(mirrorMatrix, 180);
        MatrixHelper.rotateX(halfMatrix, 90);
        MatrixHelper.rotateX(quarterMatrix, 45);
    }

    private void initPaints() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.BLACK);
    }

    public void setTextPaint(Paint paint) {
        mNumberPaint = paint;
    }

    public void measure(int width, int height) {
        Rect area = new Rect(-width / 2, -height / 2, width / 2, height / 2);
        startBounds.set(area);
        startBounds.offset(0, height / 2);
        endBounds.set(area);
        endBounds.offset(0, -height / 2);

        calculateTextSize();
    }

    public int maxWith() {
        RectF rect = new RectF(startBounds);
        Matrix projectionMatrix = new Matrix();
        MatrixHelper.translate(projectionMatrix, startBounds.left, -startBounds.top, 0);
        measuredMatrixWidth.reset();
        measuredMatrixWidth.setConcat(projectionMatrix, halfMatrix);
        measuredMatrixWidth.mapRect(rect);
        return (int) rect.width();
    }

    public int maxHeight() {
        RectF rect = new RectF(startBounds);
        Matrix projectionMatrix = new Matrix();
        measuredMatrixHeight.reset();
        measuredMatrixHeight.setConcat(projectionMatrix, quarterMatrix);
        measuredMatrixHeight.mapRect(rect);
        return (int) rect.height();
    }

    private void calculateTextSize() {
        mNumberPaint.getTextBounds("8", 0, 1, textBounds);
    }

    public void setCharacters(String[] chars) {
        mChars = chars;
    }

    public void setChar(int index) {
        currIndex = index > mChars.length ? 0 : index;
    }

    public void next(){
        currIndex++;
        if (currIndex >= mChars.length) {
            currIndex = 0;
        }
    }

    public void rotate(int alpha) {
        mAlpha = alpha;
        MatrixHelper.rotateX(mRotationModelViewMatrix, alpha);
    }

    public void draw(Canvas canvas, Matrix projectionMatrix) {
        canvas.save();
        drawBackground(canvas, projectionMatrix);
        drawText(canvas, projectionMatrix);
        canvas.restore();
    }

    private void drawBackground(Canvas canvas, Matrix projectionMatrix) {
        canvas.save();
        mModelViewProjectionMatrix.reset();
        mModelViewMatrix.set(mRotationModelViewMatrix);
        mModelViewProjectionMatrix.setConcat(projectionMatrix, mModelViewMatrix);
        canvas.concat(mModelViewProjectionMatrix);
        canvas.drawRect(startBounds, mBackgroundPaint);
        canvas.restore();
    }

    private void drawText(Canvas canvas, Matrix projectionMatrix) {
        canvas.save();
        mModelViewProjectionMatrix.reset();
        mModelViewMatrix.set(mRotationModelViewMatrix);
        Rect clip = startBounds;
        if (mAlpha > 90) {
            mModelViewMatrix.setConcat(mModelViewMatrix, mirrorMatrix);
            clip = endBounds;
        }
        mModelViewProjectionMatrix.setConcat(projectionMatrix, mModelViewMatrix);
        canvas.concat(mModelViewProjectionMatrix);
        canvas.clipRect(clip);
        canvas.drawText(mChars[currIndex], 0, 1, -textBounds.centerX(), -textBounds.centerY(), mNumberPaint);
        canvas.restore();
    }
}
