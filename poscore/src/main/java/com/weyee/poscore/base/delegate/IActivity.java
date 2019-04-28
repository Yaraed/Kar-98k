package com.weyee.poscore.base.delegate;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.weyee.poscore.base.BaseFragment;
import com.weyee.poscore.di.component.AppComponent;
import com.weyee.sdk.api.observer.listener.ProgressAble;

/**
 * Created by liu-feng on 2017/6/5.
 */
public interface IActivity extends ProgressAble {
    /**
     * 提供AppComponent(提供所有的单例对象)给实现类，进行Component依赖
     *
     * @param appComponent
     */
    void setupActivityComponent(@Nullable AppComponent appComponent);

    boolean useEventBus();

    /**
     * 是否使用Loading框
     *
     * @return
     */
    boolean useProgressAble();


    /**
     * 如果initView返回0,框架则不会调用{@link android.app.Activity#setContentView(int)}
     *
     * @return
     */
    int getResourceId();

    void initView(@Nullable Bundle savedInstanceState);

    void initData(@Nullable Bundle savedInstanceState);

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();
}
