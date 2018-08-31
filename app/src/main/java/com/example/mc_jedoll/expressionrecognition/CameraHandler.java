package com.example.mc_jedoll.expressionrecognition;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
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

public class CameraHandler implements CameraBridgeViewBase.CvCameraViewListener2 {
    private String TAG = "CameraHandler_TAG";
    private CameraBridgeViewBase mCameraView;
    private ImageView mImageView;
    private CascadeClassifier mDetection;
    private int absoluteFaceSize;
    private Mat mRgba, mGray, grayscaleImage;
    private Bitmap mBitmap;
    private Context context;

    public CameraHandler(Context context){
        this.context = context;
    }

    // Connect to OpenCV
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            switch(status){
                case LoaderCallbackInterface.SUCCESS:{
                    Log.i(TAG, "OpenCV loaded successfully");
                    intializeOpenCVDependencies();
                } break;
                default: {
                    super.onManagerConnected(status);
                } break;
            }
            super.onManagerConnected(status);
        }
    };

    // Use depedencies from OpenCV
    private void intializeOpenCVDependencies(){
        try {
            InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1){
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            mDetection = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            mDetection.load(mCascadeFile.getAbsolutePath());
            cascadeDir.delete();
        } catch (Exception e){
            Log.e(TAG, "Error loading cascade", e);
        }
        mCameraView.enableView();
    }

    public void startCamera(CameraBridgeViewBase mCamera, ImageView imageView){
        mCamera.setVisibility(SurfaceView.VISIBLE);
        mCamera.setCvCameraViewListener(this);
        this.mCameraView = mCamera;

        this.mImageView = imageView;
    }

    public void startAsync(){
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, context, mLoaderCallback);
    }

    public void disableCamera(){
        if (mCameraView != null)
            mCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        grayscaleImage = new Mat(height, width, CvType.CV_8UC4);
        absoluteFaceSize = (int)(height*0.2);
        mRgba = new Mat();
        mGray = new Mat();
        mBitmap = Bitmap.createBitmap(width/4, height/4, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        // math to bitmap
        try {
            mBitmap = Bitmap.createBitmap(grayscaleImage.cols(), grayscaleImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(grayscaleImage, mBitmap);
            mImageView.setImageBitmap(mBitmap);
            mImageView.invalidate();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }

        Imgproc.cvtColor(inputFrame.rgba(), grayscaleImage, Imgproc.COLOR_RGBA2RGB);

        MatOfRect faces = new MatOfRect();
        if(mDetection != null){
            mDetection.detectMultiScale(grayscaleImage, faces, 1.1, 2, 2, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        Rect[] facesArray = faces.toArray();
        for (int i=0; i<facesArray.length; i++){
            Imgproc.rectangle(inputFrame.gray(),facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 255), 3);
        }

        return inputFrame.gray();
    }
}
