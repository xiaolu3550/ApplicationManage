package com.xiaolu.applicationmanage.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benlian.commlib.base.BaseMvpFragment
import com.benlian.commlib.mvpbase.IPresenter
import com.benlian.commlib.mvpbase.IView
import com.benlian.commlib.weigit.RecycleViewDivider
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.tamsiree.rxkit.RxClipboardTool
import com.tamsiree.rxkit.RxImageTool
import com.xiaolu.applicationmanage.R
import com.xiaolu.applicationmanage.bean.AppInfoBean
import com.xiaolu.applicationmanage.databinding.AppFragmentBinding
import com.xiaolu.applicationmanage.ui.adapter.AppListAdapter
import com.xiaolu.applicationmanage.util.AppUtil
import com.xiaolu.applicationmanage.util.LoadAppUtil

class UserAppFragment : BaseMvpFragment<AppFragmentBinding, IView, IPresenter<IView>>() {
    companion object {
        fun newInstance(): AllAppFragment {
            return AllAppFragment()
        }
    }

    var appListAdapter: AppListAdapter? = null
    override fun initImmersionBar() {
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): AppFragmentBinding {
        return AppFragmentBinding.inflate(layoutInflater)
    }

    override fun setToolbarLayout(): Int {
        return 0
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
        appListAdapter = AppListAdapter(R.layout.app_list_item)
        appListAdapter?.addChildClickViewIds(R.id.tv_copy)
        initDate()
    }

    override fun initDate() {
        binding.sm.autoRefresh()
        LoadAppUtil.init()
            .load("USER", object : LoadAppUtil.LoadListener {
            override fun success(bean: MutableList<AppInfoBean>) {
                binding.rv.adapter = appListAdapter
                appListAdapter?.data = bean
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