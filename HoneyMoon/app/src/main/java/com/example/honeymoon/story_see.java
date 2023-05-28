package com.example.honeymoon;

import static android.view.View.GONE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import adapters.seen;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import model.highmodel;
import model.statusmodel;
import model.usermodel;
import utility.NetworkChangeListener;

public class story_see extends AppCompatActivity {



  
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    


    public String userid,type,highid;
    public boolean islast;
    long pressTime=0L;
    long limit =500L;
    public TextView username,title,seevw;

    public RelativeLayout bott,topp;
    public ImageView usrprof,send,annocru,annofri,annoadm,himen,imgg;
//    public ViewPager2 viewp;
    public EditText reply;
    public View left,right;
    public StoriesProgressView spv;
    public int counter=0;
    public List<statusmodel> statlst;
    public List<highmodel> highlst;
    public Context context;
    public List<String> dalst,strstalst;
    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pressTime=System.currentTimeMillis();
                    spv.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    spv.resume();
                    return limit<now-pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_story_see);

        bott = findViewById(R.id.stobot);
        topp = findViewById(R.id.topp);
        username = findViewById(R.id.stousrnme);
        title = findViewById(R.id.stotit);
        usrprof = findViewById(R.id.stopro);
        send = findViewById(R.id.stosen);
        reply = findViewById(R.id.stoeepl);
        seevw = findViewById(R.id.sevw);
        himen = findViewById(R.id.himen);

//        viewp = itemView.findViewById(R.id.sevie);

        annocru = findViewById(R.id.cru);
        annofri = findViewById(R.id.fri);
        annoadm = findViewById(R.id.adm);
        imgg = findViewById(R.id.imgsto);
        left = findViewById(R.id.lft);
        right = findViewById(R.id.rft);
        spv = findViewById(R.id.spv);


//        viewp=findViewById(R.id.viewpa);

//        stalst=new ArrayList<>();
        highlst = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        userid = bundle.get("userid") + "";
        bundle.remove("userid");
        type = bundle.getString("type");
        bundle.remove("type");

        if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            bott.setVisibility(GONE);
            seevw.setVisibility(View.VISIBLE);
        } else {
            seevw.setVisibility(GONE);
            bott.setVisibility(View.VISIBLE);
        }


        if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            bott.setVisibility(GONE);
            seevw.setVisibility(View.VISIBLE);
        } else {
            seevw.setVisibility(GONE);
            bott.setVisibility(View.VISIBLE);
        }
        getuserdet();


        if (type == null) {
            statlst = new ArrayList<>();
            getstory();
            statusmodel stadel = statlst.get(counter);
            spv.setStoriesCount(statlst.size());
//            Toast.makeText(context, , Toast.LENGTH_SHORT).show();
            setstoryseen(stadel);
            Picasso.get().load(stadel.getUri()).into(imgg, new Callback() {
                @Override
                public void onSuccess() {
                    spv.resume();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } else {
            if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                himen.setVisibility(View.VISIBLE);
            } else {
                himen.setVisibility(GONE);
            }
            highlst=new ArrayList<>();
            dalst=new ArrayList<>();
            strstalst=new ArrayList<>();
            gethigh();


            highmodel hgdl = highlst.get(counter);
            spv.setStoriesCount(highlst.size());
//            spv.pause();
            Picasso.get().load(hgdl.getImageuri()).into(imgg, new Callback() {
                @Override
                public void onSuccess() {
                    spv.resume();
                }

                @Override
                public void onError(Exception e) {

                }
            });

            if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                himen.setVisibility(View.VISIBLE);
            } else {
                himen.setVisibility(GONE);
            }

        }
        spv.setStoryDuration(5000L);
//        Activity act =
        spv.startStories(counter);


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spv.reverse();
            }
        });
        left.setOnTouchListener(onTouchListener);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spv.skip();
            }
        });
        right.setOnTouchListener(onTouchListener);

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
//                viewp.setEnabled(false);
                send.setEnabled(false);
                if (!TextUtils.isEmpty(reply.getText() + "") && send.getTag().equals("notblocked")) {
                    String ddd = effecy.instance.getmilltime();
                    DatabaseReference sef = FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("chats")
                            .child(userid);
                    String id = sef.push().getKey();
                    HashMap<String, Object> vp = new HashMap<>();
                    vp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    vp.put("chatid", id);
                    vp.put("chat", reply.getText() + "");
                    vp.put("liked", "false");
                    if (type != null) {
                        vp.put("uri", highlst.get(counter).getImageuri());
                    } else {
                        vp.put("uri", statlst.get(counter).getUri());
                    }
                    vp.put("type", "story");
                    vp.put("time", ddd.substring(11));


                    sef.child(effecy.instance.gettime().substring(0, 10)).child(id).setValue(vp);

                    HashMap<String, Object> mp = new HashMap<>();
                    mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mp.put("chatid", id);
                    mp.put("chat", reply.getText() + "");
                    mp.put("liked", "false");
                    mp.put("type", "story");
                    if (type != null) {
                        mp.put("uri", highlst.get(counter).getImageuri());
                    } else {
                        mp.put("uri", statlst.get(counter).getUri());
                    }
                    mp.put("time", ddd.substring(11));


                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("chats")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(effecy.instance.gettime().substring(0, 10))
                            .child(id).setValue(mp);


                    effecy.instance.sendnotii(context, userid, reply.getText() + "", "message", null, FirebaseAuth.getInstance().getCurrentUser().getUid(), null);


                    reply.setText("");
                } else if (send.getTag().equals("blocked")) {
                    Toast.makeText(context, "You have been Blocked", Toast.LENGTH_SHORT).show();
                }
                send.setEnabled(true);
