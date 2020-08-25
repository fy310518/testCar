package com.zjp.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cxy.plugin.PluginManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void skipPlugin(View view) {
        PluginManager.loadPlugin(this, Environment.getExternalStorageDirectory() + "/carApp-debug.apk");
        Bundle bundle = new Bundle();
        bundle.putBoolean("takePicMode", true);
        PluginManager.jumpPlugin(this, "com.plateid.activity.PlateidCameraActivity", bundle, 1212);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}