package com.example.honeymoon;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import model.usermodel;
import utility.NetworkChangeListener;

public class editprof extends AppCompatActivity {
    private ImageView prof;
    private EditText username,name,abt;
    private RadioGroup rg;
    private String[] hlst,wlst;
    private Spinner heigt,weigt;
    private ArrayAdapter<String> headp,weadp;
    private String stat ;
    private Button cahnge;
    private SwipeRefreshLayout srl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private List<usermodel> udelst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_editprof);

        heigt= (Spinner) findViewById(R.id.height);
        weigt= (Spinner) findViewById(R.id.weight);
        prof=findViewById(R.id.profffd);
        username=findViewById(R.id.usmes);
        name=findViewById(R.id.name);
        rg=findViewById(R.id.showcru);
        cahnge=findViewById(R.id.changes);
        abt=findViewById(R.id.edet);
        srl=findViewById(R.id.srl);
//        recy=findViewById(R.id.starecy);

        udelst=new ArrayList<>();

//        sadp=new statusadap(editprof.this,udelst);
//        recy.setAdapter(sadp);
//        recy.setLayoutManager(new LinearLayoutManager(editprof.this));

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
        cur();


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

                }
            }
        });

        cahnge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cahnge.setEnabled(false);
                username.setEnabled(false);
                name.setEnabled(false);
                abt.setEnabled(false);
                String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^`{|} ";
                for(int pl = 0;pl < specialCharactersString.length();pl++){
                    username.setTag("");
                    if(username.getText().toString().contains(specialCharactersString.charAt(pl)+"")) {
                        username.setText("");
                        Toast.makeText(editprof.this, "Special Chars shouldn't be used except '_'", Toast.LENGTH_SHORT).show();
                        username.setTag("err");
                        break;
                    }

                }
                getusdel();
                for(usermodel udel : udelst){

                    if(username.getText().toString().trim().equals(udel.getUsername() )){
                        if(!udel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            Toast.makeText(editprof.this, "username already exist", Toast.LENGTH_SHORT).show();
                            username.setText("");
                            break;
                        }

                    }
                }



                if(username.getText().equals("")) {
                    Toast.makeText(editprof.this, "Fill the Username properly", Toast.LENGTH_SHORT).show();

                }
                else if(!TextUtils.isEmpty(username.getText()) && !TextUtils.isEmpty(name.getText())){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("username").setValue(username.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("name").setValue(name.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("status").setValue(stat);
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("about").setValue(abt.getText().toString().trim());
                    Toast.makeText(editprof.this, "Successfully Changed", Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("height").setValue(heigt.getSelectedItem()+"");
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("weight").setValue(weigt.getSelectedItem()+"");

                }
                cahnge.setEnabled(true);
                username.setEnabled(true);
                name.setEnabled(true);
                abt.setEnabled(true);
            }
        });
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cur();
                srl.setRefreshing(false);
            }
        });

    }



    public void cur(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot so : snapshot.getChildren()){
                        if(so.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            usermodel udr = so.getValue(usermodel.class);
                            username.setText(udr.getUsername());
                            name.setText(udr.getName());
                            abt.setText(udr.getAbout());
                            stat=udr.getStatus();
                            heigt.setSelection(headp.getPosition(udr.getHeight()));
                            weigt.setSelection(weadp.getPosition(udr.getWeight()));


                            Picasso.get().load(udr.getImageurl()).placeholder(R.drawable.logo_background).into(prof);

                            if (udr.getStatus()=="single"){
                                rg.check(R.id.sinle);
                            }else if(udr.getStatus()=="committed"){
                                rg.check(R.id.commited);
                            }else{
                                rg.check(R.id.married);
                            }
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getusdel(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                udelst.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    usermodel userdel = ds.getValue(usermodel.class);
                    udelst.add(userdel);

                }


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