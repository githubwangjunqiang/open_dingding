package com.app.xq.dingding


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*
import kotlin.math.floor

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/26 16:26
 */
class DiamondView : View {
    var bitmapNo: Bitmap? = null
    var bitmapTrue: Bitmap? = null

    var fraction: Double = 2.2

    init {
        bitmapNo = BitmapFactory.decodeResource(resources, R.mipmap.app_green_diamond_no)
        bitmapTrue = BitmapFactory.decodeResource(resources, R.mipmap.app_green_diamond)

    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        var size = MeasureSpec.getSize(heightMeasureSpec)
        var sizeWidth = 0
        if (mode == MeasureSpec.EXACTLY) {
        }
        if (mode == MeasureSpec.AT_MOST) {
            size = 18F.dpInt()
        }
        if (mode == MeasureSpec.UNSPECIFIED) {
            size = 18F.dpInt()
        }
        sizeWidth = size * 4

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(sizeWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        )
    }

    var draw: Boolean = false
    var rectf = RectF()
    var rectNo = RectF()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (bitmapNo == null || bitmapTrue == null) {
            return
        }
        rectf.set(0F, 0F, height.toFloat(), height.toFloat())
        rectNo.set(rectf)
        for (index in 0..3) {
            rectNo.set(
                (height * index).toFloat(),
                0F,
                (height * (index + 1)).toFloat(),
                height.toFloat()
            )
            canvas?.drawBitmap(bitmapNo!!, null, rectNo, null)
        }


        if (fraction < 0.0) {
            return
        }
        if (fraction > 4) {
            fraction = 4.0
        }
        draw = false
        for (data in 0 until fraction.toInt()) {
            rectf.set(
                data * height.toFloat(),
                0F,
                data * height + height.toFloat(),
                height.toFloat()
            )
            canvas?.drawBitmap(
                bitmapTrue!!, null, rectf, null
            )
            draw = true
        }


        val floor = floor(fraction)
        val compareTo = fraction.compareTo(floor)
        val b = compareTo == 0
        val infinite = fraction.isInfinite()
        val returnValue = b && !infinite
        var half: Boolean = returnValue



        if (!half) {
            if (rectf.bottom <= 0) {
                rectf.bottom = height.toFloat()
            }
            if (draw) {
                rectf.left = rectf.right
                rectf.right = rectf.right + height / 2
            } else {
                rectf.left = 0F
                rectf.right = height * 0.5F
            }


            canvas?.drawBitmap(
                bitmapTrue!!, Rect(
                    0, 0, bitmapTrue!!.width / 2,
                    bitmapTrue!!.height
                ), rectf, null
            )
        }

    }

}

