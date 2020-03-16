package com.share.contrify.qfrat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity implements signup_frag.OnFragmentInteractionListener, testfragment.OnFragmentInteractionListener, login_frag.OnFragmentInteractionListener, sess_reset.OnFragmentInteractionListener, attemptlogin.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mytoolbar = findViewById(R.id.my_toolbar);
        //mytoolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(mytoolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.hamburg_icon));
        //getSupportActionBar().setHomeButtonEnabled(true);
        ab.setLogo(R.drawable.qfrat_logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        DrawerLayout dl = findViewById(R.id.testdraw);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       apbr = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(dl).build();
        //Toolbar toolbar = findViewById(R.id.my_toolbar);
        navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(mytoolbar, navController);
        NavigationUI.setupWithNavController(navView, navController);
        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG_TAG", "navigation clicked");
            }
        });
        if (!universals.name.equals("nf"))
        {
            /*NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.testfragment, true)
                    .build();
            navController.navigate(R.id.action_attemptlogin_to_testfragment, savedInstanceState, navOptions);*/
            NavGraph bg = navController.getGraph();
            bg.setStartDestination(R.id.attemptlogin);
            navController.setGraph(bg);
        }
        navhead();

    }
    AppBarConfiguration apbr;
    Toolbar mytoolbar;
    NavController navController;
    NavigationView navView;
    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, apbr)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void loginact(View v)
    {
        Intent it = new Intent(MainActivity.this, login_signup.class);
        startActivity(it);
    }
    private void navhead()
    {
        View head = navView.getHeaderView(0);
        TextView tv = head.findViewById(R.id.textView4);
        String conc = "Welcome "+universals.name;
        tv.setText(conc);
        tv = head.findViewById(R.id.nav_urname);
        tv.setText(universals.email);

    }


}
