package no.twomonkeys.sneek.app.components.camera;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.shared.helpers.GraphicsHelper;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;

/**
 * Created by simenlie on 05.10.2016.
 */

public class CameraEditFragment extends Fragment {
    View view;
    ImageView imageView;
    ImageButton cancelBtn, cancelEditBtn;
    Callback callback;
    RelativeLayout bottomBv;
    Button captionBtn;
    RelativeLayout captionView;
    EditText editText;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void cameraEditOnClose();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camera_edit, container, false);
        imageView = getImageView();
        cancelBtn = getCancelBtn();
        bottomBv = getBottomBv();
        captionBtn = getCaptionBtn();
        captionView = getCaptionView();
        editText = getEditText();
        cancelEditBtn = getCancelEditBtn();

        attachImage();

        return view;
    }

    private void attachImage() {
        boolean isSelfie = getArguments().getBoolean("isSelfie");
        byte[] data = getArguments().getByteArray("imageData");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap processedBitmap;
        if (isSelfie) {
            processedBitmap = GraphicsHelper.mirrorImage(bitmap);
        } else {
            processedBitmap = bitmap;
        }
        int width = processedBitmap.getWidth();
        double height = width * 1.333333;
        processedBitmap = Bitmap.createBitmap(processedBitmap, 0, 0, processedBitmap.getWidth(), (int) height);


        imageView.setImageBitmap(processedBitmap);
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
                    captionViewClick();
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
        //animate in edit view here
        animateCaptionIn();
    }

    public void animateCaptionIn() {
        captionView.setVisibility(View.VISIBLE);
        captionView.animate().translationY(0).setDuration(250);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
    public void animateCaptionOut() {
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
            this.captionView = captionView;
        }
        return this.captionView;
    }

    public EditText getEditText() {
        if (this.editText == null) {
            EditText editText = (EditText) view.findViewById(R.id.edit_text);
            this.editText = editText;
        }
        return this.editText;
    }

    public void captionViewClick() {
        String captionTxt = editText.getText().length() == 0 ? "Tap to add a caption" : editText.getText().toString();
        captionBtn.setText(captionTxt);
        animateCaptionOut();
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().
                        getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
