package com.app.xq.mvpbase.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.xq.mvpbase.viewmodel.BaseViewModel
import com.app.xq.mvpbase.utils.To
import com.app.xq.mvpbase.entiy.ViewStats
import com.app.xq.mvpbase.interfaces.IBaseView
import com.app.xq.mvpbase.interfaces.ILoadingDialog
import com.app.xq.mvpbase.interfaces.IView
import com.app.xq.mvpbase.interfaces.IViewState

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 11:57
 */
 abstract class MvpActivity<VM : BaseViewModel> : SuperActivity(), IBaseView<VM, IViewState>,
    IView {
    /**
     * 改变 状态视图的  vm
     */
    protected lateinit var mViewModel: VM

    /**
     * 状态试图 的控制器接口
     */
    protected var mIViewState: IViewState? = null

    /**
     * 对话框 加载框 的接口
     */
    private val mLoadingDialog: ILoadingDialog by lazy {
        getILoadingDialog().apply {
            setListener(object : ILoadingDialog.LoadingListener {
                override fun showListener() {
                }

                override fun dismissListener() {
                    finish()
                }

            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val contentViewId = getContentViewId()
            val contentView = getContentView(LayoutInflater.from(this))
            if (contentViewId != 0) {
                setContentView(contentViewId)
            } else contentView?.let { setContentView(it) }
            initStateLayout()
            mViewModel = getBaseViewModel()
            addViewModelStats()
            initView(savedInstanceState)
            setListener()
            startRefresh()
        } catch (e: Exception) {
            e.printStackTrace()
            To.show(e.message)
            finish()
        }
    }

    override fun showErrorView(drawable: Int, msg: String?) {
        closeLoading()
        if (mIViewState != null) {
            mIViewState!!.showErrorView(msg)
        } else {
            To.show(msg)
        }
    }

    override fun showNetWorkErrorView(errorMsg: String?) {
        closeLoading()
        if (mIViewState != null) {
            mIViewState!!.showNetWorkErrorView(errorMsg)
        } else {
            To.show(errorMsg)
        }
    }

    override fun showEmptyError(drawable: Int, errorMsg: String?) {
        closeLoading()
        if (mIViewState != null) {
            mIViewState!!.showEmptyView(errorMsg)
        } else {
            To.show("没有数据")
        }
    }

    override fun showContentView() {
        closeLoading()
        if (mIViewState != null) {
            mIViewState!!.showContentView()
        }
    }

    override fun showLoadingView(msg: String?) {
        if (mIViewState != null) {
            mIViewState!!.showLoadingView(msg)
        } else {
            showLoadDialog(msg)
        }
    }

    override fun showLoadDialog(msg: String?) {
        mLoadingDialog.show()
        mLoadingDialog.setMsg(msg)
    }

    override fun closeLoading() {
        mLoadingDialog.dismiss()
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun getContentView(inflater: LayoutInflater?, container: ViewGroup?): View? {
        return null
    }

    override fun initStateLayout() {
        mIViewState = getStatsView()
        if (mIViewState == null) {
            return
        }
        mIViewState?.setOnClickStateListener {
            try {
                refreshAgain()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                finish()
            }
        }
    }


    override fun addViewModelStats() {
        mViewModel.mViewStatsLiveData.observe(this,
            { viewStats -> changeViewStats(viewStats) })
    }

    override fun changeViewStats(viewStats: ViewStats?) {
        viewStats?.let {
            when (it.stats) {
                ViewStats.errorView -> showErrorView(viewStats.drawableId, viewStats.msg)
                ViewStats.emptyView -> showEmptyError(viewStats.drawableId, viewStats.msg)
                ViewStats.networkErrorView -> showNetWorkErrorView(viewStats.msg)
                ViewStats.loadView -> showLoadingView(viewStats.msg)
                ViewStats.loadDialogView -> showLoadDialog(viewStats.msg)
                else -> showContentView()
            }
        }

    }

}