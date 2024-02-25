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
import adapters.notifyadp;
import model.notifymodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notify_others#} factory method to
 * create an instance of this fragment.
 */
public class notify_others extends Fragment {

    private List<notifymodel> notilst;
    private RecyclerView dview;
    private notifyadp adp;
    private TextView nn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notify_others, container, false);

        nn=view.findViewById(R.id.nn);
        dview=view.findViewById(R.id.othrecy);
        notilst=new ArrayList<>();
        adp=new notifyadp(getContext(),notilst);
        dview.setAdapter(adp);
        dview.setLayoutManager(new LinearLayoutManager(getContext()));

        getnotify();
        return view;
    }
    public void getnotify(){

        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("notify").child("others").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<notifymodel> ndelst  = new ArrayList<>();
                        List<String> unorddalst = new ArrayList<>();

                        for (DataSnapshot dss : snapshot.getChildren()) {
                            for (DataSnapshot sd : snapshot.child(dss.getKey()).getChildren()) {
                                notifymodel ndel = sd.getValue(notifymodel.class);

                                if (ndel.getSubject().equals("likes") || ndel.getSubject().equals("commentliked")) {
                                    if (effecy.instance.isold(dss.getKey() + " " + ndel.getTime(), 2 * 60 * 24)) {
                                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("notify").child("others").child(dss.getKey()).child(ndel.getNotifyid()).removeValue();
                                    }
                                }
                            }
                        }
                        for (DataSnapshot dss : snapshot.getChildren()) {
                            for (DataSnapshot sd : snapshot.child(dss.getKey()).getChildren()) {
                                notifymodel ndel = sd.getValue(notifymodel.class);

                                ndelst.add(ndel);
                                unorddalst.add(dss.getKey() + " " + ndel.getTime());
                            }
                        }


                        notilst.clear();
                        for(String strdat : effecy.instance.getdalst(unorddalst)){
                            for(notifymodel noadel : ndelst){
                                if(noadel.getTime().equals(strdat.substring(11)) && !notilst.contains(noadel)){
                                    noadel.setTime(strdat);
                                    notilst.add(noadel);
                                    break;
                                }
                            }
                        }

                        Collections.reverse(notilst);
                        adp.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}