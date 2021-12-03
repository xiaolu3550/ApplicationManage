package com.benlian.commlib.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.benlian.commlib.R;

/**
 * Created by ljn on 2018/12/24.
 * Explain 权限管理工具
 */

public class RxPermissionSettingUtil {
    /**
     * 跳转“权限设置”界面
     */
    public static void goToPermissionSetting(final Context context, String... permissionNameArray) {
        StringBuilder dialogMessageBuilder = new StringBuilder();
        for (int i = 0; i < permissionNameArray.length; i++) {
            if(i != 0) {
                dialogMessageBuilder.append(" ");
            }
            dialogMessageBuilder.append(permissionNameArray[i]);
        }
        new AlertDialog.Builder(context)
                .setTitle(R.string.permission_tip)
                .setMessage(String.format(context.getString(R.string.permission_remind_open_XX_permission), dialogMessageBuilder.toString()))
                .setCancelable(true)
                .setNegativeButton(R.string.permission_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.permission_go_to_setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(RomUtil.isMiui()) {
                            goXiaoMiMainager(context);
                        } else if(RomUtil.isEmui()) {
                            goHuaWeiMainager(context);
                        } else if(RomUtil.isFlyme()) {
                            goMeizuMainager(context);
                        } else {
                            goIntentSetting(context);
                        }
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    /**
     * 华为
     */
    private static void goHuaWeiMainager(Context context) {
        try {
            Intent intent = new Intent(context.getApplicationContext().getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    /**
     * 小米
     */
    private static void goXiaoMiMainager(Context context) {
        String rom_version = RomUtil.getProp(RomUtil.KEY_VERSION_MIUI);
        if ("V6".equals(rom_version) || "V7".equals(rom_version)) {
            try {
                Intent intent = new Intent();
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getApplicationContext().getPackageName());
                context.startActivity(intent);
            } catch(Exception e) {
                e.printStackTrace();
                goIntentSetting(context);
            }
        } else if ("V8".equals(rom_version) || "V9".equals(rom_version)) {
            try {
                Intent intent = new Intent();
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getApplicationContext().getPackageName());
                context.startActivity(intent);
            } catch(Exception e) {
                e.printStackTrace();
                goIntentSetting(context);
            }
        } else {
            goIntentSetting(context);
        }
    }

    /**
     * 魅族
     */
    private static void goMeizuMainager(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getApplicationContext().getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    /**
     * 其他
     */
    private static void goIntentSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
