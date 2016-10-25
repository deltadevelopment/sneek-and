package no.twomonkeys.sneek.app.components.camera;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.filters.IFBrannanFilter;
import no.twomonkeys.sneek.app.components.filters.IFInkwellFilter;
import no.twomonkeys.sneek.app.components.filters.IFWaldenFilter;
import no.twomonkeys.sneek.app.shared.NetworkCallback;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DiskHelper;
import no.twomonkeys.sneek.app.shared.helpers.GraphicsHelper;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.ErrorModel;
import no.twomonkeys.sneek.app.shared.models.PostModel;

/**
 * Created by simenlie on 05.10.2016.
 */

public class CameraEditFragment extends Fragment {
    View view;
    ImageView imageView;
    ImageButton cancelBtn, cancelEditBtn, saveToDiskbBtn;
    Callback callback;
    RelativeLayout bottomBv;
    Button captionBtn;
    RelativeLayout captionView;
    EditText editText;
    Button expireButton;
    int expireIndex;
    Bitmap photoTaken;
    private LinearLayout progressLayout;
    private TextView progressTxtView;
    private GPUImage mGPUImage;
    private int filterIndex;
    private boolean movingBackwards;
    private float filterTuning;
    GPUImageContrastFilter contrastFilter;
    ImageButton ceSendBtn;
    Timer timer;
    String caption;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void cameraEditOnClose();
        void cameraEditOnPost(PostModel postModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera_edit, container, false);


