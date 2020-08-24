package com.zjp.plugin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fy.baselibrary.aop.annotation.NeedPermission;
import com.fy.baselibrary.plugin.PluginManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    //跳转插件
    @NeedPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.VIBRATE, Manifest.permission.INTERNET})
    public void skipPlugin(View view) {
        PluginManager.loadPlugin(this, Environment.getExternalStorageDirectory() + "/orderfood.apk");
        Bundle bundle = new Bundle();
        bundle.putBoolean("takePicMode", false);
        PluginManager.jumpPlugin(this, "com.plateid.activity.PlateidCameraActivity", bundle, 1212);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}