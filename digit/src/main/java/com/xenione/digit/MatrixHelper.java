package com.xenione.digit;

import android.graphics.Camera;
import android.graphics.Matrix;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class MatrixHelper {

    public static final Camera camera = new Camera();

    /**
     * Matrix with 180 degrees x rotation defined
     */
    public static final Matrix MIRROR_X = new Matrix();
    static {
        MatrixHelper.rotateX(MIRROR_X, 180);
    }

    /**
     * Matrix with 0 degrees x rotation defined
     */
    public static final Matrix ROTATE_X_0 = new Matrix();

    static {
        MatrixHelper.rotateX(ROTATE_X_0, 0);
    }

    /**
     * Matrix with 90 degrees x rotation defined
     */
    public static final Matrix ROTATE_X_90 = new Matrix();

    static {
        MatrixHelper.rotateX(ROTATE_X_90, 90);
    }

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
