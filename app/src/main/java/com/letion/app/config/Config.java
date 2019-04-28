package com.letion.app.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.multidex.MultiDex;
import com.blankj.utilcode.util.ProcessUtils;
import com.bumptech.glide.Glide;
import com.squareup.leakcanary.LeakCanary;
import com.weyee.poscore.base.delegate.AppDelegate;
import com.weyee.poscore.base.integration.IConfigModule;
import com.weyee.poscore.base.integration.IRepositoryManager;
import com.weyee.poscore.di.module.CustomModule;
import com.weyee.sdk.imageloader.glide.GlideApp;

import java.util.List;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/12/3 0003
 */
public class Config implements IConfigModule {
    /**
     * 使用{@link CustomModule.Builder}给框架配置一些配置参数
     *
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, CustomModule.Builder builder) {

    }

    /**
     * 使用{@link IRepositoryManager}给框架注入一些网络请求和数据缓存等服务
     *
     * @param context
     * @param repositoryManager
     */
    @Override
    public void registerComponents(Context context, IRepositoryManager repositoryManager) {

    }

    /**
     * 使用{@link AppDelegate.Lifecycle}在Application的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     * @return
     */
    @Override
    public void injectAppLifecycle(Context context, List<AppDelegate.Lifecycle> lifecycles) {
        lifecycles.add(new AppDelegate.Lifecycle() {
            @Override
            public void attachBaseContext(Application application) {
                MultiDex.install(application);
            }

            @Override
            public void onCreate(Application application) {
                if (LeakCanary.isInAnalyzerProcess(application)) {
                    // This process is dedicated to LeakCanary for heap analysis.
                    // You should not init your app in this process.
                    return;
                }
                LeakCanary.install(application);
                if (ProcessUtils.isMainProcess()) {
                    com.weyee.poscore.config.Config.init(application);

                    //DoraemonKit.install(application);


                }
            }

            @Override
            public void onTerminate(Application application) {

            }

            @Override
            public void onLowMemory(Application application) {
                // 当内存不足时，清空Glide中的内存缓存
                GlideApp.get(application).clearMemory();
                // TODO 类似不是很重要的内存数据，都可以在此去做清理
                // 例如文件缓存，大数据，资源

                // 还会在四大组件和Fragment中回调，除了广播
            }

            @Override
            public void onTrimMemory(int level, Application application) {
                // 用户点击了Home键或者Back键退出应用
                if (level == TRIM_MEMORY_UI_HIDDEN){
                    Glide.get(application).clearMemory();
                }
                GlideApp.get(application).trimMemory(level);

                // 还会在四大组件和Fragment中回调，除了广播
            }
        });
    }

    /**
     * 使用{@link Application.ActivityLifecycleCallbacks}在Activity的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 使用{@link FragmentManager.FragmentLifecycleCallbacks}在Fragment的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
            /**
             * Called right before the fragment's {@link Fragment#onAttach(Context)} method is called.
             * This is a good time to inject any required dependencies or perform other configuration
             * for the fragment before any of the fragment's lifecycle methods are invoked.
             *
             * @param fm      Host FragmentManager
             * @param f       Fragment changing state
             * @param context Context that the Fragment is being attached to
             */
            @Override
            public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                super.onFragmentPreAttached(fm, f, context);
            }

            /**
             * Called after the fragment has been attached to its host. Its host will have had
             * <code>onAttachFragment</code> called before this call happens.
             *
             * @param fm      Host FragmentManager
             * @param f       Fragment changing state
             * @param context Context that the Fragment was attached to
             */
            @Override
            public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                super.onFragmentAttached(fm, f, context);
            }

            /**
             * Called right before the fragment's {@link Fragment#onCreate(Bundle)} method is called.
             * This is a good time to inject any required dependencies or perform other configuration
             * for the fragment.
             *
             * @param fm                 Host FragmentManager
             * @param f                  Fragment changing state
             * @param savedInstanceState Saved instance bundle from a previous instance
             */
            @Override
            public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentPreCreated(fm, f, savedInstanceState);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onCreate(Bundle)}. This will only happen once for any given
             * fragment instance, though the fragment may be attached and detached multiple times.
             *
             * @param fm                 Host FragmentManager
             * @param f                  Fragment changing state
             * @param savedInstanceState Saved instance bundle from a previous instance
             */
            @Override
            public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onActivityCreated(Bundle)}. This will only happen once for any given
             * fragment instance, though the fragment may be attached and detached multiple times.
             *
             * @param fm                 Host FragmentManager
             * @param f                  Fragment changing state
             * @param savedInstanceState Saved instance bundle from a previous instance
             */
            @Override
            public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentActivityCreated(fm, f, savedInstanceState);
            }

            /**
             * Called after the fragment has returned a non-null view from the FragmentManager's
             * request to {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}.
             *
             * @param fm                 Host FragmentManager
             * @param f                  Fragment that created and owns the view
             * @param v                  View returned by the fragment
             * @param savedInstanceState Saved instance bundle from a previous instance
             */
            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onStart()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentStarted(fm, f);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onResume()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentResumed(fm, f);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onPause()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentPaused(fm, f);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onStop()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentStopped(fm, f);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onSaveInstanceState(Bundle)}.
             *
             * @param fm       Host FragmentManager
             * @param f        Fragment changing state
             * @param outState Saved state bundle for the fragment
             */
            @Override
            public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
                super.onFragmentSaveInstanceState(fm, f, outState);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onDestroyView()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentViewDestroyed(fm, f);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onDestroy()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDestroyed(fm, f);
            }

            /**
             * Called after the fragment has returned from the FragmentManager's call to
             * {@link Fragment#onDetach()}.
             *
             * @param fm Host FragmentManager
             * @param f  Fragment changing state
             */
            @Override
            public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentDetached(fm, f);
            }
        });
    }
}
