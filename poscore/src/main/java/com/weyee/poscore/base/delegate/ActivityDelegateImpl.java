package com.weyee.poscore.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;

import com.letion.geetionlib.base.App;
import com.letion.geetionlib.vender.log.L;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liu-feng on 2017/6/5.
 */
public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;
    private Unbinder mUnbinder;

    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (iActivity.useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(mActivity);//注册到事件主线
        iActivity.setupActivityComponent(((App) mActivity.getApplication()).getAppComponent());
        //依赖注入
        try {
            int layoutResID = iActivity.getResourceId();
            if (layoutResID != 0)//如果initView返回0,框架则不会调用setContentView()
                mActivity.setContentView(layoutResID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //绑定到butterknife
        mUnbinder = ButterKnife.bind(mActivity);
        iActivity.initView(savedInstanceState);
        iActivity.initData(savedInstanceState);
    }

    @Override
    public void onStart() {
        L.d("params : " + " maybe [file part] , activity start");
        L.e("params : " + " maybe [file part] , activity start");
    }

    @Override
    public void onResume() {
        L.d("params : " + " maybe [file part] , activity resume");
    }

    @Override
    public void onPause() {
        L.d("params : " + " maybe [file part] , activity pause");
    }

    @Override
    public void onStop() {
        L.d("params : " + " maybe [file part] , activity stop");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (iActivity.useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(mActivity);
        this.mUnbinder = null;
        this.iActivity = null;
        this.mActivity = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected ActivityDelegateImpl(Parcel in) {
        this.mActivity = in.readParcelable(Activity.class.getClassLoader());
        this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
        this.mUnbinder = in.readParcelable(Unbinder.class.getClassLoader());
    }

    public static final Creator<ActivityDelegateImpl> CREATOR = new Creator<ActivityDelegateImpl>
            () {
        @Override
        public ActivityDelegateImpl createFromParcel(Parcel source) {
            return new ActivityDelegateImpl(source);
        }

        @Override
        public ActivityDelegateImpl[] newArray(int size) {
            return new ActivityDelegateImpl[size];
        }
    };
}
