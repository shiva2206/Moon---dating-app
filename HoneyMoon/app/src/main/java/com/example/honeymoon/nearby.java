package com.example.honeymoon;

import static android.view.View.GONE;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import array_adapters.search_arradp;
import model.constants;
import model.usermodel;
import utility.NetworkChangeListener;
//original activity
public class nearby extends AppCompatActivity {


    private List<usermodel> lstuser;
    private List<String> distlst,strlst;
    private TextView nn,nby;
    private search_arradp sadp;
    private SwipeFlingAdapterView swie;
    private effecy cl;
    private Bundle bundle;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_nearby);


        nn=findViewById(R.id.nn);
        nby=findViewById(R.id.nby);
        swie=findViewById(R.id.uswipe);
        lstuser =new ArrayList<>();
        distlst=new ArrayList<>();
        strlst=new ArrayList<>();

        bundle = getIntent().getExtras();
//        tny = new Intent(nearby.this,Mainactivity.class);
//
//        tny.putExtra("gender", (String) bundle.get("gender"));
//        tny.putExtra("status", (String) bundle.get("status"));
//        tny.putExtra("distance", (String) bundle.get("distance"));
//        tny.putExtra("agefrom", (String) bundle.get("agefrom"));
//        tny.putExtra("ageto", (String) bundle.get("ageto"));
//        tny.putExtra("nearby","nearby");
        sadp = new search_arradp(nearby.this,R.layout.adap_arr_search,lstuser,distlst);

//        cl=new effecy(nearby.this);

        swie.setAdapter(sadp);

        for(String key : bundle.keySet()){

            strlst.add(key);

        }
        getusers();
        swie.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

                lstuser.remove(0);
                distlst.remove(0);
                sadp.notifyDataSetChanged();
                nby.setText("NEARBY "+sadp.getAllusers().size());
                if(lstuser.isEmpty()){

//                    startActivity(tny);
                    finish();
                }
            }

            @Override
            public void onLeftCardExit(Object o) {
                if (lstuser.size()!=0) {
                    setswiped(lstuser.get(0));
                }
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(nearby.this,o+"" , Toast.LENGTH_SHORT).show();
                usermodel usd = (usermodel) o;
                FirebaseDatabase.getInstance().getReference().child("users").child(usd.getUserid()).child(constants.SWIPERIGHT).setValue((Integer.parseInt(usd.getSwipes()+"")+1)+"");
                setswiped(usd);
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

    }
    public void getusers(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstuser.clear();
                distlst.clear();
                for(String uid : strlst){
                    usermodel udel = snapshot.child(uid).getValue(usermodel.class);
                    if(udel!=null) {
                        lstuser.add(udel);
                        distlst.add(bundle.get(uid).toString());
                    }
                }
                if(lstuser.isEmpty()){
                    nn.setVisibility(View.VISIBLE);
                    finish();
                }else{
                    nn.setVisibility(GONE);
                }
                nby.setText("NEARBY "+lstuser.size());
                sadp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setswiped(usermodel udel){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(constants.SWIPED).child(udel.getUserid()).setValue("true");
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
        super.onBackPressed();
    }

}