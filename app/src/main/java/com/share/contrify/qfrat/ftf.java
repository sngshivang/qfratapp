package com.share.contrify.qfrat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Network;
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
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ftf.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ftf#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ftf extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View fragview;
    private AlertDialog.Builder adb;
    private AlertDialog ad;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ftf() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ftf.
     */
    // TODO: Rename and change types and number of parameters
    public static ftf newInstance(String param1, String param2) {
        ftf fragment = new ftf();
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
        fragview = inflater.inflate(R.layout.fragment_ftf, container, false);
        listeners();
        return fragview;
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

    private void listeners()
    {
        Button bt =  fragview.findViewById(R.id.yesbut);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onyesbut();
            }
        });
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
        void fetchques(String inp);
        void ftffrag();
    }
    private void loadspindialog()
    {
        adb = new AlertDialog.Builder(getContext());
        LayoutInflater lf = getLayoutInflater();
        View adbview = lf.inflate(R.layout.alert_spin2,null);
        TextView tv = adbview.findViewById(R.id.headmsg);
        String str = "Please wait while we fetch the necessary quiz data...";
        tv.setText(str);
        adb.setView(adbview);
        ad = adb.create();
        ad = adb.show();
    }
    private void onyesbut()
    {
        String qid = null;
        try {

            qid = universals.arl.get(0).getString(universals.qlt.get(quizplay.pos));
        }
        catch (Exception e)
        {
            Log.e("ftf",e.toString());
        }
        Log.i("qid",qid);
        if (universals.name.equals("nf"))
        {
            adb = new AlertDialog.Builder(getContext());
            adb.setTitle("NOT LOGGED IN");
            adb.setMessage("You are not logged in. Do you want to login now?");
            adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.ftffrag();

                }
            });
            adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (ad!=null)
                        ad.dismiss();
                }
            });
            ad = adb.create();
            ad = adb.show();

        }
        else {
            loadspindialog();
            network nt = new network();
            nt.ft = this;
            nt.execute("5", qid);
        }

    }
    void retcall(String data) {
        adb = new AlertDialog.Builder(getContext());
        if (ad != null)
            ad.dismiss();
        if (data.equals("ERR")) {
            adb.setTitle("ERROR!");
            adb.setMessage("Failed to fetch necessary information from the server. Please try again");
            adb.show();
        } else {
            try {
                JSONObject jsb = new JSONObject(data);
                String sdatm = jsb.getString("sdatm");
                String st = jsb.getString("st");
                switch (st) {
                    case "62":
                        fetchques();
                        break;
                    case "63":
                        adb.setTitle("QUIZ NOT STARTED");
                        adb.setMessage("This quiz has not yet started, please wait till " + sdatm);
                        adb.show();
                        break;
                    case "61":
                        adb.setTitle("SERVER ERROR");
                        adb.setMessage("There has been an error communicating with the server. Please check your internet connection, close and reopen your app.");
                        adb.show();
                        break;

                }

            } catch (Exception e) {
                Log.e("ftf", e.toString());
            }
        }
    }
    private void fetchques()
    {
        loadspindialog();
        network nt = new network();
        nt.ft = this;
        nt.execute("6");
    }
    void retcallques(String inp)
    {
        if (ad!=null)
            ad.dismiss();
        mListener.fetchques(inp);
        Navigation.findNavController(fragview).navigate(R.id.action_ftf_to_mainquiz);
    }
}
