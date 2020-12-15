package com.app.xq.dingding.guidingmask;

import android.animation.ObjectAnimator;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.app.xq.dingding.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Android-小强 on 2019/2/15 12:31
 * @email: 15075818555@163.com
 * @ProjectName: progress
 * @Package: com.xiaoqiang.cusview.guidingmask
 * @ClassName: guidingMask
 */
public class GuidingMaskView extends FrameLayout implements IGuidingMaskView {
    /**
     * 扣层的矩形区域
     */
    private RectF mRectTop;
    private List<RectF> mRectFList;
    /**
     * 蒙层的颜色
     */
    private int mBottomColor;
    /**
     * 绘制的画笔
     */
    private Paint mPaint;
    /**
     * 绘制的画笔  外圆角 蒙层
     */
    private Paint mPaintRoundBorder;
    /**
     * 圆边框颜色
     */
    private int mRounBorderColor;
    /**
     * 边框
     */
    private int mRounBorderWidth;
    /**
     * 画笔的模式
     */
    private Xfermode mXfermode;
    /**
     * 顶部的 状态栏的高度
     */
    private float statBarHeight;
    /**
     * 圆形 或者 矩形 的模式
     */
    private GuidingMode mMode;
    /**
     * 矩形区域 向外扩张的间距
     */
    private float leftSpacing, topSpacing, riteSpacing, bottomSpacing;
    /**
     * 真实画的矩形
     */
    private RectF rect;
    /**
     * 点击遮罩部分区域的点击事件
     */
    private Callback mCallback;
    /**
     * 是否添加圆边框
     */
    private boolean addRoundBorder = false;
    /**
     * 矩形圆角
     */
    private float mRadius;
    /**
     * 是否开启动画
     */
    private boolean openAnimation;

    private GuidingMaskView(Context context) {
        this(context, null);
    }

    public GuidingMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuidingMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void init(Context context) {
        mBottomColor = Color.parseColor("#BF000000");
        mRounBorderWidth = ScreenUtils.dpToPxInt(11, context);
        mRounBorderColor = Color.parseColor("#33FFFFFF");
        mPaint = new Paint();
        mPaintRoundBorder = new Paint();
        mPaint.setAntiAlias(true);
        mPaintRoundBorder.setAntiAlias(true);
        mPaintRoundBorder.setStyle(Paint.Style.STROKE);
        mPaintRoundBorder.setStrokeWidth(mRounBorderWidth);
        mPaintRoundBorder.setColor(mRounBorderColor);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        statBarHeight = ScreenUtils.getStatusBarHeight(context);
        mMode = GuidingMode.ROUND_RECT;
        mRectFList = new ArrayList<>(1);
    }

    @Override
    public void setBottomColor(int bottomColor) {
        mBottomColor = bottomColor;
    }

    @Override
    public void setGuidingMode(GuidingMode mode) {
        mMode = mode;
    }

    @Override
    public void setMaskLayer(RectF rectTop, boolean removeStatusBar) {
        if (removeStatusBar) {
            rectTop.top -= statBarHeight;
            rectTop.bottom -= statBarHeight;
        }
        mRectTop = rectTop;
        mRectFList.clear();
        invalidate();
    }

    @Override
    public ValueAnimator setMaskLayerForAnimator(RectF rectTop, boolean removeStatusBar) {
        if (removeStatusBar) {
            rectTop.top -= statBarHeight;
            rectTop.bottom -= statBarHeight;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new RectEvaluator(),
                new Rect((int) mRectTop.left, (int) mRectTop.top, (int) mRectTop.right, (int) mRectTop.bottom),
                new Rect((int) rectTop.left, (int) rectTop.top, (int) rectTop.right, (int) rectTop.bottom));


        valueAnimator.addUpdateListener(animation -> {
            Rect animatedValue = (Rect) animation.getAnimatedValue();
            mRectTop = new RectF(animatedValue);
            invalidate();

        });
        mRectFList.clear();
        return valueAnimator;
    }

    @Override
    public void addMaskLayer(RectF rectTop, boolean removeStatusBar) {
        if (removeStatusBar) {
            rectTop.top -= statBarHeight;
            rectTop.bottom -= statBarHeight;
        }
        mRectFList.add(rectTop);
    }

    @Override
    public void setSpacing(float leftSpacing, float topSpacing, float riteSpacing, float bottomSpacing) {
        this.leftSpacing = leftSpacing;
        this.topSpacing = topSpacing;
        this.riteSpacing = riteSpacing;
        this.bottomSpacing = bottomSpacing;
    }

