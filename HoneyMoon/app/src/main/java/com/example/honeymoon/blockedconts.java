package com.example.honeymoon;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.blockadap;
import adapters.effecy;
import model.usermodel;
import utility.NetworkChangeListener;

public class blockedconts extends AppCompatActivity {

    private RecyclerView recy;
    private List<usermodel> lstusr;
    private List<String> strlst;
    private blockadap bladp;
    private effecy cl;
    private EditText etx;
    private TextView nn;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_blockedconts);


        recy = findViewById(R.id.jklp);
        nn = findViewById(R.id.nn);
        etx=findViewById(R.id.etx);

        lstusr = new ArrayList<>();
        strlst = new ArrayList<>();
        bladp = new blockadap(blockedconts.this, lstusr);
        recy.setAdapter(bladp);
        recy.setLayoutManager(new LinearLayoutManager(blockedconts.this));
        getsr(etx.getText()+"");
        etx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getsr(s+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        cl=new effecy(blockedconts.this);
    }

    public void getuser(String ss) {
        Query que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(ss).endAt(ss + "\uf8ff");

        que.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstusr.clear();
                for (DataSnapshot dsss : snapshot.getChildren()) {
                    if (strlst.contains(dsss.getKey())) {
                        lstusr.add(dsss.getValue(usermodel.class));
                    }
                }
                if (lstusr.isEmpty()) {
                    nn.setVisibility(View.VISIBLE);
                } else {
                    nn.setVisibility(View.GONE);
                }
                bladp.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getsr(String ss) {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("blocked")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        strlst.clear();
                        for (DataSnapshot sp : snapshot.getChildren()) {
                            strlst.add(sp.getKey());
                        }
                        getuser(ss);
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

//        cl = new effecy(blockedconts.this);
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
