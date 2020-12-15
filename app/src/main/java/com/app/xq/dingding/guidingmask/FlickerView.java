package com.app.xq.dingding.guidingmask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


import com.app.xq.dingding.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * @author Android-小强 on 2019/1/25 14:47
 * @email: 15075818555@163.com
 * @ProjectName: iMoney
 * @Package: com.app.aiyingli.imoney.costorview
 * @ClassName: FlickerTextView
 */
public class FlickerView extends View {
    private int mViewWidth;
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private int mTranslate;
    private int startColor;
    private int centerColor;
    private int endColor;
    private Disposable mDisposable;
    private Path mPath;
    private Rect mRect;

    /**
     * 画几个箭头
     */
    private int count = 1;
    /**
     * 箭头宽度
     */
    private float paintWidth;
    /**
     * 动画时间
     */
    private long duration;
    /**
     * 画笔是否圆角  圆角半径多少
     */
    private float paintRoundGap;

    public FlickerView(Context context) {
        this(context, null);
    }

    public FlickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    /**
     * 初始化
     *
     * @param attributeSet
     */
    private void initView(AttributeSet attributeSet) {
        mPath = new Path();
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        boolean style_Fill = false;
        setStartColor(Color.parseColor("#a97300"));
        setCenterColor(Color.parseColor("#33a97300"));
        setEndColor(Color.parseColor("#e6a97300"));
        duration = 100;

        if (attributeSet != null) {
            TypedArray typedValue = getContext().obtainStyledAttributes(attributeSet, R.styleable.FlickerView);
            if (typedValue != null) {
                startColor = typedValue.getColor(R.styleable.FlickerView_flv_startColor, Color.parseColor("#a97300"));
                centerColor = typedValue.getColor(R.styleable.FlickerView_flv_centerColor, Color.parseColor("#33a97300"));
                endColor = typedValue.getColor(R.styleable.FlickerView_flv_endColor, Color.parseColor("#e6a97300"));
                style_Fill = typedValue.getBoolean(R.styleable.FlickerView_flv_paint_style_fill,
                        style_Fill);
                paintRoundGap = typedValue.getDimension(R.styleable.FlickerView_flv_paint_round, 0.0F);
                count = typedValue.getInteger(R.styleable.FlickerView_flv_count, 1);
                paintWidth = typedValue.getDimension(R.styleable.FlickerView_flv_paintWidth, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics()));
                typedValue.recycle();
            }
        }
        if (paintWidth <= 0) {
            paintWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics());
        }
        mPaint.setStrokeWidth(paintWidth);
        mPaint.setColor(endColor);
        if (style_Fill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }


        if (paintRoundGap > 0) {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setPathEffect(new CornerPathEffect(paintRoundGap));
        }
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        postInvalidate();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mRect = new Rect(2, 5, w - 5, h - 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = mRect.width() / count;

        for (int i = 0; i < count; i++) {
            mPath.reset();
            mPath.moveTo(mRect.left + width * i, mRect.top);
            mPath.lineTo(mRect.left + width * (i + 1), mRect.top + mRect.height() / 2);
            mPath.lineTo(mRect.left + width * i, mRect.bottom);
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        clearTranslate();
        super.onDetachedFromWindow();
    }

    /**
     * 停止动画 清空动画
     */
    public void clearTranslate() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 开始动画
     */
    public void startTranslateAnimation() {
        if (getVisibility() == GONE) {
            return;
        }
        clearTranslate();
        mDisposable = Observable.interval(0, duration, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {

                    if (mLinearGradient == null) {
                        mLinearGradient = new LinearGradient(0, 0,
                                mViewWidth, 0,
                                new int[]{startColor, centerColor, endColor
                                        , startColor, centerColor, endColor
                                },
                                null, Shader.TileMode.CLAMP);
                        mPaint.setShader(mLinearGradient);
                        mGradientMatrix = new Matrix();
                    }
                    mTranslate += mViewWidth / 5;
                    if (mTranslate > 2 * mViewWidth) {
                        mTranslate = -mViewWidth;
                    }
                    mGradientMatrix.setTranslate(mTranslate, 0);
                    mLinearGradient.setLocalMatrix(mGradientMatrix);
                    postInvalidate();
                }, throwable -> {
                });
    }
}