    @Override
    public void drawCircle(Canvas canvas, Paint paint) {
        if (mRectTop == null) {
            throw new NullPointerException("您必须设置遮罩目标view的位置【void setMaskLayer(RectF rectTop, boolean removeStatusBar)】");
        }
        float size = Math.max(mRectTop.width(), mRectTop.height());
        float radius = size * 0.5F;
        canvas.drawCircle(mRectTop.left + mRectTop.width() * 0.5F,
                mRectTop.top + mRectTop.height() * 0.5F, radius, mPaint);
    }

    @Override
    public void drawRoundedRect(Canvas canvas, Paint paint) {
        if (mRectTop == null) {
            throw new NullPointerException("您必须设置遮罩目标view的位置【void setMaskLayer(RectF rectTop, boolean removeStatusBar)】");
        }
        rect = new RectF(
                mRectTop.left - leftSpacing,
                mRectTop.top - topSpacing,
                mRectTop.right + riteSpacing,
                mRectTop.bottom + bottomSpacing);
        float min = mRadius;
        canvas.drawRoundRect(rect, min, min, paint);
    }

    @Override
    public void drawRoundedRectList(Canvas canvas, Paint paint) {
        if (mRectFList.isEmpty()) {
            return;
        }
        for (int i = 0; i < mRectFList.size(); i++) {
            float min = mRadius;
            canvas.drawRoundRect(mRectFList.get(i), min, min, paint);
        }
    }

    @Override
    public void drawRoundBorder(Canvas canvas, Paint paint) {
        if (mRectTop == null) {
            throw new NullPointerException("您必须设置遮罩目标view的位置【void setMaskLayer(RectF rectTop, boolean removeStatusBar)】");
        }
        rect = new RectF(
                mRectTop.left - leftSpacing,
                mRectTop.top - topSpacing,
                mRectTop.right + riteSpacing,
                mRectTop.bottom + bottomSpacing);


        float min = mRadius;
        canvas.drawRoundRect(rect, min, min, paint);
    }

