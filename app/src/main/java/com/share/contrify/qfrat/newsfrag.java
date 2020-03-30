package com.share.contrify.qfrat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link newsfrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link newsfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newsfrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mainview;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public newsfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newsfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static newsfrag newInstance(String param1, String param2) {
        newsfrag fragment = new newsfrag();
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
        mainview = inflater.inflate(R.layout.fragment_newsfrag, container, false);
        setdefs();
        listeners();
        process();
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
    private void process()
    {
        try {
            int lpos = MainActivity.newspos;
            JSONObject ijs = new JSONObject(MainActivity.news.getString(lpos));
            JSONObject src = new JSONObject(ijs.getString("source"));
            String srcs = src.getString("name");
            String auth = ijs.getString("author");
            String title = ijs.getString("title");
            String descp = ijs.getString("description");
            String url = ijs.getString("url");
            String iurl = ijs.getString("urlToImage");
            String content = ijs.getString("content");
            String pub = ijs.getString("publishedAt");
            TextView tv;
            if (title!=null) {
                tv = mainview.findViewById(R.id.newstit);
                tv.setText(title);
            }
            if (auth!=null) {
                tv = mainview.findViewById(R.id.newsauth);
                tv.setText(auth);
            }
            if (pub!=null) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date ft = format.parse(pub);
                    tv = mainview.findViewById(R.id.newsdat);
                    tv.setText(ft.toString());
                }
                catch (Exception e)
                {
                    Log.e("newsfrag",e.toString());
                }
            }
            if (descp!=null) {
                tv = mainview.findViewById(R.id.newsdesc);
                tv.setText(descp);
            }
            if (url!=null) {
                Spanned txtur = Html.fromHtml("<a href = '" + url + "'>Click Here to read more on the news website!</a>");
                tv = mainview.findViewById(R.id.newsurl);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(txtur);
            }
            if (srcs!=null) {
                tv = mainview.findViewById(R.id.newsrc);
                tv.setText(srcs);
            }
            if (iurl!=null) {
                String fln = "newsfl" + lpos;
                loadres(iurl, fln);
            }


        }
        catch (Exception e)
        {
            Log.e("newsfrag",e.toString());
        }
    }
    private void loadres(String url, String fln)
    {
        ConstraintLayout cl = mainview.findViewById(R.id.imagecont);
        cl.setVisibility(View.VISIBLE);
        TextView tv = mainview.findViewById(R.id.textview4);
        tv.setText(getResources().getText(R.string.mainquiz_h4));
        tv.setVisibility(View.VISIBLE);
        tv = mainview.findViewById(R.id.textview5);
        tv.setText(getResources().getText(R.string.mainquiz_h7));
        tv.setVisibility(View.VISIBLE);
        ProgressBar pr = mainview.findViewById(R.id.progressBar);
        pr.setVisibility(View.VISIBLE);
        network_res nr = new network_res();
        nr.nf = this;
        nr.execute("1", url, fln);
    }
    public void upprog(Integer val, String fln)
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
                ImageView iv = mainview.findViewById(R.id.imageView4);
                Bitmap bmp = BitmapFactory.decodeFile(universals.stpth + File.separator + fln);
                int nh = (int) ( bmp.getHeight() * (1024.0 / bmp.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bmp, 1024, nh, true);
                iv.setImageBitmap(scaled);
                iv.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;
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
            ImageView iv = mainview.findViewById(R.id.imageView4);
            iv.setVisibility(View.GONE);
            ProgressBar pr = mainview.findViewById(R.id.progressBar);
            pr.setVisibility(View.INVISIBLE);
            ImageButton ib = mainview.findViewById(R.id.imageButton);
            ib.setVisibility(View.VISIBLE);
            String err = "A news resource cannot be loaded due to connection error. Please reload the resource";
            TextView tv = mainview.findViewById(R.id.textview5);
            tv.setVisibility(View.GONE);
            tv = mainview.findViewById(R.id.textview4);
            tv.setText(err);
            newssrcposmod();


        }
    }
    private void listeners()
    {
        ImageButton ib = mainview.findViewById(R.id.imageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process();
            }
        });
    }
    private void setdefs()
    {
        ImageView iv = mainview.findViewById(R.id.imageView4);
        iv.requestLayout();
        iv.setVisibility(View.GONE);
        ImageButton ib = mainview.findViewById(R.id.imageButton);
        ib.setVisibility(View.GONE);
        ProgressBar pr = mainview.findViewById(R.id.progressBar);
        pr.setVisibility(View.GONE);
        TextView tv = mainview.findViewById(R.id.textview5);
        tv.setVisibility(View.GONE);
        tv = mainview.findViewById(R.id.textview4);
        tv.setVisibility(View.GONE);
        ConstraintLayout cl = mainview.findViewById(R.id.imagecont);
        cl.setVisibility(View.GONE);
    }
    private void newssrcposmod()
    {
        TextView tv = mainview.findViewById(R.id.newsauth);
        tv.setTranslationY(0);
        ConstraintLayout cl = mainview.findViewById(R.id.content_frame);
        ConstraintSet ct = new ConstraintSet();
        ct.clone(cl);
        ct.connect(R.id.newstit,ConstraintSet.TOP,R.id.newsrc,ConstraintSet.BOTTOM,10);
        ct.applyTo(cl);
    }
}
