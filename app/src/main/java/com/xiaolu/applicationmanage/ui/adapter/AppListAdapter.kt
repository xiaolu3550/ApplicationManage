package com.xiaolu.applicationmanage.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaolu.applicationmanage.R
import com.xiaolu.applicationmanage.bean.AppInfoBean

class AppListAdapter(layoutResId: Int) :
    BaseQuickAdapter<AppInfoBean, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: AppInfoBean) {
        holder.setText(R.id.tv_appName, item.appName)
            .setText(R.id.tv_packageName, "包名: ${item.packageName}")
            .setText(R.id.tv_version, "版本号: ${item.version}")
            .setText(R.id.tv_packageName, "签名/MD5: ${item.md5}")
            .setText(R.id.tv_packageName, "SHA1: ${item.sha1}")
            .setImageDrawable(R.id.iv_icon, item.icon)
    }
}