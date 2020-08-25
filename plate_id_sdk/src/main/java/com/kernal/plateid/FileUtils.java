package com.kernal.plateid;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:51.
 */
public class FileUtils {

    private FileUtils() {}

    public static String getVersionFileInfos(Resources pluginResource) {
        String Result = null;
        BufferedReader br = null;

        try {
//            InputStream is = this.getClass().getResourceAsStream("wtversion.lsc");
            InputStream is = pluginResource.getAssets().open("wtversion.lsc");
            if (is != null) {
                br = new BufferedReader(new InputStreamReader(is));
                String s = "";

                while((s = br.readLine()) != null) {
                    if (Result == null) {
                        Result = s;
                    } else {
                        Result = Result + s;
                    }
                }

                return Result;
            } else {
                return null;
            }
        } catch (IOException var5) {
            var5.printStackTrace();
            return null;
        }
    }

}
