package com.share.contrify.qfrat;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link signup_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link signup_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signup_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View vw;
    private AlertDialog.Builder adb;
    private AlertDialog ad;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public signup_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static signup_frag newInstance(String param1, String param2) {
        signup_frag fragment = new signup_frag();
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
        vw = inflater.inflate(R.layout.fragment_signup_frag,
                container, false);
        Button button = vw.findViewById(R.id.signupbut);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                vfcaptcha();
            }
        });
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
    private String vlresp;
    public void vfcaptcha()
    {
        SafetyNet.getClient(getActivity()).verifyWithRecaptcha("6Ld0G9kUAAAAAH1UNaPF4E5YFAroLPfzNtLj0rgj")
                .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                        // Indicates communication with reCAPTCHA service was
                        // successful.
                        String userResponseToken = recaptchaTokenResponse.getTokenResult();
                        if (!userResponseToken.isEmpty()) {
                            vlresp = userResponseToken;
                            Log.d("Captcha_RESP",userResponseToken);
                            // Validate the user response token using the
                            // reCAPTCHA siteverify API.
                            Log.d("Captcha_TAG", "VALIDATION STEP NEEDED");
                            signup();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.e("Captcha_TAG", "Error: " + CommonStatusCodes
                                    .getStatusCodeString(statusCode));
                        } else {
                            // A different, unknown type of error occurred.
                            Log.e("CAPTCHA_TAG", "Error: " + e.getMessage());
                        }
                    }
                });
    }
    private void signup()
    {

        EditText ed = vw.findViewById(R.id.usrnme);
        String un = ed.getText().toString();
        ed = vw.findViewById(R.id.usrpwd);
        String pw = ed.getText().toString();
        ed = vw.findViewById(R.id.usrflnme);
        String nm = ed.getText().toString();
        ed = vw.findViewById(R.id.usrmob);
        String mob = ed.getText().toString();
        ed = vw.findViewById(R.id.usrrepwd);
        String repwd = ed.getText().toString();
        ed = vw.findViewById(R.id.usrcolyr);
        String add = ed.getText().toString();
        if (pw.equals(repwd))
        {
            loadspindialog();
            network nt = new network();
            nt.sf = this;
            nt.execute("1",un,pw,mob,nm,add,vlresp);
        }
        else
        {
            adb = new AlertDialog.Builder(getContext());
            adb.setTitle("ERROR");
            adb.setMessage("Please make sure that passwords match");
            adb.show();
        }
    }

    private void loadspindialog()
    {
        adb = new AlertDialog.Builder(getContext());
        LayoutInflater lf = getLayoutInflater();
        View adbview = lf.inflate(R.layout.alert_spin2,null);
        TextView tv = adbview.findViewById(R.id.headmsg);
        String str = "Please wait while we communicate with the server...";
        tv.setText(str);
        adb.setView(adbview);
        ad = adb.create();
        ad = adb.show();
    }
    public void retcall(String inp)
    {
        if (ad!=null)
            ad.dismiss();
        adb = new AlertDialog.Builder(getContext());
        if (inp.equals("ERR"))
        {
            adb.setTitle("ERROR");
            adb.setMessage("There seems to be a communication issue. Please ensure your connectivity is ensured and try again.");
            adb.show();

        }
        else
        {
            if (inp.equals("6"))
            {
                adb.setTitle("SUCCESS");
                adb.setMessage("You have successfully registered this user account. Please login using this same information");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(vw).navigate(R.id.action_signup_frag_to_testfragment);
                    }
                });
                adb.setCancelable(false);
                adb.create();
                adb.show();
            }
            else
            {
                adb.setTitle("ERROR");
                if (inp.equals("9"))
                    adb.setMessage("E-Mail id is already registered! Please try a different email");
                else if (inp.equals("2"))
                    adb.setMessage("SQL Configuration fault! Contact admin at admin[at]qfrat.co.in");
                else if (inp.equals("8"))
                    adb.setMessage("Password do not match");
                else if (inp.equals("10"))
                    adb.setMessage("Captcha Authentication Error! Prove that you are a human or refresh the page");
                adb.show();

            }
        }

    }
}
