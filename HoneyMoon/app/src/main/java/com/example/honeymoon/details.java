package com.example.honeymoon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import adapters.fragadp;
import fragment.docs;
import fragment.links;
import fragment.media;
import model.usermodel;
import utility.NetworkChangeListener;


//displaying userinfo
public class details extends AppCompatActivity {
    private String userid;
    private ImageView profile,bcmi,annofri,annocru,annoadm;
    private Button usernme,stats;
    private TextView line;
    private Button mute,block;
    private TabLayout tb;
    private ViewPager vp;
    private fragadp adp;
    private List<Fragment> lstfrag;
    private List<String> lstttl;
    private effecy cl;
    private SwipeRefreshLayout srl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_details);


        Intent inte = getIntent();
        userid = inte.getStringExtra("userid");

        lstfrag=new ArrayList<>();
        lstttl=new ArrayList<>();

        profile=findViewById(R.id.cfg);
        bcmi=findViewById(R.id.bcim);
        usernme=findViewById(R.id.deusrnme);
        stats=findViewById(R.id.destats);
        line=findViewById(R.id.line);
        mute=findViewById(R.id.mute);
        block=findViewById(R.id.block);
        srl=findViewById(R.id.srl);

        annocru = findViewById(R.id.cru);
        annofri=findViewById(R.id.fri);
        annoadm=findViewById(R.id.adm);
        effecy anno = new effecy(details.this,userid,annocru,annoadm,annofri);

        tb=findViewById(R.id.tbbb);
        vp=findViewById(R.id.vvpag);

        lstfrag.add(new media(userid));
        lstfrag.add(new docs(userid));
        lstfrag.add(new links(userid));

        lstttl.add("Media");
        lstttl.add("Docs");
        lstttl.add("Links");

        getuser();

        blck();
        effecy sen = new effecy(userid,bcmi);
        adp=new fragadp(getSupportFragmentManager(),lstfrag,lstttl);
        vp.setAdapter(adp);
        tb.setupWithViewPager(vp);

//        cl=new effecy(details.this);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getuser();
                srl.setRefreshing(false);
            }
        });
        usernme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernme.setEnabled(false);
                Intent inte = new Intent(details.this,Mainactivity.class);
                inte.putExtra("userid",userid);
                startActivity(inte);
                usernme.setEnabled(true);
            }
        });
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                block.setEnabled(false);
                if (block.getTag().equals("block")){

                    AlertDialog.Builder alt = new AlertDialog.Builder(details.this);
                    alt.setTitle("Block Contact!").setMessage("Do you want to Block \n"+usernme.getText().toString())
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("blocked").child(userid).setValue("true");
                                }
                            }).show();

                }else {
                    AlertDialog.Builder alt = new AlertDialog.Builder(details.this);
                    alt.setTitle("Unblock Contact!").setMessage("Do you want to UnBlock \n"+usernme.getText().toString())
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("blocked").child(userid).removeValue();
                                }
                            }).show();

                }
                block.setEnabled(true);
            }
        });
    }
    public void blck(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("blocked")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(userid).exists()) {
                            block.setText("Unblock Contact");
                            block.setTag("blocked");
                        } else {
                            block.setText("Block Contact");
                            block.setTag("block");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getuser(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel currdel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(usermodel.class);
                for (DataSnapshot dss : snapshot.getChildren()){
                    if(dss.getKey().equals(userid)){
                        usermodel umel = dss.getValue(usermodel.class);
                        Picasso.get().load(umel.getImageurl()).into(profile);
                        usernme.setText(umel.getUsername());
                        stats.setText(umel.getName());
                        effecy.instance.onlinesta(userid,umel.getShowlastseen(),line,true);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
//        cl = new effecy(details.this);
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