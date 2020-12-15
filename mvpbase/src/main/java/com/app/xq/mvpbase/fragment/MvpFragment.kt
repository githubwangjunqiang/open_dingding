package com.app.xq.mvpbase.fragment

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
 * @data:  on 2020/11/10 14:28
 */
abstract class MvpFragment<VM : BaseViewModel> : SuperFragment(), IView,
    IBaseView<VM, IViewState> {


    /**
     * 改变 状态视图的  vm
     */
    protected var mViewModel: VM? = null

    /**
     * 状态试图 的控制器接口
     */
    protected var mIViewState: IViewState? = null

    /**
     * 碎片的 父控件
     */
    private var mRootView: View? = null

    /**
     * 对话框 加载框 的接口
     */
    private val mLoadingDialog: ILoadingDialog by lazy {
        getILoadingDialog().apply {
            setListener(object : ILoadingDialog.LoadingListener {
                override fun showListener() {
                }

                override fun dismissListener() {
                    Log.e("12345", "dismissListener: 用户取消了请求加载框...")
                }

            })
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parent = mRootView?.parent
        parent?.let {
            if (it is ViewGroup) {
                it.removeView(mRootView)
            }
            return mRootView
        }

        try {
            val contentViewId = getContentViewId()
            val contentView = getContentView(inflater, container)
            if (contentViewId != 0) {
                mRootView = inflater.inflate(contentViewId, container, false)
            } else if (contentView != null) {
                mRootView = contentView
            }
            initStateLayout()
            mViewModel = getBaseViewModel()
            addViewModelStats()
            initView(mRootView, savedInstanceState)
            setListener()
            startRefresh()
        } catch (e: Exception) {
            e.printStackTrace()
            To.show(e.localizedMessage)
        }
        return mRootView
    }

    override fun showErrorView(drawble: Int, msg: String?) {
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

    override fun showEmptyError(drawble: Int, errorMsg: String?) {
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
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss()
        }
    }

    override fun getContentView(inflater: LayoutInflater): View? {
        return null
    }

    override fun initStateLayout() {
        mIViewState = getStatsView()
        if (mIViewState == null) {
            return
        }
        mIViewState!!.setOnClickStateListener { v ->
            Log.d("12345", "initStateLayout: ")
            try {
                refreshAgain()
            } catch (e: Exception) {
                e.printStackTrace()
                To.show(e.message)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {}

    override fun addViewModelStats() {
        mViewModel?.mViewStatsLiveData?.observe(this,
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