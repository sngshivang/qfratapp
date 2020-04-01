package com.share.contrify.qfrat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class firstrun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstrun);
        initializers();
        if (reader())
        {
            universals.setdefs(this);
            setfolders();
            Intent it = new Intent(this,MainActivity.class);
            startActivity(it);
            finish();
        }
    }
    private void initializers()
    {
        universals.arl = new ArrayList<>();
        universals.qlt = new ArrayList<>();
    }
    private void finalend()
    {
        try{
            FileWriter fw = new FileWriter(new File(this.getFilesDir(),"SYSFILE1"));
            BufferedWriter out = new BufferedWriter(fw);
            out.write("FR_DISABLE");
            out.close();
        }catch (Exception e)
        {
            Log.e("firstrun",e.toString());
        }
        Intent it = new Intent(this,MainActivity.class);
        startActivity(it);
        finish();

    }
    private boolean reader()
    {
        String fnl="";
        try {
            FileReader fr = new FileReader(new File(this.getFilesDir(), "SYSFILE1"));
            BufferedReader br = new BufferedReader(fr);
            String join;
            while ((join=br.readLine())!=null)
            {
                fnl+=join;
            }
        }catch (Exception e)
        {
            Log.e("firstrun",e.toString());
        }
        Log.i("reader",fnl);
        if (fnl.equals("FR_DISABLE")) {
            return true;
        }
        else {
            universals.sysfile2cr(this, "nf", "nf", "nf", "nf", false);
            universals.sysfile2cr(this , null);
            finalend();
            return false;
        }
    }
    private void setfolders()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date dt = new Date();
        String fldnm = sdf.format(dt);
        boolean res=false;
        File fl = new File(this.getExternalFilesDir(null), fldnm);
        if (!fl.exists())
            res = fl.mkdirs();
        Log.i("filecr",String.valueOf(res));
        universals.stpth = fl;

    }
}
