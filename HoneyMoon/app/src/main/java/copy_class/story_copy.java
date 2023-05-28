package com.example.honeymoon;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import adapters.seen;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import model.highmodel;
import model.statusmodel;
import model.usermodel;
import utility.NetworkChangeListener;

public class story_see extends AppCompatActivity {



    private String highid,type,userid;
    //    private List<statusmodel> stalst;
    private List<statusmodel> statodel;
    //    private List<Integer> countlst = new ArrayList<>();
    private List<highmodel> highlst;
    private List<String> userlst,mainuserlst;
    //    public story_seeadp adp;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private List<String> dalst,strstalst,strhighlst;
    private int p=0;



    long pressTime = 0L;
    long limit = 500L;
    private TextView username, title, seevw;
    private CardView topp;
    private RelativeLayout bott;
    private ImageView usrprof, send, annocru, annofri, annoadm, himen, imgg;
    //    private ViewPager2 viewp;
    private EditText reply;
    private View left, right;
    private StoriesProgressView spv;
    private int counter = 0;
    private int posi;
    private String what;
    private List<statusmodel> statlst;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    spv.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    spv.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        highlst=new ArrayList<>();
        strhighlst=new ArrayList<>();
        dalst=new ArrayList<>();
        statodel=new ArrayList<>();
        strstalst=new ArrayList<>();
        userlst=new ArrayList<>();
//        countlst=new ArrayList<>();
        mainuserlst=new ArrayList<>();



        Bundle bundle = getIntent().getExtras ();
        userid=bundle.get("userid")+"";
        bundle.remove("userid");
        type=bundle.getString("type");
        bundle.remove("type");

        if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            bott.setVisibility(GONE);
            seevw.setVisibility(View.VISIBLE);
        } else {
            seevw.setVisibility(GONE);
            bott.setVisibility(View.VISIBLE);
        }


        if(type==null){


            bundle.remove("p");

            for(String ky : bundle.keySet()){

                mainuserlst.add(ky);

            }
            p=mainuserlst.indexOf(userid);
            getuser();
        }else{
//            highid= bundle.get("highlightid")+"";
//            userid = bundle.getString("userid");


            for(String ky : bundle.keySet()){
                mainuserlst.add(userid);
                strhighlst.add(bundle.get(ky)+"");



            }

            p = strhighlst.indexOf(highid);
            if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                himen.setVisibility(View.VISIBLE);
            } else {
                himen.setVisibility(GONE);
            }
            gethigh();
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

        topp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topp.setEnabled(false);
                Intent tent = new Intent(story_see.this, Mainactivity.class);
                tent.putExtra("userid", userid);
                startActivity(tent);
                topp.setEnabled(true);
            }
        });
        seevw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seevw.setEnabled(false);
                if (highid == null) {
                    startActivity(new Intent(story_see.this, com.example.honeymoon.view.class));
                } else {
                    Intent ient = new Intent(story_see.this, seen.class);
///check                    ient.putExtra("date", strstalst.get(counter).getTime().substring(0, 10));
                    ient.putExtra("purpose", "allstories");
                    startActivity(ient);
                }
                seevw.setEnabled(true);
            }
        });

        spv.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
//                Toast.makeText(context, counterlst.get(position) + "", Toast.LENGTH_SHORT).show();
//                counterlst.set(position, counterlst.get(position) + 1);
                if (what.equals("story")) {

                    Picasso.get().load(statlst.get(counter).getUri()).into(imgg);
                } else {
                    Picasso.get().load(highlst.get(counter).getImageuri()).into(imgg);
                }
