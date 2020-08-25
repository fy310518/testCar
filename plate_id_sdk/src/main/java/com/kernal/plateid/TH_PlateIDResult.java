package com.kernal.plateid;

/**
 * description </p>
 * Created by fangs on 2020/8/25 14:00.
 */
public class TH_PlateIDResult {
    public String license = "";
    public String color = "";
    public int nColor;
    public int nType;
    public int nConfidence;
    public int nBright;
    public int nDirection;
    public int left;
    public int top;
    public int right;
    public int bottom;
    public byte[] pbyBits;
    public int nTime;
    public int nCarBright = 0;
    public int nCarColor = 0;
    public String reserved = "";

    public TH_PlateIDResult() {
    }
}
