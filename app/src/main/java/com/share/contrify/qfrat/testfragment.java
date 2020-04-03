package com.share.contrify.qfrat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.share.contrify.qfrat.universals.jsb;
import static com.share.contrify.qfrat.MainActivity.news;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link testfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link testfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class testfragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mainview;
    private ListView lst;
    private AlertDialog.Builder adb;
    private AlertDialog afcr;
    private ArrayList<newsfield> al;
    private newsadap nz;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public testfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment testfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static testfragment newInstance(String param1, String param2) {
        testfragment fragment = new testfragment();
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
        Log.i("Hello","this is a statement");
        mainview = inflater.inflate(R.layout.fragment_testfragment,
                container, false);
        //mListener.layoutmanip();
        instant();
        sendreq(false);
        listeners();
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
        void jumptonewsfrag();
    }
    private  void instant()
    {
        lst = mainview.findViewById(R.id.qzlist);
        al = new ArrayList<>();
        nz = new newsadap(getContext(),al);
    }
    private void lstfiller(String comb)
    {
        newsfield ifo = new newsfield(comb);
        nz.add(ifo);
        lst.setAdapter(nz);
    }
    private void sendreq(boolean or)
    {
        instant();
        if (jsb==null||or) {
            adb = new AlertDialog.Builder(getContext());
            LayoutInflater lf = getLayoutInflater();
            View spinview = lf.inflate(R.layout.alert_spin2, null);
            TextView tv = spinview.findViewById(R.id.headmsg);
            String msg = "Please wait while we fetch latest news feed for you...";
            tv.setText(msg);
            adb.setView(spinview);
            afcr = adb.create();
            afcr = adb.show();
            network nt = new network();
            nt.tf = this;
            nt.execute("10");
        }
        else
            retcall(jsb.toString());
    }
    public void retcall(String inp)
    {
        if (afcr!=null)
        afcr.dismiss();
        Log.i("retcall",inp);
        String tit, url, stat;
        if (inp.equals("ERR"))
        {
            lstfiller("Failed to fetch the latest news feed. Please reload the page.");
        }
        else {
            try {
                jsb = new JSONObject(inp);
                universals.sysfile2cr(getContext(), jsb);
                stat = jsb.getString("status");
                int lnks = 0;
                if (stat.equals("ok")) {
                    lnks = jsb.getInt("totalResults");
                    news = new JSONArray(jsb.getString("articles"));
                    for (int i = 0; i < 20; i++) {
                        String col = news.getString(i);
                        JSONObject insrc = new JSONObject(col);
                        tit = insrc.getString("title");
                        url = insrc.getString("url");
                        lstfiller(tit);

                    }
                }
            } catch (Exception e) {
                Log.e("testfragment", e.toString());
            }
        }
        welcome();

    }
    private void listeners()
    {
        ImageButton bt = mainview.findViewById(R.id.imageButton3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendreq(true);
            }
        });
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                MainActivity.newspos = pos;

                Navigation.findNavController(view).navigate(R.id.action_testfragment_to_newsfrag);

            }
            });
    }
    private void welcome()
    {
        if (!universals.iswlcme) {
            adb = new AlertDialog.Builder(getContext());
            adb.setTitle("Welcome to QFrat Beta");
            adb.setMessage("If you want to play a quiz, select the appropriate option. You can always select the quiz option from the menu drawer icon on top left of this app.\n\nNote:- This APP may require restart on some devices after fresh install.\n\n THIS APP IS IN BETA.");
            adb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (afcr != null)
                        afcr.dismiss();
                }
            });
            adb.setPositiveButton("PLAY QUIZ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent it = new Intent(getContext(), quizplay.class);
                    startActivity(it);
                }
            });
            adb.setCancelable(false);
            afcr = adb.create();
            afcr = adb.show();
            universals.iswlcme = true;
        }
    }
}
