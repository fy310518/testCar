package com.zjp.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fy.baselibrary.plugin.PluginManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipPlugin();
            }
        });
    }

    public void skipPlugin() {
        PluginManager.loadPlugin(this, Environment.getExternalStorageDirectory() + "/carApp-debug.apk");
        Bundle bundle = new Bundle();
        bundle.putBoolean("takePicMode", true);
        bundle.remove("ActivityBean");
        PluginManager.jumpPlugin(this, "com.plateid.activity.PlateidCameraActivity", bundle, 1212);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, data.getStringArrayExtra("RecogResult")[0], Toast.LENGTH_LONG).show();
    }
}