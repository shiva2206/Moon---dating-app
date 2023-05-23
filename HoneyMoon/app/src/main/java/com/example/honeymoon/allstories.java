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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.dateadap;
import adapters.effecy;
import model.statusmodel;
import utility.NetworkChangeListener;

public class allstories extends AppCompatActivity {

    private RecyclerView recy ;
    private List<String> dalst;
    private Button next;
    private dateadap adp;
    private TextView nn;
    private List<statusmodel> selectedstadel;
    private String highid;
    private effecy cl;
    private SwipeRefreshLayout srl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_allstories);

        next=findViewById(R.id.nex);
        nn=findViewById(R.id.nn);
        srl=findViewById(R.id.srl);
        dalst=new ArrayList<>();
        recy=findViewById(R.id.gr);
        adp=new dateadap(this,dalst,"allstories");
        recy.setAdapter(adp);
        recy.setLayoutManager(new LinearLayoutManager(this));
        getstst();
//        cl=new effecy(allstories.this);
        highid = getIntent().getStringExtra("highid");

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getstst();
                srl.setRefreshing(false);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                next.setEnabled(false);
                if(highid == null) {
                    if (adp.getSelectedstadel() != null && !adp.getSelectedstadel().isEmpty()) {
                        Intent iny = new Intent(allstories.this, addhighlight.class);
                        selectedstadel = adp.getSelectedstadel();
                        for (statusmodel sdel : selectedstadel) {
                            iny.putExtra(sdel.getStatusid(), sdel.getTime());
//                            Toast.makeText(allstories.this, sdel.getStatusid(), Toast.LENGTH_SHORT).show();
                        }
                        iny.putExtra("from","add");
                        startActivity(iny);
                    } else {
                        Toast.makeText(allstories.this, "Long Press any Story To Select and Click NEXT", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (adp.getSelectedstadel() != null && !adp.getSelectedstadel().isEmpty()) {
                        selectedstadel = adp.getSelectedstadel();
                        for(statusmodel sdel : selectedstadel) {
                            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                    .child(highid).child("statusmodelist").child(sdel.getStatusid()).setValue(sdel.getTime().substring(0,10));
                            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                    .child(highid).child("time").setValue(effecy.instance.gettime());
                        }
                        Toast.makeText(allstories.this, "Sucessfully added", Toast.LENGTH_SHORT).show();
                        Intent intet = new Intent(allstories.this,addhighlight.class);
                        intet.putExtra("highid",highid);
                        intet.putExtra("from","edit");
                        startActivity(intet);
                        finish();

                    }
                }
                next.setEnabled(true);
            }
        });


    }
    public void getstst(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("allstories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dalst.clear();
                List<String> undalst = new ArrayList<>();
                for(DataSnapshot dsh : snapshot.getChildren()){
                    undalst.add(dsh.getKey()+" 00:00:00");
                }
                for(String datr :effecy.instance.getdalst(undalst)){
                    dalst.add(datr.substring(0,10));
                };
                Collections.reverse(dalst);
//                Toast.makeText(allstories.this, dalst.size()+"", Toast.LENGTH_SHORT).show();
                adp.notifyDataSetChanged();
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

//        cl = new effecy(allstories.this);
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