package adapters;

import android.graphics.Color;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.statusmodel;

public class seen {
    private List<statusmodel> stalst;
    private String userid;
    private CardView cardView;
    private int p = 0;

    public seen(String userid, CardView cardView) {
        this.userid = userid;
        this.cardView = cardView;
        stalst=new ArrayList<>();
        seenornot(userid,cardView);
    }

    public void seenornot(String userid, CardView cardView){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stalst.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dss: snapshot.getChildren()){
                        statusmodel sta = dss.getValue(statusmodel.class);
                        stalst.add(sta);
                    }
                    getsen(userid ,cardView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }public void getsen(String userid,CardView cardView){
        for(statusmodel i : stalst) {
            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("seen").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dg : snapshot.getChildren()) {
                            if (dg.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                p=p+1;
                            }if(p!= stalst.size() && stalst.get(-1).getStatusid().equals(i.getStatusid())){
                                cardView.setCardBackgroundColor(Color.parseColor("#FF01D6"));
                            }else if(stalst.get(-1).getStatusid().equals(i.getStatusid())){
                                cardView.setCardBackgroundColor(Color.parseColor("#737272"));
                            }
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
