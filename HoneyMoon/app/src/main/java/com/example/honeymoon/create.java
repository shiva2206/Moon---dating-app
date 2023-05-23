package com.example.honeymoon;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import adapters.statusadap;
import model.usermodel;
import utility.NetworkChangeListener;

public class create extends AppCompatActivity {
    private EditText username,nme,emil,pass,repass;
    private TextView login,dob,hnytxt;
    private Button create;
    private String[] hlst,wlst;
    private Spinner heigt,weigt;
    private ArrayAdapter<String> headp,weadp;
    private statusadap sadp;
    private RecyclerView recy;
    private List<usermodel> udelst;
    private ProgressDialog pd;
    private RadioGroup gendr,status,privacy;
    private FirebaseAuth auth;
    private DatabaseReference fbdr;
    private String gend,stat,priv;
    private DatePickerDialog.OnDateSetListener setliseneer;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create);

        username=findViewById(R.id.usmes);
        nme=findViewById(R.id.name);
        emil=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        repass=findViewById(R.id.repassword);
        create=findViewById(R.id.register);
        dob=findViewById(R.id.dob);
        hnytxt= findViewById(R.id.honeytext);
        gendr=findViewById(R.id.radsex);
        status=findViewById(R.id.showcru);
        privacy=findViewById(R.id.radpri);
        recy=findViewById(R.id.starecy);

        heigt= (Spinner) findViewById(R.id.height);
        weigt= (Spinner) findViewById(R.id.weight);
        login=findViewById(R.id.logn);
        auth=FirebaseAuth.getInstance();
        fbdr=FirebaseDatabase.getInstance().getReference();
        pd=new ProgressDialog(this);
        udelst=new ArrayList<>();

        gend="male";
        stat="single";
        priv="public";


        hlst = new String[130];
        wlst=new String[130];

        hlst[0]="Ignore";
        wlst[0]="Ignore";
        for(int p=1;p<130;p++){
            hlst[p]=(p+90)+"";
            wlst[p]=p+40+"";
        }
        headp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hlst);
        weadp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, wlst);

        heigt.setAdapter(headp);
        weigt.setAdapter(weadp);

