package com.share.contrify.qfrat;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class universals {
    protected static String name,email, phpsess, pwd;
    protected static ArrayList<JSONArray> arl;
    protected static JSONArray qids;
    public static JSONObject jsb;
    protected static boolean isgoogle, iswlcme;
    protected static ArrayList<Integer> qlt;
    static File stpth;
    protected static GoogleSignInClient mGoogleSignInClient;
    protected static void setter(String nm, String pw, String em, String phps, boolean isgoog)
    {
        name = nm;
        email = em;
        phpsess = phps;
        pwd = pw;
        isgoogle = isgoog;
    }
    public static void setter(JSONObject js)
    {
        jsb = js;
    }
    protected static void sysfile2cr(Context ct, String nm, String pwd, String em, String sess, boolean isgoog)
    {
        JSONObject js = new JSONObject();
        try {
            js.put("name", nm);
            js.put("pwd", pwd);
            js.put("email", em);
            js.put("isgoog",isgoog);
            js.put("phpsess", sess);
            String out = js.toString();
            FileWriter fw = new FileWriter(new File(ct.getFilesDir(),"SYSFILE2"));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(out);
            bw.close();
            setdefs(ct);
        }
        catch (Exception e)
        {
            Log.e("sysfile2cr",e.toString());
        }
    }
    public static void sysfile2cr(Context ct, JSONObject job)
    {
        try {
            FileWriter fw = new FileWriter(new File(ct.getFilesDir(), "SYSFILE3"));
            String out = job.toString();
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(out);
            bw.close();
            setdefs(ct);
        }
        catch (Exception e)
        {
            Log.e("sysfile2cr",e.toString());
        }
    }
    protected static void setdefs(Context ct)
    {
        try {
            String fnl="";
            File syslf = new File(ct.getFilesDir(), "SYSFILE2");
            FileReader fr = new FileReader(syslf);
            BufferedReader br = new BufferedReader(fr);
            String join;
            while ((join = br.readLine()) != null) {
                fnl += join;
            }
            JSONObject js = new JSONObject(fnl);
            String nm = js.getString("name");
            String pw = js.getString("pwd");
            String em = js.getString("email");
            boolean isgoog = js.getBoolean("isgoog");
            String phpsess = js.getString("phpsess");
            universals.setter(nm,pw, em,phpsess, isgoog);
        }
        catch (Exception e)
        {
            Log.e("setdefs", e.toString());
        }
        try
        {
            File syslf = new File(ct.getFilesDir(), "SYSFILE3");
            FileReader fr = new FileReader(syslf);
            BufferedReader br = new BufferedReader(fr);
            String join, fnl ="";
            while ((join = br.readLine()) != null) {
                fnl += join;
            }
            JSONObject ja = new JSONObject(fnl);
            universals.setter(ja);
        }catch (Exception e)
        {
            Log.e("setdefs",e.toString());
        }
    }
}
