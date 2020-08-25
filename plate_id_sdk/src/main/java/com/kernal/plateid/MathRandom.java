package com.kernal.plateid;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:52.
 */
public class MathRandom {
    public static double rate0 = 0.5D;
    public static double rate1 = 0.2D;
    public static double rate2 = 0.15D;
    public static double rate3 = 0.06D;
    public static double rate4 = 0.04D;
    public static double rate5 = 0.05D;

    public MathRandom() {
    }

    public static int PercentageRandom() {
        double randomNumber = Math.random();
        if (randomNumber >= 0.0D && randomNumber <= rate0) {
            return 0;
        } else if (randomNumber >= rate0 / 100.0D && randomNumber <= rate0 + rate1) {
            return 1;
        } else if (randomNumber >= rate0 + rate1 && randomNumber <= rate0 + rate1 + rate2) {
            return 2;
        } else if (randomNumber >= rate0 + rate1 + rate2 && randomNumber <= rate0 + rate1 + rate2 + rate3) {
            return 3;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4) {
            return 4;
        } else {
            return randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 ? 5 : -1;
        }
    }
}
