package com.kernal.plateid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.fy.baselibrary.plugin.service.PluginBaseService;
import com.kernal.lisence.CDKey;
import com.kernal.lisence.CTelephoneInfo;
import com.kernal.lisence.Common;
import com.kernal.lisence.DateAuthFileOperate;
import com.kernal.lisence.DeviceFP;
import com.kernal.lisence.MachineCode;
import com.kernal.lisence.ModeAuthFileOperate;
import com.kernal.lisence.ModeAuthFileResult;
import com.kernal.lisence.ProcedureAuthOperate;
import com.kernal.lisence.VersionAuthFileOperate;
import com.kernal.lisence.WintoneAuthOperateTools;
import com.kernal.lisence.WintoneLSCOperateTools;
import com.kernal.lisence.WintoneLSCXMLInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.parsers.ParserConfigurationException;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:57.
 */
public class RecogService extends PluginBaseService {
    public static final String TAG = "RecogService";
    private static Common common = new Common();
    public MyBinder binder;
    private PlateIDAPI plateIDAPI;
    private ConfigArgument configArg;
    private int iTH_InitPlateIDSDK = -1;
    private int nRet = -1;
    private int nResultNum = 0;
    public String returnGetVersion = "";
    int imageformat = 1;
    int bVertFlip = 0;
    int bDwordAligned = 1;
    private Boolean new_lsc_Boolean = false;
    private Boolean isTF = false;
    private ModeAuthFileResult mafr = new ModeAuthFileResult();
    private String productType = "10";
    public static int recogModel = 2;
    private byte[] data = null;
    private String[] SDarray;
    CTelephoneInfo telephonyInfo;
    public String imeiSIM1;
    public String imeiSIM2;
    private MachineCode machineCode;
    private String mcode;

    public RecogService() {
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }

