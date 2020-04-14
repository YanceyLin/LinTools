package com.lin.toollibs.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * <pre>
 *     author : 林熠贤
 *     e-mail : linyixianwork@163.com
 *     time   : 2020/2/27
 *     desc   : 缩放布局控件
 *     version: 1.0
 * </pre>
 */
public class LZoomLayout extends FrameLayout {

    private long motionEventRecordTime = -1;
    private ScaleAnimation zoomInAnimation;
    private ScaleAnimation zoomOutAnimation;

    public LZoomLayout(Context context) {
        this(context, null);
    }

    public LZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        /**
         * 初始化动画
         */
        zoomInAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInAnimation.setFillAfter(true);
        zoomInAnimation.setDuration(150);
        zoomOutAnimation = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoomOutAnimation.setFillAfter(true);
        zoomOutAnimation.setDuration(150);
        zoomInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                invalidate();
                startAnimation(zoomOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        zoomOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 拦截触摸事件，自行触发一次触摸事件处理（触摸事件处理中，如果有经过处理，则会将该事件记录起来)
     *
     * @param ev 触摸事件
     * @return 是否消费掉该事件，不予传递下去
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 如果该触摸事件还未处理过（以触发时间为唯一标识）且为点下动作，则进行缩放动画并记录下来该事件，同时将该事件消费掉。否则不予处理，传递下去
     *
     * @param event 触摸事件
     * @return 是否消费掉该事件，不予传递下去
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //事件是否在这里处理过
        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEventTime() != motionEventRecordTime) {
            motionEventRecordTime = event.getEventTime();
            startAnimation(zoomInAnimation);//执行动画
            return true;//将事件消费掉
        }
        return super.onTouchEvent(event);
    }

    /**************************************************/

    public static ZoomBuilder on(View child) {
        return new ZoomBuilder(child);
    }

    public static class ZoomBuilder {
        private final Context context;
        private final View child;

        public ZoomBuilder(View child) {
            this.child = child;
            context = child.getContext();
        }

        public LZoomLayout create() {
            LZoomLayout layout = new LZoomLayout(context);

            ViewGroup.LayoutParams params = child.getLayoutParams();
            ViewGroup parent = (ViewGroup) child.getParent();
            if (parent != null) {
                int index = parent.indexOfChild(child);
                parent.removeView(child);
                layout.addView(child, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                parent.addView(layout, index, params);
            }

            return layout;
        }
    }
}
