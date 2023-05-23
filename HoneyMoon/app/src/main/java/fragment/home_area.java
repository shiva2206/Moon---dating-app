package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import adapters.homepostadp;
import butterknife.ButterKnife;
import model.postmodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_area} factory method to
 * create an instance of this fragment.
 */
public class home_area extends Fragment {
    private homepostadp adp;
    private TextView nn;
    private SwipeRefreshLayout srl;
    private RecyclerView recy;
    private List<postmodel> postmodelList,posmos;
    private List<String> follogns;
    private boolean ishomearea;


    public home_area(boolean ishomearea) {
        this.ishomearea = ishomearea;
    }

    public home_area(List<postmodel> posmos,boolean ishomearea) {
        this.posmos = posmos;
        this.ishomearea=ishomearea;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_area, container, false);
        ButterKnife.inject(getActivity());
        follogns=new ArrayList<>();
        postmodelList = new ArrayList<>();
        nn=view.findViewById(R.id.nn);
        srl=view.findViewById(R.id.srl);
        if(ishomearea){
            posmos=new ArrayList<>();

            adp=new homepostadp(getContext(),postmodelList);
            recy = view.findViewById(R.id.rere);
            recy.setAdapter(adp);
            recy.setLayoutManager(new LinearLayoutManager(getContext()));

            gettingfollngs();
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    gettingfollngs();
                    srl.setRefreshing(false);
                }
            });

        }else{
            adp=new homepostadp(getContext(),postmodelList);
            recy = view.findViewById(R.id.rere);
            recy.setAdapter(adp);
            recy.setLayoutManager(new LinearLayoutManager(getContext()));

            getonepost();
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getonepost();
                    srl.setRefreshing(false);
                }
            });


        }
        return view;


    }
    public void getonepost(){
        FirebaseDatabase.getInstance().getReference().child("info").child(posmos.get(0).getPublisherid()).child("posts").child(posmos.get(0).getPostid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postmodelList.clear();
                postmodel pdel= snapshot.getValue(postmodel.class);
                if(pdel != null){
                    postmodelList.add(pdel);
                }
                adp.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getposts(){

            FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    postmodelList.clear();
                    posmos.clear();
                    List<String> dalst = new ArrayList<>();
                    for(String ig : follogns) {
                        for (DataSnapshot ds : snapshot.child(ig).child("posts").getChildren()) {
                            postmodel po = ds.getValue(postmodel.class);
                            posmos.add(po);
                            dalst.add(po.getTime());
                        }
                    }
                    for(String date : effecy.instance.getdalst(dalst)){
                        for(postmodel pdel : posmos){
                            if(pdel.getTime().equals(date) && !postmodelList.contains(pdel)){
                                postmodelList.add(pdel);
                            }
                        }
                    }
                    Collections.reverse(postmodelList);
                    if (postmodelList.isEmpty()) {
                        nn.setVisibility(View.VISIBLE);
                    } else {
                        nn.setVisibility(View.INVISIBLE);
                    }
                    adp.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }
    private void gettingfollngs(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("followings").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        follogns.clear();
                        for (DataSnapshot dt : snapshot.getChildren()) {
                            follogns.add(dt.getKey());
                        }
                        if (follogns.isEmpty()) {
                            nn.setVisibility(View.VISIBLE);
                        } else {
                            nn.setVisibility(View.INVISIBLE);

                        }
                        getposts();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}