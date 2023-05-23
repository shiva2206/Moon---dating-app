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

import adapters.dateadap;
import adapters.docsadp;
import adapters.gridadp;
import model.chatmodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link docs#} factory method to
 * create an instance of this fragment.
 */
public class docs extends Fragment {

    private String userid;
    private RecyclerView recv;
    private dateadap adp;
    private List<String> lstdte;
    public docs(String userid) {
        this.userid = userid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_docs, container, false);

        lstdte=new ArrayList<>();
        adp=new dateadap(getContext(),lstdte,userid,"docs");
        recv=view.findViewById(R.id.docsrecy);
        recv.setAdapter(adp);
        recv.setLayoutManager(new LinearLayoutManager(getContext()));
        getdte();


        return  view;
    }
    public void getdte(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstdte.clear();

                for(DataSnapshot dss : snapshot.getChildren()){
                    lstdte.add(dss.getKey());
                }
                adp.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}