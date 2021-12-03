package com.xiaolu.applicationmanage.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.benlian.commlib.base.BaseMvpActivity
import com.benlian.commlib.mvpbase.IPresenter
import com.benlian.commlib.mvpbase.IView
import com.benlian.commlib.utils.ToolbarHelper
import com.gyf.immersionbar.ImmersionBar
import com.xiaolu.applicationmanage.R
import com.xiaolu.applicationmanage.databinding.MainActivityBinding
import com.xiaolu.applicationmanage.ui.adapter.MyPagerAdapter
import java.util.*

class MainActivity : BaseMvpActivity<MainActivityBinding, IView, IPresenter<IView>>() {
    private val fragments = ArrayList<Fragment>()
    private val title = arrayOf("全部", "系统", "用户")
    override fun onCreateViewBinding(layoutInflater: LayoutInflater): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }

    override fun initToolbar(toolbarHelper: ToolbarHelper?, immersionBar: ImmersionBar?) {
        toolbarHelper?.setTitle(getString(R.string.app_name))
    }

    override fun initParms(parms: Bundle?) {
    }

    override fun setToolbarLayout(): Int {
        return 0
    }

    override fun initView(view: View?) {
        initFragment()
        binding.tl.setViewPager(binding.vp, title, this, fragments)
    }

    private fun initFragment() {
        //all
        fragments.add(AllAppFragment.newInstance())
        //expenditure
        fragments.add(SystemAppFragment.newInstance())
        //income
        fragments.add(UserAppFragment.newInstance())
        binding.vp.adapter = MyPagerAdapter(supportFragmentManager, fragments, title)
    }

    override fun setListener() {
    }

    override fun widgetClick(v: View?) {
    }

    override fun doBusiness(mContext: Context?) {
    }

    override fun createPresenter(): IPresenter<IView>? {
        return null
    }

    override fun createView(): IView? {
        return null
    }
}