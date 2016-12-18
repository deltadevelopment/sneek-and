package no.twomonkeys.sneek.app.components.camera;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.PostModel;

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
    private boolean isSwitchingCamera, flashOn;
    private ImageButton cBackBtn;

    public interface Callback {
        void cameraFragmentTappedClose();

        void cameraFragmentDidPost(PostModel postModel);
    }

    Callback callback;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("CREATING VIEW");
        super.onCreate(savedInstanceState);

        mPreview = new CameraPreview(
                this.getActivity().getBaseContext()
        );
        mPreview.setVisibility(View.INVISIBLE);
        mPreview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                System.out.println("LAYOUT COMPLETE");
                //At this point the layout is complete and the
                //dimensions of myView and any child views are known.
                mPreview.setVisibility(View.VISIBLE);

            }
        });


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera, container, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");
                    selfieClick();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    tapToFocus(e);
                    return super.onSingleTapUp(e);
                }
                // implement here other callback methods like onFling, onScroll as necessary
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        preview = getPreview();
        bottomBv = getBottomBv();
        cameraBtn = getCameraBtn();
        selfieBtn = getSelfieBtn();
        flashBtn = getFlashBtn();
        cBackBtn = getcBackBtn();

        //initCamera();
        return view;
    }

    private ImageButton getcBackBtn() {
        final CameraFragment f = this;
        if (this.cBackBtn == null) {
            ImageButton cBackBtn = (ImageButton) view.findViewById(R.id.cBackBtn);
            cBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  getActivity().getFragmentManager().beginTransaction().remove(f).commit();
                    callback.cameraFragmentTappedClose();
                }
            });

            this.cBackBtn = cBackBtn;
        }

        return this.cBackBtn;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("HEllo");
        return super.onOptionsItemSelected(item);
    }

    public void tapToFocus(MotionEvent event) {
        if (mCamera != null) {
            Log.v("FOCUSING", "FOCUSING");
            Camera camera = mCamera;
            camera.cancelAutoFocus();

            float x = event.getX();
            float y = event.getY();

            Rect touchRect = new Rect(
                    (int) (x - 100),
                    (int) (y - 100),
                    (int) (x + 100),
                    (int) (y + 100));

            final Rect targetFocusRect = new Rect(
                    touchRect.left * 2000 / view.getWidth() - 1000,
                    touchRect.top * 2000 / view.getHeight() - 1000,
                    touchRect.right * 2000 / view.getWidth() - 1000,
                    touchRect.bottom * 2000 / view.getHeight() - 1000);

            Rect focusRect = new Rect(-1000, -1000, 1000, 0);
            focusRect = targetFocusRect;
            //left, top, right, bottm
            // Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
            Log.v("the result is", "result it" + event.getX() + " : " + event.getY());
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getFocusMode() != Camera.Parameters.FOCUS_MODE_AUTO) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if (parameters.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> mylist = new ArrayList<Camera.Area>();
                mylist.add(new Camera.Area(focusRect, 1000));
                parameters.setFocusAreas(mylist);
            }

            try {
                camera.cancelAutoFocus();
                camera.setParameters(parameters);
                camera.startPreview();
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (camera.getParameters().getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                            Camera.Parameters parameters = camera.getParameters();
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            if (parameters.getMaxNumFocusAreas() > 0) {
                                parameters.setFocusAreas(null);
                            }
                            camera.setParameters(parameters);
                            camera.startPreview();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ON START");
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("ON RESUME");
        cameraId = 1;

        if (safeCameraOpen(cameraId)) {
            mPreview.setCamera(mCamera);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("RELEASING CAMERA");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.mHolder.removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
        //WAS here
    }

    @Override
    public void onStop() {
        System.out.println("RELEASING CAMERA STOP");
        super.onStop();
    }

    //Opens the fragment_camera safely
    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;
        System.out.println("Opening camera again");
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

    private void initCamera() {
        //create preview
        //add it to preview
        //open camera
        mPreview = new CameraPreview(
                this.getActivity().getBaseContext()
        );
        mPreview.setVisibility(View.INVISIBLE);
        preview.addView(mPreview);
        if (safeCameraOpen(cameraId)) {
            mPreview.setCamera(mCamera);
        }

        mPreview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                System.out.println("LAYOUT COMPLETE");
                //At this point the layout is complete and the
                //dimensions of myView and any child views are known.
                mPreview.setVisibility(View.VISIBLE);
            }
        });


    }

    //Releases the fragment_camera
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

    public void hide() {
        view.setVisibility(View.INVISIBLE);
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
        if (!isSwitchingCamera) {
            isSwitchingCamera = true;
            Thread flipCamThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (safeCameraOpen(cameraId)) {
                        mPreview.switchCamera(mCamera, getActivity(), cameraId);
                        isSwitchingCamera = false;
                    }
                }
            });
            flipCamThread.start();
        }
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
        flashOn = !flashOn;
        if (flashOn) {
            flashBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.flashonx3));
        } else {
            flashBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.flashx3));
        }
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
        turnOnFlash(flashOn);
        mCamera.takePicture(null, null, pictureCallback);
    }

    public void turnOnFlash(boolean flashOn) {
        if (hasFlash()) {
            Camera.Parameters p = mCamera.getParameters();

            p.setFlashMode(flashOn ? Camera.Parameters.FLASH_MODE_ON : Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(p);
        } else {
            //Implement flash for selfie here
        }
    }

    private boolean hasFlash() {
        Camera.Parameters params = mCamera.getParameters();
        List<String> flashModes = params.getSupportedFlashModes();
        if (flashModes == null) {
            return false;
        }

        for (String flashMode : flashModes) {
            if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
                return true;
            }
        }

        return false;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container2, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    //Camera layout_edit fragment callback
    @Override
    public void cameraEditOnClose() {
        mCamera.startPreview();
    }

    @Override
    public void cameraEditOnPost(PostModel postModel) {
        callback.cameraFragmentDidPost(postModel);
    }

}
