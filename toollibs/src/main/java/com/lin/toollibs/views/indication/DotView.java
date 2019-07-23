package com.lin.toollibs.views.indication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * 用贝塞尔曲线绘制点移动的控件尝试
 */
public class DotView extends View {

    //用于画圆和画点的画笔
    private Paint mCirclePaint;
    private Paint mPointPaint;

    private Path mPath = new Path();
    private Path mPath2 = new Path();

    //间隔距离
    private float distance = 80;
    //起始圆初始半径
    private float mRadius = 50;
    //起始圆变化半径
    private float mChangeRadius;
    //辅助圆变化半径
    private float mSupportChangeRadius;
    //起始圆圆心坐标
    float mCircleX;
    float mCircleY;
    //辅助圆圆心坐标
    float mSupportCircleX;
    float mSupportCircleY;

    //第一部分运动进度
    private float mProgress = 0;
    //第二部分运动进度
    private float mProgress2 = 0;
    //整体运动进度 也是原始进度
    private float mOriginProgress;

    //第一阶段运动
    private int MOVE_STEP_ONE = 1;
    //第二阶段运动
    private int MOVE_STEP_TWO = 2;

    float controlPointX;
    float controlPointY;
    float mStartX;
    float mStartY;
    float mEndX;
    float mEndY;
    private int mDrection;
    //向右滑 向左滚动
    public static int DIRECTION_LEFT = 1;
    //向左滑 向右滚动
    public static int DIRECTION_RIGHT = 2;

    Interpolator accelerateinterpolator = new AccelerateDecelerateInterpolator();

    public DotView(Context context) {
        this(context, null);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        moveToNext();
    }

