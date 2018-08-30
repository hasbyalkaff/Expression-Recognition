package com.example.mc_jedoll.expressionrecognition;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

public class CameraHadler implements CameraBridgeViewBase.CvCameraViewListener2 {
    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return null;
    }
}
