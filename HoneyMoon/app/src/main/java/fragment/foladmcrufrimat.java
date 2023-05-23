package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.honeymoon.R;
import com.example.honeymoon.mutualfoll;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import adapters.hashadp;
import adapters.searchadp;
import model.usermodel;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link foladmcrufrimat#} factory method to
 * create an instance of this fragment.
 */
public class foladmcrufrimat extends Fragment {

    private String userid,what,isfollowing;
    private Context context;
    private usermodel userinfo,curruserinfo;
    private searchadp seradp;
    private List<String> lststr,secsrlt;
    private List<usermodel> lstusr;
    private InterstitialAd mInterstitialAd;
    private effecy cl;
    private TextView nobu;
    private EditText etx;
    private RecyclerView urecy;
    private TextView nn;
    public static String sg="";

    public foladmcrufrimat(Context context,String userid, String what,TextView nobu) {
        this.userid = userid;
        this.context=context;
        this.what = what;
        this.nobu=nobu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_foladmcrufrimat, container, false);

        etx = view.findViewById(R.id.etx);
        nn=view.findViewById(R.id.nn);
        urecy = view.findViewById(R.id.urecy);

        lstusr=new ArrayList<>();
        lststr=new ArrayList<>();
        secsrlt=new ArrayList<>();

        seradp=new searchadp(context,lstusr,"mutual",userid);
        urecy.setAdapter(seradp);
        urecy.setLayoutManager(new LinearLayoutManager(context));

        getstr();
        etx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sg = s +"";
                getuser();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public List<usermodel> getLstusr() {
        return lstusr;
    }

    public void getuser(){
        Query que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(sg).endAt(sg+"\uf8ff");
        que.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstusr.clear();
                for(DataSnapshot dss : snapshot.getChildren()){

                    if(dss.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        curruserinfo = dss.getValue(usermodel.class);
                    }
                    if(dss.getKey().equals(userid)){
                        userinfo     = dss.getValue(usermodel.class);
                    }
                    if(secsrlt.contains(dss.getKey())){
                        lstusr.add(dss.getValue(usermodel.class));
                    }
                }
                nobu.setText(lstusr.size()+"");
                seradp.notifyDataSetChanged();

                if((what.equals("followings") || what.equals("followers")) && (userinfo.getMode().equals("public") || isfollowing.equals("yes"))){
                    urecy.setVisibility(VISIBLE);
                }else{
                    if((!what.equals("followings") && !what.equals("followers"))){
                        if(what.equals("admirers") && userinfo.getShowadm().equals("all") && curruserinfo.getShowadm().equals("all")){
                            urecy.setVisibility(VISIBLE);
                        }else if(what.equals("crushs") && userinfo.getShowcru().equals("all") && curruserinfo.getShowcru().equals("all")){
                            urecy.setVisibility(VISIBLE);
                        }else if((what.equals("bfs") || what.equals("gfs")) && userinfo.getShowfri().equals("all") && curruserinfo.getShowcru().equals("all")){
                            urecy.setVisibility(VISIBLE);
                        }else{
                            urecy.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                if(urecy.getVisibility() == INVISIBLE){
                    nn.setText("You Cant See This");
                    nn.setVisibility(VISIBLE);

                }else{
                    if(lstusr.isEmpty()){
                        nn.setText("None");
                        nn.setVisibility(VISIBLE);

                    }else{
                        nn.setVisibility(GONE);
                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getmut(){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                secsrlt.clear();
                for(DataSnapshot dh : snapshot.child(what).getChildren()){
                    if(lststr.contains(dh.getKey())){
                        secsrlt.add(dh.getKey());
                    }
                }
                if(snapshot.child("followings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    isfollowing = "yes";
                    getuser();
                }else{
                    isfollowing="no";
                    getuser();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getstr(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(what).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lststr.clear();
                for(DataSnapshot sn : snapshot.getChildren()){
                    lststr.add(sn.getKey());
                }
                getmut();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}