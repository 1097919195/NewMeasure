package com.example.gxkj.newmeasure.app;

import android.content.Context;

import com.example.gxkj.newmeasure.BuildConfig;
import com.facebook.stetho.Stetho;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.polidea.rxandroidble2.RxBleClient;
import com.squareup.leakcanary.LeakCanary;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class AppApplication extends BaseApplication {
    private RxBleClient rxBleClient;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger,注意拷贝的话BuildConfig.LOG_DEBUG一定要是在当前module下的包名，配置文件中判断测适和发行版本
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
        rxBleClient = RxBleClient.create(this);
        setRxJavaErrorHandler();

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);//浏览器抓包
    }

    public static RxBleClient getRxBleClient(Context context) {
        AppApplication application = ((AppApplication) context.getApplicationContext());
        return application.rxBleClient;
    }

    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：http://engineering.rallyhealth.com/mobile/rxjava/reactive/2017/03/15/migrating-to-rxjava-2.html#Error Handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwable -> LogUtils.loge("throw test RxJava2===="+throwable.getMessage()));
    }
}
