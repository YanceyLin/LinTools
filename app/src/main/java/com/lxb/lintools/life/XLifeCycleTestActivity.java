package com.lxb.lintools.life;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lin.toollibs.views.life.XLifeManager;
import com.lin.toollibs.views.life.listener.XLifecycleListener;
import com.lxb.lintools.R;
import com.lxb.lintools.adapter.ViewPageTabAdapter;
import com.lxb.lintools.views.LinViewPager;

import java.util.ArrayList;
import java.util.List;

public class XLifeCycleTestActivity extends AppCompatActivity implements View.OnClickListener {

    private LinViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();

    private XLifecycleListener lifecycleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlife_cycle_test);

        initView();
        init();

        config();
    }

    private void initView() {
        findViewById(R.id.btn_fragment_first).setOnClickListener(this);
        findViewById(R.id.btn_fragment_second).setOnClickListener(this);
        mViewPager = findViewById(R.id.view_pager_lin);

        mFragmentList.clear();
        XLifeTestFragment fragment1 = new XLifeTestFragment();
        XLifeTestFragment fragment2 = new XLifeTestFragment();
        fragment1.setText("测试碎片1");
        fragment2.setText("2222222222222");
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mViewPager.setAdapter(new ViewPageTabAdapter(getSupportFragmentManager(), mFragmentList));
    }

    private void init() {

        lifecycleListener = new XLifecycleListener() {

            @Override
            public void onStart() {
                Toast.makeText(XLifeCycleTestActivity.this, "Activity—onStart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResume() {
                Toast.makeText(XLifeCycleTestActivity.this, "Activity—onResume", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }

            @Override
            public void onPause() {
                Toast.makeText(XLifeCycleTestActivity.this, "Activity—onPause", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStop() {
                Toast.makeText(XLifeCycleTestActivity.this, "Activity—onStop", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDestroy() {
                Toast.makeText(XLifeCycleTestActivity.this, "Activity—onDestroy", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFragmentFocusChanged(boolean inFocused) {

            }

        };
    }

    private void config() {
        XLifeTestFragment fragment = (XLifeTestFragment) mFragmentList.get(0);
        fragment.setLifecycleListener(lifecycleListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_fragment_first) {
            mViewPager.setCurrentItem(0);
        } else if (v.getId() == R.id.btn_fragment_second) {
            mViewPager.setCurrentItem(1);
        }
    }
}
