package com.app.xq.mvpbase.utils

import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:12
 */
object To {
    /**
     * 实现类
     */
    var mITo: ITo? = null


    /**
     * 显示 土司提示
     *
     * @param string
     */
    fun show(string: String?) {
        string?.let {
            GlobalScope.launch(Dispatchers.Main) {
                mITo?.show(string)
            }

        }

    }

    /**
     * 显示 Snackbar
     *
     * @param view
     * @param msg
     */
    fun showSnackbar(view: View?, msg: String?) {
        mITo?.showSnackbar(view, msg)
    }


    interface ITo {
        /**
         * 显示 土司
         *
         * @param string
         */
        fun show(string: String?)

        /**
         * 显示 Snackbar
         *
         * @param view
         * @param msg
         */
        fun showSnackbar(view: View?, msg: String?)
    }
}