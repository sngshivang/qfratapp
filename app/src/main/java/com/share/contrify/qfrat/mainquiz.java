package com.share.contrify.qfrat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Text;

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
    mainquiz mq;

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
        loadans();
        submit();
        mListener.invokeNavMethods();
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
        void invokeNavMethods();
        void incrques();
    }
    private void pubques()
    {
        int pos = quizplay.itr;
        mListener.incrques();
        try {
            TextView tv = mainview.findViewById(R.id.textview1);
            checkans();
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
        ImageButton ib = mainview.findViewById(R.id.imageButton);
        ib.setVisibility(View.GONE);
        TextView tv = mainview.findViewById(R.id.textview4);
        tv.setText(getResources().getText(R.string.mainquiz_h4));
        tv.setVisibility(View.VISIBLE);
        tv = mainview.findViewById(R.id.textview5);
        tv.setText(getResources().getText(R.string.mainquiz_h7));
        tv.setVisibility(View.VISIBLE);
        ProgressBar pr = mainview.findViewById(R.id.progressBar);
        pr.setVisibility(View.VISIBLE);
        try {
            String fln = quizplay.qud.getString(quizplay.itr);
            File fl = new File(universals.stpth + File.separator + (fln+"_pic.png"));
            if (fl.exists())
                upprog(100);
            else {
                network_res nr = new network_res();
                nr.mq = this;
                nr.execute("0", fln);
            }
        }
        catch (Exception e)
        {
            Log.e("mainquiz",e.toString());
        }

    }
    public void upprog(Integer val)
    {
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
    public void reserr(String inp)
    {
        if (inp.equals("ERR"))
        {
            ProgressBar pr = mainview.findViewById(R.id.progressBar);
            pr.setVisibility(View.GONE);
            ImageButton ib = mainview.findViewById(R.id.imageButton);
            ib.setVisibility(View.VISIBLE);
            String err = "An important image resource cannot be loaded due to connection error. Please reload the resource";
            TextView tv = mainview.findViewById(R.id.textview5);
            tv.setVisibility(View.GONE);
            tv = mainview.findViewById(R.id.textview4);
            tv.setText(err);


        }
    }
    private void resetelements()
    {
        TextView tv = mainview.findViewById(R.id.textview4);
        tv.setVisibility(View.GONE);
        tv = mainview.findViewById(R.id.textview5);
        tv.setVisibility(View.GONE);
        tv = mainview.findViewById(R.id.textview6);
        tv.setVisibility(View.GONE);
        ProgressBar pr = mainview.findViewById(R.id.progressBar);
        pr.setVisibility(View.GONE);
        ImageView iv = mainview.findViewById(R.id.quesimg);
        iv.setVisibility(View.GONE);
        iv = mainview.findViewById(R.id.imageView3);
        iv.setVisibility(View.GONE);
        EditText ed = mainview.findViewById(R.id.editText);
        ed.setText("");
        medprog = 100;
    }
    private void forward()
    {
        if (quizplay.itr<quizplay.utqu)
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
        Button gto = getActivity().findViewById(R.id.gotoques);
        gto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpques();
            }
        });
        ImageButton ib = mainview.findViewById(R.id.imageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadres();
            }
        });
    }
    private void loadans()
    {
        mq = this;
        network nt = new network();
        nt.nmq = mq;
        nt.execute("8");
    }

    public void retans(String inp, boolean typ)
    {
        try {
            quizplay.qans = new JSONArray(inp);
            checkans();
            if (typ)
                forward();
        }
        catch (Exception e)
        {
            Log.e("mainquiz",e.toString());
        }

    }
    private void checkans()
    {
        try {
            String an = quizplay.qans.getString(quizplay.itr);
            if (!an.equals(""))
            {
                TextView tv = mainview.findViewById(R.id.textview6);
                tv.setVisibility(View.VISIBLE);
                ImageView iv = mainview.findViewById(R.id.imageView3);
                iv.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            Log.e("mainquiz", e.toString());
        }

    }
    private void submit()
    {
        Button bt = mainview.findViewById(R.id.subbut);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText ed = mainview.findViewById(R.id.editText);
                    String ans = ed.getText().toString();
                    quizplay.qans.put(quizplay.itr, ans);
                    Log.i("JSON", String.valueOf(quizplay.qans));
                    network nt = new network();
                    nt.nmq = mq;
                    nt.execute("9", ans, String.valueOf(quizplay.itr));
                }
                catch (Exception e)
                {
                    Log.e("mainquiz",e.toString());
                }

            }
        });
    }
    private void jumpques()
    {
        EditText ed = getActivity().findViewById(R.id.editText2);
        String pos = ed.getText().toString();
        int val = Integer.parseInt(pos);
        if (val < quizplay.utqu && val > 0)
        {
            quizplay.itr = val-1;
            resetelements();
            pubques();
        }
    }
}
