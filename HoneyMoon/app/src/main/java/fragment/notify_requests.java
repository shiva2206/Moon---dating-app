package fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
 * Use the {@link notify_requests#} factory method to
 * create an instance of this fragment.
 */
public class notify_requests extends Fragment {

    private List<notifymodel> follnotilst,frinotilst;
    private RecyclerView follrview,friview;
    private notifyadp folladp,friadp;
    private TextView folltxt,fritxt,nn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notify_requests, container, false);

        follrview=view.findViewById(R.id.rdve);
        friview=view.findViewById(R.id.frirdve);
        folltxt=view.findViewById(R.id.follreqtxt);
        fritxt=view.findViewById(R.id.frireqtxt);
        nn=view.findViewById(R.id.nn);

        follnotilst=new ArrayList<>();
        frinotilst=new ArrayList<>();


        folladp=new notifyadp(getContext(),follnotilst);
        follrview.setAdapter(folladp);
        follrview.setLayoutManager(new LinearLayoutManager(getContext()));


        friadp=new notifyadp(getContext(),frinotilst);
        friview.setAdapter(friadp);
        friview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("notify").child("requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                follnotilst.clear();
                frinotilst.clear();

                List<notifymodel> unordlst = new ArrayList<>();
                List<String> unorddalst= new ArrayList<>();

                for (DataSnapshot dt : snapshot.child("follows").getChildren()) {
                    notifymodel mul = dt.getValue(notifymodel.class);
                    unordlst.add(mul);
                    unorddalst.add(mul.getTime());

                }

                for(String strdat : effecy.instance.getdalst(unorddalst)){
                    for(notifymodel ndel : unordlst){
                        if(ndel.getTime().equals(strdat) && !follnotilst.contains(ndel)){
                            follnotilst.add(ndel);
                            break;

                        }
                    }
                }

                List<notifymodel> unordlsttwo = new ArrayList<>();
                List<String> unorddalsttwo = new ArrayList<>();

                for(DataSnapshot dss : snapshot.child("friends").getChildren()){
                    notifymodel ndell = dss.getValue(notifymodel.class);
                    unordlsttwo.add(ndell);
                    unorddalsttwo.add(ndell.getTime());

                }

                for(String strdat : effecy.instance.getdalst(unorddalsttwo)){
                    for(notifymodel ndel : unordlsttwo){
                        if(ndel.getTime().equals(strdat)  &&  !frinotilst.contains(ndel)){
                            frinotilst.add(ndel);
                            break;

                        }
                    }
                }





                Collections.reverse(follnotilst);
                Collections.reverse(frinotilst);
                if (follnotilst.isEmpty()){
                    folltxt.setVisibility(GONE);
                }else{
                    folltxt.setVisibility(VISIBLE);
                }

                if(frinotilst.isEmpty()){
                    fritxt.setVisibility(GONE);
                }
                else{
                    fritxt.setVisibility(VISIBLE);
                }

                if(follnotilst.isEmpty() && frinotilst.isEmpty()){
                    nn.setVisibility(VISIBLE);
                }else{
                    nn.setVisibility(GONE);
                }


                folladp.notifyDataSetChanged();
                friadp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    public void arrange(List<String> unorddalst,List<notifymodel>  notilst,String what){

    }
}