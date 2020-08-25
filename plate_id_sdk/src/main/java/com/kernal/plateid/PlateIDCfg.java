package com.kernal.plateid;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kernal.lisence.Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * description </p>
 * Created by fangs on 2020/8/25 13:55.
 */
public class PlateIDCfg extends Activity {
    private Button mbutlog;
    private Button returnbutlog;
    private Button butclose;
    private EditText editnMinPlateWidth;
    private EditText editnMaxPlateWidth;
    private EditText editbVertCompress;
    private EditText editbIsFieldImage;
    private EditText editbOutputSingleFrame;
    private EditText editbMovingImage;
    private EditText editbIsNight;
    private EditText editnImageFormat;
    private EditText editnLastError;
    private EditText editnErrorModelSN;
    private EditText editreserved;
    private EditText bEnlarge;
    private EditText dFormat;
    private EditText szProvince;
    private EditText nPlateLocate_Th;
    private EditText nOCR_Th;
    private EditText nContrast;
    private EditText bIsAutoSlope;
    private EditText nSlopeDetectRange;
    private RadioButton radiobutbIsNightyes;
    private RadioButton radiobutbIsNightno;
    private RadioButton radiobutbVertCompressyes;
    private RadioButton radiobutbVertCompressno;
    private RadioButton radiobutbIsFieldImageyes;
    private RadioButton radiobutbIsFieldImageno;
    private RadioButton radiobutbOutputSingleFrameyes;
    private RadioButton radiobutbOutputSingleFrameno;
    private RadioButton radiobutbMovingImageyes;
    private RadioButton radiobutbMovingImageno;
    private RadioButton radiobutdFormatyes;
    private RadioButton radiobutdFormatno;
    private EditText editnPlateLocate_Th;
    private RadioButton radiobutbIsAutoSlopeyes;
    private RadioButton radiobutbIsAutoSlopeno;
    private EditText editnSlopeDetectRange;
    private EditText editszProvince;
    private EditText editnContrast;
    private EditText editnOCR_Th;
    private RadioButton radiobuttworowyellowyes;
    private RadioButton radiobuttworowyellowno;
    private RadioButton radiobutarmpoliceyes;
    private RadioButton radiobutarmpoliceno;
    private RadioButton radiobuttworowarmyyes;
    private RadioButton radiobuttworowarmyno;
    private RadioButton radiobuttractoryes;
    private RadioButton radiobuttractorno;
    private RadioButton radiobutonlytworowyellowyes;
    private RadioButton radiobutonlytworowyellowno;
    private RadioButton radiobutembassyyes;
    private RadioButton radiobutembassyno;
    private RadioButton radiobutonlylocationyes;
    private RadioButton radiobutonlylocationno;
    private RadioButton radiobutarmpolice2yes;
    private RadioButton radiobutarmpolice2no;

    public PlateIDCfg() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        int plateidcfg = this.getResources().getIdentifier("plateidcfg", "layout", this.getPackageName());
        this.setContentView(plateidcfg);
        Common common = new Common();
        String file = common.getSDPath() + "/AndroidWT/plateid.cfg";
        boolean isFile = false;
        File dir = new File(file);
        if (dir.exists()) {
            isFile = true;
        }

