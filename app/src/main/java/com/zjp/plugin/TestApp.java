package com.zjp.plugin;

import android.app.Application;
import android.content.Context;

import com.fy.baselibrary.application.BaseActivityLifecycleCallbacks;
import com.fy.baselibrary.application.ioc.ConfigUtils;
import com.fy.baselibrary.utils.ResUtils;
import com.fy.baselibrary.utils.ScreenUtils;

/**
 * description </p>
 * Created by fangs on 2020/6/19 16:55.
 */
public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        initConfig(this);

        initConfig();
    }

    private void initConfig() {
        int designWidth = (int) ResUtils.getMetaData(this, "rudeness_Adapter_Screen_width", 0);
        ScreenUtils.setCustomDensity(this, designWidth);

//        设置activity 生命周期回调
        registerActivityLifecycleCallbacks(new BaseActivityLifecycleCallbacks());
    }

    private void initConfig(Context context) {
        new ConfigUtils.ConfigBiuder()
                .setBgColor(R.color.appHeadBg)
                .setTitleColor(R.color.white)
                .setTitleCenter(true)
                .setBASE_URL("")
                .create(context);
    }

}
