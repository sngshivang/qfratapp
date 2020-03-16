package com.share.contrify.qfrat;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
 * {@link sess_reset.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link sess_reset#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sess_reset extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View vw;
    private AlertDialog.Builder ab;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public sess_reset() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sess_reset.
     */
    // TODO: Rename and change types and number of parameters
    public static sess_reset newInstance(String param1, String param2) {
        sess_reset fragment = new sess_reset();
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
        vw = inflater.inflate(R.layout.fragment_sess_reset, container, false);
        fillfields();
        ab = new AlertDialog.Builder(getActivity());
        buttonlisteners();
        // Inflate the layout for this fragment
        return vw;
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
    private void fillfields()
    {
        network nw = new network();
        nw.sr = this;
        nw.execute("2");
    }
    private void buttonlisteners()
    {
        Button bt1= vw.findViewById(R.id.yesbut);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes();
            }
        });
        Button bt2= vw.findViewById(R.id.nobut);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no();
            }
        });
    }
    protected void fillfieldsr(String inp, sess_reset sr)
    {
        try {
            JSONObject js = new JSONObject(inp);
            TextView tv = vw.findViewById(R.id.textview3);
            String set = "IP Address: "+js.getString("ipad");
            tv.setText(set);
            set = "Client: "+js.getString("clnt");
            TextView tv2 = vw.findViewById(R.id.textView4);
            //tv2.setText(set);
            set = "Date&Time: "+js.getString("datm");
            tv = vw.findViewById(R.id.textview5);
            tv.setText(set);

        }
        catch (Exception e)
        {
            Log.e("sess_reset",e.toString());

        }
    }
    private void yes()
    {
        network nw = new network();
        nw.sr = this;
        nw.execute("3");

    }
    private void no()
    {
        ab.setTitle("SESSION CANCELLED");
        ab.setMessage("Your this login session has been cancelled and you will be taken to the home activity.");
        ab.show();
        universals.setter("nf", "nf", "nf","nf");
        universals.setdefs(getActivity());
    }
}
