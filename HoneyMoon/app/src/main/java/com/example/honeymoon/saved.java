package com.example.honeymoon;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import adapters.effecy;
import adapters.gridadp;
import model.postmodel;
import utility.NetworkChangeListener;

public class saved extends AppCompatActivity {
    
    private List<String> lstid,lstusr;
    private List<postmodel> lstpdl;
    private TextView nn;
    private effecy cl;
    private SwipeRefreshLayout srl;
    private RecyclerView recyclerView;
    private gridadp adp ;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_saved);

        lstpdl=new ArrayList<>();
        lstusr=new ArrayList<>();
        lstid=new ArrayList<>();
        recyclerView=findViewById(R.id.r);
        srl=findViewById(R.id.srl);
        nn=findViewById(R.id.nn);
        adp = new gridadp(this, FirebaseAuth.getInstance().getCurrentUser().getUid(),lstpdl,false);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        getpsts();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getpsts();
                srl.setRefreshing(false);
            }
        });
    }
    public void getsaved(){
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstpdl.clear();
                List<postmodel> psdelst = new ArrayList<>();
                List<String> dalst = new ArrayList<>();
                for(int yu = 0;yu < lstid.size();yu++){
                    for(DataSnapshot dspp :snapshot.child(lstusr.get(yu)).child("posts").getChildren() ){
                        if(dspp.getKey().equals(lstid.get(yu))){
                            postmodel pdel = dspp.getValue(postmodel.class);
                            psdelst.add(pdel);
                            dalst.add(pdel.getTime());
                        }
                    }

                }

                for(String dat : effecy.instance.getdalst(dalst)){
                    for(postmodel pd : psdelst){
                        if(pd.getTime().equals(dat) && !lstpdl.contains(pd)){
                            lstpdl.add(pd);
                        }
                    }
                }
                Collections.reverse(lstpdl);
                if(lstpdl.isEmpty()){
                    nn.setVisibility(View.VISIBLE);
                }else{
                    nn.setVisibility(View.GONE);
                }

                adp.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
    public void getpsts(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("saved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstusr.clear();
                lstid.clear();

                for(DataSnapshot ds : snapshot.getChildren()){
                  lstusr.add((String) ds.getValue());
                  lstid.add(ds.getKey());
                }



                getsaved();
                
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
//        cl = new effecy(saved.this);
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