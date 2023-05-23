package fragment;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.honeymoon.R;
import com.example.honeymoon.addstory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import adapters.statusadap;
import model.statusmodel;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_stories#} factory method to
 * create an instance of this fragment.
 */
public class home_stories extends Fragment {

    private RecyclerView cler,reler;
    private statusadap adp,sadp;
    private ImageView stopro,mn;
    private TextView udnam,nn;
    private RelativeLayout tap;
    private String fri;
    private SwipeRefreshLayout srl;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_stories, container, false);



        mn=view.findViewById(R.id.mn);
        nn=view.findViewById(R.id.nn);
        stopro=view.findViewById(R.id.poos);
        udnam=view.findViewById(R.id.udemy);
        tap=view.findViewById(R.id.tp);
        cler=view.findViewById(R.id.samll);
        reler=view.findViewById(R.id.reler);
        srl=view.findViewById(R.id.srl);



        get_curruser_prof();


        change_stry();
        get_menu();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_curruser_prof();
                srl.setRefreshing(false);
            }
        });
        mn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mn.setEnabled(false);
                startActivity(new Intent(getContext(), com.example.honeymoon.view.class));
                mn.setEnabled(true);
            }
        });
        tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tap.setEnabled(false);
                startActivity(new Intent(getContext(), addstory.class));
                tap.setEnabled(true);
            }
        });
        return view;

    }
    public void change_stry(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    List<statusmodel> stalst = new ArrayList<>();
                    List<statusmodel> statodel = new ArrayList<>();
                    for(DataSnapshot ds : snapshot.child("story").getChildren()){
                        statusmodel sta = ds.getValue(statusmodel.class);
                        SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                        try {
                            Calendar c = Calendar.getInstance();
                            c.setTime(formatter6.parse(sta.getTime()));
                            Calendar or = c;
                            c.add(Calendar.MINUTE, 60*24);// number of days to add
                            if(c.getTime().before(LocalDateTime.now().toDate())){
                                statodel.add(sta);
                            }else{
                                stalst.add(sta);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    for(statusmodel st : statodel){
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("allstories")
                                .child(st.getTime().substring(0,11)).child(st.getStatusid()).setValue(st);
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("story").child(st.getStatusid())
                                .removeValue();
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void get_menu(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mn.setVisibility(View.VISIBLE);
                }else{
                    mn.setVisibility(GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void get_curruser_prof(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot sss : snapshot.getChildren()){
                    if(sss.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        usermodel del = sss.getValue(usermodel.class);
                        Picasso.get().load(del.getImageurl()).placeholder(R.drawable.profile).into(stopro);
                        udnam.setText(del.getUsername());
                        if(del.getGender().equals("male")){
                            fri = "gfs";
                        }else{
                            fri = "bfs";
                        }
                    }
                }


                getfolw();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getuserwhohavestatus(List<String> listone ,List<String> listwo){
        List<usermodel> orderusrdl = new ArrayList<>();
        List<usermodel> seenorderusrdl=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderusrdl.clear();
                seenorderusrdl.clear();
                for (DataSnapshot st : snapshot.getChildren()) {
                    if (listone.contains(st.getKey())) {
                        usermodel uf = st.getValue(usermodel.class);
                        orderusrdl.add(uf);
                    }
                    if (listwo.contains(st.getKey())) {
                        usermodel uf = st.getValue(usermodel.class);
                        seenorderusrdl.add(uf);
                    }


                }

                adp=new statusadap(getContext(),orderusrdl);

                cler.setAdapter(adp);
                cler.setLayoutManager(new LinearLayoutManager(getContext()));


                sadp=new statusadap(getContext(),seenorderusrdl);
                reler.setAdapter(sadp);
                reler.setLayoutManager(new LinearLayoutManager(getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void seperate(List<String> hvsta,List<String> oldhvsta){
        List<String> orderedstr =new ArrayList<>();
        List<String> seenorderstr = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dt : snapshot.child("matches").getChildren()){
                    if(hvsta.contains(dt.getKey())){
                        orderedstr.add(dt.getKey());
                    }
                    if(oldhvsta.contains(dt.getKey())){
                        seenorderstr.add(dt.getKey());
                    }
                }

                for (DataSnapshot ssd : snapshot.child("crushs").getChildren()) {
                    if(hvsta.contains(ssd.getKey()) && !orderedstr.contains(ssd.getKey())){
                        orderedstr.add(ssd.getKey());
                    }
                    if(oldhvsta.contains(ssd.getKey()) && !seenorderstr.contains(ssd.getKey())){
                        seenorderstr.add(ssd.getKey());
                    }
                }



                for (DataSnapshot dss : snapshot.child(fri).getChildren()){
                    if(hvsta.contains(dss.getKey()) && !orderedstr.contains(dss.getKey())){
                        orderedstr.add(dss.getKey());

                    }
                    if(oldhvsta.contains(dss.getKey()) && !seenorderstr.contains(dss.getKey())){
                        seenorderstr.add(dss.getKey());

                    }
                }
                for(DataSnapshot dfg : snapshot.child("admirers").getChildren()){
                    if(hvsta.contains(dfg.getKey()) && !orderedstr.contains(dfg.getKey())){
                        orderedstr.add(dfg.getKey());
                    }
                    if(oldhvsta.contains(dfg.getKey()) && !seenorderstr.contains(dfg.getKey())){
                        seenorderstr.add(dfg.getKey());
                    }
                }

                for(String io :hvsta){
                    if(!orderedstr.contains(io)){
                        orderedstr.add(io);
                    }
                }
                for(String i :oldhvsta){
                    if(!seenorderstr.contains(i)){
                        seenorderstr.add(i);
                    }
                }


                Collections.reverse(orderedstr);
                Collections.reverse(seenorderstr);

                getuserwhohavestatus(orderedstr,seenorderstr);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getfollhvsta(List<String> followgs){


        List<String> hvfollsta = new ArrayList<>();
        List<String> oldhvfollsta = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hvfollsta.clear();
                oldhvfollsta.clear();
                for(String i : followgs){
                    int o =0;
                    if(snapshot.child(i).child("story").exists()) {
                        for (DataSnapshot dss : snapshot.child(i).child("story").getChildren()) {
                            statusmodel sdel = dss.getValue(statusmodel.class);
                            if (!snapshot.child(i).child("seen").child(sdel.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                                hvfollsta.add(i);

                                break;
                            } else {

                                o = o + 1;
                                if ((snapshot.child(i).child("story").getChildrenCount() + "").equals( o + "")) {
                                    oldhvfollsta.add(i);
                                }

                            }
                        }
                    }
                }




                if(hvfollsta.isEmpty() && oldhvfollsta.isEmpty()){
                    nn.setVisibility(View.VISIBLE);
                }else{
                    nn.setVisibility(View.INVISIBLE);
                }

                seperate(hvfollsta,oldhvfollsta);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getfolw(){
        List<String> follwgs = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("followings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                follwgs.clear();
                for(DataSnapshot ss: snapshot.getChildren()){
                    follwgs.add(ss.getKey());
                }

                if(!follwgs.isEmpty()) {
                    getfollhvsta(follwgs);
                }else{
                    nn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}