package com.share.contrify.qfrat;

import androidx.annotation.NonNull;
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

public class quizplay extends AppCompatActivity implements quizlist.OnFragmentInteractionListener, ftf.OnFragmentInteractionListener, mainquiz.OnFragmentInteractionListener, about.OnFragmentInteractionListener, quiz_conc.OnFragmentInteractionListener{
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
        navdrawer(navView);
        navhead();
    }

    static int pos,itr, utme, utqu;
    static String ques;
    static JSONArray que,typ,res,qud,posi,neg,qans;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!universals.name.equals("nf"))
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.profile_green));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.profile_white));
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
        navigatetomain("3");
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
                    gotoconc();
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
                52,
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
                                navigatetomain("1");
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
                                navigatetomain("3");
                                break;
                            case R.id.nav_so:
                                navigatetomain("3");
                                break;


                        }
                        return true;
                    }
                });
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
    private void navigatetomain(String val)
    {
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("OPT", val);
        startActivity(it);
    }
    private void gotoconc()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        quiz_conc qc = new quiz_conc();
        ft.add(R.id.nav_host_fragment, qc);
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miProfile:
                navigatetomain("3");
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
