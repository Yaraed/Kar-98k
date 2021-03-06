package com.weyee.poscore.mvp;

/**
 * Created by liu-feng on 2017/6/5.
 */
public interface IView {

    /**
     * 显示加载
     */
    default void showLoading() {

    }

    /**
     * 隐藏加载
     */
    default void hideLoading() {

    }

    /**
     * 显示信息
     *
     * @param mode    模式 1：toast 2. snackbar 3. alert
     * @param message
     */
    default void showMessage(int mode, String message) {

    }
}
