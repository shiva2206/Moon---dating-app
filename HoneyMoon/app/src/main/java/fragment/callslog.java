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
import java.util.List;

import adapters.calladp;
import adapters.effecy;
import model.callmodel;
import model.usermodel;
import retrofituu.UsersListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link callslog# factory method to
 * create an instance of this fragment.
 */
public class callslog extends Fragment implements UsersListener{

    private RecyclerView recy;
    private List<callmodel> ordcalldel;
    private calladp adp;
    private TextView nn;
    private SwipeRefreshLayout srl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_callslog, container, false);

        recy=view.findViewById(R.id.cllrecy);
        nn=view.findViewById(R.id.nn);
        srl=view.findViewById(R.id.srl);

        ordcalldel=new ArrayList<>();
        adp = new calladp(getContext(),ordcalldel,this);
        recy.setAdapter(adp);
        recy.setLayoutManager(new LinearLayoutManager(getContext()));

        getcalldetails();

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getcalldetails();
                srl.setRefreshing(false);
            }
        });
        return view;

    }
    public void getcalldetails(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calls").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<callmodel> lstcal = new ArrayList<>();
                        List<String> unorddalst = new ArrayList<>();

                        ordcalldel.clear();


                        for (DataSnapshot dss : snapshot.getChildren()) {
                            for (DataSnapshot df : snapshot.child(dss.getKey()).getChildren()) {
                                callmodel cdel = df.getValue(callmodel.class);
                                cdel.setTime(dss.getKey()+" "+cdel.getTime());
                                lstcal.add(cdel);

                                unorddalst.add(cdel.getTime());
                            }
                        }


                        for(String strdat : effecy.instance.getdalst(unorddalst)){
                            for(callmodel caldel : lstcal){
                                if(caldel.getTime().equals(strdat)  &&  !ordcalldel.contains(caldel)){
                                    ordcalldel.add(caldel);
                                    break;
                                }
                            }
                        }


                       adp.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    @Override
    public void initiateVideoMeeting(usermodel udel) {

    }

    @Override
    public void initiateAudioMeeting(usermodel udel) {

    }
}