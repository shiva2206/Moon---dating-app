package com.example.honeymoon;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.hendraanggrian.appcompat.widget.SocialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.commentsadp;
import adapters.effecy;
import model.commentsmodel;
import model.postmodel;
import model.usermodel;
import utility.NetworkChangeListener;

public class comments extends AppCompatActivity {

    private commentsadp adp;
    private ImageView iv,currprof,sedpr,bcimg;
    private TextView desc,tm,nn;
    private RecyclerView recy;
    private List<postmodel> lstpostmdl;
    private List<commentsmodel> commlst;
    private SocialEditText edt;
    private effecy cl;
    private SwipeRefreshLayout srl;
    private postmodel psdel;
    private String deltepost;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_comments);

        iv = findViewById(R.id.pr);
        tm=findViewById(R.id.tm);
        nn=findViewById(R.id.nn);
        desc=findViewById(R.id.descri);
        recy=findViewById(R.id.rec);
        bcimg=findViewById(R.id.cardview);
        currprof=findViewById(R.id.curprod);
        edt=findViewById(R.id.commentedt);
        sedpr=findViewById(R.id.sendcomm);
        srl=findViewById(R.id.srl);

        commlst = new ArrayList<>();
        lstpostmdl = new ArrayList<>();

        effecy bcmg = new effecy(FirebaseAuth.getInstance().getCurrentUser().getUid(),bcimg);

        Intent inte = getIntent();

        if(inte.getStringExtra("delete") != null){
            deltepost=inte.getStringExtra("delete");
        }else{
            deltepost= null;
        }
        allset();
        postmodel pdel = new postmodel(inte.getStringExtra("publisherid"),inte.getStringExtra("postid"),
                inte.getStringExtra("uri"),inte.getStringExtra("time"),inte.getStringExtra("location"),
                inte.getStringExtra("description"),inte.getStringExtra("type"),(HashMap)inte.getSerializableExtra("tags"));
//        lstpostmdl.add(psdel);
        effecy blked= new effecy(comments.this,inte.getStringExtra("publisherid"),sedpr);


        adp=new commentsadp(this,commlst,deltepost,pdel);
//        effecy.instance.layoutanime(recy, anim_one);
        recy.setAdapter(adp);
        recy.setLayoutManager(new LinearLayoutManager(this));
//        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adp);
////        SlideInBottomAnimationAdapter scaleInAnimationAdapter = new SlideInBottomAnimationAdapter(adp);
////        SlideInLeftAnimationAdapter scaleInAnimationAdapter = new SlideInLeftAnimationAdapter(adp);
//        scaleInAnimationAdapter.setDuration(1000);
//        scaleInAnimationAdapter.setInterpolator(new AccelerateDecelerateInterpolator());
//        scaleInAnimationAdapter.setFirstOnly(false);
//        recy.setAdapter(scaleInAnimationAdapter);
//        cl=new effecy(comments.this);

        sedpr.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                edt.setEnabled(false);
                sedpr.setEnabled(false);
                if((currprof.getTag()+"").equals("yes")) {

                    String dat = effecy.instance.gettime();
                    if (sedpr.getTag().equals("notblocked")) {
                        if (!TextUtils.isEmpty(edt.getText())) {
                            DatabaseReference def = FirebaseDatabase.getInstance().getReference().child("info").child(inte.getStringExtra("publisherid"))
                                    .child("comments").child(inte.getStringExtra("postid"));
                            String key = def.push().getKey();
                            HashMap<String, Object> mp = new HashMap<>();
                            mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mp.put("commentid", key);
                            mp.put("publisherid", inte.getStringExtra("publisherid"));
                            mp.put("comment", edt.getText().toString());
                            mp.put("time", dat.substring(11));
                            mp.put("date", dat.substring(0, 10));
                            mp.put("postid", inte.getStringExtra("postid"));
                            def.child(key).setValue(mp);

                            HashMap<String, String> notymp = new HashMap<>();
                            notymp.put("subject", "commented");
                            notymp.put("notify", edt.getText().toString().trim());
                            notymp.put("postid", inte.getStringExtra("postid"));
                            notymp.put("publisherid", inte.getStringExtra("publisherid"));
                            notymp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notymp.put("notifyid", key);
                            notymp.put("time", dat.substring(0, 11));

                            if (!lstpostmdl.get(0).getPublisherid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                FirebaseDatabase.getInstance().getReference().child("info").child(inte.getStringExtra("publisherid"))
                                        .child("notify").child("others").child(dat.substring(0, 10)).child(key).setValue(notymp);
                                effecy.instance.sendnotii(comments.this, inte.getStringExtra("publisherid"), "Commented: '" + edt.getText() + "'", "comment", null,key,null);

                            }
                            edt.setText("");
                        }
                    } else {
                        Toast.makeText(comments.this, "You Are Blocked by the user", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(comments.this, "This Post is Deleted", Toast.LENGTH_SHORT).show();
                }
                sedpr.setEnabled(true);
                edt.setEnabled(true);
            }
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allset();
                srl.setRefreshing(false);
            }
        });
    }
    public void allset(){
        FirebaseDatabase.getInstance().getReference().child("info").child(getIntent().getStringExtra("publisherid"))
                .child("posts").child(getIntent().getStringExtra("postid")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        psdel = snapshot.getValue(postmodel.class);
                        if (psdel!=null){
                            lstpostmdl.add(psdel);
                            desc.setText(psdel.getDescription());
                            tm.setText(effecy.instance.getzonetime(psdel.getTime().substring(0,16)));
                            effecy.instance.postexist(psdel,currprof);

                            gettigcomm(psdel.getPublisherid(),psdel.getPostid());
                            gettpro(psdel.getPublisherid(),iv,currprof);

                        }else{
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    public void gettpro(String userid,ImageView imageView,ImageView currimg){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel currudel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(usermodel.class);
                Picasso.get().load(currudel.getImageurl()).into(currimg);
                for(DataSnapshot dt : snapshot.getChildren()){
                    if(dt.getKey().equals(userid)) {
                        usermodel use = dt.getValue(usermodel.class);
                        Picasso.get().load(use.getImageurl()).into(imageView);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void gettigcomm (String userid,String postid){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("comments")
                .child(postid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commlst.clear();
                List<commentsmodel> commdelst = new ArrayList<>();
                List<String> dalst = new ArrayList<>();
                for (DataSnapshot dt : snapshot.getChildren()) {
                    commentsmodel com = dt.getValue(commentsmodel.class);
                    commdelst.add(com);
                    dalst.add(com.getDate()+" "+com.getTime());
                }

                for(String strdat : effecy.instance.getdalst(dalst)){
                    for(commentsmodel comdel : commdelst){
                        if(strdat.equals(comdel.getDate()+" "+comdel.getTime()) && !commlst.contains(comdel)){
                            commlst.add(comdel);
                            break;
                        }

                    }
                }
                if(commlst.isEmpty()){
                    nn.setVisibility(View.VISIBLE);
                }else{
                    nn.setVisibility(GONE);
                }
                if(deltepost != null && !snapshot.exists()){
                    FirebaseStorage.getInstance().getReference().child(psdel.getPublisherid()).child("posts").child(psdel.getPostid()).delete();

                    FirebaseDatabase.getInstance().getReference().child("info").child(psdel.getPublisherid())
                            .child("posts").child(psdel.getPostid()).removeValue();

                    finish();
                }

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
//        cl = new effecy(comments.this);
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

    @Override
    public void onBackPressed() {
        if(deltepost == null) {
            super.onBackPressed();
        }
    }
}