    private String readAssetFile(String filename) {
        String typeModeString = null;

        try {
            InputStream iStream = getResources().getAssets().open(filename);
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

    @SuppressLint({"MissingPermission", "WrongConstant"})
    public void onCreate() {
        super.onCreate();
        iTH_InitPlateIDSDK = -10003;
        binder = new MyBinder();
        configArg = new ConfigArgument();
        String miwenxml = readAssetFile("authmode.lsc");
        ModeAuthFileOperate mafo = new ModeAuthFileOperate();
        mafr = mafo.ReadAuthFile(miwenxml);
        if (miwenxml != null && mafr.isTF(productType)) {
            Log.d("RecogService", "TF卡授权");
            isTF = true;
            plateIDAPI = new PlateIDAPI();
            TH_PlateIDCfg c_Config = new TH_PlateIDCfg();
            DeviceFP deviceFP = new DeviceFP();
            if (recogModel == 2) {
                c_Config.bMovingImage = 2;
            } else if (recogModel == 9) {
                c_Config.bMovingImage = 9;
            }

            getPathArray();
            plateIDAPI.TH_SetTFPath(SDarray);
            Log.d("RecogService", "识别模式(0:快速、导入识别模式;2:精准识别模式):" + c_Config.bMovingImage);
            iTH_InitPlateIDSDK = plateIDAPI.TH_InitPlateIDSDKTF(c_Config, deviceFP);
            Log.d("RecogService", "TF卡初始化核心:" + iTH_InitPlateIDSDK);
        } else {
            TelephonyManager telephonyManager = null;
            String deviceId = null;
            String androidId = null;
            String simId = null;
            if (miwenxml != null && !mafr.isCheckPRJMode(productType)) {
                telephonyManager = (TelephonyManager)getBaseContext().getSystemService("phone");

                StringBuilder sb1;
                try {
                    sb1 = new StringBuilder();
                    sb1.append(telephonyManager.getDeviceId());
                    deviceId = sb1.toString();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(telephonyManager.getSimSerialNumber());
                    simId = sb2.toString();
                    telephonyInfo = CTelephoneInfo.getInstance(getApplicationContext());
                    telephonyInfo.setCTelephoneInfo();
                    imeiSIM1 = telephonyInfo.getImeiSIM1();
                    imeiSIM2 = telephonyInfo.getImeiSIM2();
                } catch (Exception var25) {
                    var25.printStackTrace();
                }

                sb1 = new StringBuilder();
                sb1.append(Settings.Secure.getString(getContentResolver(), "android_id"));
                androidId = sb1.toString();
                machineCode = new MachineCode();
                mcode = machineCode.MachineNO("1.0", deviceId, androidId, simId);
                if (miwenxml != null && mafr.isSIM(productType)) {
                    if (simId == null || simId.equals("") || simId.equals("null")) {
                        iTH_InitPlateIDSDK = -10501;
                        return;
                    }

                    deviceId = simId;
                }
            }

            String[] str = new String[]{"", "", "", ""};
            DeviceFP deviceFP = new DeviceFP();
            String path = Environment.getExternalStorageDirectory() + "/wintone/plateid.lsc";
            String wintonePathString = Environment.getExternalStorageDirectory() + "/AndroidWT/wt.lsc";
            String wintoneDateFilePath = Environment.getExternalStorageDirectory() + "/AndroidWT/wtdateinit.lsc";
            String versionInitFilePatnString = Environment.getExternalStorageDirectory() + "/AndroidWT/wtversioninit.lsc";
            File file = new File(path);
            File wintoneFile = new File(wintonePathString);
            File wintoneDateFile = new File(wintoneDateFilePath);
            File versionInitFile = new File(versionInitFilePatnString);
            WintoneLSCXMLInformation wli = null;
            String cdkeyString = "";
            String serialString = "";
            boolean fleg = false;
            if (miwenxml != null && mafr.isCheckPRJMode(productType)) {
                Log.d("RecogService", "项目授权");
                fleg = true;
                deviceFP.deviceid = "DeviceIdIsNull";
            } else if (versionInitFile.exists()) {
                Log.d("RecogService", "版本授权");
                if (deviceId == null) {
                    fleg = true;
                } else if (deviceId != null && deviceId.equals(readInitFileString(versionInitFilePatnString))) {
                    deviceFP.deviceid = readInitFileString(versionInitFilePatnString);
                    fleg = true;
                }
            } else if (wintoneDateFile.exists()) {
                Log.d("RecogService", "时间授权");
                if (deviceId == null) {
                    fleg = true;
                } else if (deviceId != null && deviceId.equals(readInitFileString(wintoneDateFilePath))) {
                    deviceFP.deviceid = readInitFileString(wintoneDateFilePath);
                    fleg = true;
                }
            } else {
                Log.d("RecogService", "序列号授权");
                if (file.exists() || wintoneFile.exists() || !wintoneFile.exists()) {
                    ProcedureAuthOperate pao = new ProcedureAuthOperate(getBaseContext());
                    if (file.exists()) {
                        try {
                            str = pao.readOriginalAuthFileContent(path);
                            deviceFP.deviceid = str[3];
                            cdkeyString = str[2];
                            serialString = str[1];
                            CDKey cdKey = new CDKey();
                            fleg = cdKey.checkjhm(cdkeyString, mcode, serialString);
                            if (!fleg) {
                                if (deviceId != null && !deviceId.equals(imeiSIM1)) {
                                    mcode = machineCode.MachineNO("1.0", imeiSIM1, androidId, simId);
                                    fleg = cdKey.checkjhm(cdkeyString, mcode, serialString);
                                } else if (deviceId != null && !deviceId.equals(imeiSIM2)) {
                                    mcode = machineCode.MachineNO("1.0", imeiSIM2, androidId, simId);
                                    fleg = cdKey.checkjhm(cdkeyString, mcode, serialString);
                                }
                            }

                            if (!fleg && str.length >= 8) {
                                if (str[8] != null && str[7] != null) {
                                    deviceFP.deviceid = str[9];
                                }

                                fleg = cdKey.checkjhm(str[8], mcode, str[7]);
                            }
                        } catch (Exception var26) {
                            serialString = "";
                            cdkeyString = "";
                        }
                    } else {
                        WintoneLSCOperateTools wintoneLSCOperateTools = new WintoneLSCOperateTools(getApplicationContext());

                        try {
                            if (deviceId != null && !deviceId.equals("")) {
                                wli = wintoneLSCOperateTools.ReadAuthFile(deviceId, androidId);
                            } else {
                                wli = wintoneLSCOperateTools.ReadAuthFile(androidId, androidId);
                            }
                        } catch (ParserConfigurationException var27) {
                            var27.printStackTrace();
                        }

                        if (wli != null) {
                            cdkeyString = wli.anoString;
                            serialString = wli.snoString;
                            deviceFP.deviceid = wli.deviceIdString;
                            CDKey cdKey = new CDKey();
                            fleg = cdKey.checkjhm(cdkeyString, mcode, serialString);
                            if (!fleg) {
                                if (!deviceId.equals(imeiSIM1)) {
                                    mcode = machineCode.MachineNO("1.0", imeiSIM1, androidId, simId);
                                    fleg = cdKey.checkjhm(cdkeyString, mcode, serialString);
                                } else if (!deviceId.equals(imeiSIM2)) {
                                    mcode = machineCode.MachineNO("1.0", imeiSIM2, androidId, simId);
                                    fleg = cdKey.checkjhm(cdkeyString, mcode, serialString);
                                }
                            }

                            if (!fleg && str.length >= 8) {
                                if (str[8] != null && str[7] != null) {
                                    deviceFP.deviceid = str[9];
                                }

                                fleg = cdKey.checkjhm(str[8], mcode, str[7]);
                            }
                        }
                    }
                }
            }

            if (fleg) {
                if (Build.VERSION.SDK_INT > 28 && miwenxml != null && !mafr.isCheckPRJMode(productType)) {
                    String deviceid = null;

                    try {
                        deviceid = telephonyManager.getDeviceId();
                    } catch (Exception var24) {
                        var24.printStackTrace();
                    }

                    if (deviceid == null) {
                        deviceFP.deviceid = "DeviceIdIsNull";
                    }
                }

                plateIDAPI = new PlateIDAPI();
                TH_PlateIDCfg c_Config = new TH_PlateIDCfg();
                if (recogModel == 2) {
                    c_Config.bMovingImage = 2;
                } else if (recogModel == 9) {
                    c_Config.bMovingImage = 9;
                }

                Log.d("RecogService", "识别模式(0:快速、导入识别模式;2:精准识别模式):" + c_Config.bMovingImage);
                iTH_InitPlateIDSDK = plateIDAPI.TH_InitPlateIDSDK(c_Config, telephonyManager, deviceFP);
                Log.d("RecogService", "初始化返回值:=" + iTH_InitPlateIDSDK);
                Log.d("RecogService", "识别核心版本号:" + plateIDAPI.TH_GetVersion());
            } else {
                Log.e("RecogService", "未匹配到授权方式");
                iTH_InitPlateIDSDK = -10015;
            }
        }

    }

    private synchronized String[] recogPlate(byte[] picByte, String pic, int width, int height, String userData, TH_PlateIDCfg plateIDCfg) {
        int[] nResultNums = new int[]{10};
        int[] nRets = new int[]{-1};
        TH_PlateIDResult plateidresult = new TH_PlateIDResult();
        TH_PlateIDResult[] plateidresults;
        if (picByte != null && picByte.length > 0) {
            plateidresults = plateIDAPI.TH_RecogImageByte(picByte, width, height, plateidresult, nResultNums, plateIDCfg.left, plateIDCfg.top, plateIDCfg.right, plateIDCfg.bottom, nRets, plateIDCfg.bRotate, plateIDCfg.scale);
        } else {
            plateidresults = plateIDAPI.TH_RecogImage(pic, width, height, plateidresult, nResultNums, plateIDCfg.left, plateIDCfg.top, plateIDCfg.right, plateIDCfg.bottom, nRets);
        }

        nRet = nRets[0];
        nResultNum = nResultNums[0];
        String[] tempFieldvalue = new String[15];
        if (nRets[0] != 0) {
            tempFieldvalue[14] = userData;
        } else {
            tempFieldvalue[14] = userData;

            for(int i = 0; i < nResultNums[0]; ++i) {
                if (plateidresults != null && plateidresults[i] != null) {
                    if (i == 0) {
                        tempFieldvalue[0] = plateidresults[i].license;
                        tempFieldvalue[1] = plateidresults[i].color;
                        tempFieldvalue[2] = int2string(plateidresults[i].nColor);
                        tempFieldvalue[3] = int2string(plateidresults[i].nType);
                        tempFieldvalue[4] = int2string(plateidresults[i].nConfidence);
                        tempFieldvalue[5] = int2string(plateidresults[i].nBright);
                        tempFieldvalue[6] = int2string(plateidresults[i].nDirection);
                        tempFieldvalue[7] = int2string(plateidresults[i].left);
                        tempFieldvalue[8] = int2string(plateidresults[i].top);
                        tempFieldvalue[9] = int2string(plateidresults[i].right);
                        tempFieldvalue[10] = int2string(plateidresults[i].bottom);
                        tempFieldvalue[11] = int2string(plateidresults[i].nTime);
                        tempFieldvalue[12] = int2string(plateidresults[i].nCarBright);
                        tempFieldvalue[13] = int2string(plateidresults[i].nCarColor);
                        tempFieldvalue[14] = userData;
                        data = plateidresults[i].pbyBits;
                    } else {
                        tempFieldvalue[0] = tempFieldvalue[0] + ";" + plateidresults[i].license;
                        tempFieldvalue[1] = tempFieldvalue[1] + ";" + plateidresults[i].color;
                        tempFieldvalue[2] = tempFieldvalue[2] + ";" + int2string(plateidresults[i].nColor);
                        tempFieldvalue[3] = tempFieldvalue[3] + ";" + int2string(plateidresults[i].nType);
                        tempFieldvalue[4] = tempFieldvalue[4] + ";" + int2string(plateidresults[i].nConfidence);
                        tempFieldvalue[5] = tempFieldvalue[5] + ";" + int2string(plateidresults[i].nBright);
                        tempFieldvalue[6] = tempFieldvalue[6] + ";" + int2string(plateidresults[i].nDirection);
                        tempFieldvalue[7] = tempFieldvalue[7] + ";" + int2string(plateidresults[i].left);
                        tempFieldvalue[8] = tempFieldvalue[8] + ";" + int2string(plateidresults[i].top);
                        tempFieldvalue[9] = tempFieldvalue[9] + ";" + int2string(plateidresults[i].right);
                        tempFieldvalue[10] = tempFieldvalue[10] + ";" + int2string(plateidresults[i].bottom);
                        tempFieldvalue[11] = tempFieldvalue[11] + ";" + int2string(plateidresults[i].nTime);
                        tempFieldvalue[12] = tempFieldvalue[12] + ";" + int2string(plateidresults[i].nCarBright);
                        tempFieldvalue[13] = tempFieldvalue[13] + ";" + int2string(plateidresults[i].nCarColor);
                    }
                }
            }
        }

        plateidresults = null;
        return tempFieldvalue;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public String readInitFileString(String filePathString) {
        String SysCertVersion = "wtversion5_5";
        String deviceidString = "";
        File dateInitFile = new File(filePathString);
        if (dateInitFile.exists()) {
            try {
                BufferedReader bfReader = new BufferedReader(new FileReader(dateInitFile));
                deviceidString = bfReader.readLine();
                bfReader.close();
                deviceidString = common.getSrcPassword(deviceidString, SysCertVersion);
            } catch (FileNotFoundException var6) {
                deviceidString = "";
                var6.printStackTrace();
            } catch (IOException var7) {
                deviceidString = "";
                var7.printStackTrace();
            } catch (Exception var8) {
                deviceidString = "";
                var8.printStackTrace();
            }
        }

        return deviceidString;
    }

    private String int2string(int i) {
        String str = "";

        try {
            str = String.valueOf(i);
        } catch (Exception var4) {
        }

        return str;
    }

    public String getSDPath(Context mContext, boolean is_removale) {
        String path = "";
        String temppath = "";
        String lastPath = "";
        @SuppressLint("WrongConstant")
        StorageManager mStorageManager = (StorageManager)mContext.getSystemService("storage");
        Class storageVolumeClazz = null;

        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            int length = Array.getLength(result);

            for(int i = 0; i < length; ++i) {
                Object storageVolumeElement = Array.get(result, i);
                temppath = (String)getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean)isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    if (path != null && !path.equals("")) {
                        if (!temppath.equals(lastPath)) {
                            path = path + "#" + temppath;
                            lastPath = temppath;
                        }
                    } else if (path != null && path.equals("")) {
                        path = temppath;
                        lastPath = temppath;
                    }
                }
            }
        } catch (ClassNotFoundException var16) {
            var16.printStackTrace();
        } catch (InvocationTargetException var17) {
            var17.printStackTrace();
        } catch (NoSuchMethodException var18) {
            var18.printStackTrace();
        } catch (IllegalAccessException var19) {
            var19.printStackTrace();
        }

        return path;
    }

