package com.app.xq.mvpbase.interfaces

import android.view.View

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:07
 */
interface IViewState {
    /**
     * 设置 刷新 监听器
     *
     * @param listener
     */
    fun setOnClickStateListener(listener: View.OnClickListener?)

    /**
     * 显示失败 视图
     *
     * @param msg
     */
    fun showErrorView(msg: String?)

    /**
     * 网络失败视图
     *
     * @param errorMsg
     */
    fun showNetWorkErrorView(errorMsg: String?)

    /**
     * 空试图
     *
     * @param errorMsg
     */
    fun showEmptyView(errorMsg: String?)

    /**
     * 显示正常试图
     */
    fun showContentView()

    /**
     * 显示 加载中
     * @param msg
     */
    fun showLoadingView(msg: String?)
}