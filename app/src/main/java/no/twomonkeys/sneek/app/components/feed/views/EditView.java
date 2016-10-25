package no.twomonkeys.sneek.app.components.feed.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import no.twomonkeys.sneek.R;
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
    ImageButton sendBtn;

    Handler handler;
    Runnable r;

    public interface Callback {
        void editViewSizeChange(int sizeChange);
        void editViewDidPost(String postMsg);
        void editViewDidClickCamera();
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
        inflater.inflate(R.layout.layout_edit, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        editEt = getEditEt();
        editRl = (RelativeLayout) findViewById(R.id.editRl);
        originalHeight = editRl.getHeight();
        this.cameraBtn = getCameraBtn();
        toolbarLl = (LinearLayout) findViewById(R.id.toolbarLl);
        this.sendBtn = getSendBtn();

    }

    public ImageButton getSendBtn() {
        if (this.sendBtn == null) {
            ImageButton sendBtn = (ImageButton) findViewById(R.id.send_button);
            sendBtn.setVisibility(GONE);
            sendBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.editViewDidPost(editEt.getText().toString());
                    editEt.setText("");
                }
            });
            this.sendBtn = sendBtn;
        }
        return this.sendBtn;
    }

    public ImageButton getCameraBtn() {
        if (this.cameraBtn == null) {
            ImageButton cameraBtn = (ImageButton) findViewById(R.id.editCameraBtn);
            cameraBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Clicked now");
                    callback.editViewDidClickCamera();
                }
            });
            this.cameraBtn = cameraBtn;
        }
        return cameraBtn;
    }

    public EditText getEditEt() {
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
                        sendBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sendx3));

                    } else {
                        Typeface type = Typeface.createFromAsset(context.getAssets(), "arial-rounded-mt-bold.ttf");
                        editEt.setTypeface(type);
                        sendBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.senddisabledx3));
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
        this.sendBtn.setVisibility(VISIBLE);

        LayoutParams lp = (RelativeLayout.LayoutParams) editEt.getLayoutParams();
        LayoutParams lp2 = (RelativeLayout.LayoutParams) cameraBtn.getLayoutParams();
        editEt.setPadding(0, 0, 0, UIHelper.dpToPx(getContext(), 40));
        //lp.setMargins(0, 0, 0, UIHelper.dpToPx(getContext(), 40));
        //lp2.setMargins(0, 0, 0, UIHelper.dpToPx(getContext(), 50));
        editEt.setCursorVisible(true);
    }

    public void editModeEnded() {
        cameraBtn.setVisibility(VISIBLE);
        toolbarLl.setVisibility(INVISIBLE);
        this.sendBtn.setVisibility(INVISIBLE);
        editEt.setPadding(0, 0, 0, UIHelper.dpToPx(getContext(), 0));
        editEt.setCursorVisible(false);
    }


}
