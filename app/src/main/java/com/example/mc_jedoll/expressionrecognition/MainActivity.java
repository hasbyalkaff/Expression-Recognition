package com.example.mc_jedoll.expressionrecognition;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MAIN_TAG";
    private CameraHandler cameraHandler;
    private VolleyRequest volleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called");
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ImageView mImage = findViewById(R.id.imageView);
        CameraBridgeViewBase mCamera = findViewById(R.id.cameraView);

        // create cameraHandler
        cameraHandler = new CameraHandler(this);
        volleyRequest = new VolleyRequest(this);

        // open camera
        cameraHandler.startCamera(mCamera, mImage);

        volleyRequest.methodGET();
        volleyRequest.jsonPOST();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(TAG, "onResume called");
        cameraHandler.startAsync();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.i(TAG, "onPause called");
        cameraHandler.disableCamera();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
        cameraHandler.disableCamera();
    }
}
