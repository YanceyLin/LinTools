package com.lxb.lintools.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LinViewPager extends ViewPager {


    /**
     * 是否可以滑动
     */
    private boolean isScrollable = false;


    public LinViewPager(Context context) {
        super(context);
    }

    public LinViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isScrollable) {
            return super.onTouchEvent(arg0);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isScrollable) {
            return super.onInterceptTouchEvent(arg0);
        }
        return false;
    }


    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    /**
     * 设置能否滑动
     *
     * @param isScrollable 是否可滑动
     */
    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

}