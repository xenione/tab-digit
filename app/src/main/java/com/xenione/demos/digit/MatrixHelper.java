package com.xenione.demos.digit;

import android.graphics.Camera;
import android.graphics.Matrix;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class MatrixHelper {

    public static final Camera camera = new Camera();

    public static void mirrorX(Matrix matrix) {
        rotateX(matrix, 180);
    }

    public static void rotateX(Matrix matrix, int alpha) {
        synchronized (camera) {
            camera.save();
            camera.rotateX(alpha);
            camera.getMatrix(matrix);
            camera.restore();
        }
    }

    public static void rotateZ(Matrix matrix, int alpha) {
        synchronized (camera) {
            camera.save();
            camera.rotateZ(alpha);
            camera.getMatrix(matrix);
            camera.restore();
        }
    }

    public static void translate(Matrix matrix, float dx, float dy, float dz) {
        synchronized (camera) {
            camera.save();
            camera.translate(dx, dy, dz);
            camera.getMatrix(matrix);
            camera.restore();
        }
    }

    public static void translateY(Matrix matrix, float dy) {
        synchronized (camera) {
            camera.save();
            camera.translate(0, dy, 0);
            camera.getMatrix(matrix);
            camera.restore();
        }
    }
}
