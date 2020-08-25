package com.cxy.plugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cxy.plugin.service.ProxyService;

/**
 * 接口方式插件化
 * 插件app 父类 activity
 */
public class PluginBaseActivity extends Activity implements PluginInterface {

    protected Activity mProxyActivity;

    @Override
    public void attach(Activity proxyActivity) {
        this.mProxyActivity = proxyActivity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle saveInstanceState) {
        if (null == mProxyActivity) {
            super.onCreate(saveInstanceState);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {
        if (null == mProxyActivity) {
            super.onStart();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {
        if (null == mProxyActivity) {
            super.onResume();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {
        if (null == mProxyActivity) {
            super.onPause();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {
        if (null == mProxyActivity) {
            super.onStop();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
        if (null == mProxyActivity) {
            super.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == mProxyActivity) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (null != mProxyActivity) {
            mProxyActivity.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (null != mProxyActivity) {
            mProxyActivity.startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if (null != mProxyActivity) {
            return mProxyActivity.startService(service);
        } else {
            return super.startService(service);
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if (null != mProxyActivity) {
            return mProxyActivity.bindService(service, conn, flags);
        } else {
            return super.bindService(service, conn, flags);
        }
    }

    @Override
    public void onSaveInstanceStates(@NonNull Bundle outState) {
        if (null != mProxyActivity) {
//            mProxyActivity.onSaveInstanceState(outState);
        } else {
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void setContentView(View view) {
        if (null != mProxyActivity) {
            mProxyActivity.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (null != mProxyActivity) {
            mProxyActivity.setContentView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if (null != mProxyActivity) {
            return mProxyActivity.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if (null != mProxyActivity) {
            return mProxyActivity.getLayoutInflater();
        } else {
            return super.getLayoutInflater();
        }
    }

    @Override
    public Intent getIntent() {
        if (null != mProxyActivity) {
            return mProxyActivity.getIntent();
        } else {
            return super.getIntent();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if (null != mProxyActivity) {
            return mProxyActivity.getClassLoader();
        } else {
            return super.getClassLoader();
        }
    }

    @Override
    public Window getWindow() {
        if (null != mProxyActivity) {
            return mProxyActivity.getWindow();
        } else {
            return super.getWindow();
        }
    }


    @Override
    public WindowManager getWindowManager() {
        if (null != mProxyActivity) {
            return mProxyActivity.getWindowManager();
        } else {
            return super.getWindowManager();
        }
    }

    @Override
    public Resources getResources() {
        if (null != mProxyActivity) {
            return PluginManager.getInstance().getPluginResource();
        } else {
            return super.getResources();
        }
    }

    //获取当前环境
    public Activity getContext(){
        if (null != mProxyActivity) {
            return mProxyActivity;
        } else {
            return this;
        }
    }

    public void finish() {
        if (null != mProxyActivity) {
            mProxyActivity.finish();
        } else {
            super.finish();
        }
    }

    /**
     * 插件内 activity 启动另一个activity
     * @param activityName 要启动的 activity 完整包名【如：com.fy.baselibrary.plugin.ProxyActivity】
     * @param bundle
     */
    public void jumpPlugin(@NonNull String activityName, Bundle bundle) {
        Intent intent = getIntent();
        intent.setAction(getLocalPackageName() + ".plugin.ProxyActivity");
        intent.putExtra("className", activityName);

        Bundle targetBundle = intent.getExtras();
        if (null != bundle){
            assert targetBundle != null;
            targetBundle.putAll(bundle);
        }
        intent.putExtras(targetBundle);

        startActivity(intent);
    }

    /**
     * 插件内 activity 启动另一个activity
     * @param activityName 要启动的 activity 完整包名【如：com.fy.baselibrary.plugin.ProxyActivity】
     * @param bundle
     * @param requestCode
     */
    public void jumpPlugin(@NonNull String activityName, Bundle bundle, int requestCode) {
        Intent intent = getIntent();
        intent.setAction(getLocalPackageName() + ".plugin.ProxyActivity");
        intent.putExtra("className", activityName);

        Bundle targetBundle = intent.getExtras();
        if (null != bundle){
            assert targetBundle != null;
            targetBundle.putAll(bundle);
        }
        intent.putExtras(targetBundle);

        startActivityForResult(intent, requestCode);
    }

    /**
     * 插件中，退出当前activity 并带数据回到上一个Activity
     * @param bundle 可空
     */
    public void jumpResult(Bundle bundle){
        Intent intent = getIntent();
        Bundle targetBundle = intent.getExtras();

        if (null != bundle){
            assert targetBundle != null;
            targetBundle.putAll(bundle);
        }
        intent.putExtras(targetBundle);

        if (null != mProxyActivity){
            mProxyActivity.setResult(Activity.RESULT_OK, intent);
            mProxyActivity.finish();
        } else {
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    public void startPservice(@NonNull String serviceName, Bundle bundle) {
        Intent intent = getIntent();
        if (null != mProxyActivity) {
            intent.setAction(getLocalPackageName() + ".plugin.ProxyService");
            intent.setPackage(getLocalPackageName());
            intent.putExtra("serviceName", serviceName);
        } else {
            intent.setClassName(getLocalPackageName(), serviceName);
        }

        Bundle targetBundle = intent.getExtras();
        if (null != bundle){
            assert targetBundle != null;
            targetBundle.putAll(bundle);
        }
        intent.putExtras(targetBundle);

        startService(intent);
    }

    public void bindPservice(@NonNull String serviceName, Bundle bundle, ServiceConnection conn, int flags) {
        Intent intent = getIntent();
        if (null != mProxyActivity) {
            intent.setClassName(getLocalPackageName(), "com.cxy.plugin.service.ProxyService");
            intent.putExtra("serviceName", serviceName);
        } else {
            intent.setClassName(getLocalPackageName(), serviceName);
        }

        Bundle targetBundle = intent.getExtras();
        if (null != bundle){
            assert targetBundle != null;
            targetBundle.putAll(bundle);
        }
        intent.putExtras(targetBundle);

        bindService(intent, conn, flags);
    }

    public String getLocalPackageName() {
        try {
            Context context = getContext();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "获取失败";
        }
    }
}