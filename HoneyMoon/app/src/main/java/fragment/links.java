package fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.linkadp;
import adapters.mediaadp;
import model.chatmodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link links#} factory method to
 * create an instance of this fragment.
 */
public class links extends Fragment {

    private String userid;
    private RecyclerView recv;
    private List<chatmodel> lstcht;
    private linkadp adp;

    public links(String userid) {
        this.userid = userid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_links, container, false);
       recv =view.findViewById(R.id.lkrecy);
       lstcht=new ArrayList<>();
       adp=new linkadp(getContext(),lstcht);

       recv.setAdapter(adp);
       recv.setLayoutManager(new LinearLayoutManager(getContext()));

       getcht();

       return view;
    }
    public void getcht(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstcht.clear();
                if(snapshot.exists()){
                    for(DataSnapshot dss : snapshot.getChildren()){


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