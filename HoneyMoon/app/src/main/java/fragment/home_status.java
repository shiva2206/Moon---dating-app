package fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.honeymoon.R;
import com.example.honeymoon.addstory;
import com.example.honeymoon.view;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapters.statusadap;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_status#} factory method to
 * create an instance of this fragment.
 */
public class home_status extends Fragment {
    private List<String> followg,follhvsta;
    private List<usermodel> usemdl;
    private RecyclerView cler;
    private statusadap adp;
    private ImageView stopro,mn;
    private TextView udnam;
    private RelativeLayout tap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_status, container, false);
        followg=new ArrayList<>();
        follhvsta=new ArrayList<>();

        mn=view.findViewById(R.id.mn);
        stopro=view.findViewById(R.id.poos);
        udnam=view.findViewById(R.id.udemy);
        tap=view.findViewById(R.id.tp);
        usemdl=new ArrayList<>();
        cler=view.findViewById(R.id.samll);

        adp=new statusadap(getContext(),usemdl);

        cler.setAdapter(adp);
        cler.setLayoutManager(new LinearLayoutManager(getContext()));
        getsti();
        getfolw();
        getmn();
        mn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getContext(), com.example.honeymoon.view.class));
            }
        });
        tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), addstory.class));

            }
        });
        return view;

    }public void getmn(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getsti(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot sss : snapshot.getChildren()){
                        if(sss.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            usermodel del = sss.getValue(usermodel.class);
                            Picasso.get().load(del.getImageurl()).placeholder(R.drawable.profile).into(stopro);
                            udnam.setText(del.getUsername());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }public void getu(List<String> follhavsta){
        for(String ig : follhavsta) {
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot st : snapshot.getChildren()) {
                        if (st.getKey().equals(ig)) {
                            usermodel uf = st.getValue(usermodel.class);

                            usemdl.add(uf);

                        }
                    }
                    adp.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    public void getusr(List<String> followg){
        usemdl.clear();
        for(String i : followg){
            FirebaseDatabase.getInstance().getReference().child("info").child(i).child("story").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.exists()){
                       follhvsta.add(i);
                   }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        getu(follhvsta);


    }
    public void getfolw(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("followings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    followg.clear();
                    for(DataSnapshot ss: snapshot.getChildren()){
                        followg.add(ss.getKey());
                    }
                    if(!followg.isEmpty()) {
                        getusr(followg);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}