package com.share.contrify.qfrat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity implements signup_frag.OnFragmentInteractionListener, testfragment.OnFragmentInteractionListener, login_frag.OnFragmentInteractionListener, sess_reset.OnFragmentInteractionListener, attemptlogin.OnFragmentInteractionListener, newsfrag.OnFragmentInteractionListener, signout.OnFragmentInteractionListener, about.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dl = findViewById(R.id.testdraw);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        ng = navController.getGraph();
        apbr = new AppBarConfiguration.Builder(ng).setDrawerLayout(dl).build();
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
        inputintent();
        navdrawer(navView);
        triggerstuff();
        //layoutmanip2();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        //AdSize ads = new AdSize(320,50);
        //mAdView.setAdSize(ads);
        //mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111")
        // ;
        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
        catch (Exception e)
        {
            Log.e("ADERROR", e.toString());
        }
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded()
            {
                Log.i("AD","addloaded");
                layoutmanip2();
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("ADFAIL",String.valueOf(errorCode));
            }

        });

    }
    AppBarConfiguration apbr;
    Toolbar mytoolbar;
    DrawerLayout dl;
    NavController navController;
    NavigationView navView;
    Menu umenu;
    private AdView mAdView;
    NavGraph ng;
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
                inorout();
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
        umenu = menu;
        triggerstuff();
        return true;
    }
    @Override
    public void triggerstuff()
    {
        navhead();
        if (umenu!=null&&!universals.name.equals("nf"))
            umenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.profile_green));
        else if (umenu!=null)
            umenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.profile_white));
    }
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
                                break;
                            case R.id.nav_abt:
                                FragmentManager fm = getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                about ab = new about();
                                ft.add(R.id.nav_host_fragment, ab);
                                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                                ft.addToBackStack(null);
                                ft.commit();
                                dl.closeDrawers();
                                break;
                            case R.id.nav_log:
                                inorout();
                                dl.closeDrawers();
                                break;
                            case R.id.nav_so:
                                inorout();
                                dl.closeDrawers();
                                break;

                        }
                        return true;
                    }
                });
    }
    private void inputintent()
    {
        Intent it = getIntent();
        String opt = it.getStringExtra("OPT");
        try {
            NavDestination id = navController.getCurrentDestination();
            NavDestination md = ng.findNode(R.id.testfragment);
            while (id != md)
            {
                navController.navigateUp();
                id = navController.getCurrentDestination();
            }
        if (opt==null) {
            opt = "null";
            if (!universals.name.equals("nf"))
            {
                navController.navigate(R.id.action_testfragment_to_attemptlogin);
            }
        }
        Log.i("inputintent",opt);
        if (opt==null)
            opt="";
        if (opt.equals("3"))
        {
            if (!universals.name.equals("nf"))
                navController.navigate(R.id.action_testfragment_to_signout2);
            else
            navController.navigate(R.id.action_testfragment_to_login_frag);

        }
        }
        catch (Exception e)
        {
            Log.e("inorout",e.toString());
        }
    }
    private void inorout()
    {
        try {
            NavDestination id = navController.getCurrentDestination();
            NavDestination md = ng.findNode(R.id.testfragment);
            //NavDestination nd = navController.
            while (id != md)
            {
                navController.popBackStack();
                id = navController.getCurrentDestination();
            }

            if (!universals.name.equals("nf"))
            navController.navigate(R.id.action_testfragment_to_signout2);
        else
            navController.navigate(R.id.action_testfragment_to_login_frag);
        }
        catch (Exception e)
        {
            Log.e("inorout",e.toString());
        }
    }
    public void layoutmanip2()
    {
        final ConstraintLayout dl = findViewById(R.id.content_frame);
        dl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()  {
            @Override
            public void onGlobalLayout() {
                int hei = dl.getMeasuredHeight();
                //dl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                final TypedArray styledAttributes = MainActivity.this.getTheme().obtainStyledAttributes(
                        new int[]{android.R.attr.actionBarSize});
                final int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
                styledAttributes.recycle();
                int ad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                final int net = hei - mActionBarSize - ad;
                final FragmentContainerView ft = findViewById(R.id.nav_host_fragment);
                ft.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()  {
                    @Override
                    public void onGlobalLayout() {
                        int hei = ft.getHeight();
                        ft.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ConstraintLayout.LayoutParams fl = (ConstraintLayout.LayoutParams) ft.getLayoutParams();
                        fl.height = net;
                        ft.setLayoutParams(fl);
                    }});


            }
        });
    }





}
