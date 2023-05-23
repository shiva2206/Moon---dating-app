package com.example.honeymoon;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.chatadap;
import adapters.effecy;
import butterknife.ButterKnife;
import model.chatmodel;
import model.usermodel;
import utility.NetworkChangeListener;


public class chats extends AppCompatActivity {

    private String userid;
    private TextView username, line,repcht,repnme;
    private VideoView repvid;
    private EditText caht;
    private ImageView profile,bcgm,back, call, video, send, hme,attach,annocru,annofri,annoadm,reclo ,repimg;
    private NestedScrollView nested;
    private chatadap adap;
    private List<String> dalst,islastdatlst;
    private List<chatmodel> chtlst;
//    private effecy cl;
    private CardView repcd;
    private RecyclerView recy;
    private chatmodel repdel;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chats);
        ButterKnife.inject(this);


        bcgm = findViewById(R.id.cardview);
        nested=findViewById(R.id.nesd);
        username = findViewById(R.id.chatusernme);
        line = findViewById(R.id.chatlin);
        hme=findViewById(R.id.dellll);
        caht = findViewById(R.id.chatmes);
        profile = findViewById(R.id.chatpro);
        call = findViewById(R.id.chatcall);
        video = findViewById(R.id.chatvide);
        send = findViewById(R.id.chasend);
        attach = findViewById(R.id.attach);
        recy = findViewById(R.id.chatsw);
        back=findViewById(R.id.bck);


        repcd=findViewById(R.id.rt);
        reclo=findViewById(R.id.rclo);
        annocru = findViewById(R.id.cru);
        annofri=findViewById(R.id.fri);
        annoadm=findViewById(R.id.adm);
        repnme=findViewById(R.id.rtname);
        repcht=findViewById(R.id.rtcht);
        repimg=findViewById(R.id.rtimg);
        repvid=findViewById(R.id.rtvid);

        dalst = new ArrayList<>();
        islastdatlst=new ArrayList<>();
//        stack=new ArrayList();
        chtlst=new ArrayList<>();
        Intent inte = getIntent();
        userid = inte.getStringExtra("userid");
        getpro();
//        cl=new effecy(chats.this);

        adap = new chatadap(chats.this,chtlst,islastdatlst,userid,nested,hme);
        recy.setAdapter(adap);
        recy.setLayoutManager(new LinearLayoutManager(chats.this));

//        effecy.instance.layoutanime(recy,R.anim.layout_fall_down);
        getdate();

        effecy anno = new effecy(chats.this,userid,annocru,annoadm,annofri);
        effecy blocked = new effecy(chats.this,userid,send);

        bcgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bcgm.setEnabled(false);
                if(bcgm.getTag() != null && bcgm.getTag().equals("yes")){
                    Intent inte = new Intent(chats.this, story_see.class);
                    inte.putExtra("userid",userid);
                    startActivity(inte);
                }
                bcgm.setEnabled(true);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setEnabled(false);
                Intent inte = new Intent(chats.this,details.class);
                inte.putExtra("userid",userid);
                startActivity(inte);
                username.setEnabled(true);
            }
        });
        reclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reclo.setEnabled(false);
                repcd.setVisibility(GONE);
                reclo.setEnabled(true);
            }
        });
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attach.setEnabled(false);
                PopupMenu pme = new PopupMenu(chats.this,attach);
                pme.getMenuInflater().inflate(R.menu.attach,pme.getMenu());
                pme.show();
                pme.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.igg:
                                Intent inte = new Intent(chats.this,attach.class);
                                inte.putExtra("type","image/*");
                                inte.putExtra("userid",userid);
                                startActivity(inte);
                                break;
                            case R.id.ivv:
                                Intent intee = new Intent(chats.this,attach.class);
                                intee.putExtra("type","video/*");
                                intee.putExtra("userid",userid);
                                startActivity(intee);
                                break;
                        }
                        return true;
                    }
                });
                attach.setEnabled(true);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                Toast.makeText(chats.this, send.getTag()+"", Toast.LENGTH_SHORT).show();
                caht.setEnabled(false);
                send.setEnabled(false);
                if (!TextUtils.isEmpty(caht.getText().toString()) && send.getTag().equals("notblocked")) {

                    if (repcd.getVisibility() != VISIBLE){
                       repdel=null;
                    }
                    String ddd = effecy.instance.getmilltime();
                    DatabaseReference sef = FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("chats")
                            .child(userid);
                    String id = sef.push().getKey();
                    HashMap<String, Object> vp = new HashMap<>();
                    vp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    vp.put("chatid", id);
                    vp.put("chat", caht.getText().toString());
                    vp.put("liked","false");
                    vp.put("type", "text");
                    vp.put("time", ddd.substring(11));
                    if(repdel != null){
                        vp.put("replyid",repdel.getTime()+" "+repdel.getChatid());
                    }

                    sef.child(effecy.instance.gettime().substring(0, 10)).child(id).setValue(vp);

                    HashMap<String, Object> mp = new HashMap<>();
                    mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mp.put("chatid", id);
                    mp.put("chat", caht.getText().toString());
                    mp.put("liked","false");
                    mp.put("type", "text");
                    mp.put("time", ddd.substring(11));
                    if(repdel != null){
                        mp.put("replyid",repdel.getTime()+" "+repdel.getChatid());
                    }

                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("chats")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(effecy.instance.gettime().substring(0, 10))
                            .child(id).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    repdel=null;
                                    repcd.setVisibility(GONE);
                                }
                            });


                    effecy.instance.sendnotii(chats.this,userid,caht.getText()+"","message",null,FirebaseAuth.getInstance().getCurrentUser().getUid(),null);


                    caht.setText("");
                }else if(send.getTag().equals("blocked")){
                    Toast.makeText(chats.this, "You have been Blocked", Toast.LENGTH_SHORT).show();
                }
