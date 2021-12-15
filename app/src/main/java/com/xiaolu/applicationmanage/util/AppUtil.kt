package com.xiaolu.applicationmanage.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.pm.Signature
import com.tamsiree.rxkit.RxAppTool
import com.xiaolu.applicationmanage.bean.AppInfoBean
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


object AppUtil {
    @JvmStatic
    fun queryFilterAppInfo(context: Context, type: String): MutableList<AppInfoBean> {
        val pm: PackageManager = context.packageManager
        // 查询所有已经安装的应用程序
        val appInfos =
            pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES) // GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        val appInfoListBean: MutableList<AppInfoBean> = ArrayList()

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        val resolveinfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(resolveIntent, 0)
        val allowPackages: MutableSet<String> = HashSet()
        for (resolveInfo in resolveinfoList) {
            allowPackages.add(resolveInfo.activityInfo.packageName)
        }
        for (app in appInfos) {
            var appInfoBean = AppInfoBean()
            if (type.equals("USER")) {
                val systemApp = RxAppTool.isSystemApp(context, app.packageName)
                if (isSysApp(systemApp, appInfoBean, app.packageName, context)) continue

            } else if (type.equals("SYS")) {
                val systemApp = RxAppTool.isSystemApp(context, app.packageName)
                if (isSysApp(!systemApp, appInfoBean, app.packageName, context)) continue
            } else {
                addBean(appInfoBean, app.packageName, context)
            }
            appInfoListBean.add(appInfoBean)
        }
        return appInfoListBean
    }

    private fun isSysApp(
        systemApp: Boolean,
        appInfoBean: AppInfoBean,
        packageName: String,
        context: Context,
    ): Boolean {
        if (!systemApp) {
            addBean(appInfoBean, packageName, context)
        } else {
            return true
        }
        return false
    }

    private fun addBean(
        appInfoBean: AppInfoBean,
        packageName: String,
        context: Context
    ) {
        appInfoBean.packageName = packageName
        appInfoBean.md5 = getAppSign(context, packageName, "MD5")
        appInfoBean.sha1 = getAppSign(context, packageName, "SHA1")
        appInfoBean.appName = RxAppTool.getAppName(context, packageName)
        appInfoBean.icon = RxAppTool.getAppIcon(context, packageName)
        appInfoBean.version = RxAppTool.getAppVersionName(context, packageName)
    }

    /**
     * 将获取到得编码进行16进制转换
     *
     * @param arr
     * @return
     */
    private fun byte2HexFormatted(arr: ByteArray): String? {
        val str = StringBuilder(arr.size * 2)
        for (i in arr.indices) {
            var h = Integer.toHexString(arr[i].toInt())
            val l = h.length
            if (l == 1) h = "0$h"
            if (l > 2) h = h.substring(l - 2, l)
            str.append(h.toUpperCase())
            if (i < arr.size - 1) str.append(':')
        }
        return str.toString()
    }

    /**
     * 获得app 的sha1值 *
     * @param context
     * @return
     */
    fun getAppSign(context: Context, packageName: String, type: String = "MD5"): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            // X509证书，X.509是一种非常通用的证书格式
            val signs: Array<Signature> = packageInfo.signatures
            val sign: Signature = signs[0]
            val certFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
            val cert: X509Certificate =
                certFactory.generateCertificate(ByteArrayInputStream(sign.toByteArray())) as X509Certificate // md5
            val md: MessageDigest = MessageDigest.getInstance(type)
            // 获得公钥
            val b = md.digest(cert.encoded)
            return byte2HexFormatted(b)?.replace(":", "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun copyText(appName: String?, packageName: String?, md5: String?, sha1: String?): String {
        return "应用名称: $appName\n包名: $packageName\n签名/MD5: $md5\nSHA1: $sha1"
    }
}