package com.xiaolu.applicationmanage.util

import com.xiaolu.applicationmanage.App
import com.xiaolu.applicationmanage.bean.AppInfoBean
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoadAppUtil {
    companion object {
        fun init(): LoadAppUtil {
            return LoadAppUtil()
        }
    }

    fun load(type: String, loadListener: LoadListener) {
        Observable.create(ObservableOnSubscribe<MutableList<AppInfoBean>> { emitter ->
            emitter.onNext(AppUtil.queryFilterAppInfo(App.getInstance(), type))
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MutableList<AppInfoBean>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: MutableList<AppInfoBean>) {
                    loadListener.success(t)
                }

                override fun onError(e: Throwable) {
                    loadListener.error(e.message)
                }

                override fun onComplete() {
                }

            })
    }

    interface LoadListener {
        fun success(bean: MutableList<AppInfoBean>)
        fun error(msg: String?)
    }
}