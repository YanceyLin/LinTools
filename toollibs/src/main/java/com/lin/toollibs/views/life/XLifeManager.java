package com.lin.toollibs.views.life;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.lin.toollibs.views.life.fragment.XLifeFragment;
import com.lin.toollibs.views.life.fragment.XLifeSupportFragment;
import com.lin.toollibs.views.life.listener.XLifecycleListener;

/**
 * <pre>
 *     author : 林熠贤
 *     e-mail : linyixianwork@163.com
 *     time   : 2020/4/13
 *     desc   : 滚动声明协助者
 *     version: 1.0
 * </pre>
 */
public class XLifeManager implements XLifecycleListener {
    public static final String FRAGMENT_MANAGER_TAG = "RollFragmentTag";

    public static XLifeManager with(Context context) {
        return !checkNull(context) ? new XLifeManager(context) : null;
    }

    public static XLifeManager with(Fragment supportFragment) {
        if (!checkNull(supportFragment)) {
            View view = supportFragment.getView();
            return new XLifeManager(view);
        } else {
            return null;
        }
    }

    public static XLifeManager with(android.app.Fragment fragment) {
        if (!checkNull(fragment)) {
            View view = fragment.getView();
            return new XLifeManager(view);
        } else {
            return null;
        }
    }

    public static XLifeManager with(View view) {
        return !checkNull(view) ? new XLifeManager(view) : null;
    }

    private static boolean checkNull(Object obj) {
        boolean result = obj == null;
        if (result) {
            Log.e("XLifeManager", "You cannot manage with a null");
        }
        return result;
    }


    private Context context;
    private View view;
    private XLifecycleListener lifecycleListener;

    public XLifeManager(Context context) {
        this.context = context;
    }

    public XLifeManager(View view) {
        this.view = view;
        if (view != null) {
            context = view.getContext();
        }
    }

    public XLifeManager setLifeCycleListener(XLifecycleListener listener) {
        this.lifecycleListener = listener;
        return this;
    }

    public void build() {
        if (context == null && view == null) {
            throw new IllegalArgumentException("You cannot load on a null Context/SupportFragment/Fragment");
        }
        if (context != null && context instanceof Activity) {
            register(context);
        }
    }

    private void register(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            XLifeSupportFragment fragment = (XLifeSupportFragment) fm.findFragmentByTag(FRAGMENT_MANAGER_TAG);
            fragment = fragment == null ? new XLifeSupportFragment() : fragment;
            fragment.setLifecycleListener(this);

            if (!fragment.isAdded()) {
                fm.beginTransaction().add(fragment, FRAGMENT_MANAGER_TAG).commitAllowingStateLoss();
            }
        } else if (context instanceof Activity && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Activity activity = (Activity) context;
            XLifeFragment fragment = (XLifeFragment) activity.getFragmentManager().findFragmentByTag(FRAGMENT_MANAGER_TAG);
            fragment = fragment == null ? new XLifeFragment() : fragment;
            fragment.setLifecycleListener(this);
            if (!fragment.isAdded()) {
                activity.getFragmentManager().beginTransaction().add(fragment, FRAGMENT_MANAGER_TAG).commit();
            }
        }
    }

    @Override
    public void onStart() {
        if (lifecycleListener != null) {
            lifecycleListener.onStart();
        }
        if (view != null) {
            view.addOnAttachStateChangeListener(this);
        }
    }

    @Override
    public void onResume() {
        if (lifecycleListener != null) {
            lifecycleListener.onResume();
        }
    }

    @Override
    public void onPause() {
        if (lifecycleListener != null) {
            lifecycleListener.onPause();
        }
    }

    @Override
    public void onStop() {
        if (lifecycleListener != null) {
            lifecycleListener.onStop();
        }
        if (view != null) {
            view.removeOnAttachStateChangeListener(this);
        }
    }

    @Override
    public void onDestroy() {
        if (lifecycleListener != null) {
            lifecycleListener.onDestroy();
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        if (lifecycleListener != null) {
            lifecycleListener.onViewAttachedToWindow(v);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (lifecycleListener != null) {
            lifecycleListener.onViewDetachedFromWindow(v);
        }
    }
}
