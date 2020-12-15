package com.app.xq.mvpbase.interfaces

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:09
 */
interface ILoadingDialog {

    /**
     * 设置 用户取消 监听器
     *
     * @param listener
     */
    fun setListener(listener: LoadingListener?)

    /**
     * 显示
     */
    fun show()

    /**
     * 设置显示信息
     *
     * @param message
     */
    fun setMsg(message: String?)

    /**
     * 消失
     */
    fun dismiss()

    interface LoadingListener {
        /**
         * 开始显示
         */
        fun showListener()

        /**
         * 消失  用户手动点击返回按钮 消失
         */
        fun dismissListener()
    }
}