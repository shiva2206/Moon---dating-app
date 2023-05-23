package adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.story_see;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.commentsmodel;
import model.replymodel;
import model.usermodel;

public class replyadp extends RecyclerView.Adapter<replyadp.viewhlder> {
    private Context context;
    private List<replymodel> replst;
    private commentsmodel commm;
    private String delete;


    public replyadp(Context context, List<replymodel> replst, commentsmodel commm, String delete) {
        this.context = context;
        this.replst = replst;
        this.commm = commm;
        this.delete = delete;
    }

    public List<replymodel> getReplst() {
        return replst;
    }

    @NonNull
    @Override
    public viewhlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_reply,parent,false);
        return new viewhlder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewhlder holder, int position) {
        replymodel rel = replst.get(position);
        holder.tmer.setText(effecy.instance.getzonetime(rel.getTime().substring(0,16)));
        effecy sen=new effecy(rel.getUserid(),holder.bcg);
        effecy anno = new effecy(context,rel.getUserid(),holder.annocru,holder.annoadm,holder.annofri);
        ;
        gettg(rel.getUserid(),holder.imag,holder.usernem);
        holder.reply.setText(rel.getReply());
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.reply.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",rel.getUserid());
                context.startActivity(inte);
                holder.reply.setEnabled(true);
            }
        });
        holder.bcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bcg.setEnabled(false);
                if(holder.bcg.getTag().equals("yes")){
                    Intent inte = new Intent(context, story_see.class);
                    inte.putExtra("userid", rel.getUserid());
                    context.startActivity(inte);
                }
                holder.bcg.setEnabled(true);
            }
        });
        if(delete != null){
            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("notify")
                    .child("others").child(rel.getTime().substring(0,10)).child(rel.getReplyid()).removeValue();

            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify")
                    .child("others").child(rel.getTime().substring(0,10)).child(rel.getReplyid()).removeValue();

            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("replys")
                    .child(rel.getPostid())
                    .child(rel.getCommentid())
                    .child(rel.getReplyid()).removeValue();
        }
    }
    public void gettg(String userid,ImageView imageView,TextView username){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dt : snapshot.getChildren()){
                    if(dt.getKey().equals(userid)) {
                        usermodel use = dt.getValue(usermodel.class);
                        Picasso.get().load(use.getImageurl()).placeholder(R.drawable.logo_background).into(imageView);
                        username.setText(use.getUsername());

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
        return replst.size();
    }

    public class viewhlder extends RecyclerView.ViewHolder{

        private ImageView imag,bcg,annocru,annofri,annoadm;
        private TextView reply,usernem,tmer;

        public viewhlder(@NonNull View itemView) {
            super(itemView);

            imag=itemView.findViewById(R.id.chatpro);
            reply=itemView.findViewById(R.id.chatch);
            usernem=itemView.findViewById(R.id.chatusernme);
            tmer=itemView.findViewById(R.id.tmer);
            bcg=itemView.findViewById(R.id.cardview);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
        }
    }
}
