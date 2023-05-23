package com.example.honeymoon;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import adapters.effecy;
import fragment.search;
import utility.NetworkChangeListener;

public class admirers extends AppCompatActivity {
    private String userid;
    private Fragment fra;
    private effecy cl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

//    private InterstitialAd adview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_admirers);


        Intent inte = getIntent();
        userid = inte.getStringExtra("userid");

//        cl = new effecy(admirers.this);
        fra=new search(false,userid,"admirers");
        getSupportFragmentManager().beginTransaction().replace(R.id.fra,fra).commitAllowingStateLoss();

//        MobileAds.initialize(this,"ca-app-pub-9694169298661099~6980277796");

//        adview = new InterstitialAd(this);
//        adview.setAdUnitId("ca-app-pub-9694169298661099/6745431934");
//        adview.loadAd(new AdRequest.Builder().build());
//        adview.show();

    }
    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

//        cl = new effecy(admirers.this);
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