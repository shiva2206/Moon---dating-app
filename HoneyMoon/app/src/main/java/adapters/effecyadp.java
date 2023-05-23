package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.commentsmodel;
import model.keymodel;
import model.replymodel;

public class effecyadp extends RecyclerView.Adapter<effecyadp.vhdeefr> {

    private Context context;
    private List<commentsmodel> commlst;
    private List<replymodel> repllstlst;
    private commentsmodel cmdel;

    public List<commentsmodel> getCommlst() {
        if(commlst.isEmpty()){
            commlst=new ArrayList<>();
        }
        return commlst;
    }

    public List<replymodel> getRepllstlst() {
        if(repllstlst.isEmpty()){
            repllstlst =  new ArrayList<>();
        }
        return repllstlst;
    }

    public effecyadp(Context context, List<commentsmodel> commlst) {
        this.context = context;
        this.commlst = commlst;
    }

    public effecyadp(Context context,List<replymodel> repllstlst, commentsmodel cmdel) {
        this.context = context;
        this.repllstlst = repllstlst;
        this.cmdel = cmdel;
    }

    @NonNull
    @Override
    public vhdeefr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_effecy,parent,false);
        return new effecyadp.vhdeefr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhdeefr holder, int position) {

        if(repllstlst == null) {
            commentsmodel commdel = commlst.get(position);

            List<replymodel> repdlst = new ArrayList<>();
            effecyadp adp = new effecyadp(context,repdlst,commdel);
            holder.recy.setAdapter(adp);
            holder.recy.setLayoutManager(new LinearLayoutManager(context));

            FirebaseDatabase.getInstance().getReference().child("info").child(commdel.getPublisherid()).child("replys").child(commdel.getPostid())
                    .child(commdel.getCommentid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    repdlst.clear();
                    for(DataSnapshot dsn : snapshot.getChildren()){
                        replymodel rdel = dsn.getValue(replymodel.class);
                        repdlst.add(rdel);
                    }

                    adp.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            if(adp.getRepllstlst().isEmpty()) {
                FirebaseDatabase.getInstance().getReference().child("info").child(commdel.getPublisherid()).child("notify").child("others")
                        .child(commdel.getDate()).child(commdel.getCommentid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("info").child(commdel.getPublisherid()).child("comments")
                        .child(commdel.getPostid()).child(commdel.getCommentid()).removeValue();

            }
        }else{
            replymodel redel = repllstlst.get(position);
            FirebaseDatabase.getInstance().getReference().child("info").child(cmdel.getPublisherid()).child("notify").child("others")
                    .child(redel.getTime().substring(0, 10)).child(redel.getReplyid()).removeValue();
            FirebaseDatabase.getInstance().getReference().child("info").child(cmdel.getUserid()).child("notify").child("others")
                    .child(redel.getTime().substring(0, 10)).child(redel.getReplyid()).removeValue();
            FirebaseDatabase.getInstance().getReference().child("info").child(cmdel.getPublisherid()).child("commlikes")
                    .child(cmdel.getPostid()).child(cmdel.getCommentid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dst : snapshot.getChildren()) {
                        keymodel kkkel = dst.getValue(keymodel.class);
                        FirebaseDatabase.getInstance().getReference().child("info").child(cmdel.getUserid()).child("notify").child("others")
                                .child(kkkel.getDate()).child(kkkel.getKey()).removeValue();


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if(commlst == null){
            return repllstlst.size();
        }else {
            return commlst.size();
        }
    }

    public class vhdeefr extends RecyclerView.ViewHolder{

        private RecyclerView recy;
        public vhdeefr(@NonNull View itemView) {
            super(itemView);

            recy = itemView.findViewById(R.id.effrecy);

        }
    }
}
