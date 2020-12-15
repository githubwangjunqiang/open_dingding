package com.app.xq.dingding.guidingmask;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.app.xq.dingding.R;


/**
 * @author Android-小强 on 2019/3/6 17:59
 * @email: 15075818555@163.com
 * @ProjectName: iMoney
 * @Package: com.app.aiyingli.imoney.costorview.guidingmask
 * @ClassName: MaskView
 */
public class MaskView extends LinearLayout {
    private TextView mTextViewBtn, mTextViewMsg;
    private FlickerView mFlickerView, mFlickerView1;
    private float mLeftMargin;
    private ValueAnimator valueAnimator;

    public MaskView(Context context) {
        this(context, null);
    }

    public MaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_layout_mine_mask, this);
        mTextViewBtn = findViewById(R.id.app_view_mine_mask_btn);
        if (mTextViewBtn instanceof TextView) {
//            ((TextView) mTextViewBtn).setTime(900);
        }
        mTextViewMsg = findViewById(R.id.app_view_mine_mask_tvmsg);
        mFlickerView = findViewById(R.id.app_view_mine_mask_flick);
        mFlickerView1 = findViewById(R.id.app_view_mine_mask_flick2);
    }

    public TextView getTextViewMsg() {
        return mTextViewMsg;
    }

    @Override
    protected void onDetachedFromWindow() {
        clearAnimations();
        super.onDetachedFromWindow();
    }

    /**
     * 停止动画
     */
    private void clearAnimations() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    public float getLeftMargin() {
        return mLeftMargin;
    }

    /**
     * 创建 父布局 参数
     *
     * @param margin
     * @return
     */
    public FrameLayout.LayoutParams createLayoutParams(int... margin) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (margin == null) {
            return params;
        }
        for (int i = 0; i < margin.length; i++) {
            switch (i) {
                case 0:
                    params.leftMargin = margin[i];
                    break;
                case 1:
                    params.topMargin = margin[i];
                    break;
                case 2:
                    params.rightMargin = margin[i];
                    break;
                case 3:
                    params.bottomMargin = margin[i];
                    break;
                default:
                    break;
            }
        }
        return params;
    }

    /**
     * 设置 顶部  间距
     *
     * @param padding
     */
    public ValueAnimator setLayoutParamsTopPaddingAnimatior(int padding) {

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null && layoutParams instanceof FrameLayout.LayoutParams) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(((FrameLayout.LayoutParams) layoutParams).topMargin, padding);
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();
                setLayoutParamsTopPadding(animatedValue);
            });
            return valueAnimator;
        }
        return ValueAnimator.ofInt(0);

    }

    /**
     * 设置 顶部  间距
     *
     * @param padding
     */
    public void setLayoutParamsTopPadding(int padding) {


        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null && layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).topMargin = padding;
            setLayoutParams(layoutParams);
        }
    }

    /**
     * 设置 两个 箭头的 左边距 默认 居中显示
     *
     * @param padingleft
     */
    public ValueAnimator setFlickerViewPadingleftForAnimatior(float padingleft) {
        float curr = mLeftMargin;
        mLeftMargin = padingleft;


        valueAnimator = ValueAnimator.ofFloat(curr, padingleft);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            setFlickerViewPadingleft((int) (animatedValue + 0.5));
        });
        return valueAnimator;
    }

    /**
     * 设置箭头 左边距
     *
     * @param padingleft
     */
    public void setFlickerViewPadingleft(int padingleft) {
        mLeftMargin = padingleft;
        if (mFlickerView != null && mFlickerView1 != null) {
            ViewGroup.LayoutParams layoutParams = mFlickerView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).leftMargin = padingleft;
                mFlickerView.setLayoutParams(layoutParams);
            }
            ViewGroup.LayoutParams layoutParams1 = mFlickerView1.getLayoutParams();
            if (layoutParams1 != null && layoutParams1 instanceof LayoutParams) {
                ((LayoutParams) layoutParams1).leftMargin = padingleft;
                mFlickerView1.setLayoutParams(layoutParams1);
            }
        }
    }

    /**
     * 设置 两个 箭头的 左边距 默认 居中显示
     *
     * @param gravity
     */
    public void setFlickerViewGravity(int gravity) {
        if (mFlickerView != null && mFlickerView1 != null) {
            ViewGroup.LayoutParams layoutParams = mFlickerView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).gravity = gravity;
                ((LayoutParams) layoutParams).leftMargin = 0;
                mFlickerView.setLayoutParams(layoutParams);
            }
            ViewGroup.LayoutParams layoutParams1 = mFlickerView1.getLayoutParams();
            if (layoutParams1 != null && layoutParams1 instanceof LayoutParams) {
                ((LayoutParams) layoutParams1).gravity = gravity;
                ((LayoutParams) layoutParams1).leftMargin = 0;
                mFlickerView1.setLayoutParams(layoutParams1);
            }
        }
    }

    /**
     * 设置 按钮 点击事件
     *
     * @param onClickListener
     */
    public void setBtnOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null && mTextViewBtn != null) {
            mTextViewBtn.setOnClickListener(onClickListener);
        }
    }

    /**
     * 隐藏 按钮
     */
    public void setButtonGong() {
        if (mTextViewBtn != null) {
            mTextViewBtn.setVisibility(GONE);
        }
    }

    /**
     * 给按钮设置 图标 会清除按钮的显示文字
     */
    public void setButtonIcon(@DrawableRes int mipmap, boolean clearText, int width, int height, int... margin) {
        if (mTextViewBtn == null) {
            return;
        }
        if (clearText) {
            mTextViewBtn.setText("");
        }
        mTextViewBtn.setBackgroundResource(mipmap);
        LayoutParams layoutParams = null;
        try {
            layoutParams = (LayoutParams) mTextViewBtn.getLayoutParams();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (width > 0) {
            layoutParams.width = width;
        }
        if (height > 0) {
            layoutParams.height = height;
        }
        if (margin == null) {
            mTextViewBtn.setLayoutParams(layoutParams);
            return;
        }
        for (int i = 0; i < margin.length; i++) {
            switch (i) {
                case 0:
                    layoutParams.leftMargin = margin[i];
                    break;
                case 1:
                    layoutParams.topMargin = margin[i];
                    break;
                case 2:
                    layoutParams.rightMargin = margin[i];
                    break;
                case 3:
                    layoutParams.bottomMargin = margin[i];
                    break;
                default:
                    break;
            }
        }
        mTextViewBtn.setLayoutParams(layoutParams);
    }

    /**
     * 设置 显示文案
     *
     * @param msg
     */
    public void setContentMsg(String msg) {
        if (msg != null && mTextViewMsg != null) {
            mTextViewMsg.setText(msg);
        }
    }
}
