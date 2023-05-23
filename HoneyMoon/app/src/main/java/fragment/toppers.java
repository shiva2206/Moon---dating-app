package fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.honeymoon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapters.topperadp;
import model.toppermodel;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link toppers#} factory method to
 * create an instance of this fragment.
 */
public class toppers extends Fragment {

    private topperadp toppadp;
    private List<usermodel> lstsrt;
    private RecyclerView recy;
    private Context context;
    private List<toppermodel> toppmdel;
    private SwipeRefreshLayout srl;
    public toppers(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toppers, container, false);


        recy=view.findViewById(R.id.jklp);
        srl=view.findViewById(R.id.srl);
        toppmdel=new ArrayList<>();
        lstsrt=new ArrayList<>();


        toppadp=new topperadp(getContext(),toppmdel);
        recy.setAdapter(toppadp);
        recy.setLayoutManager(new LinearLayoutManager(context));


        allus();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allus();
                srl.setRefreshing(false);
            }
        });

        return view;
    }
    public void allus(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstsrt.clear();
                for(DataSnapshot dss : snapshot.getChildren()){
                    usermodel uel =dss.getValue(usermodel.class);
                    if(!uel.getShowmat().equals("lock")  && !uel.getShowadm().equals("lock") && !uel.getShowcru().equals("lock")){
                        lstsrt.add(uel);
                    }

                }

                getus();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getus(){
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                toppmdel.clear();
                for(usermodel str : lstsrt) {
                    double a = 0;
                    List<Integer> intsss = new ArrayList<>();
                    intsss.add(Integer.parseInt(snapshot.child(str.getUserid()).child("matches").getChildrenCount() + ""));

                    intsss.add(Integer.parseInt(snapshot.child(str.getUserid()).child("admirers").getChildrenCount() + ""));

                    if (str.getGender().equals("male")) {
                        intsss.add((Integer.parseInt(snapshot.child(str.getUserid()).child("gfs").getChildrenCount() + "")));
                    } else {
                        intsss.add((Integer.parseInt(snapshot.child(str.getUserid()).child("bfs").getChildrenCount() + "")));
                    }

                    a = a + Integer.parseInt(snapshot.child(str.getUserid()).child("matches").getChildrenCount() + "");
                    a = a + (Integer.parseInt(snapshot.child(str.getUserid()).child("admirers").getChildrenCount() + "") * 0.7);
                    a = a + (Integer.parseInt(snapshot.child(str.getUserid()).child("bfs").getChildrenCount() + "") * 0.5);
                    a = a + (Integer.parseInt(snapshot.child(str.getUserid()).child("gfs").getChildrenCount() + "") * 0.5);
                    toppmdel.add(new toppermodel(str,intsss,a));
                }

                Collections.sort(toppmdel, new Comparator<toppermodel>() {
                    @Override
                    public int compare(toppermodel o1, toppermodel o2) {
                        return o1.getPoints().compareTo(o2.getPoints());
                    }
                });
                Collections.reverse(toppmdel);

//                int u = lstsrt.size();
//                for (int i = 0; i < u; i++) {
//
//                    System.out.println(lstsrt.toString() + lsturmdl.toString());
//                    lsturmdl.add(lstsrt.get(intlst.indexOf(Collections.max(intlst))));
//                    admcrufrilst.add(funnnnlst.get(intlst.indexOf(Collections.max(intlst))));
//                    lstsrt.remove(intlst.indexOf(Collections.max(intlst)));
//                    funnnnlst.remove(intlst.indexOf(Collections.max(intlst)));
//                    intlst.remove(intlst.indexOf(Collections.max(intlst)));
//
//
//                }
                toppadp.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}