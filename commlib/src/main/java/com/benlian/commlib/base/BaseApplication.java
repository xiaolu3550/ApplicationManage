package com.benlian.commlib.base;

import android.app.Application;

import com.hjq.toast.ToastUtils;
import com.tamsiree.rxkit.RxTool;
import com.tencent.mmkv.MMKV;

public class BaseApplication extends Application {
    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RxTool.init(this);
        ToastUtils.init(this);
        MMKV.initialize(this);
    }
}
