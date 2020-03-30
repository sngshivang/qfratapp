package com.share.contrify.qfrat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.google.android.material.bottomnavigation.BottomNavigationView.*;

public class quizplay extends AppCompatActivity implements quizlist.OnFragmentInteractionListener, ftf.OnFragmentInteractionListener, mainquiz.OnFragmentInteractionListener{
    private Toolbar mytoolbar;
    NavController navController;
    NavigationView navView;
    DrawerLayout dl;
    AppBarConfiguration apbr;
    CountDownTimer cdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizplay);
        dl = findViewById(R.id.quizdraw);
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
        //Toolbar toolbar = findViewById(R.id.my_toolbar);
        navView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupWithNavController(mytoolbar, navController, apbr);
        navhead();
        navdrawer(navView);
        //Fragment fr = findViewById(R.id.nav_host_fragment);
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);
        /*toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.bottom_nav);

        toolbar.setTitle("Shop");*/
        //bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    static int pos,itr, utme, utqu;
    static String ques;
    static JSONArray que,typ,res,qud,posi,neg,qans;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                FragmentManager fr = this.getSupportFragmentManager();
                FragmentTransaction ft = fr.beginTransaction();
                login_frag lf = new login_frag();
                ft.replace(R.id.nav_host_fragment, lf);
                ft.commit();
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
            String cdatm = universals.arl.get(4).getString(universals.qlt.get(pos));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sdf.parse(cdatm);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(Calendar.MINUTE, Integer.parseInt(tme));
            String newTime = sdf.format(cal.getTime());
            Date dt2 = sdf.parse(newTime);
            Date curdat = new Date();
            long diff = dt2.getTime()-curdat.getTime();
            Log.e("Total_mil", String.valueOf(diff));
            TextView tv = findViewById(R.id.textView);
            String mk = "/ "+tqu;
            tv.setText(mk);
            final TextView tv1 = findViewById( R.id.textView1);
            utme = Integer.parseInt(tme);
            utqu = Integer.parseInt(tqu);
            Log.i("timer",""+utme);
            if (cdt!=null)
                cdt.cancel();
            cdt = new CountDownTimer(diff, 1000) { // adjust the milli seconds here

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
                                Intent it = new Intent(quizplay.this, quizplay.class);
                                startActivity(it);


                        }
                        return true;
                    }
                });
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
