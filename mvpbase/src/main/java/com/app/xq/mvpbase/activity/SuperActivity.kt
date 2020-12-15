package com.app.xq.mvpbase.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:06
 */
open class SuperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.BLACK
        }
    }
}