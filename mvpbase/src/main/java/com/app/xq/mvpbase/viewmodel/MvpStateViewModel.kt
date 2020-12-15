package com.app.xq.mvpbase.viewmodel

import com.app.xq.mvpbase.BaseApp
import com.app.xq.mvpbase.R
import com.app.xq.mvpbase.entiy.ViewStats
import com.app.xq.mvpbase.interfaces.IView
import com.app.xq.mvpbase.utils.MyException
import com.app.xq.mvpbase.utils.To
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import retrofit2.HttpException
import java.net.HttpRetryException
import java.net.UnknownHostException

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:44
 */
open class MvpStateViewModel : BaseViewModel(), IView {

    /**
     * 返回处理异常的handler
     */
    fun getErrorHandler(showError: Boolean = true): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            when (throwable) {
                is CancellationException -> closeLoading()
                is HttpRetryException -> {
                    if (showError) {
                        showNetWorkErrorView(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                    } else {
                        To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                    }

                }
                is UnknownHostException -> {
                    if (showError) {
                        showNetWorkErrorView(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                    } else {
                        To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                    }
                }

                is HttpException -> {
                    if (showError) {
                        showNetWorkErrorView(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                    } else {
                        To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                    }
                }
                is JsonSyntaxException -> {
                    if (showError) {
                        showErrorView(0, BaseApp.mContext.getString(R.string.The_json_error))
                    } else {
                        To.show(BaseApp.mContext.getString(R.string.The_json_error))
                    }
                }
                else -> {
                    if (showError) {
                        showErrorView(0, throwable.message)
                    } else {
                        To.show(throwable.message)
                    }

                }
            }
        }
    }

    /**
     * 返回处理异常的handler 但是 对状态试图不做改变只是消失loading 和提示错误结果
     */
    fun getErrorHandlerNoView(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            closeLoading()
            when (throwable) {
                is CancellationException -> return@CoroutineExceptionHandler
                is HttpException -> To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                is UnknownHostException -> To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                is HttpRetryException -> To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                is MyException -> To.show(throwable.msg)
                is JsonSyntaxException -> To.show(BaseApp.mContext.getString(R.string.The_json_error))
                else -> {
                    To.show(throwable.localizedMessage)
                }
            }
            coroutineContext.cancel()
        }
    }

    /**
     * 就土司提示而已
     */
    fun getErrorHandlerToast(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            when (throwable) {
                is CancellationException -> return@CoroutineExceptionHandler
                is MyException -> {
                    To.show(throwable.msg)
                }
                is HttpException -> {
                    To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                }
                is UnknownHostException -> {
                    To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                }
                is HttpRetryException -> {
                    To.show(BaseApp.mContext.getString(R.string.The_network_server_is_unstable))
                }
                is JsonSyntaxException -> {
                    To.show(BaseApp.mContext.getString(R.string.The_json_error))
                }
                else -> {
                    To.show(throwable.localizedMessage)
                }
            }
        }
    }

    override fun showErrorView(drawble: Int, msg: String?) {
        mViewStatsLiveData.postValue(ViewStats(ViewStats.errorView, msg))
    }

    override fun showNetWorkErrorView(errorMsg: String?) {
        mViewStatsLiveData.postValue(ViewStats(ViewStats.networkErrorView, errorMsg))
    }

    override fun showEmptyError(drawble: Int, errorMsg: String?) {
        mViewStatsLiveData.postValue(ViewStats(ViewStats.emptyView, errorMsg))
    }

    override fun showContentView() {
        mViewStatsLiveData.postValue(ViewStats(ViewStats.contentView))
    }

    override fun showLoadingView(msg: String?) {
        mViewStatsLiveData.postValue(ViewStats(ViewStats.loadView, msg))
    }

    override fun showLoadDialog(msg: String?) {
        mViewStatsLiveData.postValue(ViewStats(ViewStats.loadDialogView, msg))
    }

    override fun closeLoading() {
        showContentView()
    }


}