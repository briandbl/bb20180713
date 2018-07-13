package com.performance.ubt.sdkTest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.performance.ubt.sdkTest.ui.AcceleratorFragment;
import com.performance.ubt.sdkTest.ui.LedFragment;
import com.performance.ubt.sdkTest.ui.MotionFragment;
import com.performance.ubt.sdkTest.ui.NoSlidingViewPaper;
import com.performance.ubt.sdkTest.ui.RecorderFragment;
import com.performance.ubt.sdkTest.ui.IflytekRecoderFragment;
import com.performance.ubt.sdkTest.ui.SysFragment;
import com.ubtechinc.alpha.sdk.AlphaRobotApi;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NoSlidingViewPaper mViewPager;
    private int TEST_ITEM_NUMBERS=6;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_led:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_motion:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_speech:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_system:
                    mViewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_accelerometer:
                    mViewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlphaRobotApi.get().initializ(this);
        setContentView(R.layout.activity_main);
        mViewPager = (NoSlidingViewPaper) findViewById(R.id.vp_main_container);
        final ArrayList<Fragment> fgLists = new ArrayList<>(TEST_ITEM_NUMBERS);
        fgLists.add(new LedFragment());
        fgLists.add(new MotionFragment());
        //Test standard Android Audio Recorder function
       //fgLists.add(new RecorderFragment());
       //Test Iflytek standard Recorder function
        //fgLists.add(new IflytekRecoderFragment());
       fgLists.add(new SysFragment());
        fgLists.add(new AcceleratorFragment());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    protected void onDestroy() {
        AlphaRobotApi.get().destroy();
        super.onDestroy();
    }
}
