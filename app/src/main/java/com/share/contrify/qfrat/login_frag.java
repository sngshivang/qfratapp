package com.share.contrify.qfrat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.share.contrify.qfrat.universals.mGoogleSignInClient;


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
    private AlertDialog ad;
    private Context ct;
    private int reqc;
    private boolean isgoog;
    private ArrayList<String> gstr;
    private String scid = "867433872221-br6tp5c5i5e58eclsaga8mid8nbdlcbi.apps.googleusercontent.com";
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(scid)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        isgoog = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vv = inflater.inflate(R.layout.fragment_login_frag, container, false);
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
        Button bt = vv.findViewById(R.id.loginbut),bt2,bt4;
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
        Button bt3 = vv.findViewById(R.id.googsignin);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        bt4 = vv.findViewById(R.id.button2);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout();
            }
        });
        TextView tv = vv.findViewById(R.id.textview7);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniresetpwd();
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
        loadspindialog();
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1997);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1997) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String pid = account.getId();
            String token = account.getIdToken();
            gstr = new ArrayList<>();
            gstr.add(personName);
            gstr.add(personEmail);
            gstr.add(pid);
            gstr.add(token);
            isgoog = true;
            upwd = pid;
            loadspindialog();
            network nt = new network();
            nt.lf = this;
            nt.execute("11",personEmail, pid, token);

            // Signed in successfully, show authenticated UI
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void netret(String inp, login_frag lf)
    {
        if (ad!=null)
            ad.dismiss();
        if (inp.equals("ERR"))
        {
            comerr(null,null);
        }
        else {
            try {
                adb = new AlertDialog.Builder(getContext());
                JSONObject js = new JSONObject(inp);
                int cd = js.getInt("status");
                String alst = "";
                //AlertDialog.Builder ab = new AlertDialog.Builder(lf.getActivity());
                if (cd == 6) {
                    adb.setTitle("LOGIN SUCCESSFUL");
                    adb.setMessage("You will now be redirected the Main Page");
                    adb.show();
                    universals.sysfile2cr(lf.getContext(), js.getString("fname"), upwd, js.getString("email"), js.getString("sid"), isgoog);
                    universals.setdefs(lf.getContext());
                } else {
                    if (cd == 1) {
                        alst = "You are already logged in";
                    } else if (cd == 2) {
                        alst = "SQL Configuration fault! Contact admin at admin[at]qfrat.co.in";
                    } else if (cd == 3) {
                        alst = "This method of login is not allowed for this account";
                    } else if (cd == 4) {
                        if (isgoog)
                            vfcaptcha();
                        else
                            alst = "Incorrect Username or password";
                    } else if (cd == 5) {
                        alst = "Record fetch error for input! Contact admin at admin[at]qfrat.co.in";
                    } else if (cd == 7) {
                        if (isgoog)
                            vfcaptcha();
                        else
                            alst = "Username not found in record";
                    } else if (cd == 12) {
                        universals.sysfile2cr(lf.getContext(), "nf", "nf", "nf", js.getString("sid"), false);
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
            } catch (Exception e) {
                Log.e("login_frag", e.toString());
            }
        }
    }
    private void signout()
    {
        mGoogleSignInClient.signOut();
        network nw = new network();
        nw.lf = this;
        nw.execute("12");
    }
    public void retsignout(String inp)
    {
        Log.i("signout",inp);
        universals.sysfile2cr(getContext(),"nf", "nf", "nf", "nf", false);
        universals.setdefs(getContext());
    }
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
                            triggermoreinfo(userResponseToken);
                            Log.d("Captcha_RESP",userResponseToken);
                            // Validate the user response token using the
                            // reCAPTCHA siteverify API.
                            Log.d("Captcha_TAG", "VALIDATION STEP NEEDED");
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
    private void triggermoreinfo(final String gresp)
    {
        adb = new AlertDialog.Builder(getContext());
        LayoutInflater lf = getLayoutInflater();
        View view = lf.inflate(R.layout.google_add, null);
        final EditText emob= view.findViewById(R.id.editText);
        final EditText ecbr = view.findViewById(R.id.editText2);
        adb.setView(view);
        adb.setCancelable(false);
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                signout();

            }
        });
        adb.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mob = emob.getText().toString();
                String colbr = ecbr.getText().toString();
                gsignup(gresp, mob, colbr);

            }
        });
        adb.create();
        adb.show();
    }
    private void gsignup(String gpresp,String mob, String colbr)
    {
        network nk = new network();
        nk.lf = this;
        nk.execute("13",gstr.get(1),gstr.get(2),mob,gstr.get(0),colbr,gpresp);
    }
    private void iniresetpwd()
    {
        adb = new AlertDialog.Builder(getContext());
        LayoutInflater lf = getLayoutInflater();
        View rpwd = lf.inflate(R.layout.forgot_pass, null);
        final EditText ed = rpwd.findViewById(R.id.editText);
        Log.i("email",ed.getText().toString());
        adb.setView(rpwd);
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ad!=null)
                    ad.dismiss();

            }
        });
        adb.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String em = ed.getText().toString();
                sendresreq(em);

            }
        });
        ad = adb.create();
        ad = adb.show();


    }
    private void sendresreq(String em)
    {
        Log.i("email",em);
        if (ad!=null)
            ad.dismiss();
        loadspindialog();
        adb = new AlertDialog.Builder(getContext());
        network nt = new network();
        nt.lf= this;
        nt.execute("14", em);
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
    public void respwres(String inp)
    {
        ad.dismiss();
        if (inp.equals("ERR"))
        {
            comerr(null, null);
        }
        else
        {
            if (inp.equals("err:nf"))
            {
                String msg = "This email ID is not found in our records";
                comerr(null, msg);

            }
            else
            {
                String msg = "Password reset request has been sent to your email ID. Please follow the instructions in your mail to reset your password.\nIf you are in hurry, we highly recommend you to use Google Sign-IN instead";
                String tit = "SUCCESS";
                comerr(tit, msg);
            }
        }
    }
    public void comerr(String tit, String msg)
    {
        adb = new AlertDialog.Builder(getContext());
        if (tit!=null)
            adb.setTitle(tit);
        else
        adb.setTitle("ERROR!");
        if (msg!=null)
            adb.setMessage(msg);
        else
        adb.setMessage("There seems to be some problem connecting to the server. Please check your connectivity and try again...");
        adb.show();
    }
}
