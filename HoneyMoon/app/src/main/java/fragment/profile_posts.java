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
 * Use the {@link profile_posts#} factory method to
 * create an instance of this fragment.
 */
public class profile_posts extends Fragment {

    private String userid;
//    private String foll;
    private List<postmodel> postmodelst;
    private gridadp adp;
    private RecyclerView recyclerView;
    private TextView nn;


    public profile_posts(String userid) {
        this.userid = userid;
//        this.foll = foll;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_posts, container, false);

        postmodelst=new ArrayList<>();
        recyclerView=view.findViewById(R.id.pstrecy);
        nn=view.findViewById(R.id.nn);
        adp= new gridadp(getContext(),userid,postmodelst,true);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        getposts();
        getmode();

        return view;
    }
    public void getposts(){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postmodelst.clear();

                        List<postmodel> pstdel = new ArrayList<>();
                        List<String> dalst = new ArrayList<>();

                        if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            postmodelst.add(new postmodel());
                        }

                        for (DataSnapshot dt : snapshot.getChildren()) {
                            postmodel pdel =dt.getValue(postmodel.class);
                            pstdel.add(pdel);
                            dalst.add(pdel.getTime());
                        }

                        List<String> dddaalllssttt =  effecy.instance.getdalst(dalst);
                        Collections.reverse(dddaalllssttt);
                        for(String strdat : dddaalllssttt){
                            for(postmodel pdel : pstdel){
                                if(pdel.getTime().equals(strdat) &&  !postmodelst.contains(pdel)){
                                    postmodelst.add(pdel);
                                    break;
                                }
                            }
                        }
                        adp.notifyDataSetChanged();

                        if(postmodelst.isEmpty()){
                            nn.setVisibility(View.VISIBLE);
                        }else{
                            nn.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getmode() {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot df : snapshot.getChildren()) {
                    if (df.getKey().equals(userid)) {
                        usermodel udr = df.getValue(usermodel.class);
//                        if (!userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && udr.getMode().equals("private") && !foll.equals("following")) {
//                            recyclerView.setVisibility(GONE);
//
//                        } else {
//                            recyclerView.setVisibility(View.VISIBLE);
//
//                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}