package com.app.xq.dingding

import android.app.Application
import android.content.Context

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/12/15 10:41
 */
class App : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}