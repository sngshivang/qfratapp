package com.share.contrify.qfrat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link quizlist.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link quizlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class quizlist extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView lst;
    private ArrayList<fieldsinfo> al;
    private qzadap qz;
    private View mainview;
    private JSONArray qname, tme, tqu, cdatm, prz, plbt;
    private AlertDialog.Builder adb;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public quizlist() {
        // Required empty public constructor
    }

    private  void instant()
    {
        lst = mainview.findViewById(R.id.qzlist);
        al = new ArrayList<>();
        qz = new qzadap(getContext(),al);
    }
    private void lstfiller(String qzn, String prz, String datm, String totm, String totq, String accbut)
    {
        fieldsinfo ifo = new fieldsinfo(qzn, prz, datm, totm, totq, accbut);
        qz.add(ifo);
        lst.setAdapter(qz);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment quizlist.
     */
    // TODO: Rename and change types and number of parameters
    public static quizlist newInstance(String param1, String param2) {
        quizlist fragment = new quizlist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainview = inflater.inflate(R.layout.fragment_quizlist, container, false);
        mListener.quizlistfrag();
        instant();
        sendreq();
        listeners();
        adb = new AlertDialog.Builder(getActivity());
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                try {
                    String chc = plbt.getString(universals.qlt.get(pos));
                    if (chc.equals("Y")) {

                    }
                    else if (chc.equals("N"))
                    {
                        adb.setTitle("NOT ALLOWED");
                        adb.setMessage("This quiz has not started yet. Please wait till the mentioned start date-time");
                        adb.show();
                    }
                    else if (chc.equals("P"))
                    {
                        adb.setTitle("QUIZ HAS ENDED");
                        adb.setMessage("The quiz you are attempting to play has already ended.");
                        adb.show();
                    }
                    mListener.sendtoparent(pos);
                    Navigation.findNavController(mainview).navigate(R.id.action_quizlist_to_ftf);
                }
                catch (Exception e)
                {
                    Log.e("quizlist",e.toString());
                }
            }
        });
        return mainview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void sendtoparent(int pos);
        void quizlistfrag();
    }
    private void sendreq()
    {
        /*LayoutInflater li = getLayoutInflater();
        View alertLayout = li.inflate(R.layout.alert_spin, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Info");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        alert.show();*/
        TextView tv = mainview.findViewById(R.id.textview1);
        tv.setText(getResources().getString(R.string.qzlst_h6));
        ProgressBar pr = mainview.findViewById(R.id.progressBar2);
        pr.setVisibility(View.VISIBLE);
        network nw = new network();
        nw.qz  = this;
        nw.execute("4");
    }

    protected void retcall(String inp)
    {
        Log.i("RETCALL",inp);
        TextView tv = mainview.findViewById(R.id.textview1);
        ProgressBar pr = mainview.findViewById(R.id.progressBar2);
        pr.setVisibility(View.GONE);
        if (inp.equals("ERR"))
        {
            tv.setText(getResources().getString(R.string.qzlst_h7));
            ImageButton ib = mainview.findViewById(R.id.imageButton);
            ib.setVisibility(View.VISIBLE);
        }
        else {
        try{
            tv.setText(getResources().getString(R.string.qzlist_h1));
            lst.setVisibility(View.VISIBLE);
            JSONObject js = new JSONObject(inp);
            qname = new JSONArray(js.getString("qname"));
            universals.arl.add(new JSONArray(js.getString("qid")));
            universals.arl.add(qname);
            tme = new JSONArray(js.getString("tme"));
            universals.arl.add(tme);
            tqu = new JSONArray(js.getString("tqu"));
            universals.arl.add(tqu);
            cdatm = new JSONArray(js.getString("cdatm"));
            universals.arl.add(cdatm);
            prz = new JSONArray(js.getString("prz"));
            universals.arl.add(prz);
            plbt = new JSONArray(js.getString("plbt"));
            universals.arl.add(plbt);
            for (int j = 0;j < qname.length(); j++)
            {
                if (!plbt.getString(j).equals("S")) {
                    universals.qlt.add(j);
                    String csch = plbt.getString(j);
                    switch (csch) {
                        case "Y":
                            lstfiller(qname.getString(j), prz.getString(j), cdatm.getString(j), (tme.getString(j) + " MINS"), (tqu.getString(j) + " Questions"), "PLAY");
                            break;
                        case "N":
                            lstfiller(qname.getString(j), prz.getString(j), cdatm.getString(j), (tme.getString(j) + " MINS"), (tqu.getString(j) + " Questions"), "WAIT");
                            break;
                        case "P":
                            lstfiller(qname.getString(j), prz.getString(j), cdatm.getString(j), (tme.getString(j) + " MINS"), (tqu.getString(j) + " Questions"), "QUIZ ENDS");
                            break;

                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.e("quizlist", e.toString());
        }
        }

    }
    private void listeners()
    {
        ImageButton ib = mainview.findViewById(R.id.imageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendreq();
            }
        });
    }
}
