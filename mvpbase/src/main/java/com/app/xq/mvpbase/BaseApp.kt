package com.app.xq.mvpbase

import android.app.Application
import android.content.Context

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/17 14:51
 */
open class BaseApp : Application() {
    companion object {
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}