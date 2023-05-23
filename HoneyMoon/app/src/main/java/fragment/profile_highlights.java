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
import adapters.highlightadp;
import model.highmodel;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_highlights#} factory method to
 * create an instance of this fragment.
 */
public class profile_highlights extends Fragment {

    private String userid;
    private String foll;
    private TextView nn;
    private List<highmodel> hglst;
    private highlightadp hihadp;
    private RecyclerView highrecy;



    public profile_highlights(String userid) {
        this.userid = userid;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_highlights, container, false);

        hglst=new ArrayList<>();
        nn=view.findViewById(R.id.nn);
        highrecy=view.findViewById(R.id.higrecy);
        hihadp=new highlightadp(getContext(),userid,hglst);
        highrecy.setAdapter(hihadp);
        highrecy.setLayoutManager(new GridLayoutManager(getContext(),3));

        gethigh();
        getmode();
        return view;
    }
    public void gethigh(){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("highlights").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hglst.clear();
                List<highmodel> unordhgdel = new ArrayList<>();
                List<String> dslst = new ArrayList<>();

                if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    hglst.add(new highmodel());

                }

                for(DataSnapshot dss :snapshot.getChildren()){
                    highmodel hg = dss.getValue(highmodel.class);
                    dslst.add(hg.getTime());
                    unordhgdel.add(hg);
                }

                List<String> dddaaalllstt = effecy.instance.getdalst(dslst);
                Collections.reverse(dddaaalllstt);
                for(String strdat : dddaaalllstt){
                    for(highmodel hgdel : unordhgdel){
                        if(hgdel.getTime().equals(strdat) && !hglst.contains(hgdel)){
                            hglst.add(hgdel);
                            break;
                        }
                    }
                }

                if(hglst.isEmpty() && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    highrecy.setVisibility(GONE);
                    nn.setVisibility(VISIBLE);
                }else{
                    highrecy.setVisibility(VISIBLE);
                    nn.setVisibility(GONE);
                }
//                Toast.makeText(getContext(), unordhgdel.size()+"", Toast.LENGTH_SHORT).show();

                hihadp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getmode() {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot df : snapshot.getChildren()) {
                    if (df.getKey().equals(userid)) {
                        usermodel udr = df.getValue(usermodel.class);
//                        if (!userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && udr.getMode().equals("private") && !foll.equals("following")) {
//                            highrecy.setVisibility(GONE);
//
//                        } else {
//                            highrecy.setVisibility(VISIBLE);
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