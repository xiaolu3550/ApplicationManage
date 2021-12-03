package com.benlian.commlib.base;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.benlian.commlib.log.LogUtil;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;


/**
 * @ClassName: BaseDialog
 * @Description: : (dialog 基类)
 */
public abstract class BaseDialog<VB extends ViewBinding> extends RxDialogFragment {
    protected final String TAG = this.getClass().getSimpleName();
    private VB binding;

    public enum DialogType {
        mid, bottom, top, full, right, left
    }

    Window window;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate", TAG + "-->onCreate()");
        setStyle(DialogFragment.STYLE_NORMAL, getDialogStyle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LogUtil.d("onCreateView", TAG + "-->onCreateView()");
        binding = onCreateViewBinding(inflater, container);
        window = getDialog().getWindow();
        return binding.getRoot();
    }

    public VB getBinding() {
        return binding;
    }

    protected abstract VB onCreateViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d("onViewCreated", TAG + "-->onViewCreated()");
        bindView(savedInstanceState);
    }

    /**
     * 绑定视图
     *
     * @param bundle savedInstanceState
     */
    protected void bindView(@Nullable Bundle bundle) {
        build(DialogType.mid);
        this.getDialog().setCanceledOnTouchOutside(false);
    }

    /**
     * 再次根据选择的类型进行布局设置
     *
     * @param type 风格
     * @return dialog
     */
    protected BaseDialog build(DialogType type) {
        switch (type) {
            case mid:
                initMidDialog();
                break;
            case bottom:
                initBottomDialog();
                break;
            case top:
                break;
            case full:
                initFullDialog();
                break;
            case left:
                initLeftDialog();
                break;
            case right:
                initRightiDialog();
                break;
            default:
                break;
        }
        return this;
    }

    protected void initLeftDialog() {
        if (null != window) {
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            //设置dialog的位置在底部
            lp.gravity = Gravity.LEFT;
            window.setAttributes(lp);
            window.setGravity(Gravity.LEFT);
        }
    }

    protected void initFullDialog() {
        if (null != window) {
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            //设置dialog的位置在底部
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }
    }

    protected void initMidDialog() {
        if (null != window) {
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //设置dialog的位置在底部
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER);
        }
    }

    protected void initBottomDialog() {
        if (null != window) {
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //设置dialog的位置在底部
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    protected void initRightiDialog() {
        if (null != window) {
            window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            //设置dialog的位置在左侧
            lp.gravity = Gravity.RIGHT;
            window.setAttributes(lp);
            window.setGravity(Gravity.RIGHT);
        }
    }

    public void setAnimations(@StyleRes int animationId) {
        if (null != window)
            window.setWindowAnimations(animationId);
    }

    public void show(FragmentActivity activity) {
        if (null != this.getDialog() && this.getDialog().isShowing()) {
            return;
        }
        //        show(activity.getSupportFragmentManager(), getDialogTag());
        show(activity.getSupportFragmentManager());
    }

    public void show(Fragment fragment) {
        if (null != this.getDialog() && this.getDialog().isShowing()) {
            return;
        }
        //        show(fragment.getFragmentManager(), getDialogTag());
        show(fragment.getFragmentManager());
    }

    public void show(FragmentManager fm) {
        //这里直接调用show方法会报java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        //因为show方法中是通过commit进行的提交(通过查看源码)
        //这里为了修复这个问题，使用commitAllowingStateLoss()方法
        //注意：DialogFragment是继承自android.app.Fragment，这里要注意同v4包中的Fragment区分，别调用串了
        //DialogFragment有自己的好处，可能也会带来别的问题
        //dialog.show(getFragmentManager(), "SignDialog");
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(this, getDialogTag());
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        this.getDialog().setCanceledOnTouchOutside(isCancelableOnTouchOutside());
        this.getDialog().setCancelable(isCancelable());
    }

    protected boolean isCancelableOnTouchOutside() {
        return true;
    }

    @Override
    public void dismiss() {
        if (null == this.getDialog() || !this.getDialog().isShowing()) {
            return;
        }
        super.dismiss();
    }

    protected abstract String getDialogTag(); //获取dialog的标签

    @StyleRes
    protected abstract int getDialogStyle(); //获取dialog的风格

}
