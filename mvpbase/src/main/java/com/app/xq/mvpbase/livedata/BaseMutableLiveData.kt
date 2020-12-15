package com.app.xq.mvpbase.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.app.xq.mvpbase.utils.TryObserver

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data:  on 2020/11/10 14:46
 */
class BaseMutableLiveData<T>(val onHandlerError: (e: Exception) -> Unit) : MutableLiveData<T>() {


    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val tTryObserver: TryObserver<T> = object : TryObserver<T>() {
            override fun onChanges(o: T) {
                observer.onChanged(o)
            }

            override fun onChangesError(e: Exception) {
                onHandlerError(e)
            }
        }
        super.observe(owner, tTryObserver)
    }


    fun observe(owner: LifecycleOwner, observer: TryObserver<T>) {
        super.observe(owner, observer)
    }

}