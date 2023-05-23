package fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.gridadp;
import model.postmodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_hashtag#} factory method to
 * create an instance of this fragment.
 */
public class profile_hashtag extends Fragment {

    private String hastag;
    private TextView hahnme,totpsts;
    private RecyclerView pstrecy;
    private List<postmodel> pstlst;
    private gridadp adp;

    public profile_hashtag(String hastag) {
        this.hastag = hastag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_hashtag, container, false);

        hahnme=view.findViewById(R.id.hahnme);
        totpsts=view.findViewById(R.id.ttpst);
        pstrecy=view.findViewById(R.id.recy);

        hahnme.setText(hastag);
        pstlst=new ArrayList<>();

        adp=new gridadp(getContext(),"blabla",pstlst,false);
        pstrecy.setAdapter(adp);
        pstrecy.setLayoutManager(new GridLayoutManager(getContext(),3));


        getall();



        return view;

    }
    public void getall(){
        FirebaseDatabase.getInstance().getReference().child("hashs").child(hastag.substring(1)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    totpsts.setText(snapshot.getChildrenCount() + " Posts");
                    pstlst.clear();
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        postmodel pdel = dss.getValue(postmodel.class);
                        pstlst.add(pdel);
                    }
                    adp.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}