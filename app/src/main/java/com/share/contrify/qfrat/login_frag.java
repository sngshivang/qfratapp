package com.share.contrify.qfrat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link login_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link login_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View vv;
    private AlertDialog.Builder adb;
    private Context ct;
    String upwd = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public login_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static login_frag newInstance(String param1, String param2) {
        login_frag fragment = new login_frag();
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
        vv = inflater.inflate(R.layout.fragment_login_frag, container, false);
        adb = new AlertDialog.Builder(getActivity());
        listeners();
        return vv;
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
    private void listeners()
    {
        Button bt = vv.findViewById(R.id.loginbut),bt2;
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                login();
            }
        });
        bt2 = vv.findViewById(R.id.signupbut);
        bt2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                signup();
            }
        });
    }
    private void login()
    {
        EditText ed = vv.findViewById(R.id.usrnme);
        String un = ed.getText().toString();
        ed = vv.findViewById(R.id.usrpwd);
        String pw = ed.getText().toString();
        upwd = pw;
        network nw = new network();
        nw.lf = this;
        nw.execute("0",un,pw);
    }

    private void signup()
    {
        signup_frag sf = new signup_frag();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, sf);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void netret(String inp, login_frag lf)
    {
        try {
            JSONObject js = new JSONObject(inp);
            int cd = js.getInt("status");
            String alst="";
            //AlertDialog.Builder ab = new AlertDialog.Builder(lf.getActivity());
            if (cd ==6)
            {
                adb.setTitle("LOGIN SUCCESSFUL");
                adb.setMessage("You will now be redirected the Main Page");
                adb.show();
                universals.sysfile2cr(lf.getContext(),js.getString("fname"),upwd, js.getString("email"), js.getString("sid"));
                universals.setdefs(lf.getContext());
            }
            else{
                if (cd==1)
                {
                    alst = "You are already logged in";
                }
                else if (cd==2)
                {
                    alst = "SQL Configuration fault! Contact admin at admin[at]qfrat.co.in";
                }
                else if (cd ==3)
                {
                    alst = "This method of login is not allowed for this account";
                }
                else if (cd == 4)
                {
                    alst = "Incorrect Username or password";
                }
                else if (cd == 5)
                {
                    alst  = "Record fetch error for input! Contact admin at admin[at]qfrat.co.in";
                }
                else if (cd == 7)
                {
                    alst = "Username not found in record";
                }
                else if (cd  == 12)
                {
                    universals.sysfile2cr(lf.getContext(),"nf", "nf", "nf", js.getString("sid"));
                    universals.setdefs(lf.getContext());
                    sess_reset sr = new sess_reset();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, sr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                adb.setTitle("ERROR");
                adb.setMessage(alst);
                adb.show();
            }
        }
        catch (Exception e)
        {
            Log.e("login_frag",e.toString());
        }

    }
    private void nonstat()
    {

    }
}
