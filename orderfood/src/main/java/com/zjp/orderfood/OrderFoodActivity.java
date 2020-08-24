package com.zjp.orderfood;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fy.baselibrary.plugin.PluginBaseActivity;


public class OrderFoodActivity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_order_food);

        this.findViewById(R.id.txtTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.setAction("com.zjp.plugin.ProxyActivity");
                intent.putExtra("className", "com.zjp.orderfood.OrderFoodActivity2");
                mProxyActivity.startActivityForResult(intent, 1313);
            }
        });

        this.findViewById(R.id.txtReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("test", "大王");
                Bundle bundle = intent.getExtras();
//                if (null == bundle) bundle = new Bundle();
//                assert bundle != null;
//                bundle.putString("asas", "大王");
                intent.putExtras(bundle);
                mProxyActivity.setResult(Activity.RESULT_OK, intent);
                mProxyActivity.finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
