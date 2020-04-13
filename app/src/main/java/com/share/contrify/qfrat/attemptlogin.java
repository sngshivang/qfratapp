package com.share.contrify.qfrat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.share.contrify.qfrat.universals.mGoogleSignInClient;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
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
    private View mainview;
    private int ri;
    private String scid = "867433872221-br6tp5c5i5e58eclsaga8mid8nbdlcbi.apps.googleusercontent.com";
    private ArrayList<String> gstr;
    private Handler task;
    private boolean isgoog;
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(scid)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
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
        if(universals.isgoogle)
        {
            signIn();

        }
        else {
            network nt = new network();
            nt.al = this;
            nt.execute("7", universals.email, universals.pwd);
        }

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
            network nt = new network();
            nt.al = this;
            nt.execute("11",personEmail, pid, token);

            // Signed in successfully, show authenticated UI
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
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
                ri = 3;
                task = new Handler(Looper.getMainLooper());
                task.postDelayed(newexp, 1000);
            }
            else{
                universals.sysfile2cr(getContext(),"nf", "nf", "nf", js.getString("sid"), false);
                universals.setdefs(getContext());
                //signout();
                if (cd  == 12)
                {
                    Navigation.findNavController(mainview).navigate(R.id.action_attemptlogin_to_sess_reset);

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
    private void signout()
    {
        mGoogleSignInClient.signOut();
        network nw = new network();
        nw.al = this;
        nw.execute("12");
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
            Navigation.findNavController(mainview).navigate(R.id.action_attemptlogin_to_testfragment);

        }
    }
    Runnable newexp = new Runnable() {
        @Override
        public void run() {
            testmod(ri--);
        }
    };
}
