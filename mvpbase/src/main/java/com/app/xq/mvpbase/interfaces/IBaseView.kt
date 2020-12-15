package com.app.xq.mvpbase.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.xq.mvpbase.entiy.ViewStats

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:07
 */
interface IBaseView<VM, State>  {

    /**
     * 获取布局id
     *
     * @return
     */
    fun getContentViewId(): Int{
        return 0
    }

    /**
     * 获取布局 viewbingding
     *
     * @return
     */
    fun getContentView(inflater: LayoutInflater): View?

    /**
     * 获取布局 viewbingding
     *
     * @param inflater
     * @param container
     * @return
     */
    fun getContentView(inflater: LayoutInflater?, container: ViewGroup?): View?


    /**
     * 获取状态试图 View
     *
     * @return
     */
    fun getStatsView(): State?

    /**
     * 初始化 状态试图
     */
    fun initStateLayout()

    /**
     * 获取 viewModel
     *
     * @return
     */
    fun getBaseViewModel(): VM

    /**
     * 获取  loading dialog
     *
     * @return
     */
    fun getILoadingDialog(): ILoadingDialog


    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化
     *
     * @param rootView
     * @param savedInstanceState
     */
    fun initView(rootView: View?, savedInstanceState: Bundle?)

    /**
     * 监听器
     */
    fun setListener()

    /**
     * 添加 视图状态 model
     */
    fun addViewModelStats()

    /**
     * 改变状态试图
     *
     * @param stats
     */
    fun changeViewStats(stats: ViewStats?)

    /**
     * 开始加载数据初始化数据时 调用此函数 如果时 vm 得判断 数据是否已经获取了
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun startRefresh()

    /**
     * 状态试图失败时 点击屏幕重新加载 调用此函数
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun refreshAgain()
}