package com.zjp.plugin;

import android.app.Application;

/**
 * description </p>
 * Created by fangs on 2020/6/19 16:55.
 */
public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }


}
