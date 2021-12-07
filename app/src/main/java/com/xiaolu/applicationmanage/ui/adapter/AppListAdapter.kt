package com.xiaolu.applicationmanage.ui.adapter

import android.widget.Filter
import android.widget.Filterable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaolu.applicationmanage.R
import com.xiaolu.applicationmanage.bean.AppInfoBean

class AppListAdapter(layoutResId: Int, mData: MutableList<AppInfoBean>) :
    BaseQuickAdapter<AppInfoBean, BaseViewHolder>(layoutResId, data = mData), Filterable {
    var mData: MutableList<AppInfoBean>? = mData
    override fun convert(holder: BaseViewHolder, item: AppInfoBean) {
        holder.setText(R.id.tv_appName, item.appName)
            .setText(R.id.tv_packageName, "包名: ${item.packageName}")
            .setText(R.id.tv_version, "版本号: ${item.version}")
            .setText(R.id.tv_MD5, "签名/MD5: ${item.md5}")
            .setText(R.id.tv_SHA1, "SHA1: ${item.sha1}")
            .setImageDrawable(R.id.iv_icon, item.icon)
    }

    var mOriginalValues: MutableList<AppInfoBean>? = null
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                mOriginalValues = ArrayList()
                if (constraint.isNullOrEmpty()) {
                    mOriginalValues!!.addAll(mData!!)
                } else {
                    mData!!.forEach {
                        val appName = it.appName
                        val packageName = it.packageName
                        if (appName.contains(constraint) || packageName.equals(constraint)) {
                            mOriginalValues!!.clear()
                            mOriginalValues!!.add(it)
                        }
                    }
                }
                filterResults.values = mOriginalValues
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                data = results!!.values as MutableList<AppInfoBean>
                notifyDataSetChanged()
            }

        }
    }
}