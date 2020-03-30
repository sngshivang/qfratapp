package com.share.contrify.qfrat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity implements signup_frag.OnFragmentInteractionListener, testfragment.OnFragmentInteractionListener, login_frag.OnFragmentInteractionListener, sess_reset.OnFragmentInteractionListener, attemptlogin.OnFragmentInteractionListener, newsfrag.OnFragmentInteractionListener, signout.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl = findViewById(R.id.testdraw);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        apbr = new AppBarConfiguration.Builder(navController.getGraph()).setDrawerLayout(dl).build();
         mytoolbar = findViewById(R.id.my_toolbar);
        mytoolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(mytoolbar);
        final ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);
        //ab.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.hamburg_icon));
        //getSupportActionBar().setHomeButtonEnabled(true);
        ab.setLogo(R.drawable.qfrat_logo);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        abdt = new ActionBarDrawerToggle(this,dl, R.string.ftf_b1, R.string.ftf_h2);
        //Toolbar toolbar = findViewById(R.id.my_toolbar);
        navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(mytoolbar, navController, apbr);
        /*mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG_TAG", "navigation clicked");
            }
        });*/
        if (!universals.name.equals("nf"))
        {
            /*NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.testfragment, true)
                    .build();
            navController.navigate(R.id.action_attemptlogin_to_testfragment, savedInstanceState, navOptions);*/
            /*NavGraph bg = navController.getGraph();
            bg.setStartDestination(R.id.attemptlogin);
            navController.setGraph(bg);*/
        }
        navhead();
        navdrawer(navView);

    }
    AppBarConfiguration apbr;
    Toolbar mytoolbar;
    DrawerLayout dl;
    NavController navController;
    NavigationView navView;
    ActionBarDrawerToggle abdt;
    static JSONArray news;
    static int newspos;
    /*@Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, apbr)
                || super.onSupportNavigateUp();
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                FragmentManager fr = this.getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                if (universals.name.equals("nf")) {
                    login_frag lf = new login_frag();
                    ft.replace(R.id.nav_host_fragment, lf);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else
                {
                    signout so = new signout();
                    ft.replace(R.id.nav_host_fragment, so);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                return  true;
                default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

    private void navhead()
    {
        View head = navView.getHeaderView(0);
        TextView tv = head.findViewById(R.id.textView4);
        String conc, em;
        if (universals.name==null || universals.name.equals("nf")) {
            conc = "Not Logged IN";
            em = "Not Available";
        }
        else{
        conc = "Welcome "+universals.name;
        em = universals.email;
        }
        tv.setText(conc);
        tv = head.findViewById(R.id.nav_urname);
        tv.setText(em);

    }
    @Override
    public void jumptonewsfrag()
    {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_testfragment_to_newsfrag);
    }
    private void navdrawer(NavigationView ngv)
    {
        ngv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.nav_news:
                                Log.i("drawer","News drawer clicked");
                                break;
                            case R.id.nav_qms:
                                Intent it = new Intent(MainActivity.this, quizplay.class);
                                startActivity(it);


                        }
                        return true;
                    }
                });
    }


}
