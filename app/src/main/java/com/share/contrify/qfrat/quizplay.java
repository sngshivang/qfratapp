package com.share.contrify.qfrat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.google.android.material.bottomnavigation.BottomNavigationView.*;

public class quizplay extends AppCompatActivity implements quizlist.OnFragmentInteractionListener, ftf.OnFragmentInteractionListener, mainquiz.OnFragmentInteractionListener{
    private Toolbar mytoolbar;
    NavController navController;
    NavigationView navView;
    AppBarConfiguration apbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizplay);
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
        //Fragment fr = findViewById(R.id.nav_host_fragment);
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);
        /*toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.bottom_nav);

        toolbar.setTitle("Shop");*/
        //bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    /*private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    toolbar.setTitle("Shop");
                    return true;
                case R.id.navigation_gifts:
                    toolbar.setTitle("My Gifts");
                    return true;
                case R.id.navigation_cart:
                    toolbar.setTitle("Cart");
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };*/
    static int pos,itr, utme, utqu;
    static String ques;
    static JSONArray que,typ,res,qud,posi,neg,qans;
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    @Override
    public void sendtoparent(int u)
    {
        pos = u;
        Log.i("Current_pos", String.valueOf(u));
    }
    @Override
    public void fetchques(String inp)
    {
        ques = inp;
        splitproc();

    }
    @Override
    public void invokeNavMethods()
    {
        modfragheight(true);
        updbotnav();
    }
    @Override
    public void quizlistfrag()
    {
        modfragheight(false);
    }
    @Override
    public void ftffrag()
    {
        modfragheight(false);
    }
    private void splitproc()
    {
        try {
            JSONObject jsb = new JSONObject(ques);
            que = new JSONArray(jsb.getString("ques"));
            typ = new JSONArray(jsb.getString("typ"));
            res = new JSONArray(jsb.getString("res"));
            qud = new JSONArray(jsb.getString("qud"));
            posi = new JSONArray(jsb.getString("posi"));
            neg = new JSONArray(jsb.getString("neg"));
        }
        catch (Exception e)
        {
            Log.e("quizplay",e.toString());
        }
    }
    protected void updbotnav()
    {
        try {
            String tme = universals.arl.get(2).getString(universals.qlt.get(pos));
            String tqu =  universals.arl.get(3).getString(universals.qlt.get(pos));
            TextView tv = findViewById(R.id.textView);
            String mk = "/ "+tqu;
            tv.setText(mk);
            final TextView tv1 = findViewById( R.id.textView1);
            utme = Integer.parseInt(tme);
            utqu = Integer.parseInt(tqu);
            Log.i("timer",""+utme);
            new CountDownTimer((utme*60000), 1000) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    String set = String.format(Locale.getDefault(), "%d:%d",
                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    tv1.setText(set);
                }

                public void onFinish() {
                    String set = "END";
                    tv1.setText(set);
                }
            }.start();
        }
        catch (Exception e)
        {
            Log.e("quizplay",e.toString());
        }
    }
    @Override
    public void incrques()
    {
        EditText ed = findViewById(R.id.editText2);
        String mk = ""+(itr+1);
        ed.setText(mk);
    }
    public void modfragheight(boolean stat)
    {
        FragmentContainerView fr = findViewById(R.id.nav_host_fragment);
        ConstraintLayout.LayoutParams fl= (ConstraintLayout.LayoutParams)fr.getLayoutParams();
        final TypedArray styledAttributes = this.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int sz = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        Resources rs = this.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60,
                rs.getDisplayMetrics()
        );
        if (!stat)
        px = 0;
        else
        {
            ConstraintLayout cl = findViewById(R.id.cntrlbar);
            cl.setVisibility(View.VISIBLE);
        }
        fl.setMargins(0, sz, 0, px);
        fr.setLayoutParams(fl);
    }
}
