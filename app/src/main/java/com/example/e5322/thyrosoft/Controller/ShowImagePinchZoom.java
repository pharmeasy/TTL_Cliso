package com.example.e5322.thyrosoft.Controller;

import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.imagezoom.ImageAttacher.OnPhotoTapListener;

public class ShowImagePinchZoom {

    public static void ZoomImage(ImageView image_testtoui) {
        usingSimpleImage(image_testtoui);
    }

    public static void usingSimpleImage(ImageView imageView) {
        try {
            ImageAttacher mAttacher = new ImageAttacher(imageView);
            ImageAttacher.MAX_ZOOM = 5.0f; // Double the current Size
            ImageAttacher.MIN_ZOOM = 0.5f; // Half the current Size
            MatrixChangeListener mMaListener = new MatrixChangeListener();
            mAttacher.setOnMatrixChangeListener(mMaListener);
            PhotoTapListener mPhotoTap = new PhotoTapListener();
            mAttacher.setOnPhotoTapListener(mPhotoTap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
        }
    }

    private static class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF arg0) {
            // TODO Auto-generated method stub

        }
    }

}
