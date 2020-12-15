package com.app.xq.dingding

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/12/15 10:37
 */
class RadarView : View {
    constructor(context: Context) : this(
        context,
        null
    )

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context,
        attributeSet,
        0
    )

    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    )

    private val rectf = RectF()
    private var mRotate: Float = 0F
        set(value) {
            field = when {
                value >= 360 -> {
                    0F;
                }
                value < 0 -> {
                    0F;
                }
                else -> {
                    value
                }
            }

            matrixs.setRotate(mRotate, width * 0.5F, height * 0.5F)
            mShader?.setLocalMatrix(matrixs);
            postInvalidate()
        }
    private var mShader: SweepGradient? = null
    private val matrixs = Matrix()
    private var radialGradient: RadialGradient? = null
    private var radius: Int = 0
    val paintLeiDa = Paint()
    val paint = Paint()
    val paintPosi = Paint()
    val paintBottom = Paint()
    val path = Path()
    val pathBottom = Path()
    val interval = 10F.dpInt()
    val pts = mutableListOf<Float>()

    init {
        paintLeiDa.run {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        paint.run {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 1.3F.dp()
            color = Color.argb(75, 65, 235, 109)
        }
        paintBottom.run {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 0.5F.dp()
            color = Color.argb(70, 65, 235, 109)
        }
        paintPosi.run {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 5F.dp()
            color = Color.argb(80, 65, 235, 109)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectf.set(0F, 0F, w.toFloat(), h.toFloat())
        //画笔的 宽度的一半
        val flLeda = paint.strokeWidth * 0.5F
        val flBottom = paintBottom.strokeWidth * 0.5F

        pathBottom.reset()

        //添加渐变
//        if (radialGradient == null) {
//            radialGradient = RadialGradient(
//                w * 0.5F, h * 0.5F, w * 0.5F,
//                Color.argb(100, 65, 235, 109),
//                Color.argb(60, 65, 235, 109), Shader.TileMode.CLAMP
//            )
//            paintBottom.shader = radialGradient
//        }


        //绘制正放心网格
        val sqrt = interval * interval.toDouble()
        val squareWidth = sqrt(sqrt * 0.5).toFloat()

        Log.d("12345", "squareWidth: $squareWidth");

        var squareX = w * 0.5F - flBottom
        while (squareX <= w - flBottom) {
            pathBottom.moveTo(squareX, flBottom)
            pathBottom.lineTo(squareX, h - flBottom)
            squareX += squareWidth
        }
        squareX = w * 0.5F - flBottom - squareWidth
        while (squareX >= flBottom) {
            pathBottom.moveTo(squareX, flBottom)
            pathBottom.lineTo(squareX, h - flBottom)
            squareX -= squareWidth
        }
        var squareY = h * 0.5F - flBottom
        while (squareY <= h - flBottom) {
            pathBottom.moveTo(flBottom, squareY)
            pathBottom.lineTo(w - flBottom, squareY)
            squareY += squareWidth
        }
        squareY = h * 0.5F - flBottom - squareWidth
        while (squareY >= flBottom) {
            pathBottom.moveTo(flBottom, squareY)
            pathBottom.lineTo(w - flBottom, squareY)
            squareY -= squareWidth
        }

        path.reset()


        //绘制雷达圆环
        radius = interval
        while (radius + flLeda < w * 0.5F) {
            path.addCircle(w * 0.5F, h * 0.5F, radius.toFloat(), Path.Direction.CCW)
            radius += interval
        }
        radius -= interval


        //绘制雷达 十字线
        path.moveTo(w * 0.5F - flLeda, h * 0.5F - radius)
        path.lineTo(w * 0.5F - flLeda, h * 0.5F + radius)
        path.moveTo(w * 0.5F - radius, h * 0.5F - flLeda)
        path.lineTo(w * 0.5F + radius, h * 0.5F - flLeda)





        if (mShader == null) {
            mShader = SweepGradient(
                w * 0.5F, h * 0.5F, intArrayOf(
                    Color.argb(80, 65, 235, 109),
                    Color.argb(75, 65, 235, 109),
                    Color.argb(70, 65, 235, 109),
                    Color.argb(65, 65, 235, 109),
                    Color.argb(60, 65, 235, 109),
                    Color.argb(55, 65, 235, 109),
                    Color.argb(50, 65, 235, 109),
                    Color.argb(45, 65, 235, 109),
                    Color.argb(40, 65, 235, 109),
                    Color.argb(35, 65, 235, 109),
                    Color.argb(20, 65, 235, 109),
                    Color.argb(15, 65, 235, 109),
                    Color.argb(10, 65, 235, 109),
                    Color.argb(5, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                    Color.argb(0, 65, 235, 109),
                ), null
            )
            paintLeiDa.shader = mShader
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(pathBottom, paintBottom)
        canvas?.drawPath(path, paint)


        canvas?.drawCircle(width * 0.5F, height * 0.5F, radius.toFloat(), paintLeiDa)


        if (pts.isNotEmpty()) {
            canvas?.drawPoints(pts.toFloatArray(), paintPosi)
        }

    }


}