package com.example.honeymoon;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import adapters.effecy;
import model.MyService;
import model.constants;
import model.usermodel;
import utility.NetworkChangeListener;

public class login_two extends AppCompatActivity {
    private String userid,password,email,gender;
    private Button login;
    private ImageView prof;
    private TextView usernme,hnytxt;
    private ImageView close;
    private FirebaseAuth auth;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login_two);

        login=findViewById(R.id.log_in);
        prof=findViewById(R.id.prof);
        hnytxt=findViewById(R.id.honeytext);
        usernme=findViewById(R.id.usernmw);
        close=findViewById(R.id.close);
        auth=FirebaseAuth.getInstance();


        effecy sec= new effecy("sec");
        System.out.println(
                "Login_two "+this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("userid","none")+
                        this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender","none")
        );

        userid = login_two.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("userid","none");
        email=login_two.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("email","none");
        password = login_two.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("password","none");
        gender=login_two.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender","none");


        if(userid == null){
            Intent ity = new Intent(login_two.this,login.class);
            startActivity(ity);
            finish();
        }else{
            Toast.makeText(login_two.this,login_two.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender","none") , Toast.LENGTH_SHORT).show();
            String where = getIntent().getStringExtra("where");
            if (where!=null){
                start(where);
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               login.setEnabled(false);
               start(null);
               login.setEnabled(true);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.setEnabled(false);
                gotologin();
                close.setEnabled(true);
            }
        });
        getuserinfo();
    }
    public void gotologin(){
        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("userid").commit();
        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("password").commit();
        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("email").commit();
        getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("gender").commit();

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(auth.getCurrentUser().getUid()).child("devicetoken").setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                    }
                });


        stoplocationservice();
        Intent ity = new Intent(login_two.this,login.class);
        ity.putExtra("kick","kick");
        startActivity(ity);
        finish();
    }
    public void start(String where){

        Intent intr = new Intent(login_two.this,Mainactivity.class);
        if (where != null){
            intr.putExtra("where",where);
            intr.putExtra("userid",getIntent().getStringExtra("userid"));
            intr.putExtra("postid",getIntent().getStringExtra("postid"));
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(login_two.this,hnytxt, ViewCompat.getTransitionName(hnytxt));
                startActivity(intr,options.toBundle());
            }
        });


    }
    public void getuserinfo(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dss : snapshot.getChildren()){
                    if(dss.getKey().equals(userid)){
                        usermodel udell = dss.getValue(usermodel.class);
                        Picasso.get().load(udell.getImageurl()).into(prof);
                        usernme.setText(udell.getUsername());
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("status",udell.getStatus()).commit();
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("mode",udell.getMode()).commit();
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlastseen",udell.getShowlastseen()).commit();
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlocation",udell.getShowlocation()).commit();
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showmat",udell.getShowmat()).commit();
//
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showcru",udell.getShowcru()).commit();
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showadm",udell.getShowadm()).commit();
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showfri",udell.getShowfri()).commit();
//                        getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showaddcrubut",udell.getShowaddcrubut()).commit();
//


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean islocationservicerunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(MyService.class.getName().equals(service.service.getClassName())){

                    if(service.foreground){
                        System.out.println("yesssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    public void stoplocationservice(){
        if(islocationservicerunning()){
            Intent in = new Intent(getApplicationContext(),MyService.class);
            in.setAction(constants.ACTION_STOP_LOCATION_SERVICE);
            startService(in);
        }
    }

    @Override
    protected void onResume() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        gotologin();
        super.onBackPressed();
    }
}