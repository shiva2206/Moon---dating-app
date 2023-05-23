package com.example.honeymoon;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import adapters.seenadp;
import model.keymodel;
import utility.NetworkChangeListener;

public class likes extends AppCompatActivity {

    private List<String> usrlst, tmelst, ordusrlst, ordtmelst;
    private RecyclerView recy;
    private seenadp adp;
    private Button lkb;
    private effecy cl;
    private EditText etx;
    private TextView nn;
    private SwipeRefreshLayout srl;
    private String postid, publisherid, commentid;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private Query que;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_likes);

        recy = findViewById(R.id.ereecy);
        nn = findViewById(R.id.nn);
        srl = findViewById(R.id.srl);
        lkb = findViewById(R.id.lkb);
        etx = findViewById(R.id.etx);
        usrlst = new ArrayList<>();
        tmelst = new ArrayList<>();
        ordusrlst = new ArrayList<>();
        ordtmelst = new ArrayList<>();

        Intent iny = getIntent();
        postid = iny.getStringExtra("postid");
        publisherid = iny.getStringExtra("publisherid");
        commentid = iny.getStringExtra("commentid");


        adp = new seenadp(this, ordusrlst, ordtmelst, "likes");
        recy.setAdapter(adp);
        recy.setLayoutManager(new LinearLayoutManager(this));

        if (commentid == null) {
            getpostlik();
        } else {
            getcommlik();
        }
        toquery(etx.getText() + "");
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (commentid == null) {
                    getpostlik();
                } else {
                    getcommlik();
                }
                toquery(etx.getText() + "");
                srl.setRefreshing(false);
            }
        });
        etx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                toquery(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        cl=new effecy(likes.this);

    }

    public void toquery(String ss) {

//        if (ss.equals("") || ss.equals(null)) {
//            ordusrlst.clear();
//            ordtmelst.clear();
//            for (int i=0 ;i<tmelst.size();i++){
//                ordusrlst.add(usrlst.get(i));
//                ordtmelst.add(tmelst.get(i));
//            }
//
//            adp.notifyDataSetChanged();
//            if (ordusrlst.isEmpty()) {
//                nn.setVisibility(View.VISIBLE);
//            } else {
//                nn.setVisibility(GONE);
//            }
//        } else {
            que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(ss).endAt(ss + "\uf8ff");

            que.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ordusrlst.clear();
                    ordtmelst.clear();
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        if (usrlst.contains(dss.getKey())) {
                            ordusrlst.add(dss.getKey());
                            ordtmelst.add(tmelst.get(usrlst.indexOf(dss.getKey())));
                        }
                    }
//               if(aftertag == null){
                    adp.notifyDataSetChanged();

                    if (ordusrlst.isEmpty()) {
                        nn.setVisibility(View.VISIBLE);
                    } else {
                        nn.setVisibility(GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//        }
    }

    public void getcommlik() {
        FirebaseDatabase.getInstance().getReference().child("info").child(publisherid).child("commlikes").child(postid).child(commentid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usrlst.clear();
                tmelst.clear();
                HashMap<String, String> unurslst = new HashMap<>();
                List<String> untmlst = new ArrayList<>();
                for (DataSnapshot dsh : snapshot.getChildren()) {
                    keymodel kdel = dsh.getValue(keymodel.class);

                    unurslst.put(dsh.getKey(), kdel.getDate() + " " + kdel.getTime());
                    untmlst.add(kdel.getDate() + " " + kdel.getTime());

                }

                for (String dates : effecy.instance.getdalst(untmlst)) {
                    for (String key : unurslst.keySet()) {
                        if (dates.equals(unurslst.get(key)) && !usrlst.contains(key)) {
                            usrlst.add(key);
                            tmelst.add(dates.substring(0, 16));
                        }
                    }
                }
                lkb.setText("Likes : "+snapshot.getChildrenCount());
                Collections.reverse(usrlst);
                Collections.reverse(tmelst);
//                adp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getpostlik() {
        FirebaseDatabase.getInstance().getReference().child("info").child(publisherid).child("likes").child(postid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usrlst.clear();
                tmelst.clear();
                HashMap<String, String> unurslst = new HashMap<>();
                List<String> untmlst = new ArrayList<>();
                for (DataSnapshot dsh : snapshot.getChildren()) {
                    keymodel kdel = dsh.getValue(keymodel.class);

                    unurslst.put(dsh.getKey(), kdel.getDate() + " " + kdel.getTime());
                    untmlst.add(kdel.getDate() + " " + kdel.getTime());

                }

                for (String dates : effecy.instance.getdalst(untmlst)) {
                    for (String key : unurslst.keySet()) {
                        if (dates.equals(unurslst.get(key)) && !usrlst.contains(key)) {
                            usrlst.add(key);
                            tmelst.add(dates.substring(0, 16));
                        }
                    }
                }
                lkb.setText("Likes : "+snapshot.getChildrenCount());
//                if (usrlst.isEmpty()) {
//                    nn.setVisibility(View.VISIBLE);
//                } else {
//                    nn.setVisibility(GONE);
//                }
                Collections.reverse(usrlst);
                Collections.reverse(tmelst);
//                adp.notifyDataSetChanged();
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
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

//        cl = new effecy(likes.this);
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