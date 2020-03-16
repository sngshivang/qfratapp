package com.share.contrify.qfrat;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class signup_act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_act);
        startexc();
    }
    AlertDialog.Builder ab;
    private String vlresp;
    private void startexc()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.drawable.qfrat_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ab = new AlertDialog.Builder(this);
    }
    public void vfcaptcha(View v)
    {
        SafetyNet.getClient(this).verifyWithRecaptcha("6Ld0G9kUAAAAAH1UNaPF4E5YFAroLPfzNtLj0rgj")
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
    public void signup(View v)
    {
        EditText ed = findViewById(R.id.usrnme);
        String un = ed.getText().toString();
        ed = findViewById(R.id.usrpwd);
        String pw = ed.getText().toString();
        ed = findViewById(R.id.usrflnme);
        String nm = ed.getText().toString();
        ed = findViewById(R.id.usrmob);
        String mob = ed.getText().toString();
        ed = findViewById(R.id.usrrepwd);
        String repwd = ed.getText().toString();
        ed = findViewById(R.id.usrcolyr);
        String add = ed.getText().toString();
        if (pw.equals(repwd))
        {
            new network().execute("1",un,pw,mob,nm,add,vlresp);


        }
    }
}
