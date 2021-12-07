package com.xiaolu.applicationmanage.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.benlian.commlib.base.BaseMvpActivity
import com.benlian.commlib.mvpbase.IPresenter
import com.benlian.commlib.mvpbase.IView
import com.benlian.commlib.utils.ToolbarHelper
import com.gyf.immersionbar.ImmersionBar
import com.xiaolu.applicationmanage.databinding.WelcomeActivityBinding
import java.util.*

class WelcomeActivity : BaseMvpActivity<WelcomeActivityBinding, IView, IPresenter<IView>>() {
    var isFirstLogin: Boolean = false
    override fun onCreateViewBinding(layoutInflater: LayoutInflater): WelcomeActivityBinding {
        return WelcomeActivityBinding.inflate(layoutInflater)
    }

    override fun initToolbar(toolbarHelper: ToolbarHelper?, immersionBar: ImmersionBar?) {
    }

    override fun initParms(parms: Bundle?) {
    }

    override fun setToolbarLayout(): Int {
        return 0
    }

    override fun initView(view: View?) {
    }

    override fun setListener() {
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness(mContext: Context?) {
        var task = object : TimerTask() {
            override fun run() {
                skipActivityAndFinish(MainActivity::class.java)
            }
        }
        val timer = Timer()
        timer.schedule(task, 2000)
    }

    override fun createPresenter(): IPresenter<IView>? {
        return null
    }

    override fun createView(): IView? {
        return null
    }

}