        int editnMinPlateWidth_id = this.getResources().getIdentifier("editnMinPlateWidth", "id", this.getPackageName());
        this.editnMinPlateWidth = (EditText)this.findViewById(editnMinPlateWidth_id);
        int editnMaxPlateWidth_id = this.getResources().getIdentifier("editnMaxPlateWidth", "id", this.getPackageName());
        this.editnMaxPlateWidth = (EditText)this.findViewById(editnMaxPlateWidth_id);
        int radiobutbVertCompressyes_id = this.getResources().getIdentifier("radiobutbVertCompressyes", "id", this.getPackageName());
        this.radiobutbVertCompressyes = (RadioButton)this.findViewById(radiobutbVertCompressyes_id);
        int radiobutbVertCompressno_id = this.getResources().getIdentifier("radiobutbVertCompressno", "id", this.getPackageName());
        this.radiobutbVertCompressno = (RadioButton)this.findViewById(radiobutbVertCompressno_id);
        int radiobutbIsNightyes_id = this.getResources().getIdentifier("radiobutbIsNightyes", "id", this.getPackageName());
        this.radiobutbIsNightyes = (RadioButton)this.findViewById(radiobutbIsNightyes_id);
        int radiobutbIsNightno_id = this.getResources().getIdentifier("radiobutbIsNightno", "id", this.getPackageName());
        this.radiobutbIsNightno = (RadioButton)this.findViewById(radiobutbIsNightno_id);
        int editnImageFormat_id = this.getResources().getIdentifier("editnImageFormat", "id", this.getPackageName());
        this.editnImageFormat = (EditText)this.findViewById(editnImageFormat_id);
        int radiobutdFormatyes_id = this.getResources().getIdentifier("radiobutdFormatyes", "id", this.getPackageName());
        this.radiobutdFormatyes = (RadioButton)this.findViewById(radiobutdFormatyes_id);
        int radiobutdFormatno_id = this.getResources().getIdentifier("radiobutdFormatno", "id", this.getPackageName());
        this.radiobutdFormatno = (RadioButton)this.findViewById(radiobutdFormatno_id);
        int editnPlateLocate_Th_id = this.getResources().getIdentifier("editnPlateLocate_Th", "id", this.getPackageName());
        this.editnPlateLocate_Th = (EditText)this.findViewById(editnPlateLocate_Th_id);
        int radiobutbIsAutoSlopeyes_id = this.getResources().getIdentifier("radiobutbIsAutoSlopeyes", "id", this.getPackageName());
        this.radiobutbIsAutoSlopeyes = (RadioButton)this.findViewById(radiobutbIsAutoSlopeyes_id);
        int radiobutbIsAutoSlopeno_id = this.getResources().getIdentifier("radiobutbIsAutoSlopeno", "id", this.getPackageName());
        this.radiobutbIsAutoSlopeno = (RadioButton)this.findViewById(radiobutbIsAutoSlopeno_id);
        int editnSlopeDetectRange_id = this.getResources().getIdentifier("editnSlopeDetectRange", "id", this.getPackageName());
        this.editnSlopeDetectRange = (EditText)this.findViewById(editnSlopeDetectRange_id);
        int editszProvince_id = this.getResources().getIdentifier("editszProvince", "id", this.getPackageName());
        this.editszProvince = (EditText)this.findViewById(editszProvince_id);
        int editnContrast_id = this.getResources().getIdentifier("editnContrast", "id", this.getPackageName());
        this.editnContrast = (EditText)this.findViewById(editnContrast_id);
        int editnOCR_Th_id = this.getResources().getIdentifier("editnOCR_Th", "id", this.getPackageName());
        this.editnOCR_Th = (EditText)this.findViewById(editnOCR_Th_id);
        int radiobuttworowyellowyes_id = this.getResources().getIdentifier("radiobuttworowyellowyes", "id", this.getPackageName());
        this.radiobuttworowyellowyes = (RadioButton)this.findViewById(radiobuttworowyellowyes_id);
        int radiobuttworowyellowno_id = this.getResources().getIdentifier("radiobuttworowyellowno", "id", this.getPackageName());
        this.radiobuttworowyellowno = (RadioButton)this.findViewById(radiobuttworowyellowno_id);
        int radiobutarmpoliceyes_id = this.getResources().getIdentifier("radiobutarmpoliceyes", "id", this.getPackageName());
        this.radiobutarmpoliceyes = (RadioButton)this.findViewById(radiobutarmpoliceyes_id);
        int radiobutarmpoliceno_id = this.getResources().getIdentifier("radiobutarmpoliceno", "id", this.getPackageName());
        this.radiobutarmpoliceno = (RadioButton)this.findViewById(radiobutarmpoliceno_id);
        int radiobuttworowarmyyes_id = this.getResources().getIdentifier("radiobuttworowarmyyes", "id", this.getPackageName());
        this.radiobuttworowarmyyes = (RadioButton)this.findViewById(radiobuttworowarmyyes_id);
        int radiobuttworowarmyno_id = this.getResources().getIdentifier("radiobuttworowarmyno", "id", this.getPackageName());
        this.radiobuttworowarmyno = (RadioButton)this.findViewById(radiobuttworowarmyno_id);
        int radiobuttractoryes_id = this.getResources().getIdentifier("radiobuttractoryes", "id", this.getPackageName());
        this.radiobuttractoryes = (RadioButton)this.findViewById(radiobuttractoryes_id);
        int radiobuttractorno_id = this.getResources().getIdentifier("radiobuttractorno", "id", this.getPackageName());
        this.radiobuttractorno = (RadioButton)this.findViewById(radiobuttractorno_id);
        int radiobutonlytworowyellowyes_id = this.getResources().getIdentifier("radiobutonlytworowyellowyes", "id", this.getPackageName());
        this.radiobutonlytworowyellowyes = (RadioButton)this.findViewById(radiobutonlytworowyellowyes_id);
        int radiobutonlytworowyellowno_id = this.getResources().getIdentifier("radiobutonlytworowyellowno", "id", this.getPackageName());
        this.radiobutonlytworowyellowno = (RadioButton)this.findViewById(radiobutonlytworowyellowno_id);
        int radiobutembassyyes_id = this.getResources().getIdentifier("radiobutembassyyes", "id", this.getPackageName());
        this.radiobutembassyyes = (RadioButton)this.findViewById(radiobutembassyyes_id);
        int radiobutembassyno_id = this.getResources().getIdentifier("radiobutembassyno", "id", this.getPackageName());
        this.radiobutembassyno = (RadioButton)this.findViewById(radiobutembassyno_id);
        int radiobutonlylocationyes_id = this.getResources().getIdentifier("radiobutonlylocationyes", "id", this.getPackageName());
        this.radiobutonlylocationyes = (RadioButton)this.findViewById(radiobutonlylocationyes_id);
        int radiobutonlylocationno_id = this.getResources().getIdentifier("radiobutonlylocationno", "id", this.getPackageName());
        this.radiobutonlylocationno = (RadioButton)this.findViewById(radiobutonlylocationno_id);
        int radiobutarmpolice2yes_id = this.getResources().getIdentifier("radiobutarmpolice2yes", "id", this.getPackageName());
        this.radiobutarmpolice2yes = (RadioButton)this.findViewById(radiobutarmpolice2yes_id);
        int radiobutarmpolice2no_id = this.getResources().getIdentifier("radiobutarmpolice2no", "id", this.getPackageName());
        this.radiobutarmpolice2no = (RadioButton)this.findViewById(radiobutarmpolice2no_id);
        if (isFile) {
            String str = "";

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                for(String content = null; (content = reader.readLine()) != null; str = str + content) {
                }
            } catch (IOException var42) {
                var42.printStackTrace();
            }

