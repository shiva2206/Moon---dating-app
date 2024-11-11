package com.example.honeymoon;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import adapters.effecy;
import model.usermodel;
import utility.NetworkChangeListener;

public class settings extends AppCompatActivity {
    private SwitchCompat lstsce,priva,loc,showcrubut;
    private Button dele,blckc;
    private RadioGroup mat,adm,cru,fri;
    private TextView soci;
    private usermodel settmodel;
    private effecy cl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);

        lstsce =findViewById(R.id.shon);
        priva=findViewById(R.id.priacc);
        loc=findViewById(R.id.loc);
        mat=findViewById(R.id.showmat);
        cru=findViewById(R.id.showcru);
        fri=findViewById(R.id.showfri);
        adm=findViewById(R.id.showadm);
        dele=findViewById(R.id.delete);
        soci=findViewById(R.id.funpa);
        blckc=findViewById(R.id.blcans);
        showcrubut=findViewById(R.id.showadcru);




        settmodel=new usermodel(
                getIntent().getStringExtra("status"),
                getIntent().getStringExtra("mode"),
                getIntent().getStringExtra("showlastseen"),
                getIntent().getStringExtra("showlocation"),
                getIntent().getStringExtra("showmat"),

                getIntent().getStringExtra("showcru"),
                getIntent().getStringExtra("showadm"),
                getIntent().getStringExtra("showfri"),
                getIntent().getStringExtra("showaddcrubut")

//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("status","none"),
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("mode","none"),
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("showlastseen","none"),
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("showlocation","none"),
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("showmat","none"),
//
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("showcru","none"),
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("showadm","none"),
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("showfri","none"),
//                settings.this.getSharedPreferences("profile", MODE_PRIVATE).getString("showaddcrubut","none")

        );
        sete();
        showcrubut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcrubut.setEnabled(false);
                if(showcrubut.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showaddcrubut").setValue("true");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showaddcrubut","true").commit();

                }else {
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showaddcrubut").setValue("false");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showaddcrubut","false").commit();

                }
                showcrubut.setEnabled(true);
//                sete();
            }
        });
        blckc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blckc.setEnabled(false);
                startActivity(new Intent(settings.this, blockedconts.class));
                blckc.setEnabled(true);
            }
        });
        mat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.sall:
                        wht = "all";
                        break;
                    case R.id.son:
                        wht="number";
                        break;
                    case R.id.lck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showmat").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showmat",wht).commit();
//                sete();
            }
        });
        cru.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.csall:
                        wht = "all";
                        break;
                    case R.id.cson:
                        wht="number";
                        break;
                    case R.id.clck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showcru").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showcru",wht).commit();
//                sete();
            }
        });
        adm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.asall:
                        wht = "all";
                        break;
                    case R.id.ason:
                        wht="number";
                        break;
                    case R.id.aclck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showadm").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showadm",wht).commit();
//                sete();
            }
        });
        fri.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.fall:
                        wht = "all";
                        break;
                    case R.id.fson:
                        wht="number";
                        break;
                    case R.id.fclck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showfri").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showfri",wht).commit();
//                sete();
            }
        });

        priva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priva.setEnabled(false);
                if(!priva.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("mode").setValue("public");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("mode","public").commit();

                }else{
                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("notify").child("requests").child("follows").removeValue();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("mode").setValue("private");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("mode","private").commit();

                }
                priva.setEnabled(true);
//                sete();
            }
        });
        lstsce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstsce.setEnabled(false);
                if(lstsce.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlastseen").setValue("true");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlastseen","true").commit();

                }else{
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlastseen").setValue("false");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlastseen","false").commit();

                }
//                sete();
                lstsce.setEnabled(true);
            }
        });
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loc.setEnabled(false);
                if(loc.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlocation").setValue("true");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlocation","true").commit();


                }else{
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlocation").setValue("false");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlocation","false").commit();

                }
                loc.setEnabled(true);
//                sete();
            }
        });




    }


    public void sete() {

        System.out.println(settmodel.toString());
        if(settmodel.getShowaddcrubut().equals("true")){
            showcrubut.setChecked(true);
        }else {
            showcrubut.setChecked(false);
        }
        if(settmodel.getShowmat().equals("all")){
            mat.check(R.id.sall);
        }else if (settmodel.getShowmat().equals("number")){
            mat.check(R.id.son);
        }else {
            mat.check(R.id.lck);
        }

        if(settmodel.getShowcru().equals("all")){
            cru.check(R.id.csall);
        }else if (settmodel.getShowmat().equals("number")){
            cru.check(R.id.cson);
        }else {
            cru.check(R.id.clck);
        }

        if(settmodel.getShowadm().equals("all")){
            adm.check(R.id.asall);
        }else if (settmodel.getShowmat().equals("number")){
            adm.check(R.id.ason);
        }else {
            adm.check(R.id.aclck);
        }

        if(settmodel.getShowfri().equals("all")){
            fri.check(R.id.fall);
        }else if (settmodel.getShowmat().equals("number")){
            fri.check(R.id.fson);
        }else {
            fri.check(R.id.fclck);
        }

    }
    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
//        cl = new effecy(settings.this);
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