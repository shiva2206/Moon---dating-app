
package com.example.honeymoon;

import static android.view.View.GONE;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import fragment.callslog;
import fragment.flames;
import fragment.home_area;
import fragment.home_mess;
import fragment.home_stories;
import fragment.nearby_frag;
import fragment.notify;
import fragment.profile;
import fragment.profile_hashtag;
import fragment.search;
import fragment.settings;
import fragment.toppers;
import model.MyService;
import model.callmodel;
import model.chatmodel;
import model.constants;
import model.nearbymodel;
import model.postmodel;
import utility.NetworkChangeListener;

public class Mainactivity extends AppCompatActivity {

    public static Fragment frag;
    private NavigationView bnv;
    private RelativeLayout lll,mp;
    private ImageView menu;
    private effecy cl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TextView nomess;
    public static String devicetoken,currfri,currgender;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);



//        effecy cl = new effecy(this);
        effecy sec= new effecy("sec");
        currgender = Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender",null);
        if (currgender.equals("male")){
            currfri = "gfs";
        }else if (currgender.equals("female")){
            currfri="bfs";
        }

//        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataonboard());
//        FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
//        fragmentTransaction.add(R.id.frag,paperOnboardingFragment);
//        fragmentTransaction.commit();
//
//        paperOnboardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
//            @Override
//            public void onRightOut() {
//                FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
//                Fragment bf = new home_area(true);
//                fragmentTransaction.replace(R.id.frag, bf);
//                fragmentTransaction.commit();
//            }
//        });
//        Toast.makeText(Mainactivity.this,currgender, Toast.LENGTH_SHORT).show();



//        currentuserinfo = new usermodel(
//                FirebaseAuth.getInstance().getCurrentUser().getUid(),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("password","none"),
 //                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("dob","none"),
//
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("about","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("email","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender","none"),
//
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("mium","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("imageurl","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("name","none"),
//
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("status","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("line","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("verified","none"),
//
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("username","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("mode","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("showlastseen","none"),
//
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("showlocation","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("coverimage","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("latitude","none"),
//
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("longitude","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("devicetoken","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("showmat","none"),
//
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("showcru","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("showadm","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("showfri","none"),
//                Mainactivity.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("showaddcrubut","none")
//
//        );



        cl = new effecy(Mainactivity.this);
//        List<String>  df= new ArrayList<>();
//        df.add("12-09-22");
//        List<String> ppp = effecy.instance.getdalst(df);
////        Toast.makeText(Mainactivity.this, effecy.instance.getzonetime("08-08-2022 07:35:35"), Toast.LENGTH_SHORT).show();

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }else{
            startlocationservice();
        }

        devicetoken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("devicetoken").setValue(FirebaseInstanceId.getInstance().getToken());

            }
        });


//        if(currentuserinfo == null) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    // yourMethod();
//                }
//            }, 2000);   //5 seconds
//        }

        bnv = findViewById(R.id.navig);
        lll = findViewById(R.id.lll);
        menu = findViewById(R.id.navop);
        mp = findViewById(R.id.mp);
        nomess=findViewById(R.id.nomess);

        menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.setEnabled(false);
                    if (lll.getVisibility() == GONE) {
                        lll.setVisibility(View.VISIBLE);
                    } else {
                        lll.setVisibility(GONE);
                    }
                    menu.setEnabled(true);


                }
        });
        mp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lll.setVisibility(GONE);
                }
        });

        bnv.setCheckedItem(R.id.home);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag, new home_area(true)).commitAllowingStateLoss();

        bnv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            frag = new home_area(true);
                            break;
                        case R.id.nearby:
                            effecy.instance.qaz =0;
                            frag = new nearby_frag(null);
//                            stoplocationservice();
                            break;
                        case R.id.cam:
