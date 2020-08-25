package com.kernal.plateid;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.kernal.lisence.CTelephoneInfo;
import com.kernal.lisence.DateAuthFileOperate;
import com.kernal.lisence.MachineCode;
import com.kernal.lisence.ModeAuthFileOperate;
import com.kernal.lisence.ModeAuthFileResult;
import com.kernal.lisence.ProcedureAuthOperate;
import com.kernal.lisence.VersionAuthFileOperate;

import java.io.IOException;
import java.io.InputStream;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:47.
 */
public class AuthService extends Service  {

    public static final String TAG = "AuthService";
    public MyBinder binder;
    int nRet = -1;
    private String mcode;
    private String deviceId;
    private String androId;
    private String simId;
    CTelephoneInfo telephonyInfo;
    public String imeiSIM1;
    public String imeiSIM2;
    private TelephonyManager telephonyManager;
    private String productType = "10";
    private MachineCode machineCode;
    private ModeAuthFileResult mafr = new ModeAuthFileResult();

    public AuthService() {
    }

    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    private String readAssetFile(String filename) {
        String typeModeString = null;

        try {
            InputStream iStream = this.getAssets().open(filename);
            int size_is = iStream.available();
            byte[] byte_new = new byte[size_is];
            iStream.read(byte_new);
            iStream.close();
            typeModeString = new String(byte_new);
        } catch (IOException var6) {
            typeModeString = null;
        } catch (Exception var7) {
            typeModeString = null;
        }

        return typeModeString;
    }

    @SuppressLint({"WrongConstant", "MissingPermission"})
    public void onCreate() {
        super.onCreate();
        this.telephonyManager = (TelephonyManager)this.getSystemService("phone");
        this.binder = new MyBinder();
        this.telephonyManager = (TelephonyManager)this.getSystemService("phone");

        StringBuilder sb1;
        try {
            sb1 = new StringBuilder();
            sb1.append(telephonyManager.getDeviceId());
            this.deviceId = sb1.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(telephonyManager.getSimSerialNumber());
            this.simId = sb2.toString();
            this.telephonyInfo = CTelephoneInfo.getInstance(this.getApplicationContext());
            this.telephonyInfo.setCTelephoneInfo();
            this.imeiSIM1 = this.telephonyInfo.getImeiSIM1();
            this.imeiSIM2 = this.telephonyInfo.getImeiSIM2();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        sb1 = new StringBuilder();
        sb1.append(Settings.Secure.getString(this.getContentResolver(), "android_id"));
        this.androId = sb1.toString();
        String miwenxml = this.readAssetFile("plateocr/authmode.lsc");
        ModeAuthFileOperate mafo = new ModeAuthFileOperate();
        this.mafr = mafo.ReadAuthFile(miwenxml);
        if (miwenxml != null && (this.mafr.isCheckPRJMode(this.productType) || this.mafr.isTF(this.productType))) {
            Log.e("DEBUG", "DEBUG 10501");
            this.nRet = -10501;
        }

        this.machineCode = new MachineCode();
        this.mcode = this.machineCode.MachineNO("1.0", this.deviceId, this.androId, this.simId);
    }

    public class MyBinder extends Binder {
        String did = "";

        public MyBinder() {
        }

        public int getAuth(String sn, String authFile) throws Exception {
            ProcedureAuthOperate pao = new ProcedureAuthOperate(AuthService.this);
            AuthService.this.nRet = pao.getWintoneAuth(AuthService.this.productType, sn, authFile, AuthService.this.mcode, AuthService.this.deviceId, AuthService.this.androId, AuthService.this.simId, "");
            return AuthService.this.nRet;
        }

        public int getAuth(PlateAuthParameter pap) {
            if (AuthService.this.nRet == -10501) {
                return AuthService.this.nRet;
            } else {
                String productversionfile = pap.versionfile;
                String devcode = pap.devCode;
                if (productversionfile != null && !productversionfile.equals("")) {
                    VersionAuthFileOperate vafo = new VersionAuthFileOperate();
                    AuthService.this.nRet = vafo.getVersionAuthFile(productversionfile, devcode, AuthService.this.productType, "", AuthService.this.deviceId);
                } else {
                    ProcedureAuthOperate pao = new ProcedureAuthOperate(AuthService.this);
                    if (pap.dataFile != null && !pap.dataFile.equals("") && !pap.dataFile.equals("null")) {
                        DateAuthFileOperate dafo = new DateAuthFileOperate();
                        AuthService.this.nRet = dafo.getDateAuth(pap.dataFile, pap.devCode, AuthService.this.productType, AuthService.this.deviceId);
                        if (AuthService.this.nRet == -10090) {
                            Toast.makeText(AuthService.this.getApplicationContext(), "您的授权已于" + dafo.getEndDateString() + "到期，请更新授权，否则识别功能将停止使用！", Toast.LENGTH_LONG).show();
                            AuthService.this.nRet = 0;
                        }
                    } else {
                        AuthService.this.nRet = pao.getWintoneAuth(AuthService.this.productType, pap.sn, pap.authFile, AuthService.this.mcode, AuthService.this.deviceId, AuthService.this.androId, AuthService.this.simId, pap.server);
                        if (AuthService.this.nRet == -10008) {
                            pao = new ProcedureAuthOperate(AuthService.this);
                            if (AuthService.this.deviceId != null && !AuthService.this.deviceId.equals(AuthService.this.imeiSIM1)) {
                                AuthService.this.mcode = AuthService.this.machineCode.MachineNO("1.0", AuthService.this.imeiSIM1, AuthService.this.androId, AuthService.this.simId);
                                AuthService.this.nRet = pao.getWintoneAuth(AuthService.this.productType, pap.sn, pap.authFile, AuthService.this.mcode, AuthService.this.imeiSIM1, AuthService.this.androId, AuthService.this.simId, pap.server);
                            } else if (AuthService.this.deviceId != null && !AuthService.this.deviceId.equals(AuthService.this.imeiSIM2)) {
                                AuthService.this.mcode = AuthService.this.machineCode.MachineNO("1.0", AuthService.this.imeiSIM2, AuthService.this.androId, AuthService.this.simId);
                                AuthService.this.nRet = pao.getWintoneAuth(AuthService.this.productType, pap.sn, pap.authFile, AuthService.this.mcode, AuthService.this.imeiSIM2, AuthService.this.androId, AuthService.this.simId, pap.server);
                            }
                        }
                    }
                }

                return AuthService.this.nRet;
            }
        }

        public int getnRet() {
            return AuthService.this.nRet;
        }

        public String getDeviceId() {
            return this.did;
        }
    }
}
