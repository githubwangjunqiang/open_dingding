package com.app.xq.mvpbase.interfaces

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:29
 */
interface IView {
    /**
     * 失败
     *
     * @param drawable
     * @param msg
     */
    fun showErrorView(drawable: Int, msg: String?)

    /**
     * 网络失败
     *
     * @param errorMsg
     */
    fun showNetWorkErrorView(errorMsg: String?)


    /**
     * 空试图
     *
     * @param drawable
     * @param errorMsg
     */
    fun showEmptyError(drawable: Int, errorMsg: String?)

    /**
     * 显示成功试图
     */
    fun showContentView()

    /**
     * 显示加载原生loading
     *
     * @param msg
     */
    fun showLoadingView(msg: String?)

    /**
     * 显示加载原生loading 对话框
     *
     * @param msg
     */
    fun showLoadDialog(msg: String?)

    /**
     * 关闭 加载中状态
     */
    fun closeLoading()

}