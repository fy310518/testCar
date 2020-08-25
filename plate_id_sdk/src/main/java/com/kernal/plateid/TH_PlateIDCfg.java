package com.kernal.plateid;

/**
 * description </p>
 * Created by fangs on 2020/8/25 14:00.
 */
public class TH_PlateIDCfg {
    public int nMinPlateWidth = 60;
    public int nMaxPlateWidth = 400;
    public int bVertCompress = 0;
    public int bIsFieldImage = 0;
    public int bOutputSingleFrame = 1;
    public int bMovingImage = 0;
    public int bIsNight = 0;
    public int nImageFormat = 0;
    public int nLastError = 0;
    public int nErrorModelSN = 0;
    public String reserved = "";
    public int bRotate = 0;
    public int left;
    public int right;
    public int top;
    public int bottom;
    public int scale;
    public boolean isModifyRecogMode = false;

    public TH_PlateIDCfg() {
    }
}
