package com.share.contrify.qfrat;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class quizplay extends AppCompatActivity implements quizlist.OnFragmentInteractionListener, ftf.OnFragmentInteractionListener, mainquiz.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizplay);
    }
    static int pos,itr;
    static String ques;
    static JSONArray que,typ,res,qud,posi,neg;
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    @Override
    public void sendtoparent(int u)
    {
        pos = u;
        Log.i("Current_pos", String.valueOf(u));
    }
    @Override
    public void fetchques(String inp)
    {
        ques = inp;
        splitproc();
    }
    private void splitproc()
    {
        try {
            JSONObject jsb = new JSONObject(ques);
            que = new JSONArray(jsb.getString("ques"));
            typ = new JSONArray(jsb.getString("typ"));
            res = new JSONArray(jsb.getString("res"));
            qud = new JSONArray(jsb.getString("qud"));
            posi = new JSONArray(jsb.getString("posi"));
            neg = new JSONArray(jsb.getString("neg"));
        }
        catch (Exception e)
        {
            Log.e("quizplay",e.toString());
        }
    }
}
