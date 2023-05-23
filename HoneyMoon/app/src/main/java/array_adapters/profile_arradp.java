package array_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import adapters.gridadp;
import adapters.highlightadp;
import model.highmodel;
import model.postmodel;
import model.usermodel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class profile_arradp extends ArrayAdapter<String> {
    private Context context;
    private String userid;
    private List<String> whatlst;
    private String foll;

    private TextView nn;
    private List<highmodel> hglst;


    private List<postmodel> postmodelst;



    public profile_arradp(@NonNull Context context, int resource,String userid,List<String> whatlst,String foll) {
        super(context, resource,whatlst);
        this.context=context;
        this.userid=userid;
        this.whatlst=whatlst;
        this.foll=foll;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adap_profile,parent,false);
        }
        String what = whatlst.get(position);
        Button tit = convertView.findViewById(R.id.txt);


        RecyclerView recy=convertView.findViewById(R.id.recy);

        if(what.equals("highlights") && position == 0){
            hglst=new ArrayList<>();
            nn=convertView.findViewById(R.id.nn);


            gethigh(tit,recy);
        }else if(position==0){
            postmodelst=new ArrayList<>();


            getposts(tit,recy);
        }
        getmode(recy);

        return convertView;
    }

    public void gethigh(Button tit,RecyclerView recy){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("highlights").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hglst.clear();
                tit.setText("highlights "+snapshot.getChildrenCount());
                if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    hglst.add(new highmodel());

                }
                if(snapshot.exists()){
                    for(DataSnapshot dss :snapshot.getChildren()){
                        highmodel hg = dss.getValue(highmodel.class);
                        hglst.add(hg);
                    }
                    if(hglst.isEmpty() && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//                        recy.setVisibility(GONE);
//                        nn.setVisibility(VISIBLE);
                    }else{
//                        recy.setVisibility(VISIBLE);
//                        nn.setVisibility(GONE);
                    }
                }else{
                    if(!userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//                        recy.setVisibility(GONE);
//                        nn.setVisibility(VISIBLE);


                    }else{
//                        recy.setVisibility(VISIBLE);
//                        nn.setVisibility(GONE);
                    }
                }
                highlightadp hihadp=new highlightadp(getContext(),userid,hglst);
                recy.setAdapter(hihadp);
                recy.setLayoutManager(new GridLayoutManager(getContext(),3));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getmode(RecyclerView recy) {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot df : snapshot.getChildren()) {
                    if (df.getKey().equals(userid)) {
                        usermodel udr = df.getValue(usermodel.class);
                        if (!userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && udr.getMode().equals("private") && !foll.equals("following")) {
//                            recy.setVisibility(GONE);

                        } else {
                            recy.setVisibility(VISIBLE);

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getposts(Button tit,RecyclerView recy){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postmodelst.clear();
                        tit.setText("posts "+snapshot.getChildrenCount());
                        for(DataSnapshot dt : snapshot.getChildren()){
                            postmodelst.add(dt.getValue(postmodel.class));
                        }
                        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            postmodelst.add(new postmodel());
                        }

                        Collections.reverse(postmodelst);

                        gridadp adp= new gridadp(getContext(),userid,postmodelst,true);
                        recy.setAdapter(adp);
                        recy.setLayoutManager(new GridLayoutManager(getContext(),3));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