    @Override
    public void show(Activity activity) {

        if (activity == null) {
            Toast.makeText(activity, "activity->null", Toast.LENGTH_SHORT).show();
            return;
        }
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView instanceof ViewGroup) {
            dismiss((ViewGroup) contentView);
            createAnimation();
            ((ViewGroup) contentView).addView(this,
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 添加动画
     */
    private void createAnimation() {
        if (!openAnimation) {
            return;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, View.ALPHA.getName(), 0.2F, 1F);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    @Override
    public void show(ViewGroup view) {
        if (view == null) {
            Toast.makeText(getContext(), "ViewGroup->null", Toast.LENGTH_SHORT).show();
            return;
        }
        dismiss(view);
        createAnimation();
        view.addView(this, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void dismiss(ViewGroup parent) {
        if (parent != null) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = parent.getChildAt(i);
                if (childAt != null && childAt instanceof GuidingMaskView) {
                    parent.removeView(childAt);
                }
            }
        }
    }

    @Override
    public void dismiss() {
        dismiss((ViewGroup) getParent());
    }

    @Override
    public RectF getMaskViewLayer() {
        return mRectTop;
    }

    public static Builder newBuilder(Context context) {
        Builder builder = new Builder(context);
        return builder;
    }

    public void setOpenAnimation(boolean openAnimation) {
        this.openAnimation = openAnimation;
    }

    public float getLeftSpacing() {
        return leftSpacing;
    }

    public void setLeftSpacing(float leftSpacing) {
        this.leftSpacing = leftSpacing;
    }

    public float getRiteSpacing() {
        return riteSpacing;
    }

    public void setRiteSpacing(float riteSpacing) {
        this.riteSpacing = riteSpacing;
    }

    public float getBottomSpacing() {
        return bottomSpacing;
    }

    public void setBottomSpacing(float bottomSpacing) {
        this.bottomSpacing = bottomSpacing;
    }

    public float getTopSpacing() {
        return topSpacing;
    }

    public void setTopSpacing(float topSpacing) {
        this.topSpacing = topSpacing;
    }

    public void setRounBorderWidth(int rounBorderWidth) {
        mRounBorderWidth = rounBorderWidth * 2;
        mPaintRoundBorder.setStrokeWidth(mRounBorderWidth);
    }

    public void setRounBorderColor(int rounBorderColor) {
        this.mRounBorderColor = rounBorderColor;
        mPaintRoundBorder.setColor(rounBorderColor);
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public void setAddRoundBorder(boolean addRoundBorder) {
        this.addRoundBorder = addRoundBorder;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            float x = ev.getX();
            float y = ev.getY();
            if (mRectTop.contains(x, y)) {
                if (mCallback != null) {
                    if (!mCallback.onClickTop()) {
                        requestDisallowInterceptTouchEvent(true);
                        dismiss();
                        return false;
                    }
                    return true;
                } else {
                    if (getParent() instanceof ViewGroup) {
                        dismiss((ViewGroup) getParent());
                    }
                }
                requestDisallowInterceptTouchEvent(true);
                return false;
            }
            requestDisallowInterceptTouchEvent(false);
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        setBackgroundColor(Color.TRANSPARENT);
        int iCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        //画蒙层
        canvas.drawColor(mBottomColor);

        if (addRoundBorder) {
            drawRoundBorder(canvas, mPaintRoundBorder);
        }

        //画扣层
        mPaint.setXfermode(mXfermode);
        if (mMode == GuidingMode.CIRCLE) {
            //画圆圈
            drawCircle(canvas, mPaint);
        } else if (mMode == GuidingMode.ROUND_RECT) {
            //画圆角矩形
            drawRoundedRect(canvas, mPaint);
        }
        drawRoundedRectList(canvas, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(iCount);

        super.dispatchDraw(canvas);
    }

    public interface Callback {
        /**
         * 扣层的点击事件
         */
        boolean onClickTop();
    }

    public static class Builder {
        private Context mContext;

        private GuidingMaskView guidingMaskView;

        public Builder(Context context) {
            mContext = context;
            guidingMaskView = new GuidingMaskView(mContext);
        }

        /**
         * 设置背景 蒙层颜色
         *
         * @param colot
         * @return
         */
        public Builder setBackColor(int colot) {
            guidingMaskView.setBottomColor(colot);
            return this;
        }

        /**
         * 如果 设置 外边框 圆形边框 这是设置颜色
         *
         * @param rounBorderColor
         * @return
         */
        public Builder setRounBorderColor(int rounBorderColor) {
            guidingMaskView.setRounBorderColor(rounBorderColor);
            return this;
        }

        /**
         * 如果 设置 外边框 圆形边框 这是设置圆形边框 线条宽度
         *
         * @param mRounBorderWidth
         * @return
         */
        public Builder setRounBorderWidth(int mRounBorderWidth) {
            guidingMaskView.setRounBorderWidth(mRounBorderWidth);
            return this;
        }

        /**
         * 如果 设置 遮罩模式
         *
         * @param mode
         * @return
         */
        public Builder setGuidingMode(GuidingMode mode) {
            guidingMaskView.setGuidingMode(mode);
            return this;
        }

        /**
         * 设置点击遮罩部分区域 的点击事件
         *
         * @param callback
         * @return
         */
        public Builder setCallback(Callback callback) {
            guidingMaskView.setCallback(callback);
            return this;
        }

        /**
         * 设置点击遮罩部分区域 的点击事件
         *
         * @param rectTop
         * @param removeStatusBar
         * @return
         */
        public Builder setMaskLayer(RectF rectTop, boolean removeStatusBar) {
            guidingMaskView.setMaskLayer(rectTop, removeStatusBar);
            return this;
        }

        /**
         * 设置是否添加  外边框 外形圆角包裹 边框
         *
         * @param addRoundBorder
         * @return
         */
        public Builder setAddRoundBorder(boolean addRoundBorder) {
            guidingMaskView.setAddRoundBorder(addRoundBorder);
            return this;
        }

        /**
         * 设置是否添加  外边框 外形圆角包裹 边框
         *
         * @param mRadius
         * @return
         */
        public Builder setRadius(float mRadius) {
            guidingMaskView.setRadius(mRadius);
            return this;
        }

        /**
         * 设置显示时是否开启动画
         *
         * @param openAnimation
         * @return
         */
        public Builder setOpenAnimation(boolean openAnimation) {
            guidingMaskView.setOpenAnimation(openAnimation);
            return this;
        }

        /**
         * 如果 设置 遮罩区域的间距
         *
         * @param leftSpacing
         * @param topSpacing
         * @param riteSpacing
         * @param bottomSpacing
         * @return
         */
        public Builder setSpacing(float leftSpacing, float topSpacing, float riteSpacing, float bottomSpacing) {
            guidingMaskView.setSpacing(leftSpacing, topSpacing, riteSpacing, bottomSpacing);
            return this;
        }

        public GuidingMaskView build() {
            return guidingMaskView;
        }
    }


}
