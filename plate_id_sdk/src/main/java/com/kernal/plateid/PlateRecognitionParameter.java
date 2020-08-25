package com.kernal.plateid;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:56.
 */
public class PlateRecognitionParameter {
    public byte[] picByte;
    public String pic;
    public int width;
    public int height;
    public String userData;
    public String devCode = "";
    public boolean isCheckDevType = false;
    public String dataFile = "";
    public String versionfile = "";
    public TH_PlateIDCfg plateIDCfg = new TH_PlateIDCfg();

    public PlateRecognitionParameter() {
    }
}
