package com.kernal.plateid;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:59.
 */
public class TestPlateActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "TestPlateServiceDemo";
    String cls;
    String pic;
    int imageformat = 1;
    int width = 420;
    int height = 232;
    boolean bGetVersion = false;
    String sn;
    String authfile;
    int bVertFlip = 0;
    int bDwordAligned = 1;
    String userdata;
    private Button setButton;
    private Button authButton;
    private Button recogButton;
    private int ReturnAuthority = -1;
    String[] fieldvalue = new String[14];
    int nRet = -1;
    String returnGetVersion = "";
    public AuthService.MyBinder authBinder;
    public RecogService.MyBinder recogBinder;
    public Integer lock = 0;
    public String sdDir;
    public Intent recogIntent;
    private EditText editresult;
    String[] fieldname = new String[14];
    public ServiceConnection authConn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            TestPlateActivity.this.authBinder = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("TestPlateServiceDemo", "authConn onServiceConnected");
            TestPlateActivity.this.authBinder = (AuthService.MyBinder)service;
            Toast.makeText(TestPlateActivity.this.getApplicationContext(), "授权验证 绑定成功", 0).show();

            try {
                TestPlateActivity.this.ReturnAuthority = TestPlateActivity.this.authBinder.getAuth(TestPlateActivity.this.sn, TestPlateActivity.this.authfile);
                if (TestPlateActivity.this.ReturnAuthority != 0) {
                    TestPlateActivity.this.recogButton.setEnabled(false);
                    String[] str = new String[]{"" + TestPlateActivity.this.ReturnAuthority};
                    TestPlateActivity.this.getResult(str);
                    Toast.makeText(TestPlateActivity.this.getApplicationContext(), "授权验证失败", 0).show();
                } else {
                    TestPlateActivity.this.recogIntent = new Intent(TestPlateActivity.this, RecogService.class);
                    Toast.makeText(TestPlateActivity.this.getApplicationContext(), "授权验证成功", 0).show();
                }

                if (TestPlateActivity.this.authBinder != null) {
                    TestPlateActivity.this.unbindService(TestPlateActivity.this.authConn);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
                Log.i("TestPlateServiceDemo", "e=" + var4.toString());
            }

        }
    };
    public ServiceConnection recogConn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            TestPlateActivity.this.recogConn = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            TestPlateActivity.this.recogBinder = (RecogService.MyBinder)service;
            Toast.makeText(TestPlateActivity.this.getApplicationContext(), "识别程序 绑定成功", 0).show();
            if (TestPlateActivity.this.ReturnAuthority == 0) {
                if (TestPlateActivity.this.recogBinder.getInitPlateIDSDK() != 0) {
                    TestPlateActivity.this.nRet = -10001;
                } else {
                    TestPlateActivity.this.recogBinder.setRecogArgu(TestPlateActivity.this.pic, TestPlateActivity.this.imageformat, TestPlateActivity.this.bGetVersion, TestPlateActivity.this.bVertFlip, TestPlateActivity.this.bDwordAligned);
                    TestPlateActivity.this.fieldvalue = TestPlateActivity.this.recogBinder.doRecog(TestPlateActivity.this.pic, TestPlateActivity.this.width, TestPlateActivity.this.height);
                    TestPlateActivity.this.nRet = TestPlateActivity.this.recogBinder.getnRet();
                    TestPlateActivity.this.getResult(TestPlateActivity.this.fieldvalue);
                    TestPlateActivity.this.unbindService(TestPlateActivity.this.recogConn);
                }
            }

        }
    };

    public TestPlateActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TestPlateServiceDemo", "onCreate");
        this.setContentView(2130903047);
        this.findViews();
        this.setButton.setOnClickListener(this);
        this.authButton.setOnClickListener(this);
        this.recogButton.setOnClickListener(this);
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        if (sdCardExist) {
            this.sdDir = Environment.getExternalStorageDirectory().toString() + "/123456.jpg";
        }

        Log.i("TestPlateServiceDemo", "sdDir=" + this.sdDir);
        this.pic = this.sdDir;
        this.authfile = Environment.getExternalStorageDirectory().toString() + "/357816040594713_cp.txt";
    }

    private void findViews() {
        int butsetId = this.getResources().getIdentifier("butset", "id", this.getPackageName());
        this.setButton = (Button)this.findViewById(butsetId);
        int authButtonId = this.getResources().getIdentifier("butauth", "id", this.getPackageName());
        this.authButton = (Button)this.findViewById(authButtonId);
        int recogButtonId = this.getResources().getIdentifier("butrecog", "id", this.getPackageName());
        this.recogButton = (Button)this.findViewById(recogButtonId);
        int editresultId = this.getResources().getIdentifier("editText1", "id", this.getPackageName());
        this.editresult = (EditText)this.findViewById(editresultId);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.recogBinder != null && this.recogBinder.isBinderAlive()) {
            this.stopService(this.recogIntent);
        }

    }

    public void onClick(View v) {
        Log.i("TestPlateServiceDemo", "v.getId()=" + v.getId());
        switch(v.getId()) {
            case 2131034201:
                Intent intent = new Intent(this, PlateIDCfg.class);
                this.startActivity(intent);
            case 2131034202:
            case 2131034203:
            default:
                break;
            case 2131034204:
                Intent authIntent = new Intent(this, AuthService.class);
                this.bindService(authIntent, this.authConn, 1);
                break;
            case 2131034205:
                this.editresult.setText("");
                if (this.ReturnAuthority == 0) {
                    this.bindService(this.recogIntent, this.recogConn, 1);
                }
        }

    }

    private void getResult(String[] fieldvalue) {
        if (this.nRet != 0) {
            this.editresult.setText("识别结果 :" + fieldvalue[0] + "\n");
        } else {
            String result = "";
            if (this.fieldname != null) {
                int count = this.fieldname.length;

                for(int i = 0; i < count; ++i) {
                    result = result + this.fieldname[i] + ":" + fieldvalue[i] + ";\n";
                }
            }

            this.editresult.setText("识别结果 :" + this.nRet + "\n" + result);
        }

    }
}
