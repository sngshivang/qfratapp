package com.share.contrify.qfrat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class network extends AsyncTask <String,Void,String> {

    login_frag lf= null;
    sess_reset sr = null;
    quizlist qz = null;
    ftf ft = null;
    attemptlogin al = null;
    String pref = "";
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
                Log.i("data",data);
            }
            else if (pref.equals("1"))
            {
                String un = params[1];
                String pw = params[2];
                String mob = params[3];
                String nm = params[4];
                String col = params[5];
                String capt = params[6];
                url = "https://www.qfrat.co.in/php/signupbackend.php";
                data = "gresp="+capt+"&ANDAPP=1&email="+un+"&pwd="+pw+"&repwd="+pw+"&name="+nm+"&mob="+mob+"&remember=0";
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
            URL urlu = new URL(url);
            URLConnection conn = urlu.openConnection();
            String reqst;
            if (!universals.phpsess.equals("nf")) {
                reqst = "PHPSESSID=" + universals.phpsess + "; email=occupied;";
                conn.setRequestProperty("Cookie", reqst);
            }
            //conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
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
        if (pref.equals("0"))
        lf.netret(inp, lf);
        else if (pref.equals("2"))
            sr.fillfieldsr(inp, sr);
        else if (pref.equals("4"))
            qz.retcall(inp);
        else if (pref.equals("5"))
            ft.retcall(inp);
        else if (pref.equals("6"))
            ft.retcallques(inp);
        else if (pref.equals("7"))
            al.retcall(inp);
    }
}