//        sadp=new statusadap(create.this,udelst);
//        recy.setAdapter(sadp);
//        recy.setLayoutManager(new LinearLayoutManager(create.this));
//        getusdel();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dob.setClickable(false);
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int mon = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dapic = new DatePickerDialog(create.this,
                        android.R.style.Theme_Holo_Light_Dialog,setliseneer,year,mon,day);

                dapic.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dapic.show();
                dob.setEnabled(true);
            }
        });
        setliseneer = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month =month+1;
                String date = day+"/"+month+"/"+year;
                dob.setText(date);
                dob.setClickable(true);
            }
        };
        gendr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male:
                        gend = "male";
                        break;
                    case R.id.female:
                        gend = "female";
                        break;

                }
            }
        });
        privacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.pulic:
                        priv="public";
                        break;
                    case R.id.priate:
                        priv="private";
                        break;

                }
            }
        });
        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sinle:
                        stat = "single";
                        break;
                    case R.id.commited:
                        stat = "committed";
                        break;
                    case R.id.married:
                        stat="married";
                        break;

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setEnabled(false);
                Intent itt = new Intent(create.this,Mainactivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(create.this,hnytxt, ViewCompat.getTransitionName(hnytxt));
                startActivity(itt,options.toBundle());

                login.setEnabled(true);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setEnabled(false);
                username.setEnabled(false);
                nme.setEnabled(false);
                emil.setEnabled(false);
                pass.setEnabled(false);
                repass.setEnabled(false);
                gendr.setEnabled(false);
                status.setEnabled(false);
                privacy.setEnabled(false);
                pd.show();
                String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^`{|} ";
                username.setTag("");

                for(int pl = 0;pl < specialCharactersString.length();pl++){

                    if(username.getText().toString().contains(specialCharactersString.charAt(pl)+"")) {
                        username.setText("");
                        Toast.makeText(create.this, "Special Chars shouldn't be used except '_'", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }

                for(usermodel udel : sadp.getUsdmdl()){
                    if(username.getText().toString().trim().equals(udel.getUsername())){
                        Toast.makeText(create.this, "username already exist", Toast.LENGTH_SHORT).show();
                        username.setText("");
                        break;
                    }
                }

                effecy.instance.calculateage(dob.getText()+"",dob,false);
                if(username.getText().equals("")){
                    Toast.makeText(create.this, "Fill username properly", Toast.LENGTH_SHORT).show();

                    create.setEnabled(true);
                } else if(TextUtils.isEmpty(dob.getText())||TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(nme.getText()) || TextUtils.isEmpty(emil.getText()) || TextUtils.isEmpty(pass.getText()) || TextUtils.isEmpty(repass.getText())){
                    Toast.makeText(create.this, " Enter The details Fully", Toast.LENGTH_SHORT).show();
                    username.setText("");
                    nme.setText("");
                    emil.setText("");
                    pass.setText("");
                    repass.setText("");
                    dob.setText("");
                    create.setEnabled(true);

                }
                else if(!pass.getText().toString().equals(repass.getText().toString())){
                    Toast.makeText(create.this, "Password is not matching ", Toast.LENGTH_SHORT).show();
                    repass.setText("");
                    create.setEnabled(true);

                }
                else if(pass.getText().toString().length()<6){
                    pass.setText("");
                    repass.setText("");
                    Toast.makeText(create.this, "Password less than 6 char", Toast.LENGTH_SHORT).show();
                    create.setEnabled(true);


                }else if(18>Integer.parseInt(dob.getTag()+"")){
                    Toast.makeText(create.this, "Your age is less than 18", Toast.LENGTH_SHORT).show();
                } else{

                    auth.createUserWithEmailAndPassword(emil.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(create.this, "Verification Email sent, click it and login in ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(create.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });



                            HashMap<String, Object> hmp = new HashMap<>();

                            hmp.put("userid", auth.getCurrentUser().getUid());
                            hmp.put("password", pass.getText().toString());
                            if((dob.getText()+"").equals("")){
                                hmp.put("dob","1/1/2000");
                            }else{
                                hmp.put("dob",dob.getText()+"");
                            }

                            hmp.put("about","");
                            hmp.put("email", emil.getText().toString());
                            hmp.put("gender",gend);
                            hmp.put("mium","freemium");
                            hmp.put("imageurl", "https://image.shutterstock.com/image-vector/male-user-account-profile-circle-260nw-467503055.jpg");
                            hmp.put("name", nme.getText().toString());
                            hmp.put("status",stat);

//                                            hmp.put("line",DateFormat.getDateTimeInstance().format(new Date()));
                            hmp.put("verified","no");

                            hmp.put("username", username.getText().toString());

                            hmp.put("mode",priv);

                            hmp.put("showlastseen","true");
                            hmp.put("showlocation","false");
                            hmp.put("coverimage","null");
                            hmp.put("devicetoken","null");
                            hmp.put("showmat","number");
                            hmp.put("showcru","number");
                            hmp.put("showadm","number");
                            hmp.put("showfri","number");
//                                            hmp.put("latitude","20");
//                                            hmp.put("longitude","80");
                            hmp.put("showaddcrubut","true");
                            hmp.put("istagpermitted","true");
                            hmp.put("bluetick","false");

                            hmp.put("height",heigt.getSelectedItem()+"");
                            hmp.put("weight",weigt.getSelectedItem()+"");

                            HashMap<String, Object> vary = new HashMap<>();
                            vary.put("userid", auth.getCurrentUser().getUid());
                            vary.put("latitude","20");
                            vary.put("longitude","80");
                            vary.put("line", "offline");


                            fbdr.child("users").child(auth.getCurrentUser().getUid()).setValue(hmp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(create.this, "Succesfully created", Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance().getReference().child("vary").child(auth.getCurrentUser().getUid()).setValue(vary);
                                    auth.signInWithEmailAndPassword(emil.getText().toString(), pass.getText().toString());
                                    Intent inty = new Intent(create.this,login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(create.this,hnytxt, ViewCompat.getTransitionName(hnytxt));
                                    startActivity(inty,options.toBundle());
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(create.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }



                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(create.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            create.setEnabled(true);
                        }
                    });


                }

                pd.dismiss();
                create.setEnabled(true);
                username.setEnabled(true);
                nme.setEnabled(true);
                emil.setEnabled(true);
                pass.setEnabled(true);
                repass.setEnabled(true);
                gendr.setEnabled(true);
                status.setEnabled(true);
                privacy.setEnabled(true);
            }
        });
    }
    public void getusdel(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                udelst.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    usermodel userdel = ds.getValue(usermodel.class);
                    udelst.add(userdel);

                }
//                sadp.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}