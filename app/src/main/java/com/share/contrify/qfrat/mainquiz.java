package com.share.contrify.qfrat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.ExecutionException;

import static android.net.Uri.decode;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mainquiz.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mainquiz#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainquiz extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View mainview;
    int medprog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public mainquiz() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainquiz.
     */
    // TODO: Rename and change types and number of parameters
    public static mainquiz newInstance(String param1, String param2) {
        mainquiz fragment = new mainquiz();
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
        quizplay.itr = 0;
        mainview = inflater.inflate(R.layout.fragment_mainquiz, container, false);
        resetelements();
        pubques();
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
    private void pubques()
    {
        int pos = quizplay.itr;
        try {
            TextView tv = mainview.findViewById(R.id.textview1);
            tv.setText(decode(quizplay.que.getString(pos)));
            String res = quizplay.res.getString(pos);
            switch (res)
            {
                case "YNN":
                    break;
                case "YYN":
                    loadres();
                    break;
            }
        }
        catch (Exception e)
        {
            Log.e("mainquiz",e.toString());
        }

    }
    private void loadres()
    {
        TextView tv = mainview.findViewById(R.id.textview4);
        tv.setVisibility(View.VISIBLE);
        tv = mainview.findViewById(R.id.textview5);
        tv.setVisibility(View.VISIBLE);
        ProgressBar pr = mainview.findViewById(R.id.progressBar);
        pr.setVisibility(View.VISIBLE);
        try {
            String fln = quizplay.qud.getString(quizplay.itr);
            network_res nr = new network_res();
            nr.mq = this;
            nr.execute("0", fln);
        }
        catch (Exception e)
        {
            Log.e("mainquiz",e.toString());
        }

    }
    public void upprog(Integer val)
    {
        Log.i("Progress",String.valueOf(val));
        if (val!=100)
        {
            TextView tv = mainview.findViewById(R.id.textview5);
            String str = val + "% Complete";
            tv.setText(str);
        }
        else
        {
            try {
                TextView tv = mainview.findViewById(R.id.textview4);
                tv.setVisibility(View.GONE);
                tv = mainview.findViewById(R.id.textview5);
                String temp = "100% Complete";
                tv.setText(temp);
                tv.setVisibility(View.GONE);
                ProgressBar pr = mainview.findViewById(R.id.progressBar);
                pr.setVisibility(View.GONE);
                ImageView iv = mainview.findViewById(R.id.quesimg);
                String fln = quizplay.qud.getString(quizplay.itr)+"_pic.png";
                Bitmap bmp = BitmapFactory.decodeFile(universals.stpth + File.separator + fln);
                iv.setImageBitmap(bmp);
                iv.setVisibility(View.VISIBLE);
            }
            catch (Exception e)
            {
                Log.e("mainquiz",e.toString());
            }
        }

    }
    private void resetelements()
    {
        TextView tv = mainview.findViewById(R.id.textview4);
        tv.setVisibility(View.GONE);
        tv = mainview.findViewById(R.id.textview5);
        tv.setVisibility(View.GONE);
        ProgressBar pr = mainview.findViewById(R.id.progressBar);
        pr.setVisibility(View.GONE);
        ImageView iv = mainview.findViewById(R.id.quesimg);
        iv.setVisibility(View.GONE);
        medprog = 100;
    }
    private void forward()
    {
        if (quizplay.itr<30)
        {
            quizplay.itr++;
            resetelements();
            pubques();
        }


    }
    private void backward()
    {
        if (quizplay.itr>0)
        {
            quizplay.itr--;
            resetelements();
            pubques();
        }


    }
    private void listeners()
    {
        Button fr = mainview.findViewById(R.id.forbut);
        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward();
            }
        });
        Button rv = mainview.findViewById(R.id.backbut);
        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backward();
            }
        });
    }
}
