package com.lin.toollibs.views.life;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.lin.toollibs.R;
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
            return new XLifeManager(supportFragment);
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
    private Fragment fragment;
    private View view;
    private XLifecycleListener lifecycleListener;

    public XLifeManager(Context context) {
        this.context = context;
    }

    public XLifeManager(Fragment frag) {
        this.fragment = frag;
        if (frag == null) return;
        context = frag.getContext();
    }

    public XLifeManager(View view) {
        this.view = view;
        if (view == null) return;
        context = view.getContext();
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
            if (view != null) {
                fragment = getFragmentByView(view);
            }
        }
        if (fragment != null && fragment.getView() != null) {
            register(fragment);
        }
    }

    private Fragment getFragmentByView(View view) {
        if (view.getId() == View.NO_ID) {
            view.setId(R.id.life_manage_default_id);
        }
        if (context instanceof FragmentActivity) {
            for (Fragment fragment : ((FragmentActivity) context).getSupportFragmentManager().getFragments()) {
                if (fragment.getView() != null && fragment.getView().findViewById(view.getId()) == view) {
                    return fragment;
                }
            }
        }
        return null;
    }

    private void register(final Fragment fragment) {
        fragment.getView().getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (fragment.getView() == null) return;
                if (fragment.getView() instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) fragment.getView();
                    onFragmentFocusChanged(viewGroup.getFocusedChild() != null);
                } else {
                    onFragmentFocusChanged(fragment.getView().hasFocus());
                }
            }
        });
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
        } else if (context instanceof Activity) {
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
        if (lifecycleListener == null) return;
        lifecycleListener.onResume();
    }

    @Override
    public void onPause() {
        if (lifecycleListener == null) return;
        lifecycleListener.onPause();
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
        if (lifecycleListener == null) return;
        lifecycleListener.onDestroy();
    }

    @Override
    public void onFragmentFocusChanged(boolean inFocus) {
        if (lifecycleListener == null) return;
        lifecycleListener.onFragmentFocusChanged(inFocus);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        if (lifecycleListener == null) return;
        lifecycleListener.onViewAttachedToWindow(v);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (lifecycleListener == null) return;
        lifecycleListener.onViewDetachedFromWindow(v);
    }
}
