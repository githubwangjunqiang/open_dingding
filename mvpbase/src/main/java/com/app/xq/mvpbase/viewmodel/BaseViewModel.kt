package com.app.xq.mvpbase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.xq.mvpbase.entiy.ViewStats

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 11:58
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * 试图状态的 viewModel
     */
    val mViewStatsLiveData: MutableLiveData<ViewStats> = MutableLiveData<ViewStats>()


}