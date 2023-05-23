package com.example.honeymoon;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import adapters.seenadp;
import adapters.tagadp;
import model.usermodel;
import utility.NetworkChangeListener;

public class posttags extends AppCompatActivity {


    private RecyclerView recy, tgrecy;
    private seenadp adp;
    private List<String> lstusr, tmelst;
    private String postid, publisherid;
    private Intent inte;

    private tagadp tgadp;
    private effecy cl;
    private AutoCompleteTextView actv;
    private List<usermodel> newmenuserlst, newalluserdellst;
    private List<String> newusernmelst, newselectedlst;
    private ArrayAdapter mentadp;
    private RelativeLayout rll;
    private TextView tgpeo, ad;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_posttags);

        recy = findViewById(R.id.recy);
        actv = findViewById(R.id.sactv);
        tgrecy = findViewById(R.id.tgecy);
        rll = findViewById(R.id.newrl);
        tgpeo = findViewById(R.id.tgpeo);
        ad = findViewById(R.id.addd);

        lstusr = new ArrayList<>();
        tmelst = new ArrayList<>();

        newmenuserlst = new ArrayList<>();
        newalluserdellst = new ArrayList<>();
        newusernmelst = new ArrayList<>();
        newselectedlst = new ArrayList<>();


        inte = getIntent();
        postid = inte.getStringExtra("postid");
        publisherid = inte.getStringExtra("publisherid");


        adp = new seenadp(posttags.this, lstusr, tmelst, "posttags", publisherid, postid);
        recy.setAdapter(adp);
        recy.setLayoutManager(new LinearLayoutManager(posttags.this));

        tgadp = new tagadp(posttags.this, newselectedlst, newmenuserlst, newalluserdellst);
        tgrecy.setAdapter(tgadp);
        tgrecy.setLayoutManager(new LinearLayoutManager(posttags.this));
        mentadp = new ArrayAdapter(posttags.this,R.layout.support_simple_spinner_dropdown_item,lstusr);
        actv.setAdapter(mentadp);


        if (publisherid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            tgpeo.setVisibility(VISIBLE);
        } else {
            tgpeo.setVisibility(GONE);
        }
        tgpeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tgpeo.setEnabled(false);
                if (rll.getVisibility() == GONE) {
                    rll.setVisibility(VISIBLE);
                } else {
                    rll.setVisibility(GONE);
                }
                tgpeo.setEnabled(true);
            }
        });

        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setEnabled(false);
                android.app.AlertDialog.Builder alt = new AlertDialog.Builder(posttags.this);
                alt.setInverseBackgroundForced(true);
                alt.setTitle("Tag!!!").setMessage("Do you want to Tag Them")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ad.setEnabled(false);
                                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
                                String ddat = effecy.instance.gettime();


                                for (usermodel userdel : tgadp.getUsrmdelst()) {
                                    dataref.child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("posts").child(postid).child("tags").child(userdel.getUserid()).setValue(ddat.substring(0, 10));
                                }


//                            for(String i : soedts.getHashtags()){
//                                dataref.child("hashs").child(i).child(key).setValue(mp);
//
//                            }
                                for (usermodel userdel : tgadp.getUsrmdelst()) {
                                    FirebaseDatabase.getInstance().getReference().child("info").child(userdel.getUserid()).child("tags")
                                            .child(postid).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    effecy.instance.sendnotii(posttags.this,userdel.getUserid(),"tagged you in post","tag",postid,postid,null);

                                    HashMap mpp = new HashMap();
                                    mpp.put("subject", "tag");
                                    mpp.put("notify", " ");
                                    mpp.put("postid", postid);
                                    mpp.put("publisherid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    mpp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    mpp.put("notifyid", postid);
                                    mpp.put("time", ddat.substring(11));

                                    FirebaseDatabase.getInstance().getReference().child("info").child(userdel.getUserid()).child("notify").child("others")
                                            .child(ddat.substring(0, 10)).child(postid).setValue(mpp);
                                    FirebaseDatabase.getInstance().getReference().child("info").child(userdel.getUserid()).child("tags").child(postid)
                                            .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());


                                }
                                Toast.makeText(posttags.this, "Successfully Tagged", Toast.LENGTH_SHORT).show();
                                tgadp.getUsrmdelst().clear();
                                tgadp.getUsenamelst().clear();
                                tgadp.notifyDataSetChanged();
                                adp.notifyDataSetChanged();
                                rll.setVisibility(GONE);
                            }

                        })

                        .show();
                ad.setEnabled(true);
            }
        });

//        actv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//                if (newusernmelst.contains(s + "") && !newselectedlst.contains(s + "")) {
//                    int r = 0;
//                    for (int yuu = 0; yuu < tgadp.getUsrmdelst().size(); yuu++) {
//                        if (tgadp.getUsrmdelst().get(yuu).getUsername().equals(s + "")) {
//                            r = r + 1;
//                        }
//                    }
//                    if (r == 0) {
//                        newselectedlst.add(s + "");
//                        actv.setText("");
//                    } else {
//                        Toast.makeText(posttags.this, "already tagged", Toast.LENGTH_SHORT).show();
//                        actv.setText("");
//                    }
//                } else if (newselectedlst.contains(s + "")) {
//                    Toast.makeText(posttags.this, "already tagged", Toast.LENGTH_SHORT).show();
//                    actv.setText("");
//                }
//
//                tgadp.notifyDataSetChanged();
//
//            }
//        });

        getgusers();
    }

    public void getusername() {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newusernmelst.clear();
                newalluserdellst.clear();
                for (DataSnapshot dss : snapshot.getChildren()) {

                    usermodel udel = dss.getValue(usermodel.class);
                    if (udel != null) {
                        newalluserdellst.add(udel);
                        if (!lstusr.contains(udel.getUserid()) && !udel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && udel.getIstagpermitted().equals("true")) {
                            newusernmelst.add(udel.getUsername());

                        }
                    }
                }
                mentadp.notifyDataSetChanged();
                tgadp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getgusers() {
        FirebaseDatabase.getInstance().getReference().child("info").child(publisherid).child("posts").child(postid).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstusr.clear();
                tmelst.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    lstusr.add(ds.getKey());
                    tmelst.add(ds.getValue().toString());
                }
                adp.notifyDataSetChanged();

                getusername();

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
//        cl = new effecy(posttags.this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

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