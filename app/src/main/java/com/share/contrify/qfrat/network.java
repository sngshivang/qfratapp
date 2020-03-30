package com.share.contrify.qfrat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class network extends AsyncTask <String,Void,String> {

    login_frag lf= null;
    sess_reset sr = null;
    quizlist qz = null;
    signup_frag sf;
    ftf ft = null;
    attemptlogin al = null;
    mainquiz nmq = null;
    String pref = "";
    signout so = null;
    testfragment tf = null;
    @Override
    public void onPreExecute()
    {

    }
    @Override
    public String doInBackground(String... params)
    {
        pref  = params[0];
        String url="",data="",send="";
        try {
            if (pref.equals("0") || pref.equals("7")) {
                String un = params[1];
                String pw = params[2];
                url = "https://www.qfrat.co.in/php/login.php";
                data = "email=" + un + "&pwd=" + pw + "&isgoog=0";
            }
            else if (pref.equals("1")||pref.equals("13"))
            {
                String un = params[1];
                String pw = params[2];
                String mob = params[3];
                String nm = params[4];
                String col = params[5];
                String capt = params[6];
                url = "https://www.qfrat.co.in/php/signupbackend.php";
                data = "gresp="+capt+"&ANDAPP=1&email="+un+"&pwd="+pw+"&repwd="+pw+"&name="+nm+"&mob="+mob+"&remember=0&colbr="+col;
            }
            else if (pref.equals("2"))
            {
                url = "https://www.qfrat.co.in/php/end_sessions.php";
                data = "opt=2";
            }
            else if (pref.equals("3"))
            {
                url = "https://www.qfrat.co.in/php/end_sessions.php";
                data = "opt=1";
            }
            else if (pref.equals("4"))
            {
                url = "https://www.qfrat.co.in/php/quizfetch.php";
                data = "choice=1";
            }
            else if (pref.equals("5"))
            {
                url = "https://www.qfrat.co.in/php/initdat.php";
                data = "qid="+params[1];
            }
            else if (pref.equals("6"))
            {
                url = "https://www.qfrat.co.in/php/qms_questions.php";
                data = "fn=quesall";
            }
            else if (pref.equals("8"))
            {
                url = "https://www.qfrat.co.in/php/qms_questions.php";
                data = "fn=ldr";
            }
            else if (pref.equals("9"))
            {
                String ans = params[1];
                String qns = params[2];
                url = "https://www.qfrat.co.in/php/qms_questions.php";
                data = "fn=ansfet&qno="+qns+"&ans="+ans;
            }
            else if (pref.equals("10"))
            {
                url = "http://newsapi.org/v2/top-headlines?" +
                    "country=us&" +
                    "apiKey=250aa58578a64814abcc32e58c068b30";
            }
            else if (pref.equals("11"))
            {
                String un = params[1];
                String pw = params[2];
                String idtok = params[3];
                url = "https://www.qfrat.co.in/php/login.php";
                data = "email=" + un + "&pwd=" + pw + "&isgoog=1&idtoken="+idtok;
            }
            else if (pref.equals("12"))
            {
                url = "https://www.qfrat.co.in/php/signout.php";
            }
            else if (pref.equals("14"))
            {
                url = "https://www.qfrat.co.in/php/fpwd.php";
                String em = params[1];
                data = "fn=rcr&email="+em;
            }
            URL urlu = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)urlu.openConnection();
            String reqst;
            if (!universals.phpsess.equals("nf")) {
                reqst = "PHPSESSID=" + universals.phpsess + "; email=occupied;";
                conn.setRequestProperty("Cookie", reqst);
            }
            if (pref.equals("10")) {
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36 ");

            }
            else {
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
            }
            int resp = conn.getResponseCode();
            Log.i("resp",String.valueOf(resp));
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            send = sb.toString();
        }
        catch (Exception e)
        {
            send = "ERR";
            Log.e("network",e.toString());
        }
        Log.i("output",send);
        /*if (pref.equals("0"))
        {
            lf.netret(send, lf);
        }*/
        return send;
    }
    @Override
    public void onPostExecute(String inp)
    {
        if (pref.equals("0") || pref.equals("13") || pref.equals("11")) {
            if (lf!=null)
            lf.netret(inp, lf);
            if (al!=null)
                al.retcall(inp);
        }
        else if (pref.equals("1"))
            sf.retcall(inp);
        else if (pref.equals("2"))
            sr.fillfieldsr(inp, sr);
        else if (pref.equals("3"))
            sr.retcall(inp);
        else if (pref.equals("4"))
            qz.retcall(inp);
        else if (pref.equals("5"))
            ft.retcall(inp);
        else if (pref.equals("6"))
            ft.retcallques(inp);
        else if (pref.equals("7"))
            al.retcall(inp);
        else if (pref.equals("8"))
            nmq.retans(inp, false);
        else if (pref.equals("9"))
            nmq.retans(inp, true);
        else if (pref.equals("10"))
            tf.retcall(inp);
        else if (pref.equals("12")) {
            if (lf!=null)
                lf.retsignout(inp);
            if (so!=null)
                so.retsignout(inp);
        }
        else if(pref.equals("14"))
            lf.respwres(inp);
    }
}
