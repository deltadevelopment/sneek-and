package no.twomonkeys.sneek.app.components.feed;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.camera.CameraEditFragment;
import no.twomonkeys.sneek.app.shared.SimpleCallback2;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;
import no.twomonkeys.sneek.app.shared.helpers.KeyboardUtil;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;

/**
 * Created by simenlie on 19.10.2016.
 */

public class EditView extends RelativeLayout {
    EditText editEt;
    ImageButton cameraBtn;
    Context context;
    Callback callback;
    RelativeLayout editRl;
    LinearLayout toolbarLl;
    int originalHeight;

    Handler handler;
    Runnable r;

    public interface Callback {
        void editViewSizeChange(int sizeChange);
    }

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public EditView(Context context) {
        super(context);
        initializeViews(context);
    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public EditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.edit, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editEt = getEditEt();
        editRl = (RelativeLayout) findViewById(R.id.editRl);
        originalHeight = editRl.getHeight();
        this.cameraBtn = getCameraBtn();
        toolbarLl = (LinearLayout) findViewById(R.id.toolbarLl);
    }

    public ImageButton getCameraBtn() {
        if (this.cameraBtn == null) {
            ImageButton cameraBtn = (ImageButton) findViewById(R.id.editCameraBtn);
            this.cameraBtn = cameraBtn;
        }
        return cameraBtn;
    }

    private EditText getEditEt() {
        if (this.editEt == null) {
            final EditText editEt = (EditText) findViewById(R.id.editEt);
            Typeface type = Typeface.createFromAsset(context.getAssets(), "arial-rounded-mt-bold.ttf");
            editEt.setTypeface(type);
            editEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editEt.getText().length() > 0) {
                        editEt.setTypeface(Typeface.DEFAULT);

                    } else {
                        Typeface type = Typeface.createFromAsset(context.getAssets(), "arial-rounded-mt-bold.ttf");
                        editEt.setTypeface(type);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                    //System.out.println("AFTER now is " + editEt.getHeight() + " : " + editEt.getMeasuredHeight());
                    //callback.editViewSizeChange(editEt.getHeight());
                    if (handler != null) {
                        handler.removeCallbacks(r);
                    }

                    handler = new Handler();
                    r = new Runnable() {
                        @Override
                        public void run() {
                            //System.out.println("AFTER now is " + editEt.getHeight() + " : " + editEt.getMeasuredHeight());
                            callback.editViewSizeChange(editEt.getHeight());
                        }
                    };
                    handler.postDelayed(r, 50);
                }
            });
            this.editEt = editEt;
        }
        return this.editEt;
    }

    public void editModeStarted() {
        System.out.println("STARTED EDITE MODE");
        cameraBtn.setVisibility(INVISIBLE);
        toolbarLl.setVisibility(VISIBLE);

        LayoutParams lp = (RelativeLayout.LayoutParams) editEt.getLayoutParams();
        LayoutParams lp2 = (RelativeLayout.LayoutParams) cameraBtn.getLayoutParams();
        editEt.setPadding(0,0,0,UIHelper.dpToPx(getContext(), 40));
        //lp.setMargins(0, 0, 0, UIHelper.dpToPx(getContext(), 40));
        //lp2.setMargins(0, 0, 0, UIHelper.dpToPx(getContext(), 50));
        editEt.setCursorVisible(true);
    }

    public void editModeEnded() {
        cameraBtn.setVisibility(VISIBLE);
        toolbarLl.setVisibility(INVISIBLE);
        editEt.setPadding(0,0,0,UIHelper.dpToPx(getContext(), 0));
        editEt.setCursorVisible(false);
    }
}