        view.setOnTouchListener(new RepeatListener(100, 0, new RepeatListener.Listener() {
            @Override
            public void onSingleTap() {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swapFilter();
                            }
                        });
                    }
                }, 120);
            }

            @Override
            public void onLongPress() {
                timer.cancel();
                tuneFilter();
                Log.v("LONGPRESS", "LONGPRESS");
            }
        }));

        imageView = getImageView();
        cancelBtn = getCancelBtn();
        bottomBv = getBottomBv();
        captionBtn = getCaptionBtn();
        captionView = getCaptionView();
        editText = getEditText();
        cancelEditBtn = getCancelEditBtn();
        expireButton = getExpireButton();
        saveToDiskbBtn = getSaveToDiskbBtn();
        expireIndex = 1;
        progressLayout = getProgressLayout();
        progressTxtView = getProgressTxtView();
        mGPUImage = new GPUImage(getActivity());
        contrastFilter = new GPUImageContrastFilter();
        ceSendBtn = getCeSendBtn();
        // mGPUImage.setGLSurfaceView((GLSurfaceView) view.findViewById(R.id.surface_view));
        attachImage();
        return view;
    }

    private ImageButton getCeSendBtn() {
        if (ceSendBtn == null) {
            ImageButton ceSendBtn = (ImageButton) view.findViewById(R.id.ceSendBtn);
            ceSendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postMoment();
                }
            });
            this.ceSendBtn = ceSendBtn;
        }
        return this.ceSendBtn;
    }

    private void postMoment()
    {
        cancelClick();
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        final PostModel postModel = new PostModel();
        postModel.setMedia_type(0);
        postModel.setExpireIndex(expireIndex);
        postModel.setImage(bitmap);
        postModel.setCaption(caption);
        postModel.generateFile(getActivity(), new SimpleCallback2() {
            @Override
            public void callbackCall() {
                callback.cameraEditOnPost(postModel);
            }
        });
    }

    private LinearLayout getProgressLayout() {
        if (progressLayout == null) {
            LinearLayout progressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);
            progressLayout.getBackground().setAlpha(UIHelper.toAlpha(0.5f));
            this.progressLayout = progressLayout;
        }
        return this.progressLayout;
    }

    private TextView getProgressTxtView() {
        if (progressTxtView == null) {
            TextView progressTxtView = (TextView) view.findViewById(R.id.progress_text);
            this.progressTxtView = progressTxtView;
        }
        return this.progressTxtView;
    }

    private void attachImage() {
        boolean isSelfie = getArguments().getBoolean("isSelfie");
        byte[] data = getArguments().getByteArray("imageData");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (isSelfie) {
            photoTaken = GraphicsHelper.mirrorImage(bitmap);
        } else {
            photoTaken = bitmap;
        }
        int width = photoTaken.getWidth();
        double height = width * 1.333333;
        photoTaken = Bitmap.createBitmap(photoTaken, 0, 0, photoTaken.getWidth(), (int) height);

        imageView.setImageBitmap(photoTaken);
    }


    // Views
    public ImageView getImageView() {
        if (this.imageView == null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view);

            int maxWidth = UIHelper.screenWidth(this.getActivity());
            double height = maxWidth * 1.333333;
            int finalHeight = UIHelper.screenHeight(this.getActivity());

            imageView.getLayoutParams().height = (int) height;


            this.imageView = imageView;
        }
        return this.imageView;
    }

    private RelativeLayout getBottomBv() {
        if (bottomBv == null) {
            RelativeLayout bottomBv = (RelativeLayout) view.findViewById(R.id.camera_edit_bottom_bar);
            bottomBv.getBackground().setAlpha(UIHelper.toAlpha(0.95f));

            int maxWidth = UIHelper.screenWidth(this.getActivity());
            double height = maxWidth * 1.333333;
            int finalHeight = UIHelper.screenHeight(this.getActivity());

            bottomBv.getLayoutParams().height = finalHeight - (int) height;
            this.bottomBv = bottomBv;
        }
        return this.bottomBv;
    }

    public ImageButton getCancelEditBtn() {
        if (this.cancelEditBtn == null) {
            ImageButton cancelEditBtn = (ImageButton) view.findViewById(R.id.cancel_caption);
            cancelEditBtn.setColorFilter(ContextCompat.getColor(getActivity(), R.color.blackColor));
            cancelEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissCaptionEdit();
                }
            });
            this.cancelEditBtn = cancelBtn;
        }
        return this.cancelEditBtn;
    }

    public ImageButton getCancelBtn() {
        if (this.cancelBtn == null) {
            ImageButton cancelBtn = (ImageButton) view.findViewById(R.id.cancel_btn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelClick();
                }
            });
            this.cancelBtn = cancelBtn;
        }
        return this.cancelBtn;
    }

    private void cancelClick() {
        callback.cameraEditOnClose();
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    public Button getExpireButton() {
        if (this.expireButton == null) {
            Button expireButton = (Button) view.findViewById(R.id.expire_btn);
            expireButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expireClick();
                }
            });
            this.expireButton = expireButton;
        }
        return this.expireButton;
    }

    public void expireClick() {
        expireIndex++;
        if (expireIndex == 3) {
            expireIndex = 0;
        }

        String expireTxt;
        switch (expireIndex) {
            case 0:
                expireTxt = "1 " + getString(R.string.hour_txt);
                break;
            case 1:
                expireTxt = "1 " + getString(R.string.day_txt);
                break;
            case 2:
                expireTxt = "1 " + getString(R.string.week_txt);
                break;
            default:
                expireTxt = "1 " + getString(R.string.hour_txt);
                break;
        }

        Log.v("expire index", "is " + expireIndex + " " + expireTxt);

        expireButton.setText(expireTxt);
    }

    public ImageButton getSaveToDiskbBtn() {
        if (this.saveToDiskbBtn == null) {
            ImageButton saveToDiskbBtn = (ImageButton) view.findViewById(R.id.save_disk_button);
            saveToDiskbBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveToDiskClick();
                }
            });
            this.saveToDiskbBtn = saveToDiskbBtn;
        }
        return this.saveToDiskbBtn;
    }

    public void saveToDiskClick() {
        progressLayout.setAlpha(1.0f);
        progressLayout.setVisibility(View.INVISIBLE);
        progressTxtView.setText("Saving...");
        progressLayout.setVisibility(View.VISIBLE);
        DiskHelper.insertImage(getActivity().getContentResolver(), photoTaken, "sneek-img", "taken from sneek app", new NetworkCallback() {
            @Override
            public void exec(ErrorModel errorModel) {
                Log.v("Stored", "stored");
                progressTxtView.setText("Saved!");
                progressLayout.animate().
                        alpha(UIHelper.toAlpha(0)).
                        setStartDelay(150).
                        setDuration(150);
            }
        });
    }

    public Button getCaptionBtn() {
        if (this.captionBtn == null) {
            Button captionBtn = (Button) view.findViewById(R.id.caption_btn);
            captionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    captionClick();
                }
            });
            this.captionBtn = captionBtn;
        }
        return this.captionBtn;
    }

    public void captionClick() {
        //animate in layout_edit view here
        animateCaptionIn();
    }

    public void animateCaptionIn() {
        cancelBtn.setVisibility(View.INVISIBLE);
        captionView.setVisibility(View.VISIBLE);
        captionView.animate().translationY(0).setDuration(250);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void animateCaptionOut() {
        cancelBtn.setVisibility(View.VISIBLE);
        captionView.setVisibility(View.VISIBLE);
        captionView.animate().translationY(UIHelper.screenHeight(getActivity())).setDuration(250);
    }

    public RelativeLayout getCaptionView() {
        if (this.captionView == null) {
            RelativeLayout captionView = (RelativeLayout) view.findViewById(R.id.caption_view);
            captionView.setY(UIHelper.screenHeight(getActivity()));
            captionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    captionViewClick();
                }
            });
            captionView.getBackground().setAlpha(255);
            this.captionView = captionView;
        }
        return this.captionView;
    }

    public EditText getEditText() {
        if (this.editText == null) {
            final EditText editText = (EditText) view.findViewById(R.id.edit_text);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (null != editText.getLayout() && editText.getLayout().getLineCount() > 2) {
                        editText.getText().delete(editText.getText().length() - 1, editText.getText().length());
                    }
                }
            });

            this.editText = editText;
        }
        return this.editText;
    }

    public void captionViewClick() {
        dismissCaptionEdit();
    }

    public void dismissCaptionEdit() {

        if (editText.getText().length() > 0){
            caption = editText.getText().toString();
        }
        String captionTxt = editText.getText().length() == 0 ? getString(R.string.add_caption_txt) : caption;
        captionBtn.setText(captionTxt);
        animateCaptionOut();
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().
                        getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    //Filter methods

    private void swapFilter() {
        filterTuning = 1;
        contrastFilter.setContrast(filterTuning);
        filterIndex++;
        if (filterIndex > 3) {
            filterIndex = 0;
        }
        changeFilter();
    }

    private GPUImageFilter getFilter() {
        GPUImageFilter filter;
        switch (filterIndex) {
            case 0:
                filter = new GPUImageFilter();
                break;
            case 1:
                filter = new IFInkwellFilter(getActivity());
                break;
            case 2:
                filter = new IFWaldenFilter(getActivity());
                break;
            case 3:
                filter = new IFBrannanFilter(getActivity());
                break;
            default:
                filter = new GPUImageFilter();
                break;
        }
        return filter;
    }

    private void changeFilter() {
        GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
        mGPUImage.deleteImage();
        filterGroup.addFilter(getFilter());
        filterGroup.addFilter(contrastFilter);
        mGPUImage.setFilter(filterGroup);
        mGPUImage.setImage(photoTaken);
        Bitmap bitmap = mGPUImage.getBitmapWithFilterApplied();
        imageView.setImageBitmap(bitmap);
    }

    private void tuneFilter() {

        if (movingBackwards) {
            filterTuning -= 0.02;
        } else {
            filterTuning += 0.02;
        }
        if (filterTuning >= 2) {
            movingBackwards = true;
            filterTuning = 2;
        }
        if (filterTuning <= 1) {
            filterTuning = 1;
            movingBackwards = false;
        }
        Log.v("Contrast is", "contrast " + filterTuning);
        contrastFilter.setContrast(filterTuning);

        changeFilter();
    }

}
