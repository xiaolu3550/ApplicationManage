package com.xiaolu.applicationmanage.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import com.benlian.commlib.base.BaseMvpActivity
import com.benlian.commlib.eventbean.MessageEvent
import com.benlian.commlib.mvpbase.IPresenter
import com.benlian.commlib.mvpbase.IView
import com.benlian.commlib.utils.EventBusUtil
import com.benlian.commlib.utils.ToolbarHelper
import com.gyf.immersionbar.ImmersionBar
import com.xiaolu.applicationmanage.R
import com.xiaolu.applicationmanage.bean.SearchBean
import com.xiaolu.applicationmanage.databinding.MainActivityBinding
import com.xiaolu.applicationmanage.ui.adapter.MyPagerAdapter
import com.xiaolu.applicationmanage.ui.fragment.AllAppFragment
import com.xiaolu.applicationmanage.ui.fragment.SystemAppFragment
import com.xiaolu.applicationmanage.ui.fragment.UserAppFragment
import com.xiaolu.applicationmanage.util.AnimationUtil
import java.util.*


class MainActivity : BaseMvpActivity<MainActivityBinding, IView, IPresenter<IView>>() {
    private val fragments = ArrayList<Fragment>()
    private val title = arrayOf("全部", "系统", "用户")
    override fun onCreateViewBinding(layoutInflater: LayoutInflater): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }

    override fun initToolbar(toolbarHelper: ToolbarHelper?, immersionBar: ImmersionBar?) {
        val toolbar = toolbarHelper!!.toolbar
        var searchIcon = toolbar.findViewById<ImageView>(R.id.iv_search)
        var close = toolbar.findViewById<ImageView>(R.id.iv_close)
        var editText = toolbar.findViewById<EditText>(R.id.et_search)
        var titleView = toolbar.findViewById<RelativeLayout>(R.id.rl_title)
        var searchView = toolbar.findViewById<LinearLayout>(R.id.ll_search)
        searchIcon.setOnClickListener {
            AnimationUtil.fadeOut(titleView)
            AnimationUtil.fadeIn(searchView)
        }
        close.setOnClickListener {
            if (editText.text.isEmpty()) {
                AnimationUtil.fadeOut(searchView)
                AnimationUtil.fadeIn(titleView)
                EventBusUtil.sendEvent(
                    MessageEvent(
                        1000, SearchBean(
                            editText.text.toString().trim(), binding.tl.currentTab
                        )
                    )
                )
            } else {
                editText.setText("")
            }
        }
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                EventBusUtil.sendEvent(
                    MessageEvent(
                        1000, SearchBean(
                            editText.text.toString().trim(), binding.tl.currentTab
                        )
                    )
                )
            }
            return@OnEditorActionListener false
        })
    }

    override fun initParms(parms: Bundle?) {
    }

    override fun setToolbarLayout(): Int {
        return R.id.toolbar_search
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