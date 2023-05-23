package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.allstories;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.highmodel;
import model.statusmodel;

public class highadap extends RecyclerView.Adapter<highadap.vie>{

    private Context context;
    private String userid;
    private List<highmodel> highlst;

    public highadap(Context context, String userid) {
        this.context = context;
        this.userid = userid;
        highlst=new ArrayList<highmodel>();
        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            highlst.add(new highmodel());
        }
        getstalst();
    }

    @NonNull
    @Override
    public vie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_high,parent,false);
        return new highadap.vie(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vie holder, int position) {
        highmodel hg = highlst.get(position);
        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && position == 0){
            Picasso.get().load("pods").placeholder(R.drawable.add).into(holder.prol);
            holder.title.setText("add new");
        }else {
            holder.title.setText(hg.getTitle());
            Picasso.get().load(hg.getImageuri()).placeholder(R.drawable.logo_background).into(holder.prol);
        }
        holder.rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && position == 0){
                   context.startActivity(new Intent(context, allstories.class));
                }else{

                    context.startActivity(new Intent(context, story_see.class));
                }
            }
        });
    }
    public void getstalst(){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("highlights").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot sdt : snapshot.getChildren()){
                        statusmodel sty = sdt.getValue(statusmodel.class);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return highlst.size();
    }

    public class vie extends RecyclerView.ViewHolder{
        private ImageView prol;
        private RelativeLayout rela;
        private TextView title;
        public vie(@NonNull View itemView) {
            super(itemView);

            prol=itemView.findViewById(R.id.highlgpro);
            rela=itemView.findViewById(R.id.rrrr);
            title=itemView.findViewById(R.id.titl);

        }
    }
}