//                viewp.setEnabled(true);
            }
        });

        himen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                himen.setEnabled(false);
                PopupMenu pmen = new PopupMenu(context, himen);
                pmen.getMenuInflater().inflate(R.menu.del, pmen.getMenu());
                pmen.show();
                pmen.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.adsto:
                                Intent intte = new Intent(context, allstories.class);
                                intte.putExtra("highid", highlst.get(counter).getHighlightid());
                                (context).startActivity(intte);
                                break;

                            case R.id.edddit:
                                Intent intet = new Intent(context, addhighlight.class);
                                intet.putExtra("highid", highlst.get(counter).getHighlightid());
                                intet.putExtra("from", "edit");
                                (context).startActivity(intet);
                                break;
                            case R.id.remhigh:
                                AlertDialog.Builder alt = new AlertDialog.Builder(context);
                                alt.setTitle("Remove !").setMessage("Do you want to remove this From the Highlight")
                                        .setCancelable(true)
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (highlst.size() == 1) {
                                                    FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                                            .child(highlst.get(counter).getHighlightid()).delete();
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("highlights").child(highlst.get(counter).getHighlightid()).removeValue();
                                                    ((Activity) context).finish();
                                                } else {
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("highlights").child(highlst.get(counter).getHighlightid()).child("statusmodelist").child(statlst.get(counter).getStatusid()).removeValue();

                                                }

                                            }
                                        }).show();
                                break;

                        }
                        return true;
                    }
                });
                himen.setEnabled(true);
            }
        });
        topp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topp.setEnabled(false);
                Intent tent = new Intent(context, Mainactivity.class);
                tent.putExtra("userid", userid);
                (context).startActivity(tent);
                topp.setEnabled(true);
            }
        });
        seevw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seevw.setEnabled(false);
                if (highlst == null) {
                    (context).startActivity(new Intent(context, com.example.honeymoon.view.class));
                } else {
                    Intent ient = new Intent(context, seen.class);
                    ient.putExtra("date", statlst.get(counter).getTime().substring(0, 10));
                    ient.putExtra("purpose", "allstories");
                    (context).startActivity(ient);
                }
                seevw.setEnabled(true);
            }
        });

        spv.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                Toast.makeText(context, counter + "", Toast.LENGTH_SHORT).show();

                if (type==null) {

                    Picasso.get().load(statlst.get(counter).getUri()).into(imgg);
                } else {
                    Picasso.get().load(highlst.get(counter).getImageuri()).into(imgg);
                }
                setstoryseen(statlst.get(counter));
            }


            @Override
            public void onPrev() {
                if ((counter - 1) < 0) {
                    return;
                }


                Toast.makeText(context, counter + "", Toast.LENGTH_SHORT).show();

                if (type==null) {

                    Picasso.get().load(statlst.get(counter).getUri()).into(imgg);
                } else {

                    Picasso.get().load(highlst.get(counter).getImageuri()).into(imgg);

                }
            }

            @Override
            public void onComplete() {

                ((Activity) context).finish();
//                spv.destroy();
//                int ui = counter-1;
//                Toast.makeText(context,ui+"", Toast.LENGTH_SHORT).show();

//        Activity act =
//                spv.startStories(0);

            }
        });


