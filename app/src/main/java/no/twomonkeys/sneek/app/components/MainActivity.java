package no.twomonkeys.sneek.app.components;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.camera.CameraFragment;
import no.twomonkeys.sneek.app.components.channel.ChannelActivity;
import no.twomonkeys.sneek.app.components.channel.ChannelFragment;
import no.twomonkeys.sneek.app.components.feed.FeedFragment;
import no.twomonkeys.sneek.app.components.friends.FriendsFragment;
import no.twomonkeys.sneek.app.components.main.MainPagerAdapter;
import no.twomonkeys.sneek.app.components.profile.ProfileFragment;
import no.twomonkeys.sneek.app.shared.helpers.CacheKeyFactory;
import no.twomonkeys.sneek.app.shared.helpers.UIHelper;
import no.twomonkeys.sneek.app.shared.models.PostModel;
import no.twomonkeys.sneek.app.shared.models.UserModel;

public class MainActivity extends AppCompatActivity implements CameraFragment.Callback, MainPagerAdapter.Callback, ProfileFragment.Callback {
    MainPagerAdapter adapterViewPager;

    public static Activity mActivity;

    private ViewPager vpPager;
    private CameraFragment cameraFragment;
    private static int currentPage = 1;
    private boolean shouldRefreshCamera;
    private ProfileFragment profileFragment;
    private FeedFragment feedFragment;
    static String TAG = "MainActivity";

    //TextView toolbarTitle;
    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initConfiguration();

        removeTitleBar();
        setContentView(R.layout.activity_main);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setSwipeable(true);
        adapterViewPager = new MainPagerAdapter(getSupportFragmentManager(), this);
        adapterViewPager.addCallback(this);


        overridePendingTransition(0, 0);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("SCROLLED " + position + " : " + positionOffset + " : " + positionOffsetPixels + "");
                if (position == 0) {
                    if (positionOffset > 0.0) {
                        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    } else {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                    }

                    System.out.println("Size of toolbar is " + getStatusBarHeight());
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setActionBarTitle("Friends");
                        System.out.println("PageSelect: " + position);
                        // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                        break;
                    case 1:
                        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        setActionBarTitle("sneek");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("CHANGED");
            }
        });

        mActivity = this;

        vpPager.setAdapter(adapterViewPager);

        //Camera
        //getFragmentManager().beginTransaction().add(R.id.main, new CameraFragment()).commit();
        //  cameraFragment = (CameraFragment) getFragmentManager().findFragmentById(R.id.cameraFragment);
        //cameraFragment.addCallback(this);

        profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.profileFragment);
        profileFragment.addCallback(this);
        //cameraFragment.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




        //Prevent the screen from becoming darker
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        System.out.println("RESUMING");
        super.onResume();
    }


    public void setSwipeable(boolean swipeable) {
        vpPager.setSwipeable(swipeable);
    }

    private void setActionBarTitle(String title) {
        // toolbarTitle.setText(title);
    }

    private void removeTitleBar() {
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        System.out.println("BACK PRESSED");
        if (cameraFragment != null) {
            if (cameraFragment.isAdded()) {

                cameraFragmentTappedClose();
            } else {
                super.onBackPressed();

            }
        } else {
            super.onBackPressed();
        }

        //
    }

    public void initConfiguration() {
        CacheKeyFactory cacheKeyFactory = new CacheKeyFactory();

        Context context = getApplicationContext();
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)//
                .setBaseDirectoryPath(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getPackageName()))
                .setBaseDirectoryName("image")
                .setMaxCacheSize(100 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * ByteConstants.MB)
                .setVersion(1)
                .build();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)//
                .setMainDiskCacheConfig(diskCacheConfig)
                .setCacheKeyFactory(cacheKeyFactory)
                .build();

        Fresco.initialize(context, imagePipelineConfig);
        //Fresco.initialize(this);

        //DataHelper.setContext(this);
        //DataHelper.setMa(this);
        //Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Remove top bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().setStatusBarColor(getResources().getColor(R.color.cyan));
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //  WindowManager.LayoutParams attributes = getWindow().getAttributes();
        //  attributes.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        // getWindow().setAttributes(attributes);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


    }


    public ViewPager getVpPager() {
        return vpPager;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Set the viewPager to display item no 1
        vpPager.setCurrentItem(currentPage);
    }

    //Feed fragment delegates
    @Override
    public void feedFragmentOnFullScreenStart() {
        //toolbar.setVisibility(View.GONE);
    }

    @Override
    public void feedFragmentOnFullScreenEnd() {
        //toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void feedFragmentOnCameraClicked() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.fragment_container);
        rl.setVisibility(View.VISIBLE);
        if (cameraFragment == null) {
            cameraFragment = new CameraFragment();
        }
        cameraFragment.addCallback(this);
        replaceFragment(cameraFragment);
    }

    public void replaceFragment(android.app.Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void feedFragmentOnProfileClick(final UserModel userModel) {
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.profileFragmentContainer);
        rl.setBackground(ContextCompat.getDrawable(this, R.color.transparent));
        rl.setTranslationY(UIHelper.screenHeight(this));
        rl.setVisibility(View.VISIBLE);
        rl.animate().translationY(0).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                profileFragment.updateUser(userModel);
                // rl.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void mainPagerAdapterDidTapUser() {
        currentPage = 0;
        /*
        Intent i = new Intent(getApplicationContext(), ChannelActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(i, 0);
        overridePendingTransition(0, 0);
        cameraFragment = null;
        // startActivity(i);*/
        /*
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.stream_container);
        rl.setVisibility(View.VISIBLE);

        ChannelFragment channelFragment = new ChannelFragment();
        replaceFragment2(channelFragment);
        */


    }

    public void replaceFragment2(android.app.Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.stream_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void cameraFragmentTappedClose() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.fragment_container);
        rl.setVisibility(View.GONE);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    @Override
    public void cameraFragmentDidPost(PostModel postModel) {
        cameraFragmentTappedClose();
        adapterViewPager.getFeedFragment().addNewImagePost(postModel);
    }

    @Override
    public void profileFragmentOnFullScreenStart() {

    }

    @Override
    public void profileFragmentOnFullScreenEnd() {

    }

    @Override
    public void profileFragmentOnCameraClicked() {

    }

    @Override
    public void profileFragmentOnClose() {
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.profileFragmentContainer);
        rl.setTranslationY(0);
        rl.animate().translationY(UIHelper.screenHeight(this)).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                rl.setVisibility(View.INVISIBLE);
            }
        });
    }
}