//                            startActivity(new Intent(Mainactivity.this, CameraActivity.class));
                            break;
                        case R.id.search:
                            frag = new search(true);
                            break;
                        case R.id.topers:
                            frag = new toppers(Mainactivity.this);
                            break;
                        case R.id.notify:
                            frag = new notify();
                            break;
                        case R.id.profile:
                            frag = new profile(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            break;
                        case R.id.story:
                            frag = new home_stories();
                            break;
                        case R.id.chat:
                            frag = new home_mess();
                            break;
                        case R.id.call:
                            frag = new callslog();
                            break;
                        case R.id.flait:
                            frag=new flames();
                            break;
                        case R.id.logout:
                            setbuilder();
                            break;
                        case R.id.settings:

                            frag = new settings();
                            break;
                        default:
                            break;
                    }


                    try {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag, frag).commitAllowingStateLoss();
                    } catch (Exception e) {

                    }

                    lll.setVisibility(GONE);
                    return true;
                }
        });

        Bundle inte = getIntent().getExtras();

        if (inte != null) {
            String postid = inte.getString("postid");
            String hashtag = inte.getString("hashtag");
            String nearbyyy = inte.getString("nearby");
            String where = inte.getString("where");
            if (where!=null){
                String userrid = inte.getString("userid");
                Intent inten = null;
                switch (where){
                    case "tag" :
                        List<postmodel> pstdelst = new ArrayList<>();
                        FirebaseDatabase.getInstance().getReference().child("info").child(userrid).child("posts")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pstdelst.clear();
                                        postmodel psdel = snapshot.child(getIntent().getStringExtra("postid")).getValue(postmodel.class);
                                        if (psdel!=null){
                                            pstdelst.add(psdel);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        frag=new home_area(pstdelst,false);
                        break;
                    case "comment":
                        List<postmodel> pstdlst = new ArrayList<>();
                        inten=new Intent(Mainactivity.this,comments.class);
                        Intent finalInten = inten;
                        FirebaseDatabase.getInstance().getReference().child("info").child(userrid).child("posts")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pstdlst.clear();
                                        postmodel pl = snapshot.child(getIntent().getStringExtra("postid")).getValue(postmodel.class);
                                        if (pl!=null){
                                            pstdlst.add(pl);
                                            finalInten.putExtra("description",  pl.getDescription());
                                            finalInten.putExtra("uri",  pl.getUri());
                                            finalInten.putExtra("location",  pl.getLocation());
                                            finalInten.putExtra("postid",  pl.getPostid());
                                            finalInten.putExtra("publisherid",  pl.getPublisherid());
                                            finalInten.putExtra("tags",  pl.getTags());
                                            finalInten.putExtra("time",  pl.getTime());
                                            finalInten.putExtra("type",pl.getType());
                                            frag=new home_area(pstdlst,false);
                                            startActivity(finalInten);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                        break;
                    case "profile":
                        frag=new profile(userrid);
                        break;
                    case "noti":
                        frag=new notify();
                        break;
                    case "chats":
                        frag=new home_mess();
                        inten = new Intent(Mainactivity.this,chats.class);
                        inten.putExtra("userid",userrid);
                        startActivity(inten);
                        break;

                    default:


                        break;

                }
            }
            else if (postid != null) {
                HashMap tgs = (HashMap) inte.getSerializable("tags");
                HashMap saves = (HashMap) inte.getSerializable("saves");
                postmodel pdl = new postmodel(inte.getString("publisherid"), inte.getString("postid"),
                        inte.getString("uri"), inte.getString("time"), inte.getString("location"),
                        inte.getString("description"), inte.getString("type"), tgs);
                List<postmodel> posmos = new ArrayList<>();
                posmos.add(pdl);

                frag = new home_area(posmos, false);
            } else if (hashtag != null) {
                frag = new profile_hashtag(hashtag);

            } else if (nearbyyy != null) {
                effecy.instance.qaz =0;
                frag = new nearby_frag(new nearbymodel(inte.getString("gender"),inte.getString("status"),inte.getString("distance")
                        ,inte.getString("agefrom"),inte.getString("ageto")));

//                stoplocationservice();
            } else if(inte.getString("userid") != null) {
                String userid = inte.getString("userid");
                frag = new profile(userid);
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frag, frag).commitAllowingStateLoss();

        }
        getchnumb();
    }

    public ArrayList<PaperOnboardingPage> getDataonboard() {

        PaperOnboardingPage one = new PaperOnboardingPage("hotels","i the hotel", Color.parseColor("#ffb274"),R.drawable.home,R.drawable.home);
        PaperOnboardingPage two = new PaperOnboardingPage("palete","i the paltet", Color.parseColor("#22eaa3"),R.drawable.liked,R.drawable.liked);
        PaperOnboardingPage three = new PaperOnboardingPage("cringes","i the cring", Color.parseColor("#fff000"),R.drawable.send,R.drawable.send);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(one);
        elements.add(two);
        elements.add(three);

        return elements;


    }

    public void getchnumb(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pl = 0;
                for (DataSnapshot dh : snapshot.getChildren()) {
                    int zzz = 0;
                    for(DataSnapshot dsp : snapshot.child(dh.getKey()).getChildren()){
                        for(DataSnapshot  dsn : snapshot.child(dh.getKey()).child(dsp.getKey()).getChildren()){
                            chatmodel chdel = dsn.getValue(chatmodel.class);
                            if(chdel.getSeen() == null
                                    && !chdel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                pl=pl+1;
                                zzz=zzz+1;
                                break;
                            }
                        }
                        if(zzz == 1){
                            break;
                        }

                    }
                }
                getcallnumb(pl);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getcallnumb(Integer pl){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int po = 0;
                for(DataSnapshot dsp : snapshot.getChildren()){
                    for(DataSnapshot  dsn : snapshot.child(dsp.getKey()).getChildren()){
                        callmodel caldel = dsn.getValue(callmodel.class);
                        if(caldel.getGoing().equals("missed") && caldel.getTalktime().equals("false")){
                            po=po+1;
                        }
                    }


                }
                if(pl != 0 || po!=0){
                   if(pl!=0 && po==0){
                       nomess.setText(pl+"M");
                   }else if(pl==0 && po!=0){
                       nomess.setText(po+"C");
                   }else{
                       nomess.setText(pl+"M "+po+"C");
                   }
                   nomess.setVisibility(View.VISIBLE);
                }else{
                    nomess.setVisibility(GONE);
                }
                Toast.makeText(Mainactivity.this, pl+""+po+"", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setbuilder(){
        android.app.AlertDialog.Builder alt = new AlertDialog.Builder(this);
        alt.setInverseBackgroundForced(true);
        alt.setTitle("Log out").setMessage("Do you want to Log Out")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("userid").commit();
                        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("password").commit();
                        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("email").commit();
                        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("gender").commit();


                        Intent ity = new Intent(Mainactivity.this,login.class);
                        ity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ity.putExtra("kick","kick");
                        startActivity(ity);
                        finish();

                    }
                })
                .show();



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

    private boolean islocationservicerunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(MyService.class.getName().equals(service.service.getClassName())){

                    if(service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    private void startlocationservice(){
        if(!islocationservicerunning()){
            Intent in= new Intent(getApplicationContext(),MyService.class);
            in.setAction(constants.ACTION_START_LOCATION_SERVICE);
            startService(in);
        }
    }
    public void stoplocationservice(){
        if(islocationservicerunning()){
            Intent in = new Intent(getApplicationContext(),MyService.class);
            in.setAction(constants.ACTION_STOP_LOCATION_SERVICE);
            startService(in);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44 && grantResults.length>0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("showlocation").setValue("true");

                startlocationservice();
            }
        }
    }
}