package com.kernal.plateid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.kernal.lisence.Common;
import com.kernal.lisence.MachineCode;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:53.
 */
public class PlateIDBean extends Activity {
    public static final String TAG = "PlateIDBean";
    private ProgressDialog progressDialog = null;
    String cls;
    String pic;
    byte[] preByte;
    int imageformat;
    int width;
    int height;
    boolean bGetVersion;
    String sn;
    String authfile;
    String versionfile;
    String server;
    String devCode;
    String dataFile;
    int bVertFlip;
    int bDwordAligned;
    String userdata;
    private String mcode;
    private String deviceId;
    private String androId;
    private String simId;
    private String returntype = "";
    private int ReturnInitIDCard = -1;
    private int ReturnLoadImageToMemory = -1;
    private int ReturnRecogIDCard = -1;
    private int ReturnAuthority = -1;
    String[] fieldname = new String[14];
    String[] fieldvalue = new String[14];
    int nRet = -1;
    String returnGetVersion = "";
    public AuthService.MyBinder authBinder;
    public RecogService.MyBinder recogBinder;
    public Integer lock = 0;
    public ServiceConnection authConn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            PlateIDBean.this.authBinder = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized(PlateIDBean.this.lock) {
                PlateIDBean.this.authBinder = (AuthService.MyBinder)service;
                PlateIDBean.this.lock.notify();
            }
        }
    };
    public ServiceConnection recogConn = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            PlateIDBean.this.recogConn = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized(PlateIDBean.this.lock) {
                PlateIDBean.this.recogBinder = (RecogService.MyBinder)service;
                PlateIDBean.this.lock.notify();
            }
        }
    };
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Intent recogIntent = new Intent(PlateIDBean.this, RecogService.class);
                if (PlateIDBean.this.recogBinder == null) {
                    PlateIDBean.this.bindService(recogIntent, PlateIDBean.this.recogConn, 1);
                }

                (new Thread(new Runnable() {
                    public void run() {
                        Looper.prepare();
                        synchronized(PlateIDBean.this.lock) {
                            try {
                                if (PlateIDBean.this.recogBinder == null) {
                                    PlateIDBean.this.lock.wait();
                                }
                            } catch (InterruptedException var5) {
                                var5.printStackTrace();
                            }
                        }

                        try {
                            if (PlateIDBean.this.ReturnAuthority == 0) {
                                if (PlateIDBean.this.preByte.length > 0) {
                                    PlateIDBean.this.imageformat = 6;
                                    PlateIDBean.this.nRet = PlateIDBean.this.javamain(PlateIDBean.this.preByte, PlateIDBean.this.imageformat, PlateIDBean.this.width, PlateIDBean.this.height, PlateIDBean.this.bGetVersion, PlateIDBean.this.bVertFlip, PlateIDBean.this.bDwordAligned, PlateIDBean.this.sn, PlateIDBean.this.authfile);
                                } else {
                                    PlateIDBean.this.nRet = PlateIDBean.this.javamain(PlateIDBean.this.pic, PlateIDBean.this.imageformat, PlateIDBean.this.width, PlateIDBean.this.height, PlateIDBean.this.bGetVersion, PlateIDBean.this.bVertFlip, PlateIDBean.this.bDwordAligned, PlateIDBean.this.sn, PlateIDBean.this.authfile);
                                }
                            }
                        } catch (Exception var4) {
                            var4.printStackTrace();
                            Log.i("PlateIDBean", "e1=" + var4);
                        }

                        try {
                            PlateIDBean.this.progressDialog.dismiss();
                            Intent intent = new Intent("plateid.receiver");
                            Bundle bundle = new Bundle();
                            bundle.putString("returnGetVersion", PlateIDBean.this.returnGetVersion);
                            bundle.putInt("nRet", PlateIDBean.this.nRet);
                            bundle.putString("ReturnUserData", PlateIDBean.this.userdata);
                            bundle.putString("ReturnLPFileName", PlateIDBean.this.pic);
                            bundle.putSerializable("fieldname", PlateIDBean.this.fieldname);
                            bundle.putSerializable("fieldvalue", PlateIDBean.this.fieldvalue);
                            intent.putExtras(bundle);
                            if (PlateIDBean.this.returntype.equals("withvalue")) {
                                PlateIDBean.this.setResult(-1, intent);
                                PlateIDBean.this.finish();
                            } else {
                                PlateIDBean.this.startActivity(intent);
                            }
                        } catch (Exception var3) {
                            var3.printStackTrace();
                            Log.i("PlateIDBean", "没找到应用程序！");
                        }

                    }
                })).start();
            }

        }
    };

    public PlateIDBean() {
    }

    @SuppressLint("MissingPermission")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        Intent mIntent = this.getIntent();
        @SuppressLint("WrongConstant")
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService("phone");
        StringBuilder sb = new StringBuilder();
        sb.append(telephonyManager.getDeviceId());
        this.deviceId = sb.toString();
        StringBuilder sb1 = new StringBuilder();
        sb1.append(Settings.Secure.getString(this.getContentResolver(), "android_id"));
        this.androId = sb1.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(telephonyManager.getSimSerialNumber());
        this.simId = sb2.toString();
        MachineCode machineCode = new MachineCode();
        this.mcode = machineCode.MachineNO("1.0", this.deviceId, this.androId, this.simId);
        this.cls = mIntent.getStringExtra("cls");
        this.pic = mIntent.getStringExtra("pic");
        this.preByte = mIntent.getByteArrayExtra("yuv");
        this.imageformat = mIntent.getIntExtra("imageformat", 1);
        this.width = mIntent.getIntExtra("width", 420);
        this.height = mIntent.getIntExtra("height", 232);
        this.bVertFlip = mIntent.getIntExtra("bVertFlip", 0);
        this.bDwordAligned = mIntent.getIntExtra("bDwordAligned", 1);
        this.bGetVersion = mIntent.getBooleanExtra("GetVersion", false);
        this.sn = mIntent.getStringExtra("sn");
        this.server = mIntent.getStringExtra("server");
        this.devCode = mIntent.getStringExtra("devCode");
        this.dataFile = mIntent.getStringExtra("dataFile");
        this.authfile = mIntent.getStringExtra("authfile");
        this.versionfile = mIntent.getStringExtra("versionfile");
        this.userdata = mIntent.getStringExtra("userdata");
        this.returntype = mIntent.getStringExtra("returntype");
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setTitle("车牌识别");
        this.progressDialog.setMessage("！正在识别...");
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.show();
        Intent authIntent = new Intent(this, AuthService.class);
        this.bindService(authIntent, this.authConn, 1);
        (new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                synchronized(PlateIDBean.this.lock) {
                    try {
                        if (PlateIDBean.this.authBinder == null) {
                            PlateIDBean.this.lock.wait();
                        }
                    } catch (InterruptedException var6) {
                        var6.printStackTrace();
                    }
                }

                try {
                    PlateAuthParameter pap = new PlateAuthParameter();
                    pap.sn = PlateIDBean.this.sn;
                    pap.server = PlateIDBean.this.server;
                    pap.devCode = PlateIDBean.this.devCode;
                    pap.dataFile = PlateIDBean.this.dataFile;
                    pap.authFile = PlateIDBean.this.authfile;
                    pap.versionfile = PlateIDBean.this.versionfile;
                    PlateIDBean.this.ReturnAuthority = PlateIDBean.this.authBinder.getAuth(pap);
                    if (PlateIDBean.this.ReturnAuthority != 0) {
                        try {
                            PlateIDBean.this.progressDialog.dismiss();
                            Intent intent = new Intent("plateid.receiver");
                            Bundle bundle = new Bundle();
                            bundle.putString("returnGetVersion", PlateIDBean.this.returnGetVersion);
                            bundle.putInt("nRet", PlateIDBean.this.ReturnAuthority);
                            bundle.putString("ReturnUserData", PlateIDBean.this.userdata);
                            bundle.putString("ReturnLPFileName", PlateIDBean.this.pic);
                            bundle.putSerializable("fieldname", PlateIDBean.this.fieldname);
                            bundle.putSerializable("fieldvalue", PlateIDBean.this.fieldvalue);
                            intent.putExtras(bundle);
                            if (PlateIDBean.this.returntype.equals("withvalue")) {
                                PlateIDBean.this.setResult(-1, intent);
                                PlateIDBean.this.finish();
                            } else {
                                PlateIDBean.this.startActivity(intent);
                            }
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }
                    } else {
                        Message msg = new Message();
                        msg.what = PlateIDBean.this.ReturnAuthority;
                        PlateIDBean.this.handler.sendMessage(msg);
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }

            }
        })).start();
    }

    private int javamain(String pic, int imageformat, int width, int height, boolean bGetVersion, int bVertFlip, int bDwordAligned, String sn, String authfile) throws Exception {
        this.nRet = this.recogBinder.getInitPlateIDSDK();
        if (this.nRet == 0) {
            this.recogBinder.setRecogArgu(pic, imageformat, bGetVersion, bVertFlip, bDwordAligned);
            PlateRecognitionParameter prp = new PlateRecognitionParameter();
            prp.dataFile = this.dataFile;
            prp.devCode = this.devCode;
            prp.height = height;
            prp.width = width;
            prp.pic = pic;
            prp.versionfile = this.versionfile;
            this.fieldvalue = this.recogBinder.doRecogDetail(prp);
            this.nRet = this.recogBinder.getnRet();
        }

        return this.nRet;
    }

    private int javamain(byte[] picByte, int imageformat, int width, int height, boolean bGetVersion, int bVertFlip, int bDwordAligned, String sn, String authfile) throws Exception {
        this.nRet = this.recogBinder.getInitPlateIDSDK();
        if (this.nRet == 0) {
            this.recogBinder.setRecogArgu(this.pic, imageformat, bGetVersion, bVertFlip, bDwordAligned);
            PlateRecognitionParameter prp = new PlateRecognitionParameter();
            prp.dataFile = this.dataFile;
            prp.devCode = this.devCode;
            prp.height = height;
            prp.width = width;
            prp.picByte = picByte;
            prp.versionfile = this.versionfile;
            this.fieldvalue = this.recogBinder.doRecogDetail(prp);
            this.nRet = this.recogBinder.getnRet();
        }

        return this.nRet;
    }

    protected void onStop() {
        super.onStop();
        if (this.authBinder != null) {
            this.unbindService(this.authConn);
        }

        if (this.recogBinder != null) {
            this.unbindService(this.recogConn);
        }

        this.finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public String readInStream(FileInputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            boolean var4 = true;

            int length;
            while((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException var5) {
            Log.i("FileTest", var5.getMessage());
            return null;
        }
    }

    public String readtxt() throws IOException {
        Common common = new Common();
        String paths = common.getSDPath();
        if (!paths.equals("") && paths != null) {
            String fullpath = paths + "/wintone/plateid.cfg";
            File file = new File(fullpath);
            if (!file.exists()) {
                return "";
            } else {
                FileReader fileReader = new FileReader(fullpath);
                BufferedReader br = new BufferedReader(fileReader);
                String str = "";

                for(String r = br.readLine(); r != null; r = br.readLine()) {
                    str = str + r;
                }

                br.close();
                fileReader.close();
                return str;
            }
        } else {
            return "";
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation != 2) {
            int var10000 = this.getResources().getConfiguration().orientation;
        }

    }
}