            String[] temp = str.split("==##");
            if (temp != null && temp.length >= 12) {
                this.editnMinPlateWidth.setText(temp[0]);
                this.editnMaxPlateWidth.setText(temp[1]);
                if (temp[2].equals("1")) {
                    this.radiobutbVertCompressyes.setChecked(true);
                } else {
                    this.radiobutbVertCompressno.setChecked(true);
                }

                if (temp[6].equals("1")) {
                    this.radiobutbIsNightyes.setChecked(true);
                } else {
                    this.radiobutbIsNightno.setChecked(true);
                }

                this.editnImageFormat.setText(temp[7]);
            }

            if (temp != null && temp.length >= 18) {
                if (temp[11].equals("0")) {
                    this.radiobutdFormatyes.setChecked(true);
                } else {
                    this.radiobutdFormatno.setChecked(true);
                }

                this.editnPlateLocate_Th.setText(temp[12]);
                if (temp[13].equals("1")) {
                    this.radiobutbIsAutoSlopeyes.setChecked(true);
                } else {
                    this.radiobutbIsAutoSlopeno.setChecked(true);
                }

                this.editnSlopeDetectRange.setText(temp[14]);
                this.editszProvince.setText(temp[15]);
                this.editnContrast.setText(temp[16]);
                this.editnOCR_Th.setText(temp[17]);
            }