    public void getPathArray() {
        String SDpath = getSDPath(getApplicationContext(), true);
        SDarray = new String[SDpath.split("#").length];

        for(int i = 0; i < SDarray.length; ++i) {
            SDarray[i] = SDpath.split("#")[i];
        }

    }

    public class MyBinder extends Binder {
        int oldMsgWhat = 0;
        Handler plateHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 1:
                        Toast.makeText(getApplicationContext(), "您的授权已到期，请在一个月内申请延期，否则将不能使用识别功能", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "在strings文件中未找到company_name字段", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "您的授权已到期，请在一个月内申请延期，否则将不能使用识别功能", Toast.LENGTH_LONG).show();
                }

            }
        };

        public MyBinder() {
        }

        public void setRecogArgu(String pic, int imageformat, boolean bGetVersion, int bVertFlip, int bDwordAligned) {
            nRet = -10000;
            if (iTH_InitPlateIDSDK != 0) {
                nRet = -10001;
            } else {
                if (bGetVersion) {
                    returnGetVersion = plateIDAPI.TH_GetVersion();
                }

                plateIDAPI.TH_SetImageFormat(imageformat, bVertFlip, bDwordAligned);
                configArg = new ConfigArgument();
                int TH_SetEnabledPlateFormat;
                if (configArg.cfgs != null && configArg.cfgs.length >= 26) {
                    TH_SetEnabledPlateFormat = plateIDAPI.TH_SetEnabledPlateFormat(configArg.tworowyellow);
                    plateIDAPI.TH_SetEnabledPlateFormat(configArg.armpolice);
                    plateIDAPI.TH_SetEnabledPlateFormat(configArg.tworowarmy);
                    plateIDAPI.TH_SetEnabledPlateFormat(configArg.tractor);
                    plateIDAPI.TH_SetEnabledPlateFormat(configArg.onlytworowyellow);
                    plateIDAPI.TH_SetEnabledPlateFormat(configArg.embassy);
                    plateIDAPI.TH_SetEnabledPlateFormat(configArg.onlylocation);
                    plateIDAPI.TH_SetEnabledPlateFormat(configArg.armpolice2);
                }

                int TH_SetRecogThreshold;
                int TH_SetAutoSlopeRectifyMode;
                if (configArg.cfgs != null && configArg.cfgs.length >= 18) {
                    TH_SetEnabledPlateFormat = plateIDAPI.TH_SetEnabledPlateFormat(configArg.dFormat);
                    TH_SetRecogThreshold = plateIDAPI.TH_SetRecogThreshold(configArg.nPlateLocate_Th, configArg.nOCR_Th);
                    TH_SetAutoSlopeRectifyMode = plateIDAPI.TH_SetAutoSlopeRectifyMode(configArg.bIsAutoSlope, configArg.nSlopeDetectRange);
                    int TH_SetProvinceOrder = plateIDAPI.TH_SetProvinceOrder(configArg.szProvince);
                    int var11;
                    if (configArg.nContrast != 0) {
                        var11 = plateIDAPI.TH_SetContrast(configArg.nContrast);
                    }

                    var11 = plateIDAPI.TH_SetDayNightMode(configArg.c_Config.bIsNight);
                } else {
                    TH_SetEnabledPlateFormat = plateIDAPI.TH_SetEnabledPlateFormat(0);
                    TH_SetRecogThreshold = plateIDAPI.TH_SetRecogThreshold(7, 5);
                    TH_SetAutoSlopeRectifyMode = plateIDAPI.TH_SetContrast(9);
                }

                nRet = 0;
            }

        }

        public void setRecogArgu(PlateCfgParameter cfgparameter, int imageformat) {
            nRet = -10000;
            if (iTH_InitPlateIDSDK != 0) {
                nRet = -10001;
            } else {
                int bVertFlip = 0;
                int bDwordAligned = 1;
                plateIDAPI.TH_SetImageFormat(imageformat, bVertFlip, bDwordAligned);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.individual);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.tworowyellow);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.armpolice);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.tworowarmy);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.tractor);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.onlytworowyellow);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.embassy);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.onlylocation);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.armpolice2);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.newEnergy);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.consulate);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.Infactory);
                plateIDAPI.TH_SetEnabledPlateFormat(cfgparameter.civilAviation);
                int iTH_SetRecogThreshold = plateIDAPI.TH_SetRecogThreshold(cfgparameter.nPlateLocate_Th, cfgparameter.nOCR_Th);
                int iTH_SetAutoSlopeRectifyMode = plateIDAPI.TH_SetAutoSlopeRectifyMode(cfgparameter.bIsAutoSlope, cfgparameter.nSlopeDetectRange);
                int iTH_SetProvinceOrder = plateIDAPI.TH_SetProvinceOrder(cfgparameter.szProvince);
                if (configArg.nContrast != 0) {
                    int iTH_SetDayNightMode;
                    if (cfgparameter.nContrast != 0) {
                        iTH_SetDayNightMode = plateIDAPI.TH_SetContrast(cfgparameter.nContrast);
                    } else {
                        boolean var9 = false;
                    }

                    iTH_SetDayNightMode = plateIDAPI.TH_SetDayNightMode(cfgparameter.bIsNight);
                    nRet = 0;
                }
            }

        }

        @SuppressLint("MissingPermission")
        public String[] doRecogDetail(PlateRecognitionParameter prp) {
            new_lsc_Boolean = false;
            String[] fieldvalue = null;
            if (iTH_InitPlateIDSDK != 0) {
                nRet = -10001;
            } else {
//                int devCheck = true;
                int devCheckx;
                if (!mafr.isCheckDevType(productType) && !prp.isCheckDevType) {
                    devCheckx = 0;
                } else {
                    devCheckx = mafr.isAllowDevTypeAndDevCode(productType, prp.devCode);
                }

//                int check = true;
                int checkx;
                if (prp.versionfile != null && !prp.versionfile.equals("")) {
                    VersionAuthFileOperate vafo = new VersionAuthFileOperate();
                    checkx = vafo.verifyVersionAuthFile(prp.versionfile, prp.devCode, productType, "");
                } else if (prp.dataFile != null && !prp.dataFile.equals("") && !prp.dataFile.equals("null")) {
                    DateAuthFileOperate dafo = new DateAuthFileOperate();
                    checkx = dafo.verifyDateAuthFile(prp.dataFile, prp.devCode, productType);
                    if (checkx == -10090 && devCheckx == 0) {
                        if (MathRandom.PercentageRandom() == 5) {
                            sendPlateMessage(1);
                        }

                        checkx = 0;
                    }
                } else if (isTF) {
                    checkx = 0;
                } else if (mafr.isCheckPRJMode(productType)) {
                    checkx = -10600;
                    String packageName = "com.kernal.demo.plateid";//正常应该是 代码获取 应用id
                    String app_name = "快号通开发包";//正常应该是 代码获取 应用名称
                    String company_name = "Beijing Wintone Technology Co.";//正常应该是 代码获取 company_name 授权码
//                    try {
//                        int id_company_name = getResources().getIdentifier("company_name", "string", getPackageName());
//                        company_name = getResources().getString(id_company_name);
//                    } catch (Resources.NotFoundException var15) {
//                        var15.printStackTrace();
//                        sendPlateMessage(2);
//                        checkx = -10608;
//                    }

                    if (app_name != null && company_name != null) {
                        checkx = mafr.isCheckPRJOK(productType, prp.devCode, packageName, app_name, company_name, FileUtils.getVersionFileInfos(getResources()));
                        if (checkx == -10090 && devCheckx == 0) {
                            if (MathRandom.PercentageRandom() == 5) {
                                sendPlateMessage(3);
                            }
                            checkx = 0;
                        }
                    }
                } else {
                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/wintone/plateid.lsc");
                    if (file.exists()) {
                        checkx = 0;
                    } else {
                        new_lsc_Boolean = true;
                        WintoneLSCXMLInformation wlxi = null;
                        WintoneLSCOperateTools wintoneLSCOperateTools = new WintoneLSCOperateTools(getApplicationContext());
                        @SuppressLint("WrongConstant")
                        TelephonyManager telephonyManager1 = (TelephonyManager)getSystemService("phone");
                        String androidId = Settings.Secure.getString(getContentResolver(), "android_id");
                        String deviceId = null;

                        try {
                            deviceId = telephonyManager1.getDeviceId();
                        } catch (Exception var14) {
                            var14.printStackTrace();
                        }

                        if (deviceId != null && !deviceId.equals("")) {
                            try {
                                wlxi = wintoneLSCOperateTools.ReadAuthFile(deviceId, androidId);
                            } catch (ParserConfigurationException var13) {
                                var13.printStackTrace();
                            }
                        } else {
                            try {
                                wlxi = wintoneLSCOperateTools.ReadAuthFile(androidId, androidId);
                            } catch (ParserConfigurationException var12) {
                                var12.printStackTrace();
                            }
                        }

                        checkx = WintoneAuthOperateTools.accordTypeDateNumber(productType, wlxi.typeStrings, wlxi.duedateStrings, wlxi.sumStrings);
                    }
                }

                if (checkx == 0 && devCheckx == 0) {
                    fieldvalue = recogPlate(prp.picByte, prp.pic, prp.width, prp.height, prp.userData, prp.plateIDCfg);
                    if (fieldvalue[0] != null) {
                        Log.d("RecogService", "识别结果数组说明:\n0:车牌号\n1:车牌颜色\n2:车牌颜色代码\n3:车牌类型代码\n4:识别结果车牌号可信度\n5:亮度评价\n6:车牌运动方向\n7:车牌左上点横坐标\n8:车牌左上点纵坐标\n9:车牌右下点横坐标\n10:车牌右下点纵坐标（7.8.9.10是车牌区域范围）\n11:时间\n12:车身亮度\n13:车身颜色 ");
                    }

                    if (new_lsc_Boolean) {
                        WintoneLSCOperateTools.ModifyNumberInAuthFileByProjectType(productType);
                    }
                } else {
                    nRet = checkx;
                    if (devCheckx != 0) {
                        nRet = devCheckx;
                    }

                    fieldvalue = new String[15];
                    fieldvalue[14] = prp.userData;
                }
            }

            return fieldvalue;
        }

        private void sendPlateMessage(int msgWhat) {
            if (oldMsgWhat != msgWhat) {
                oldMsgWhat = msgWhat;
                Message msg = new Message();
                msg.what = msgWhat;
                plateHandler.sendMessage(msg);
            }

        }

        public String[] doRecogDetail(byte[] picByte, String pic, int width, int height, String userData) {
            PlateRecognitionParameter prp = new PlateRecognitionParameter();
            prp.picByte = picByte;
            prp.pic = pic;
            prp.width = width;
            prp.height = height;
            prp.userData = userData;
            return doRecogDetail(prp);
        }

        public String[] doRecog(String pic, int width, int height) {
            return doRecogDetail((byte[])null, pic, width, height, (String)null);
        }

        public String[] doRecog(String pic, int width, int height, String userData) {
            return doRecogDetail((byte[])null, pic, width, height, userData);
        }

        public String[] doRecog(byte[] picByte, int width, int height, String userData) {
            imageformat = 6;
            plateIDAPI.TH_SetImageFormat(imageformat, bVertFlip, bDwordAligned);
            return doRecogDetail(picByte, (String)null, width, height, userData);
        }

        public int getInitPlateIDSDK() {
            return iTH_InitPlateIDSDK;
        }

        public int getnRet() {
            return nRet;
        }

        public int getnResultNums() {
            return nResultNum;
        }

        public int UninitPlateIDSDK() {
            int iTH_UninitPlateIDSDK = -1;
            if (iTH_InitPlateIDSDK == 0) {
                iTH_UninitPlateIDSDK = plateIDAPI.TH_UninitPlateIDSDK();
                Log.d("RecogService", "释放核心:" + iTH_UninitPlateIDSDK);
            }

            return iTH_UninitPlateIDSDK;
        }

        public byte[] getRecogData() {
            return data;
        }
    }
}