//check                setstoryseen(stalstlst.get(position).get(counterlst.get(position)));
            }


            @Override
            public void onPrev() {
                if ((counter - 1) < 0) {
                    return;
                }

                counter=counter-1;
//                Toast.makeText(context, counterlst.get(position) + "", Toast.LENGTH_SHORT).show();

                if (what.equals("story")) {

                    Picasso.get().load(statlst.get(counter).getUri()).into(imgg);
                } else {

                    Picasso.get().load(highlst.get(counter).getImageuri()).into(imgg);

                }
            }

            @Override
            public void onComplete() {

                finish();
//                holder.spv.destroy();
//                int ui = counterlst.get(position)-1;
//                Toast.makeText(context,ui+"", Toast.LENGTH_SHORT).show();

//        Activity act =
//                holder.spv.startStories(0);

            }
        });


//        if(highid == null){
//
////            adp=new story_seeadp(story_see.this,userlst,stalstlst,countlst,viewp);
//            getuser();
//
//        }else{
////            adp=new story_seeadp(highlstlst,story_see.this,userlst,countlst,viewp);
//            gethigh();
//
//
//        }

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
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void getindi(int pos){
//        uriList.clear();
//        for(int y = 0 ; y<adp.getStalst().size();y++){
//            if(y==pos){
//                uriList.add(R.drawable.blackround);
//                if(highid == null) {
//                    isstoryseenornot(stalst.get(pos));
//
//                }
//            }else{
//                uriList.add(R.drawable.whiteround);
//            }
//        }
//        indadp.notifyDataSetChanged();
//    }

    public void getuserdet( String userid) {
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
    public void getstatmdel(){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("allstories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                        uriList.clear();
                        List<statusmodel> stalst = new ArrayList<>();
                        List<String> orddalst = new ArrayList<>();
                        List<statusmodel>  unordstalst = new ArrayList<>();
                        for(int u = 0 ; u< strstalst.size();u++) {
                            statusmodel stdel = snapshot.child(dalst.get(u)).child(strstalst.get(u)).getValue(statusmodel.class);
                            if (stdel != null) {
                                unordstalst.add(stdel);
                                orddalst.add(stdel.getTime());

                            }
                        }
                        for(String das : effecy.instance.getdalst(orddalst)){
                            for(statusmodel stdell : unordstalst){
                                if(stdell.getTime().equals(das) && !stalst.contains(stdell)){
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
    public void getuser(){
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userlst.clear();
//                   stalstlst.clear();
//                   uriList.clear();
//                   countlst.clear();


//getting all the users story
//                   for (String userid: mainuserlst) {
//                       statodel.clear();
//                       List<statusmodel> unstadelst = new ArrayList<>();
//                       List<String>  undalst = new ArrayList<>();
//                       List<statusmodel> stalst = new ArrayList<>();
//                       for (DataSnapshot ds : snapshot.child(userid).child("story").getChildren()) {
//                           statusmodel sta = ds.getValue(statusmodel.class);
//
//                           if (effecy.instance.isold(sta.getTime(), 60 * 24)) {
//                               statodel.add(sta);
//                           } else {
//                               unstadelst.add(sta);
//                               undalst.add(sta.getTime());
//                           }
//
//                       }
//
//
//                       for (statusmodel st : statodel) {
//                           FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("allstories")
//                                   .child(st.getTime().substring(0, 10)).child(st.getStatusid()).setValue(st);
//                           FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("story").child(st.getStatusid())
//                                   .removeValue();
//
//                       }
//
//                       for (String strda : effecy.instance.getdalst(undalst)) {
//                           for (statusmodel stdel : unstadelst) {
//                               if (stdel.getTime().equals(strda) && !stalst.contains(stdel)) {
//                                   stalst.add(stdel);
//                                   break;
//                               }
//                           }
//                       }
//                       if (!stalst.isEmpty()) {
//                           userlst.add(userid);
////                           countlst.add(0);
////                           stalstlst.add(stalst);
//                       }
//                   }
//                   if(stalstlst.isEmpty()){
//                       finish();
//                   }
//                   System.out.println(userlst.get(0)+"");
//                   adp.notifyDataSetChanged();

            }
            //
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