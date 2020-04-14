package com.lin.toollibs.views.life.listener;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

/**
 * <pre>
 *     author : 林熠贤
 *     e-mail : linyixianwork@163.com
 *     time   : 2020/4/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface XLifecycleListener extends View.OnAttachStateChangeListener {

    /**
     * Callback for when {@link Fragment:onStart()}} or {@link
     * Activity:onStart()} is called.
     */
    void onStart();

    /**
     * Callback for when {@link Fragment:onResume()} ()}} or {@link
     * Activity:onResume()} ()} is called.
     */
    void onResume();

    /**
     * Callback for when {@link Fragment:onPause()} ()}} or {@link
     * Activity:onPause()} ()} is called.
     */
    void onPause();

    /**
     * Callback for when {@link Fragment:onStop()}} or {@link
     * Activity:onStop()}} is called.
     */
    void onStop();

    /**
     * Callback for when {@link Fragment:onDestroy()}} or {@link
     * Activity:onDestroy()} is called.
     */
    void onDestroy();

}