//        viewp.setAdapter(adp);
//
//
//        viewp.canScrollVertically(100);
//        viewp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onPageSelected(int position) {
//
//                super.onPageSelected(position);
//            }
//        });


    }
    public void getuserdet() {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel udel = snapshot.child(userid).getValue(usermodel.class);
                if (udel != null) {
                    username.setText(udel.getUsername());
                    Picasso.get().load(udel.getImageurl()).into(usrprof);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void gethigh(){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("highlights").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strstalst.clear();
//                countlst.clear();
                dalst.clear();
                for(DataSnapshot df : snapshot.getChildren()){
                    if(df.getKey().equals(highid)){
                        highmodel ghb = df.getValue(highmodel.class);
//                        title.setText(ghb.getTitle());

                        if(ghb.getStatusmodelist() != null) {

                            for (String sta : ghb.getStatusmodelist().keySet()) {
//                                stalst.add(sta);

                                strstalst.add(sta);
                                dalst.add(ghb.getStatusmodelist().get(sta));

                            }
                        }


                    }

                }
                getstatmdel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getstatmdel() {
            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("allstories")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

//                        uriList.clear();
                            List<statusmodel> stalst = new ArrayList<>();
                            List<String> orddalst = new ArrayList<>();
                            List<statusmodel> unordstalst = new ArrayList<>();
                            for (int u = 0; u < strstalst.size(); u++) {
                                statusmodel stdel = snapshot.child(dalst.get(u)).child(strstalst.get(u)).getValue(statusmodel.class);
                                if (stdel != null) {
                                    unordstalst.add(stdel);
                                    orddalst.add(stdel.getTime());

                                }
                            }
                            for (String das : effecy.instance.getdalst(orddalst)) {
                                for (statusmodel stdell : unordstalst) {
                                    if (stdell.getTime().equals(das) && !stalst.contains(stdell)) {
                                        stalst.add(stdell);

                                        break;
                                    }
                                }
                            }
                            spv.setStoriesCount(stalst.size());
//                        countlst.add(0);
//                        stalst.add(stalst);
//                        if (stalst.isEmpty()){
//                            finish();
//                        }
//                        getindi(pp);
//                        adp.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        public void getstory(){
          FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("story").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                List<statusmodel> statodel=new ArrayList<>();
                List<statusmodel> unstadelst = new ArrayList<>();
                List<String> undalst = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    statusmodel sta = ds.getValue(statusmodel.class);

                    if (effecy.instance.isold(sta.getTime(), 60 * 24)) {
                        statodel.add(sta);
                    } else {
                        unstadelst.add(sta);
                        undalst.add(sta.getTime());
                    }

                }
//
//
                for (statusmodel st : statodel) {
                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("allstories")
                            .child(st.getTime().substring(0, 10)).child(st.getStatusid()).setValue(st);
                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("story").child(st.getStatusid())
                            .removeValue();

                }

                for (String strda : effecy.instance.getdalst(undalst)) {
                    for (statusmodel stdel : unstadelst) {
                        if (stdel.getTime().equals(strda) ) {
                            statlst.add(stdel);
                            break;
                        }
                    }
                }
                if (statlst.isEmpty()) {
                    finish();
                }

            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setstoryseen(statusmodel stadel) {
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.child(stadel.getPublisherid()).child("seen").child(stadel.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .exists()) {
                    FirebaseDatabase.getInstance().getReference().child("info").child(stadel.getPublisherid()).child("seen")
                            .child(stadel.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(effecy.instance.gettime());

                }

//                        getindi(pp);

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
//        cl = new effecy(story_see.this);
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
//        if (adp.getSsssto()!=null) {
//            adp.getSsssto().resume();
//        }
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue(effecy.instance.gettime());
//        adp.getSsssto().pause();
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }

}