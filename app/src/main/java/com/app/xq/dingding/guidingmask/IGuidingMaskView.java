package com.app.xq.dingding.guidingmask;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.ViewGroup;

/**
 * @author Android-小强 on 2019/2/15 13:53
 * @email: 15075818555@163.com
 * @ProjectName: progress
 * @Package: com.xiaoqiang.cusview.guidingmask
 * @ClassName: IGuidingMaskView
 */
public interface IGuidingMaskView {
    /**
     * 初始化
     *
     * @param context
     */
    void init(Context context);

    /**
     * 设置蒙层底色
     *
     * @param bottomColor
     */
    void setBottomColor(int bottomColor);

    /**
     * 设置形式  圆形 还是 圆角矩形
     *
     * @param mode
     */
    void setGuidingMode(GuidingMode mode);


    /**
     * 设置 这招蒙层
     *
     * @param rectTop
     * @param removeStatusBar 是否去除 顶部状态栏 高度
     */
    void setMaskLayer(RectF rectTop, boolean removeStatusBar);

    /**
     * 返回动画
     * @param rectTop
     * @param removeStatusBar
     */
    ValueAnimator setMaskLayerForAnimator(RectF rectTop, boolean removeStatusBar);
    /**
     * 设置 这招蒙层
     *
     * @param rectTop
     * @param removeStatusBar 是否去除 顶部状态栏 高度
     */
    void addMaskLayer(RectF rectTop, boolean removeStatusBar);

    /**
     * 设置 间距
     *
     * @param leftSpacing
     * @param topSpacing
     */
    void setSpacing(float leftSpacing, float topSpacing, float riteSpacing, float bottomSpacing);

    /**
     * 画Circle形
     *
     * @param canvas
     * @param paint
     */
    void drawCircle(Canvas canvas, Paint paint);

    /**
     * 画圆角矩形
     *
     * @param canvas
     * @param paint
     */
    void drawRoundedRect(Canvas canvas, Paint paint);
    /**
     * 画圆角矩形   list
     *
     * @param canvas
     * @param paint
     */
    void drawRoundedRectList(Canvas canvas, Paint paint);

    /**
     * 画圆角矩形  的外边框
     *
     * @param canvas
     * @param paint
     */
    void drawRoundBorder(Canvas canvas, Paint paint);

    /**
     * 显示
     *
     * @param activity
     */
    void show(Activity activity);

    /**
     * 显示
     *
     * @param view
     */
    void show(ViewGroup view);

    /**
     * 隐藏 view
     *
     * @param viewGroup
     */
    void dismiss(ViewGroup viewGroup);

    /**
     * 隐藏 view
     */
    void dismiss();

    /**
     * 获取顶部 遮罩
     */
    RectF getMaskViewLayer();
}
