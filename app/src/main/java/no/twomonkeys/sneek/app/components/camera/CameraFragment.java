package no.twomonkeys.sneek.app.components.camera;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;

/**
 * Created by simenlie on 04.10.2016.
 */

public class CameraFragment extends Fragment implements CameraEditFragment.Callback {

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private LinearLayout bottomBv;
    private ImageButton cameraBtn, selfieBtn, flashBtn;
    private View view;
    private int cameraId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreview = new CameraPreview(
                this.getActivity().getBaseContext()
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camera, container, false);
        preview = getPreview();
        bottomBv = getBottomBv();
        cameraBtn = getCameraBtn();
        selfieBtn = getSelfieBtn();
        flashBtn = getFlashBtn();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        cameraId = 1;
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
        //mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.mHolder.removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }

    //Views

    private FrameLayout getPreview() {
        if (this.preview == null) {
            FrameLayout preview = (FrameLayout) view.findViewById(R.id.cameraPreview);
            preview.addView(mPreview);
            this.preview = preview;
        }
        return this.preview;
    }

    private LinearLayout getBottomBv() {
        if (bottomBv == null) {
            LinearLayout bottomBv = (LinearLayout) view.findViewById(R.id.bottom_bar);
            bottomBv.getBackground().setAlpha(UIHelper.toAlpha(0.95f));

            int maxWidth = UIHelper.screenWidth(this.getActivity());
            double height = maxWidth * 1.333333;
            int finalHeight = UIHelper.screenHeight(this.getActivity());

            bottomBv.getLayoutParams().height = finalHeight - (int) height;
            this.bottomBv = bottomBv;
        }
        return this.bottomBv;
    }

    private ImageButton getCameraBtn() {
        if (cameraBtn == null) {
            ImageButton cameraBtn = (ImageButton) view.findViewById(R.id.camera_btn);
            cameraBtn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.cameraGrey));
            cameraBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraClick();
                }
            });
            this.cameraBtn = cameraBtn;
        }
        return this.cameraBtn;
    }

    private void cameraClick() {

        takePicture();
    }

    private ImageButton getSelfieBtn() {
        if (this.selfieBtn == null) {
            ImageButton selfieBtn = (ImageButton) view.findViewById(R.id.selfie_button);
            selfieBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selfieClick();
                }
            });
            this.selfieBtn = selfieBtn;
        }
        return this.selfieBtn;
    }

    private void selfieClick() {
        cameraId = cameraId == 1 ? 0 : 1;
        Thread flipCamThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (safeCameraOpen(cameraId)) {
                    mPreview.switchCamera(mCamera);
                }
            }
        });
        flipCamThread.start();
    }

    private ImageButton getFlashBtn() {
        if (this.flashBtn == null) {
            ImageButton flashBtn = (ImageButton) view.findViewById(R.id.flash_button);
            flashBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flashClick();
                }
            });
            this.flashBtn = flashBtn;
        }
        return this.flashBtn;
    }

    private void flashClick() {


    }

    //Camera actions

    //Take picture
    public void takePicture() {
        final CameraEditFragment cef = new CameraEditFragment();
        cef.addCallback(this);
        Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bundle bundle = new Bundle();
                bundle.putByteArray("imageData", data);
                bundle.putBoolean("isSelfie", cameraId == 1);
                cef.setArguments(bundle);
                replaceFragment(cef);
            }
        };

        mCamera.takePicture(null, null, pictureCallback);
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    //Camera edit fragment callback
    @Override
    public void cameraEditOnClose() {
        Log.v("CLOSED", "CLOSED EDIT");
        mCamera.startPreview();
    }

}
