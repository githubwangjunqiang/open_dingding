package com.app.xq.mvpbase.utils

import androidx.lifecycle.Observer

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:47
 */
open abstract class TryObserver<T> : Observer<T> {
    override fun onChanged(t: T) {
        try {
            onChanges(t)
        } catch (e: Exception) {
            e.printStackTrace()
            onChangesError(e)
        }
    }

    /**
     * 变化 变化
     *
     * @param t
     */
    protected abstract fun onChanges(t: T)

    /**
     * 变化 异常
     *
     * @param e
     */
    protected abstract fun onChangesError(e: Exception)
}