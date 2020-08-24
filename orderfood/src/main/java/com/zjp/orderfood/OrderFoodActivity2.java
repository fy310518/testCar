package com.zjp.orderfood;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fy.baselibrary.plugin.PluginBaseActivity;


public class OrderFoodActivity2 extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_order_food);
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("test plugin 二")
        ;
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
}