    private void initPaint() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(0xFF000000);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setDither(true);
    }

    /**
     * 为了能够正常展示，对于宽高进行最小的限制
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = (int) (mRadius * 2 * 2 + distance + getPaddingLeft() + getPaddingRight());
        int height = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom());

        int mHeight, mWidth;

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = Math.min(widthSize, width);
        } else {
            mWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = Math.min(heightSize, height);
        } else {
            mHeight = heightSize;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.drawCircle(mCircleX, mCircleY, mChangeRadius, mCirclePaint);
        canvas.drawCircle(mSupportCircleX, mSupportCircleY, mSupportChangeRadius, mCirclePaint);
        canvas.drawPath(mPath, mCirclePaint);
        //标出贝塞尔点
        canvas.drawCircle(controlPointX, controlPointY, 5, mPointPaint);
        canvas.drawCircle(mStartX, mStartY, 5, mPointPaint);
        canvas.drawCircle(mEndX, mEndY, 5, mPointPaint);
        canvas.restore();

    }


    /**
     * 向右移动
     */
    private void moveToNext() {
        mPath.reset();
        mPath2.reset();
        //通过动画加速器的计算，获取变化率
        float mRadiusProgress = accelerateinterpolator.getInterpolation(mOriginProgress);

        //第一部分：起始圆和辅助圆 的圆心移动和半径缩放变化***************************
        //起始的圆心
        mCircleX = getValueByMoveStep(getCenterPointXAt(1), getCenterPointXAt(2) - mRadius, MOVE_STEP_TWO);
        mCircleY = mRadius;
        //获取起始圆的半径
        mChangeRadius = getValueByProgress(mRadius, 0, mRadiusProgress);

        //辅助圆的圆心
        mSupportCircleX = getValueByMoveStep(getCenterPointXAt(1) + mRadius, getCenterPointXAt(2), MOVE_STEP_ONE);
        mSupportCircleY = mRadius;
        //辅助圆半径
        mSupportChangeRadius = getValueByProgress(0, mRadius, mRadiusProgress);

        //第二部分：贝塞尔曲线的三点位置变化——起点，终点和控制点的移动变化********************************

        //起点：从起始圆上45度到0度
        //获取起点与起始圆心的弧度（先获取角度，再将角度转化成弧度）
        double radian = Math.toRadians(getValueByProgress(45, 0, mProgress));
        //计算起始点在X轴方向与圆心的距离
        float mXDistance = (float) (Math.sin(radian) * mChangeRadius);
        //计算起始点在X轴方向与圆心的距离
        float mYDistance = (float) (Math.cos(radian) * mChangeRadius);
        //起点位置
        mStartX = mCircleX + mXDistance;
        mStartY = mCircleY - mYDistance;

        //终点：随辅助圆平移后，从0度到45度（所以需要采用进度2，因为在这之前它一直处于辅助圆0度位置，随着辅助圆的平移和缩放二变化而已）
        double supportRadian = Math.toRadians(getValueByProgress(0, 45, mProgress2));
        float mSupportXDistance = (float) (Math.sin(supportRadian) * mSupportChangeRadius);
        float mSupportYDistance = (float) (Math.cos(supportRadian) * mSupportChangeRadius);
        //终点位置
        mEndX = mSupportCircleX - mSupportXDistance;
        mEndY = mSupportCircleY - mSupportYDistance;

        //控制点：随着总进度，从最开始的起始圆的最右侧-》平移到-》最后的辅助圆的最左侧
        controlPointX = getValueByProgress(2 * mRadius, getCenterPointXAt(2) - mRadius, mOriginProgress);
        controlPointY = mRadius;

        //所有的点都标记好了，进行闭合区域的框定了
        //先将第一笔移到起点位置
        mPath.moveTo(mStartX, mStartY);

        //形成闭合区域
//        mPath.quadTo(controlPointX, controlPointY, mEndX, mEndY);
//        mPath.lineTo(mEndX, mSupportCircleY + mSupportYDistance);
//        mPath.quadTo(controlPointX, controlPointY, mStartX, mStartY + 2 * mYDistance);
//        mPath.lineTo(mStartX, mStartY);

//        //闭合区域的框定
        mPath.quadTo(controlPointX, controlPointY, mEndX, mEndY);
        mPath.lineTo(mEndX, mSupportCircleY + mSupportYDistance);
        mPath.quadTo(controlPointX, controlPointY, mStartX, mStartY + 2 * mYDistance);
        mPath.lineTo(mStartX, mStartY);
    }

    /**
     * 向左移动(与向右过程大致相同，把他的过程颠倒过来即可)
     */
    private void moveToPrivious() {
        mPath.reset();
        mPath2.reset();
        float mRadiusProgress = accelerateinterpolator.getInterpolation(mOriginProgress);

        //起始圆
        mCircleX = getValueByMoveStep(getCenterPointXAt(2), getCenterPointXAt(1) + mRadius, MOVE_STEP_TWO);
        mCircleY = mRadius;
        mChangeRadius = getValueByProgress(mRadius, 0, mRadiusProgress);

        //辅助圆
        mSupportCircleX = getValueByMoveStep(getCenterPointXAt(2) - mRadius, getCenterPointXAt(1), MOVE_STEP_ONE);
        mSupportCircleY = mRadius;
        mSupportChangeRadius = getValueByProgress(0, mRadius, mRadiusProgress);

        //第二部分：贝塞尔曲线的三点位置变化——起点，终点和控制点的移动变化********************************

        //起点：从起始圆上45度到0度
        //获取起点与起始圆心的弧度（先获取角度，再将角度转化成弧度）
        double radian = Math.toRadians(getValueByProgress(45, 0, mProgress));
        //计算起始点在X轴方向与圆心的距离
        float mXDistance = (float) (Math.sin(radian) * mChangeRadius);
        //计算起始点在X轴方向与圆心的距离
        float mYDistance = (float) (Math.cos(radian) * mChangeRadius);
        //起点位置
        mStartX = mCircleX - mXDistance;
        mStartY = mCircleY - mYDistance;

        //终点：随辅助圆平移后，从0度到45度（所以需要采用进度2，因为在这之前它一直处于辅助圆0度位置，随着辅助圆的平移和缩放二变化而已）
        double supportRadian = Math.toRadians(getValueByProgress(0, 45, mProgress2));
        float mSupportXDistance = (float) (Math.sin(supportRadian) * mSupportChangeRadius);
        float mSupportYDistance = (float) (Math.cos(supportRadian) * mSupportChangeRadius);
        //终点位置
        mEndX = mSupportCircleX + mSupportXDistance;
        mEndY = mSupportCircleY - mSupportYDistance;

        //控制点：随着总进度，从最开始的起始圆的最右侧-》平移到-》最后的辅助圆的最左侧
        controlPointX = getValueByProgress(getCenterPointXAt(2) - mRadius, 2 * mRadius, mOriginProgress);
        controlPointY = mRadius;

        //所有的点都标记好了，进行闭合区域的框定了
        //先将第一笔移到起点位置
        mPath.moveTo(mStartX, mStartY);
        //闭合区域的框定
        mPath.quadTo(controlPointX, controlPointY, mEndX, mEndY);
        mPath.lineTo(mEndX, mSupportCircleY + mSupportYDistance);
        mPath.quadTo(controlPointX, 2 * mRadius - controlPointY, mStartX, mRadius + mYDistance);
        mPath.lineTo(mStartX, mStartY);
    }

    /**
     * 直接根据进度计算变化结果
     */
    private float getValueByProgress(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    /**
     * 根据方向计算变化的结果
     */
    private float getValueByMoveStep(float start, float end, int moveStep) {
        if (moveStep == MOVE_STEP_ONE) {
            return start + (end - start) * mProgress;
        } else {
            return start + (end - start) * mProgress2;
        }
    }

    /**
     * 获取贝塞尔曲线中两个圆对应的圆心X坐标
     * 第一个圆直接是半径，第二个圆圆形就是直径加距离再加本身的半径了
     */
    private float getCenterPointXAt(int circleIndex) {
        return circleIndex == 1 ? mRadius : (mRadius * 3 + distance);
    }

    /**
     * 设置当前方向
     */
    public void setDirection(int direction) {
        mDrection = direction;
    }

    /**
     * 设置进度
     */
    public void setProgress(float progress) {
        mOriginProgress = progress;
        if (progress <= 0.5) {
            mProgress = progress / 0.5f;
            mProgress2 = 0;
        } else {
            mProgress2 = (progress - 0.5f) / 0.5f;
            mProgress = 1;
        }
        if (mDrection == DIRECTION_RIGHT) {
            moveToNext();
        } else {
            moveToPrivious();
        }

        invalidate();
    }

    /**
     * 重置进度
     */
    public void resetProgress() {
        mProgress = 0;
        mProgress2 = 0;
        mOriginProgress = 0;
    }
}
