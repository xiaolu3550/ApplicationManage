package com.xiaolu.applicationmanage;

import com.benlian.commlib.base.BaseApplication;
import com.benlian.commlib.utils.MMKV_Utils;

public class App extends BaseApplication {
    /*public static RetrofitService getClient() {
        return RetrofitClient.getInstance().setBaseUrl(SHJBaseUrlConfig.SERVER_URL)
                .getApi(RetrofitService.class);
    }*/

    public static MMKV_Utils getMMKV() {
        return MMKV_Utils.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
