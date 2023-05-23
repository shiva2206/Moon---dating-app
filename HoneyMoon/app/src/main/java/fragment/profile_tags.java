package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.effecy;
import adapters.gridadp;
import model.postmodel;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_tags#} factory method to
 * create an instance of this fragment.
 */
public class profile_tags extends Fragment {

    private RecyclerView recy;
    private String userid;
//    private String foll;
    private List<String> lstkey,lstusr;
    private List<postmodel> pstlst,fillst;
    private gridadp adp;
    private TextView nn;

    public profile_tags(String userid) {
        this.userid = userid;
//        this.foll = foll;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tags, container, false);


        pstlst=new ArrayList<>();
        lstkey=new ArrayList<>();
        lstusr=new ArrayList<>();
        fillst=new ArrayList<>();
        recy=view.findViewById(R.id.rere);
        nn = view.findViewById(R.id.nn);
        adp = new gridadp(getContext(),userid,fillst,false);
        recy.setAdapter(adp);
        recy.setLayoutManager(new GridLayoutManager(getContext(),3));
        gettags();
        return view;
    }
    public void getpub(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fillst.clear();
                List<postmodel> pstdelst = new ArrayList<>();
                List<String> dalst = new ArrayList<>();
                for(postmodel pdel : pstlst){
                    usermodel udel = snapshot.child(pdel.getPublisherid()).getValue(usermodel.class);
                    if(udel.getMode().equals("public") || udel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        pstdelst.add(pdel);
                        dalst.add(pdel.getTime());
                    }
                }

                for(String strdt : effecy.instance.getdalst(dalst)){
                    for(postmodel pdel : pstdelst){
                        if(pdel.getTime().equals(strdt) && !fillst.contains(pdel)){
                            fillst.add(pdel);
                        }
                    }
                }

                Collections.reverse(fillst);
                if(fillst.isEmpty()){
                    nn.setText("No Posts To Show");
                    nn.setVisibility(View.VISIBLE);
                }else{

                    nn.setVisibility(View.INVISIBLE);
                }
                adp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getpsts(){
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pstlst.clear();
                for(int y = 0 ;y<lstusr.size();y++){
                    for(DataSnapshot dss : snapshot.child(lstusr.get(y)).child("posts").getChildren()){
                        if(dss.getKey().equals(lstkey.get(y))){
                            pstlst.add(dss.getValue(postmodel.class));
                        }
                    }
                }
                getpub();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void gettags(){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                lstusr.clear();
                lstkey.clear();
                for(DataSnapshot dss : snapshot.getChildren()){
                    lstkey.add(dss.getKey());
                    lstusr.add(dss.getValue()+"");
                }


                getpsts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}