//                nested.smoothScrollTo(0,nested.getChildAt(0).getHeight());
                caht.setEnabled(true);
                send.setEnabled(true);
            }

        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.setEnabled(false);
                if (send.getTag().equals("notblocked")) {
//                    effecy caal = new effecy(chats.this, userid);
                    Intent intty = new Intent(chats.this,call.class);
                    intty.putExtra("userid",userid);
                    intty.putExtra("tocall","yes");
                    startActivity(intty);
                }else{
                    Toast.makeText(chats.this, "You Have Been Blocked By The User", Toast.LENGTH_SHORT).show();
                }
                call.setEnabled(true);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.setEnabled(false);
                if (send.getTag().equals("notblocked")) {
                    effecy caal = new effecy(chats.this, userid);

                }else{
                    Toast.makeText(chats.this, "You Have Been Blocked By The User", Toast.LENGTH_SHORT).show();
                }
                video.setEnabled(true);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                repdel = adap.getChatlst().get(viewHolder.getAdapterPosition());
                repcd.setVisibility(VISIBLE);
                FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dsp : snapshot.getChildren()){
                            if(dsp.getKey().equals(repdel.getUserid())) {
                                usermodel udel = dsp.getValue(usermodel.class);
                                repnme.setText(udel.getUsername());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (repdel.getType().equals("image")){
                    repimg.setVisibility(VISIBLE);
                    repvid.setVisibility(GONE);
                    repcht.setVisibility(GONE);
                    Picasso.get().load(repdel.getUri()).into(repimg);

                }else if(repdel.getType().equals("video")){
                    repimg.setVisibility(INVISIBLE);
                    repvid.setVisibility(VISIBLE);
                    repcht.setVisibility(GONE);
                    repvid.setVideoURI(Uri.parse(repdel.getUri()));

                }else{
                    repimg.setVisibility(GONE);
                    repvid.setVisibility(GONE);
                    repcht.setVisibility(VISIBLE);
                    repcht.setText(repdel.getChat());
                }

                adap.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recy);


    }




    public void getpro() {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel currudel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(usermodel.class);
                attach.setTag(currudel.getUsername());
                for (DataSnapshot st : snapshot.getChildren()) {
                    if (st.getKey().equals(userid)) {
                        usermodel udel = st.getValue(usermodel.class);
                        effecy cla = new effecy(userid, bcgm);
                        Picasso.get().load(udel.getImageurl()).into(profile);
                        username.setText(udel.getUsername());

                        effecy.instance.onlinesta(userid,udel.getShowlastseen(),line,true);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void getdate() {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dalst.clear();
                List<chatmodel> unchatlst= new ArrayList<>();
                List<String> lstdat = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    for (DataSnapshot dcht : ds.getChildren()){
                        chatmodel chdel = dcht.getValue(chatmodel.class);
                        chdel.setTime(ds.getKey()+" "+chdel.getTime());
                        lstdat.add(chdel.getTime());
                        unchatlst.add(chdel);
                    }
                }
                chtlst.clear();
                islastdatlst.clear();
                for(String datet : effecy.instance.getdalst(lstdat)){
                    for (chatmodel ch : unchatlst){
                        if (ch.getTime().equals(datet) && !chtlst.contains(ch)){
                            ch.setTime(ch.getTime().substring(0,16));
                            chtlst.add(ch);
                            islastdatlst.add("false");

                        }
                    }
                }
                if (!islastdatlst.isEmpty()){
                    islastdatlst.remove(islastdatlst.size()-1);
                    islastdatlst.add("true");
                }

                adap.notifyDataSetChanged();

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

//        cl = new effecy(chats.this);
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