package com.benlian.commlib.net;

import com.benlian.commlib.log.LogUtil;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * ================================================
 *
 * @ProjectName: MyLibraryTool
 * @Package: com.xiaolu.mylibrary.net
 * @ClassName: RxObserver
 * @Description: 自定义rxjavaObserver
 * @Author: xiaol
 * @CreateDate: 2020/11/3 16:49
 * @UpdateUser: xiaol
 * @UpdateDate: 2020/11/3 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 * ================================================
 */
public abstract class RxObserver<T extends BaseBean> implements Observer<T> {
    private RxManager rxApiManager;
    private String mKey;
    private final String successCode = "00000";

    public RxObserver(String key) {
        this.mKey = key;
        rxApiManager = RxManager.getInstance();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        rxApiManager.add(mKey, d);
    }

    @Override
    public void onNext(@NonNull T t) {
        if (successCode.equals(t.getCode())) {
            onSuccess(t);
        } else {
            onErrors(new Throwable(t.getMsg()));
        }

    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtil.e("RxObserver", mKey + " --- " + e.getMessage());
        //onErrors(e);
        if (e instanceof SocketTimeoutException) {
            onErrors(new Throwable("请求超时"));
        } else if (e instanceof ConnectException) {
            onErrors(new Throwable("网络连接异常"));
        } else if (e instanceof HttpException) {
            onErrors(new Throwable("TOKEN失效"));
        } else if (e instanceof RuntimeException) {
            onErrors(e);
        } else if (e instanceof JSONException) {
            onErrors(new Throwable("数据解析错误"));
        } else if (e instanceof JsonSyntaxException) {
            onErrors(new Throwable("数据解析错误"));
        } else {
            onErrors(new Throwable("未知错误"));
        }

    }

    @Override
    public void onComplete() {

    }

    /**
     * @param t
     */
    public abstract void onSuccess(@NonNull T t);

    /**
     * @param e
     */
    public abstract void onErrors(@NonNull Throwable e);
}
