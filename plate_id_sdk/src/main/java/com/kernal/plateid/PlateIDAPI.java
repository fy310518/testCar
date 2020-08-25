package com.kernal.plateid;

import android.telephony.TelephonyManager;
import android.util.Log;

import com.kernal.lisence.DeviceFP;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:53.
 */
public class PlateIDAPI {
    static {
        Log.e("plateid", "开始加载动态库");
        System.loadLibrary("THPlateID");
        Log.e("plateid", "动态库加载完成");
    }

    public PlateIDAPI() {
    }

    public native int TH_InitPlateIDSDKTF(TH_PlateIDCfg var1, DeviceFP var2);

    public native int TH_InitPlateIDSDK(TH_PlateIDCfg var1, TelephonyManager var2, DeviceFP var3);

    public native int TH_UninitPlateIDSDK();

    public native TH_PlateIDResult[] TH_RecogImage(String var1, int var2, int var3, TH_PlateIDResult var4, int[] var5, int var6, int var7, int var8, int var9, int[] var10);

    public native TH_PlateIDResult[] TH_RecogImageByte(byte[] var1, int var2, int var3, TH_PlateIDResult var4, int[] var5, int var6, int var7, int var8, int var9, int[] var10, int var11, int var12);

    public native int TH_SetImageFormat(int var1, int var2, int var3);

    public native int TH_SetDayNightMode(int var1);

    public native int TH_SetEnlargeMode(int var1);

    public native int TH_SetEnabledPlateFormat(int var1);

    public native int TH_SetProvinceOrder(String var1);

    public native int TH_SetRecogThreshold(int var1, int var2);

    public native String TH_GetVersion();

    public native int TH_SetContrast(int var1);

    public native int TH_SetAutoSlopeRectifyMode(int var1, int var2);

    public native int TH_SetTFPath(String[] var1);

    public native int ModifyRecMode(int var1);
}
