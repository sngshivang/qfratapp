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

    @Override
    public void onPreExecute()
    {

    }
    @Override
    public String doInBackground(String... params) {
        String pref = params[0],url="",fl="";

        try {
            if (pref.equals("0")) {

                String qud = params[1];
                fl = qud + "_pic.png";url = "https://www.qfrat.co.in/media/pictures/";
                url += fl;
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
        }
        return  null;
    }
    @Override
    protected void onProgressUpdate (Integer... val)
    {
        super.onProgressUpdate(val);
        mq.upprog(val[0]);

    }
    }
