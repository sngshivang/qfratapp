package com.share.contrify.qfrat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link attemptlogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link attemptlogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class attemptlogin extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View mainview;
    private int ri;
    private Handler task;
    private Handler hld = new Handler();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public attemptlogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment attemptlogin.
     */
    // TODO: Rename and change types and number of parameters
    public static attemptlogin newInstance(String param1, String param2) {
        attemptlogin fragment = new attemptlogin();
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
        mainview = inflater.inflate(R.layout.fragment_attemptlogin, container, false);
        autologin();
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
    private void autologin()
    {
        network nt = new network();
        nt.al = this;
        nt.execute("7", universals.email, universals.pwd);

    }
    void retcall(String inp)
    {
        try {
            JSONObject js = new JSONObject(inp);
            ProgressBar pb = mainview.findViewById(R.id.progressBar2);
            pb.setVisibility(View.GONE);
            int cd = js.getInt("status");
            String alst= null;
            if (cd ==6)
            {
                ri = 4;
                task = new Handler(Looper.getMainLooper());
                task.postDelayed(newexp, 1000);
            }
            else{
                universals.sysfile2cr(getContext(),"nf", "nf", "nf", js.getString("sid"));
                universals.setdefs(getContext());
                if (cd  == 12)
                {
                    sess_reset sr = new sess_reset();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, sr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                else {
                    if (cd == 1) {
                        alst = "You are already logged in";
                    } else if (cd == 2) {
                        alst = "SQL Configuration fault! Contact admin at admin[at]qfrat.co.in";
                    } else if (cd == 3) {
                        alst = "This method of login is not allowed for this account";
                    } else if (cd == 4) {
                        alst = "Incorrect Username or password";
                    } else if (cd == 5) {
                        alst = "Record fetch error for input! Contact admin at admin[at]qfrat.co.in";
                    } else if (cd == 7) {
                        alst = "Username not found in record";
                    }
                    TextView tv = mainview.findViewById(R.id.textview1);
                    tv.setText(alst);
                }
            }
        }
        catch (Exception e)
        {
            Log.e("login_frag",e.toString());
        }

    }
    private void testmod(int cnt)
    {
        String str = null;
        if (cnt>-1) {
            str = "Login Successful, You will be redirected to the main page in " + cnt + " seconds";
            TextView tv = mainview.findViewById(R.id.textview1);
            tv.setText(str);
            task.postDelayed(newexp, 1000);
        }
        else
        {
            testfragment tf = new testfragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, tf);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    Runnable newexp = new Runnable() {
        @Override
        public void run() {
            testmod(ri--);
        }
    };
}
