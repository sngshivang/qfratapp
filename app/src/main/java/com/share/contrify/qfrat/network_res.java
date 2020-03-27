package com.share.contrify.qfrat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class network_res extends AsyncTask <String, Integer, String> {
    mainquiz mq = null;
    newsfrag nf = null;
    private String pref = null, fl = null;

    @Override
    public void onPreExecute()
    {

    }
    @Override
    public String doInBackground(String... params) {
        pref = params[0]; fl ="";String url="";
        String out= "";

        try {
            if (pref.equals("0")) {

                String qud = params[1];
                fl = qud + "_pic.png";url = "https://www.qfrat.co.in/media/pictures/";
                url += fl;
            }
            else if (pref.equals("1"))
            {
                url = params[1];
                fl = params[2];

            }
            URL urlu = new URL(url);
            HttpsURLConnection hc = (HttpsURLConnection)urlu.openConnection();
            if (hc.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + hc.getResponseCode()
                        + " " + hc.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = hc.getContentLength();
            // download the file
            InputStream input = hc.getInputStream();
            FileOutputStream output = new FileOutputStream(new File(universals.stpth, fl));

            byte[] data = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        }
        catch (Exception e)
        {
            Log.e("network_res",e.toString());
            out = "ERR";
        }
        return  out;
    }
    @Override
    protected void onProgressUpdate (Integer... val)
    {
        super.onProgressUpdate(val);
        if (pref.equals("0"))
        mq.upprog(val[0]);
        else if (pref.equals("1"))
            nf.upprog(val[0], fl);

    }
    @Override
    protected void onPostExecute(String inp)
    {
        if (inp.equals("ERR")) {
            if (pref.equals("0"))
                mq.reserr(inp);
            else if (pref.equals("1"))
                nf.reserr(inp);
        }
    }
    }
