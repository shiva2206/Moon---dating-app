package com.example.honeymoon;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.effecy;
import adapters.seenadp;
import adapters.view_seeadp;
import model.statusmodel;
import utility.NetworkChangeListener;

public class seen extends AppCompatActivity {

    private ViewPager vpag;
    private view_seeadp vadp;
    private RecyclerView seenrecy;
    private List<statusmodel> stalst;
    private List<String> usrlst,tmelst;
    private seenadp senadp ;
    private Button noviews;
    private String purpose;
    private effecy cl;
    private TextView nn;
    private DatabaseReference fdb;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_seen);

        Intent inte = getIntent();
//        int posi=Integer.parseInt(inte.getStringExtra("position"));
        purpose = inte.getStringExtra("purpose");

        stalst=new ArrayList<>();

        usrlst=new ArrayList<>();
        tmelst=new ArrayList<>();
        vpag=findViewById(R.id.vpger);

        nn=findViewById(R.id.nn);
        noviews=findViewById(R.id.noviews);
        senadp=new seenadp(this,usrlst,tmelst,"seen");
        vadp=new view_seeadp(this,stalst);

        vpag.setAdapter(vadp);

        seenrecy=findViewById(R.id.senrecy);
        seenrecy.setAdapter(senadp);
        seenrecy.setLayoutManager(new LinearLayoutManager(this));

        if(purpose.equals("story")) {
            fdb=FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("story");

        }else{

            fdb=FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("allstories")
                    .child(inte.getStringExtra("date"));

        }
        gets();

//        vpag.setCurrentItem(posi,false);
        vpag.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getviews(position);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



//        Toast.makeText(this, String.valueOf(posi), Toast.LENGTH_SHORT).show();

    }
    public void getviews(int posi){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("allstories").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dss : snapshot.getChildren()){
                    if(snapshot.child(dss.getKey()).child(stalst.get(posi).getStatusid()).exists()){
                        statusmodel sta = snapshot.child(dss.getKey()).child(stalst.get(posi).getStatusid()).getValue(statusmodel.class);

                        SimpleDateFormat formatter6=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        try {
                            Calendar pstdate = Calendar.getInstance();
                            Calendar nxtdate = Calendar.getInstance();

                            String ddd = effecy.instance.gettime();

                            pstdate.setTime(formatter6.parse(sta.getTime()));
                            nxtdate.setTime(formatter6.parse(ddd));
                            Calendar or = pstdate;
                            pstdate.add(Calendar.MINUTE, 60*24*2);// number of days to add
                            Toast.makeText(seen.this, ddd, Toast.LENGTH_SHORT).show();
                            if(pstdate.getTime().before(nxtdate.getTime())) {
                                FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("seen").child(sta.getStatusid()).removeValue();

                                nn.setText("Views are not Available After 48 hrs");
                                nn.setVisibility(View.VISIBLE);


                            }else{

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("seen").child(stalst.get(posi).getStatusid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usrlst.clear();
                tmelst.clear();
                noviews.setText("Views "+snapshot.getChildrenCount());


                for (DataSnapshot ds : snapshot.getChildren()) {
                      usrlst.add(ds.getKey());
                      tmelst.add(ds.getValue()+"");
                }
                senadp.notifyDataSetChanged();
                if( snapshot.getChildrenCount() == 0 && nn.getVisibility() == View.INVISIBLE){
                    nn.setText("None");
                    nn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void gets(){
        fdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stalst.clear();

                for(DataSnapshot dss : snapshot.getChildren()) {
                    statusmodel stg = dss.getValue(statusmodel.class);
                    stalst.add(stg);
                }
                vadp.notifyDataSetChanged();


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