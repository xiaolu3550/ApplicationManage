package com.xiaolu.applicationmanage.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benlian.commlib.base.BaseMvpFragment
import com.benlian.commlib.eventbean.MessageEvent
import com.benlian.commlib.mvpbase.IPresenter
import com.benlian.commlib.mvpbase.IView
import com.benlian.commlib.weigit.RecycleViewDivider
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.tamsiree.rxkit.RxClipboardTool
import com.tamsiree.rxkit.RxImageTool
import com.xiaolu.applicationmanage.R
import com.xiaolu.applicationmanage.bean.AppInfoBean
import com.xiaolu.applicationmanage.bean.SearchBean
import com.xiaolu.applicationmanage.databinding.SystemAppFragmentBinding
import com.xiaolu.applicationmanage.ui.adapter.AppListAdapter
import com.xiaolu.applicationmanage.util.AppUtil
import com.xiaolu.applicationmanage.util.LoadAppUtil

class SystemAppFragment : BaseMvpFragment<SystemAppFragmentBinding, IView, IPresenter<IView>>() {
    companion object {
        fun newInstance(): SystemAppFragment {
            return SystemAppFragment()
        }
    }

    var appListAdapter: AppListAdapter? = null
    var position = 1
    override fun initImmersionBar() {
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SystemAppFragmentBinding {
        return SystemAppFragmentBinding.inflate(layoutInflater)
    }

    override fun setToolbarLayout(): Int {
        return 0
    }
    override fun isRegisterEventBus(): Boolean {
        return true
    }

    override fun receiveEvent(event: MessageEvent<*>?) {
        when (event!!.code) {
            1000 -> {
                val searchBean = event.data as SearchBean
                if (position == searchBean.position) {
                    if (appListAdapter != null) {
                        appListAdapter!!.filter.filter(searchBean.searchName)
                    }
                }
            }
        }
    }
    override fun initParams(bundle: Bundle?) {
    }

    override fun initView() {
        val LinearLayoutManager =
            LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false)
        binding.rv.apply {
            layoutManager = LinearLayoutManager
            addItemDecoration(
                RecycleViewDivider(
                    mActivity,
                    RecycleViewDivider.HORIZONTAL_LIST,
                    RxImageTool.dip2px(1F),
                    resources.getColor(R.color.line_color)
                )
            )
        }
    }

    override fun doBusiness(mActivity: Activity?) {
        val classicsHeader = ClassicsHeader(mActivity)
        binding.sm.setRefreshHeader(classicsHeader)
            .setRefreshFooter(ClassicsFooter(mActivity))
            .setEnableLoadMore(false)
        initDate()
    }

    override fun initDate() {
        binding.sm.autoRefresh()
        LoadAppUtil.init()
            .load("SYS", object : LoadAppUtil.LoadListener {
                override fun success(bean: MutableList<AppInfoBean>) {
                    appListAdapter = AppListAdapter(R.layout.app_list_item,bean)
                    appListAdapter?.addChildClickViewIds(R.id.tv_copy)
                    binding.rv.adapter = appListAdapter
                    binding.sm.finishRefresh(true)
                }

                override fun error(msg: String?) {
                    binding.sm.finishRefresh(false)
                }

            })
    }

    override fun setListener() {
        appListAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = appListAdapter?.getItem(position)
            RxClipboardTool.copyText(
                mActivity,
                AppUtil.copyText(item?.appName, item?.packageName, item?.md5, item?.sha1)
            )
            showCenterToast("应用信息复制成功")
        }

        binding.sm.setOnRefreshListener {
            initDate()
        }
    }

    override fun createPresenter(): IPresenter<IView>? {
        return null
    }

    override fun createView(): IView? {
        return null
    }
}