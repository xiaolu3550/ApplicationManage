package com.benlian.commlib.base;

import android.os.Bundle;

import androidx.viewbinding.ViewBinding;

import com.benlian.commlib.mvpbase.IPresenter;
import com.benlian.commlib.mvpbase.IView;
import com.benlian.commlib.mvpbase.MvpCallback;
import com.benlian.commlib.net.RxManager;


/**
 * ================================================
 *
 * @ProjectName: MyLibraryTool
 * @Package: com.xiaolu.mylibrary.base
 * @ClassName: BaseMvpActivity
 * @Description: java类作用描述
 * @Author: xiaol
 * @CreateDate: 2020/10/28 13:40
 * @UpdateUser: xiaol
 * @UpdateDate: 2020/10/28 13:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 * ================================================
 */
public abstract class BaseMvpActivity<VB extends ViewBinding,V extends IView, P extends IPresenter<V>> extends RxBaseActivity<VB>
        implements MvpCallback<V, P> {
    protected P mPresenter;
    protected V mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = createView();
        if (getPresenter() == null) {
            mPresenter = createPresenter();
            try {
                mPresenter.attachView((V) this);
                initDate();
                initDate("");
            } catch (Exception e) {
                new ClassCastException(this.toString() + "实现IPresenterView或者IPresenterView子类接口");
            }
        }
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public V getMvpView() {
        return mView;
    }

    @Override
    public void setMvpView(V view) {
        this.mView = view;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            RxManager.getInstance().clear(mPresenter.getClass().getName());
        }
        if (isFinishing()) {
            setMvpView(null);
            setPresenter(null);
        }
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        } else {
            mPresenter = createPresenter();
        }
        super.onResume();
    }
}
