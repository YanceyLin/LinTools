package com.lin.toollibs.views.life.fragment;


import android.app.Fragment;

import com.lin.toollibs.views.life.listener.XLifecycleListener;

/**
 * <pre>
 *     author : 林熠贤
 *     e-mail : linyixianwork@163.com
 *     time   : 2020/4/13
 *     desc   : 滚动管理所需的活动碎片
 *     version: 1.0
 * </pre>
 */
public class XLifeFragment extends Fragment {

    private XLifecycleListener lifecycleListener;

    public XLifecycleListener getLifecycleListener() {
        return lifecycleListener;
    }

    public void setLifecycleListener(XLifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (lifecycleListener != null) {
            lifecycleListener.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lifecycleListener != null) {
            lifecycleListener.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lifecycleListener != null) {
            lifecycleListener.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lifecycleListener != null) {
            lifecycleListener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lifecycleListener != null) {
            lifecycleListener.onDestroy();
        }
    }
}
