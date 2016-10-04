package no.twomonkeys.sneek.app.components.camera;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;

/**
 * Created by simenlie on 04.10.2016.
 */

public class CameraFragment extends Fragment {

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private RelativeLayout bottomBv;
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreview = new CameraPreview(
                this.getActivity().getBaseContext()
        );

        //preview = (FrameLayout) view.findViewById(R.id.camera_preview);
        //preview.addView(mPreview);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camera, container, false);
        preview = (FrameLayout) view.findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
        this.bottomBv = getBottomBv();
        return view;
    }

    private RelativeLayout getBottomBv() {
        if (bottomBv == null) {
            RelativeLayout bottomBv = (RelativeLayout) view.findViewById(R.id.bottomBar);
            bottomBv.getBackground().setAlpha(toAlpha(0.95f));

            int maxWidth = UIHelper.screenWidth(this.getActivity());
            double height = maxWidth * 1.333333;
            int finalHeight = UIHelper.screenHeight(this.getActivity());

            bottomBv.getLayoutParams().height = finalHeight - (int) height;
            this.bottomBv = bottomBv;
        }
        return this.bottomBv;
    }

    //TODO: Should move this out to a own util class
    private int toAlpha(float alpha) {
        int max = 256;
        float result = (max * alpha);
        Log.v("RESULT", "Result is " + result);
        return (int) result;
    }

    @Override
    public void onResume() {
        super.onResume();
        int cameraId = 1;
        if (safeCameraOpen(cameraId)) {
            mPreview.setCamera(mCamera);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.mHolder.removeCallback(mPreview);
            mCamera.release();
        }
    }


    //Opens the camera safely
    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    //Releases the camera
    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


}