            if (temp != null && temp.length >= 26) {
                if (temp[18].equals("2")) {
                    this.radiobuttworowyellowyes.setChecked(true);
                } else {
                    this.radiobuttworowyellowno.setChecked(true);
                }

                if (temp[19].equals("4")) {
                    this.radiobutarmpoliceyes.setChecked(true);
                } else {
                    this.radiobutarmpoliceno.setChecked(true);
                }

                if (temp[20].equals("6")) {
                    this.radiobuttworowarmyyes.setChecked(true);
                } else {
                    this.radiobuttworowarmyno.setChecked(true);
                }

                if (temp[21].equals("8")) {
                    this.radiobuttractoryes.setChecked(true);
                } else {
                    this.radiobuttractorno.setChecked(true);
                }

                if (temp[22].equals("10")) {
                    this.radiobutonlytworowyellowyes.setChecked(true);
                } else {
                    this.radiobutonlytworowyellowno.setChecked(true);
                }

                if (temp[23].equals("12")) {
                    this.radiobutembassyyes.setChecked(true);
                } else {
                    this.radiobutembassyno.setChecked(true);
                }

                if (temp[24].equals("14")) {
                    this.radiobutonlylocationyes.setChecked(true);
                } else {
                    this.radiobutonlylocationno.setChecked(true);
                }

                if (temp[25].equals("16")) {
                    this.radiobutarmpolice2yes.setChecked(true);
                } else {
                    this.radiobutarmpolice2no.setChecked(true);
                }
            }
        }

        int butset_id = this.getResources().getIdentifier("butset", "id", this.getPackageName());
        this.mbutlog = (Button)this.findViewById(butset_id);
        this.mbutlog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cfg = "";
                cfg = cfg + PlateIDCfg.this.editnMinPlateWidth.getText().toString() + "==##";
                cfg = cfg + PlateIDCfg.this.editnMaxPlateWidth.getText().toString() + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutbVertCompressyes.isChecked() ? 1 : 0) + "==##";
                cfg = cfg + "0==##";
                cfg = cfg + "0==##";
                cfg = cfg + "0==##";
                cfg = cfg + (PlateIDCfg.this.radiobutbIsNightyes.isChecked() ? 1 : 0) + "==##";
                cfg = cfg + PlateIDCfg.this.editnImageFormat.getText().toString() + "==##";
                cfg = cfg + "0==##";
                cfg = cfg + "0==##";
                cfg = cfg + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutdFormatyes.isChecked() ? 0 : 1) + "==##";
                cfg = cfg + PlateIDCfg.this.editnPlateLocate_Th.getText().toString() + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutbIsAutoSlopeyes.isChecked() ? 1 : 0) + "==##";
                cfg = cfg + PlateIDCfg.this.editnSlopeDetectRange.getText().toString() + "==##";
                cfg = cfg + PlateIDCfg.this.editszProvince.getText().toString() + "==##";
                cfg = cfg + PlateIDCfg.this.editnContrast.getText().toString() + "==##";
                cfg = cfg + PlateIDCfg.this.editnOCR_Th.getText().toString() + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobuttworowyellowyes.isChecked() ? 2 : 3) + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutarmpoliceyes.isChecked() ? 4 : 5) + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobuttworowarmyyes.isChecked() ? 6 : 7) + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobuttractoryes.isChecked() ? 8 : 9) + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutonlytworowyellowyes.isChecked() ? 10 : 11) + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutembassyyes.isChecked() ? 12 : 13) + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutonlylocationyes.isChecked() ? 14 : 15) + "==##";
                cfg = cfg + (PlateIDCfg.this.radiobutarmpolice2yes.isChecked() ? 16 : 17) + "==##";
                cfg = cfg + "END";

                try {
                    boolean b = PlateIDCfg.this.StringBufferDemo(cfg);
                    if (b) {
                        Toast.makeText(PlateIDCfg.this.getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PlateIDCfg.this.getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException var4) {
                    Toast.makeText(PlateIDCfg.this.getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
        int returnbutlog_id = this.getResources().getIdentifier("returnbutlog", "id", this.getPackageName());
        this.returnbutlog = (Button)this.findViewById(returnbutlog_id);
        this.returnbutlog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(PlateIDCfg.this.getApplicationContext(), "返回", Toast.LENGTH_SHORT).show();
            }
        });
        int butclose_id = this.getResources().getIdentifier("butclose", "id", this.getPackageName());
        this.butclose = (Button)this.findViewById(butclose_id);
        this.butclose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PlateIDCfg.this.finish();
            }
        });
    }

    public boolean StringBufferDemo(String str) throws IOException {
        Common common = new Common();
        boolean fleg = true;
        String paths = common.getSDPath();
        if (!paths.equals("") && paths != null) {
            paths = paths + "/AndroidWT";
            StringBuffer sb = new StringBuffer();
            File dir = new File(paths);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File files = new File(paths + "/plateid.cfg");
            if (files.exists()) {
                files.delete();
            }

            files.createNewFile();
            FileOutputStream out = new FileOutputStream(files, true);

            for(int i = 0; i < 1; ++i) {
                sb.append(str);
                out.write(sb.toString().getBytes("utf-8"));
            }

            out.close();
        } else {
            fleg = false;
        }

        return fleg;
    }

    protected void onStop() {
        super.onStop();
        this.finish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation != 2) {
            int var10000 = this.getResources().getConfiguration().orientation;
        }

    }
}
