package adapters;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.likes;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.commentsmodel;
import model.keymodel;
import model.postmodel;
import model.replymodel;
import model.usermodel;

//comments adp
public class commentsadp extends RecyclerView.Adapter<commentsadp.viewholder>{
    private Context context;
    private List<commentsmodel> commlst;
    private String delete;
    private postmodel pedel;


    public commentsadp(Context context, List<commentsmodel> commlst, String delete, postmodel pedel) {
        this.context = context;
        this.commlst = commlst;
        this.delete = delete;
        this.pedel = pedel;
    }

    @NonNull
    @Override

    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_comments,parent,false);
        return new commentsadp.viewholder(view);
    }

    public List<commentsmodel> getCommlst() {
        return commlst;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.start();
        commentsmodel commm = commlst.get(position);
        holder.comm.setText(commm.getComment());
        holder.tme.setText(effecy.instance.getzonetime(commm.getDate()+" "+commm.getTime().substring(0,5)));

        ImageView imgv = new ImageView(context);
        effecy sen = new effecy(commm.getUserid(),holder.bcimg);
        effecy anno = new effecy(context,commm.getUserid(),holder.annocru,holder.annoadm,holder.annofri);
        effecy blked = new effecy(context,commm.getUserid(),imgv);
        effecy.instance.postexist(pedel,holder.imagei);
        effecy.instance.commentexist(pedel,commm,holder.replimg);

        List<replymodel> replylst = new ArrayList<>();
        replyadp adpl =new replyadp(context,replylst,commm,delete);
        getting(holder.imagei,holder.username,commm);
        getcomm(commm,holder.recyclerView,replylst,adpl);

        getreplyimg(holder.replimg,holder.repusrnm);
        getcomlik(commm,holder.like,holder.nolik);
        holder.cdf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.cdf.setEnabled(false);
                if ((holder.imagei.getTag()+"").equals("yes")) {
                    if ((holder.replimg.getTag()+"").equals("yes")) {
                        if (commm.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || commm.getPublisherid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                            android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
                            alt.setInverseBackgroundForced(true);
                            alt.setTitle("Delete").setMessage("Do you want to delete the comment '" + commm.getComment() + "'")
                                    .setCancelable(true)
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("notify")
                                                    .child("others").child(commm.getDate()).child(commm.getCommentid()).removeValue();
                                            effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,commm.getCommentid(),"del");


                                            for (replymodel redel : adpl.getReplst()) {
                                                removereply(commm, redel);
                                            }
//                                    FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid())
//                                            .child("replys").child(commm.getPostid()).child(commm.getCommentid()).removeValue();

//
                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("commlikes").child(commm.getPostid())
                                                    .child(commm.getCommentid()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dspp : snapshot.getChildren()) {
                                                                keymodel kdelll = dspp.getValue(keymodel.class);
                                                                effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,commm.getCommentid(),"del");

                                                                FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify").child("others")
                                                                        .child(kdelll.getDate()).child(kdelll.getKey()).removeValue();

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("commlikes").child(commm.getPostid())
                                                    .child(commm.getCommentid()).removeValue();

                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("comments")
                                                    .child(commm.getPostid()).child(commm.getCommentid()).removeValue();


                                        }
                                    })
                                    .show();


                        }
                    }else {
                        Toast.makeText(context, "This Comment is doesn't exist ", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "This Post is Deleted", Toast.LENGTH_SHORT).show();
                }
                holder.cdf.setEnabled(true);
                return true;
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                replymodel redel = replylst.get(viewHolder.getAdapterPosition());
                if(redel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || commm.getPublisherid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                    android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
                    alt.setInverseBackgroundForced(true);
                    alt.setTitle("Delete Reply").setMessage(redel.getReply())
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removereply(commm,redel);
                                }
                            })
                            .show();

                }
                adpl.notifyDataSetChanged();
            }
        }).attachToRecyclerView(holder.recyclerView);
        holder.bcimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bcimg.setEnabled(false);
                if(holder.bcimg.getTag().equals("yes")){
                    Intent inte = new Intent(context, story_see.class);
                    inte.putExtra(commm.getUserid(), commm.getUserid());
                    context.startActivity(inte);
                }
                holder.bcimg.setEnabled(true);
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.username.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",commm.getUserid());
                context.startActivity(inte);
                holder.username.setEnabled(true);
            }
        });

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.reply.setEnabled(false);
                if ((holder.imagei.getTag()+"").equals("yes")) {
                    if ((holder.replimg.getTag() + "").equals("yes")) {
                        if (holder.relativeLayout.getVisibility() == GONE) {
                            holder.relativeLayout.setVisibility(View.VISIBLE);
                        } else {
                            holder.relativeLayout.setVisibility(GONE);
                        }
                        holder.reply.setEnabled(true);
                    }else{
                        Toast.makeText(context, "This Comment doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.socioreply.setEnabled(false);
                holder.done.setEnabled(false);
                if ((holder.imagei.getTag()+"").equals("yes")) {
                    if ((holder.replimg.getTag()+"").equals("yes")) {
                        if (imgv.getTag().equals("notblocked")) {
                            String dat = effecy.instance.gettime();
                            if (!TextUtils.isEmpty(holder.socioreply.getText().toString())) {
                                DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid())
                                        .child("replys").child(commm.getPostid()).child(commm.getCommentid());
                                String sk = df.push().getKey();
                                HashMap<String, String> mpp = new HashMap<>();
                                mpp.put("commentid", commm.getCommentid());
                                mpp.put("reply", holder.socioreply.getText().toString());
                                mpp.put("postid", commm.getPostid());
                                mpp.put("replyid", sk);
                                mpp.put("time", dat);
                                mpp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());


                                df.child(sk).setValue(mpp);

                                HashMap<String, String> notymp = new HashMap<>();
                                notymp.put("subject", "replied");
                                notymp.put("notify", holder.socioreply.getText().toString().trim());
                                notymp.put("postid", commm.getPostid());
                                notymp.put("publisherid", commm.getPublisherid());
                                notymp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                notymp.put("notifyid", sk);
                                notymp.put("time", dat.substring(11));


                                if (!commm.getPublisherid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid())
                                            .child("notify").child("others").child(dat.substring(0, 10)).child(sk).setValue(notymp);
                                    effecy.instance.sendnotii(context, commm.getPublisherid(), "Replied: '" + holder.socioreply.getText() + "'", "replied", commm.getPostid(),sk,null);

                                }
                                if (!commm.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid())
                                            .child("notify").child("others").child(dat.substring(0, 10)).child(sk).setValue(notymp);
                                    effecy.instance.sendnotii(context, commm.getUserid(), "Replied: '" + holder.socioreply.getText() + "'", "replied", commm.getPostid(),sk,null);

                                }
                                holder.socioreply.setText("");


                            }
                        } else {
                            Toast.makeText(context, "You Have Benn Blocked By the User", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                            Toast.makeText(context, "This Comment doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                }else{
                        Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();
                }
                holder.done.setEnabled(true);
                holder.socioreply.setEnabled(true);
                holder.relativeLayout.setVisibility(GONE);
            }
        });


        holder.nolik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.nolik.setEnabled(false);
                if ((holder.imagei.getTag()+"").equals("yes")) {
                    if ((holder.replimg.getTag()+"").equals("yes")) {
                        Intent inty = new Intent(context, likes.class);
                        inty.putExtra("publisherid", commm.getPublisherid());
                        inty.putExtra("postid", commm.getPostid());
                        inty.putExtra("commentid", commm.getCommentid());
                        context.startActivity(inty);
                        holder.nolik.setEnabled(true);
                    }else{
                        Toast.makeText(context, "This Comment doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.like.setEnabled(false);
                if ((holder.imagei.getTag()+"").equals("yes")) {
                    if ((holder.replimg.getTag()+"").equals("yes")) {
                        if (holder.like.getTag().toString().startsWith("liked")) {
                            String ky = holder.like.getTag().toString().substring(15);
                            String dat = holder.like.getTag().toString().substring(5, 15);

                            effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,commm.getCommentid(),"del");

                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify")
                                    .child("others").child(dat).child(ky).removeValue();

                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid())
                                    .child("commlikes").child(commm.getPostid()).child(commm.getCommentid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                        } else {
                            String sk = FirebaseDatabase.getInstance().getReference().push().getKey();
                            String dt = effecy.instance.gettime();
                            HashMap<String, String> notymp = new HashMap<>();
                            notymp.put("subject", "commentliked");
                            notymp.put("notify", " ");
                            notymp.put("postid", commm.getPostid());
                            notymp.put("publisherid", commm.getPublisherid());
                            notymp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notymp.put("notifyid", sk);
                            notymp.put("time", dt.substring(11));

                            HashMap mp = new HashMap();
                            mp.put("key", sk);
                            mp.put("date", dt.substring(0, 10));
                            mp.put("time", dt.substring(11));

                            if (!commm.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify").child("others")
                                        .child(dt.substring(0, 10)).child(sk).setValue(notymp);
                                effecy.instance.sendnotii(context, commm.getUserid(), "Liked Your Comment", "commentliked", commm.getPostid(),sk,null);
                            }

                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid())
                                    .child("commlikes").child(commm.getPostid()).child(commm.getCommentid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mp);
                        }
                    }else{
                        Toast.makeText(context, "This Comment doesn't exist", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.like.setEnabled(true);
            }
        });
    }
    public void removereply(commentsmodel commm,replymodel redel){
        effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,redel.getReplyid(),"del");
        effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,redel.getReplyid(),"del");

        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("notify")
                .child("others").child(redel.getTime().substring(0,10)).child(redel.getReplyid()).removeValue();

        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify")
                .child("others").child(redel.getTime().substring(0,10)).child(redel.getReplyid()).removeValue();

        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("replys")
                .child(redel.getPostid())
                .child(redel.getCommentid())
                .child(redel.getReplyid()).removeValue();
    }
    public void getreplyimg(ImageView reprof,TextView repusrnme){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel cdel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(usermodel.class);

                Picasso.get().load(cdel.getImageurl()).into(reprof);
                repusrnme.setText(cdel.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getcomlik(commentsmodel commmdl,ImageView comlik,TextView nolikes){
        FirebaseDatabase.getInstance().getReference().child("info").child(commmdl.getPublisherid())
                .child("commlikes").child(commmdl.getPostid()).child(commmdl.getCommentid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    keymodel kdel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(keymodel.class);
                    comlik.setTag("liked"+kdel.getDate()+kdel.getKey());
                    comlik.setImageResource(R.drawable.thumbup);
                }else {
                    comlik.setTag("like");
                    comlik.setImageResource(R.drawable.thumbupoutline);
                }
                nolikes.setText(snapshot.getChildrenCount()+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    };
    public void getcomm(commentsmodel commm,RecyclerView recyclerView,List<replymodel> replylst,replyadp adpl){
        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid())
                .child("replys").child(commm.getPostid()).child(commm.getCommentid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                replylst.clear();
                List<String> dalst = new ArrayList<>();
                List<replymodel> repdel = new ArrayList<>();


                for (DataSnapshot ds : snapshot.getChildren()) {
                    replymodel repmod = ds.getValue(replymodel.class);
                    repdel.add(repmod);
                    dalst.add(repmod.getTime());
                }

                for(String strdat : effecy.instance.getdalst(dalst)){
                    for(replymodel rdel : repdel){
                        if(rdel.getTime().equals(strdat) && !replylst.contains(rdel)){
                            replylst.add(rdel);
                            break;
                        }
                    }
                }

                if(delete != null && !snapshot.exists()){
                    FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("notify")
                            .child("others").child(commm.getDate()).child(commm.getCommentid()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("commlikes").child(commm.getPostid())
                            .child(commm.getCommentid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dspp : snapshot.getChildren()){
                                keymodel kdelll = dspp.getValue(keymodel.class);
                                effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,kdelll.getKey(),"del");

                                FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify").child("others")
                                        .child(kdelll.getDate()).child(kdelll.getKey()).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("commlikes").child(commm.getPostid())
                            .child(commm.getCommentid()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("comments")
                            .child(commm.getPostid()).child(commm.getCommentid()).removeValue();

                }

                recyclerView.setAdapter(adpl);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getting(ImageView imageView, TextView usernme, commentsmodel commentsmodel){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dt : snapshot.getChildren()){
                    if(dt.getKey().equals(commentsmodel.getUserid())){
                        usermodel uel = dt.getValue(usermodel.class);
                        Picasso.get().load(uel.getImageurl()).placeholder(R.drawable.add).into(imageView);
                        usernme.setText(uel.getUsername()+"");

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
        return commlst.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        private CardView cdf;
        private ImageView imagei,replimg,like,bcimg,annocru,annofri,annoadm;
        private TextView reply,comm,username,done,nolik,tme,repusrnm;
        private RecyclerView recyclerView;
        private RelativeLayout relativeLayout;
        private SocialEditText socioreply;


        public viewholder(@NonNull View itemView) {
            super(itemView);

            cdf= itemView.findViewById(R.id.cdf);
            nolik=itemView.findViewById(R.id.nolike);
            reply=itemView.findViewById(R.id.chatch);
            socioreply=itemView.findViewById(R.id.replytxt);
            relativeLayout=itemView.findViewById(R.id.replylayout);
            done=itemView.findViewById(R.id.done);
            username=itemView.findViewById(R.id.chatusernme);
            imagei=itemView.findViewById(R.id.chatpro);
            like=itemView.findViewById(R.id.comlk);
            reply=itemView.findViewById(R.id.chatch);
            comm=itemView.findViewById(R.id.chatlin);
            recyclerView=itemView.findViewById(R.id.revie);
            tme=itemView.findViewById(R.id.tme);
            bcimg=itemView.findViewById(R.id.cardview);
            repusrnm=itemView.findViewById(R.id.reppusernme);
            replimg=itemView.findViewById(R.id.reppro);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
        }
    }
}
