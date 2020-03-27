package com.share.contrify.qfrat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.share.contrify.qfrat.MainActivity.jsb;
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
        instant();
        sendreq();
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
    private void sendreq()
    {
        if (jsb==null) {
            network nt = new network();
            nt.tf = this;
            nt.execute("10");
        }
        else
            retcall(jsb.toString());
    }
    public void retcall(String inp)
    {
        Log.i("retcall",inp);
        Spanned txtur;
        String tit, url, imgur, stat;
        try{
            jsb = new JSONObject(inp);
            stat = jsb.getString("status");
            int lnks = 0;
            if (stat.equals("ok"))
            {
                lnks = jsb.getInt("totalResults");
                news = new JSONArray(jsb.getString("articles"));
                for (int i = 0;i< 20; i++)
                {
                    String col = news.getString(i);
                    JSONObject insrc = new JSONObject(col);
                    tit = insrc.getString("title");
                    url = insrc.getString("url");
                    imgur = insrc.getString("urlToImage");
                    txtur = Html.fromHtml("<a href = '"+url+"'>"+tit+"</a>");
                    lstfiller(tit);

                }
            }
        }
        catch (Exception e)
        {
            Log.e("testfragment",e.toString());
        }

    }
    private void listeners()
    {
        ImageButton bt = mainview.findViewById(R.id.imageButton3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendreq();
            }
        });
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                MainActivity.newspos = pos;
                newsfrag nfg = new newsfrag();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left);
                fragmentTransaction.replace(R.id.nav_host_fragment, nfg);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
            });
    }
}
