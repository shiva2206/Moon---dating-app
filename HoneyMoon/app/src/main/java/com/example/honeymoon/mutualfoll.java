package com.example.honeymoon;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import adapters.fragadp;
import fragment.foladmcrufrimat;
import utility.NetworkChangeListener;

public class mutualfoll extends AppCompatActivity {

    private FragmentManager chmag;
    private String userid,usergende,currgender,currfri;
    private List<Fragment> fraglst;
    private fragadp adp;
    private TextView mutal,nobu;
    private ViewPager vpag;
    private TabLayout tb;
    private TextView swlr;
    private effecy cl;
    private SwipeRefreshLayout srl;
    private List<String> titlst;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_mutualfoll);


        vpag = findViewById(R.id.vpag);
        tb=findViewById(R.id.tb);
        swlr=findViewById(R.id.swlr);
        nobu=findViewById(R.id.nobu);
        mutal=findViewById(R.id.mutual);
        srl=findViewById(R.id.srl);

        currgender = mutualfoll.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender",null);
        if (currgender.equals("male")){
            currfri = "gfs";
        }else if (currgender.equals("female")){
            currfri="bfs";
        }else{

        }

        userid = getIntent().getStringExtra("userid");
        usergende = getIntent().getStringExtra("gender");
        chmag=getSupportFragmentManager();

        setagain();

        swlr.setText("<--- Swipe");
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setagain();
                srl.setRefreshing(false);
            }
        });
        vpag.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mutal.setText("Mutual "+adp.getPageTitle(position));
                if (position == 0){
                    swlr.setText("<--- Swipe");
                }else if (position == 4){
                    swlr.setText("Swipe --->");
                }else{
                    swlr.setText("<--- Swipe --->");
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void setagain(){
        fraglst=new ArrayList<>();
        titlst=new ArrayList<>();

        fraglst.add(new foladmcrufrimat(mutualfoll.this,userid,"followings",nobu));
        fraglst.add(new foladmcrufrimat(mutualfoll.this,userid,"followers",nobu));
        fraglst.add(new foladmcrufrimat(mutualfoll.this,userid,"matches",nobu));
        fraglst.add(new foladmcrufrimat(mutualfoll.this,userid,"admirers",nobu));
        fraglst.add(new foladmcrufrimat(mutualfoll.this,userid,"crushs",nobu));

        titlst.add("FOLLOWINGS");
        titlst.add("FOLLOWERS");
        titlst.add("MATCHES");
        titlst.add("ADMIRERS");
        titlst.add("CRUSHS");

        if(currgender.equals(usergende)){
            fraglst.add(new foladmcrufrimat(mutualfoll.this,userid,currfri,nobu));
            titlst.add(currfri.toUpperCase());
        }


        adp = new fragadp(chmag,fraglst,titlst);
        vpag.setAdapter(adp);
        tb.setupWithViewPager(vpag);
    }

    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
//        cl = new effecy(mutualfoll.this);
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue(effecy.instance.gettime());
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }

}