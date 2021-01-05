package com.app.xq.dingding

import android.util.Log
import kotlin.math.floor

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/12/30 10:04
 */


fun isInt(variable: Double): Boolean {
    val floor = floor(variable)
    val compareTo = variable.compareTo(floor)
    val b = compareTo == 0
    val infinite = variable.isInfinite()
    val returnValue = b && !infinite
    Log.d("12345", "variable:$variable ");
    Log.d("12345", "floor:$floor ");
    Log.d("12345", "compareTo:$compareTo ");
    Log.d("12345", "b:$b ");
    Log.d("12345", "infinite:$infinite ");
    Log.d("12345", "returnValue:$returnValue ");
    return returnValue
}