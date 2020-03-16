package com.share.contrify.qfrat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class login_signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        //Toolbar myToolbar = findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.drawable.qfrat_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        //dr = findViewById(R.id.drawer_layout);

    }
    DrawerLayout dr;
    public void opendrawer(View v)
    {
        dr.openDrawer(GravityCompat.START);
    }
    public void login(View v)
    {
        EditText ed = findViewById(R.id.usrnme);
        String un = ed.getText().toString();
        ed = findViewById(R.id.usrpwd);
        String pw = ed.getText().toString();
        new network().execute("0",un,pw);
    }

    public void signup(View v)
    {
        Intent it = new Intent(login_signup.this,signup_act.class);
        startActivity(it);
    }
    public static void netret(String inp)
    {

    }
}
