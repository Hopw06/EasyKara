package com.example.easykara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.easykara.service.media.PlayerService;
import com.example.easykara.view.fragments.downloadscreen.DownloadScreenFragment;
import com.example.easykara.view.fragments.medialistscreen.MediaScreenFragment;
import com.example.easykara.view.fragments.menuscreen.MenuScreenFragment;
import com.example.easykara.view.fragments.playscreen.PlayScreenFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final String MENU_SCREEN_TAG = "menu_screen_tag";
    private static final String PLAY_SCREEN_TAG = "play_screen_tag";
    private static final String DOWNLOAD_SCREEN_TAG = "download_screen_tag";
    private static final String MEDIA_LIST_SCREEN_TAG = "media_list_screen_tag";

    private static final String KEY_FRAGMENT_TAG = "key_fragment_tag";

    private Button mBtnPlayScreen;
    private Button mBtnMenuScreen;
    private Button mBtnDownloadScreen;
    private Button mBtnMediaScreen;

    private String mCurrentTagFragment = PLAY_SCREEN_TAG;

    private PlayerService mPlayerService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(getBaseContext(), PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        mBound = false;
    }

    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentTagFragment = savedInstanceState.getString(KEY_FRAGMENT_TAG);
        }
        Fragment fragment = mCurrentTagFragment == null ? new PlayScreenFragment()
                : initFragment(mCurrentTagFragment);
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, mCurrentTagFragment)
                    .commit();
        }

        mBtnPlayScreen = findViewById(R.id.playScreenBtn);
        mBtnMenuScreen = findViewById(R.id.menuScreenBtn);
        mBtnDownloadScreen = findViewById(R.id.downloadScreenBtn);
        mBtnMediaScreen = findViewById(R.id.mediaScreenBtn);

        mBtnPlayScreen.setOnClickListener(this);
        mBtnMenuScreen.setOnClickListener(this);
        mBtnDownloadScreen.setOnClickListener(this);
        mBtnMediaScreen.setOnClickListener(this);
        setBtnColor();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString(KEY_FRAGMENT_TAG, mCurrentTagFragment);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View view) {
        loadFragment(view.getId());
        setBtnColor();
    }

    private void loadFragment(int viewId) {
        String tag = getFragmentTag(viewId);
        if (mCurrentTagFragment != null && mCurrentTagFragment.equalsIgnoreCase(tag))
            return;
        Log.i(TAG, "loadFragment: tag = " + tag);
        if (tag == null || TextUtils.isEmpty(tag)) return;
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment nextFragment = fragmentManager.findFragmentByTag(tag);
        Fragment currentFragment = fragmentManager.findFragmentByTag(mCurrentTagFragment);

        if (nextFragment == null) {
            nextFragment = initFragment(tag);
        }
        if (nextFragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (currentFragment != null) {
                transaction.detach(currentFragment);
            }
            transaction.add(R.id.container, nextFragment, tag);
            transaction.attach(nextFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            mCurrentTagFragment = tag;
            transaction.commit();
        }
    }

    private Fragment initFragment(String tag) {
        switch (tag) {
            case DOWNLOAD_SCREEN_TAG:
                return new DownloadScreenFragment();
            case PLAY_SCREEN_TAG:
                return new PlayScreenFragment();
            case MEDIA_LIST_SCREEN_TAG:
                return new MediaScreenFragment();
            case MENU_SCREEN_TAG:
                return new MenuScreenFragment();
            default:
                return null;
        }
    }

    private String getFragmentTag(int viewId) {
        if (viewId == R.id.downloadScreenBtn) {
            return DOWNLOAD_SCREEN_TAG;
        } else if (viewId == R.id.playScreenBtn) {
            return PLAY_SCREEN_TAG;
        } else if (viewId == R.id.mediaScreenBtn) {
            return MEDIA_LIST_SCREEN_TAG;
        } else if (viewId == R.id.menuScreenBtn) {
            return MENU_SCREEN_TAG;
        } else {
            return null;
        }
    }

    private void setBtnColor() {
        mBtnMenuScreen.setTextColor(ContextCompat.getColor(this, R.color.black));
        mBtnPlayScreen.setTextColor(ContextCompat.getColor(this, R.color.black));
        mBtnDownloadScreen.setTextColor(ContextCompat.getColor(this, R.color.black));
        mBtnMediaScreen.setTextColor(ContextCompat.getColor(this, R.color.black));
        switch (mCurrentTagFragment) {
            case DOWNLOAD_SCREEN_TAG:
                mBtnDownloadScreen.setTextColor(ContextCompat.getColor(this, R.color.purple_500));
                break;
            case PLAY_SCREEN_TAG:
                mBtnPlayScreen.setTextColor(ContextCompat.getColor(this, R.color.purple_500));
                break;
            case MEDIA_LIST_SCREEN_TAG:
                mBtnMediaScreen.setTextColor(ContextCompat.getColor(this, R.color.purple_500));
                break;
            case MENU_SCREEN_TAG:
                mBtnMenuScreen.setTextColor(ContextCompat.getColor(this, R.color.purple_500));
                break;
            default:
                break;
        }
    }

    public PlayerService getPlayer() {
        return this.mPlayerService;
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mPlayerService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}