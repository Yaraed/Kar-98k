package com.weyee.poscore.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.weyee.poscore.base.App;
import com.weyee.sdk.event.Bus;

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
            Bus.getDefault().register(mActivity);//注册到事件主线
        if (iActivity.useProgressAble()) {
            iActivity.initProgressAble();
        }
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
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (iActivity.useEventBus())//如果要使用eventbus请将此方法返回true
            Bus.getDefault().unregister(mActivity);
        if (iActivity.useProgressAble()) { // fix bug解决Activity销毁Dialog未关闭的情况
            iActivity.hideProgress();
        }
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
