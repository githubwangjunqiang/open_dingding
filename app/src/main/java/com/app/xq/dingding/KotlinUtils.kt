package com.app.xq.dingding

import android.util.TypedValue

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/9 11:02
 */

/**
 * px 转换 dp
 */
fun Float.dp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        App.context.resources.displayMetrics
    )
}

/**
 * px 转换 dp
 */
fun Float.dpInt(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        App.context.resources.displayMetrics
    ).toInt()
}



