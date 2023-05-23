package adapters;

import static com.example.honeymoon.Mainactivity.currgender;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import model.notifymodel;
import model.postmodel;
import model.usermodel;


public class notifyadp extends RecyclerView.Adapter<notifyadp.viewolder> {
    private Context context;
    private List<notifymodel> notylst;
    private postmodel pdel;

    public notifyadp(Context context, List<notifymodel> notylst) {
        this.context = context;
        this.notylst = notylst;
    }

    @NonNull
    @Override
    public viewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_notify,parent,false);
        return new notifyadp.viewolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewolder holder, int position) {
        notifymodel noti = notylst.get(position);
        getting(noti.getUserid(),holder.istimg,holder.usss);
        effecy sen=new effecy(noti.getUserid(), holder.bcg);
        effecy anno = new effecy(context,noti.getUserid(),holder.annocru,holder.annoadm,holder.annofri);

        if(noti.getSubject().equals("followreq")){
            effecy.instance.notifyreqexist(noti,holder.istimg);
            holder.acc.setVisibility(View.VISIBLE);
            holder.rej.setVisibility(View.VISIBLE);
            holder.rea.setText("Follow request");


            holder.acc.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    holder.acc.setEnabled(false);
                    if ((holder.istimg.getTag()+"").equals("yes")) {
                        HashMap<String, String> mp = new HashMap<>();
                        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                        String dat = effecy.instance.gettime();

                        mp.put("notify", " ");
                        mp.put("postid", " ");
                        mp.put("publisherid", " ");
                        mp.put("userid", noti.getUserid());
                        mp.put("notifyid", key);
                        mp.put("time", dat.substring(11));
                        mp.put("subject", "follows");
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify")
                                .child("others").child(dat.substring(0, 10)).child(key).setValue(mp);


                        mp.remove("userid");
                        mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        HashMap kmp = new HashMap();
                        kmp.put("key", key);
                        kmp.put("time", dat.substring(11));
                        kmp.put("date", dat.substring(0, 10));


                        effecy.instance.sendnotii(context, noti.getUserid(), "Accepted Follow Request", "accfollowreq", null,key,null);


                        FirebaseDatabase.getInstance().getReference().child("info").child(noti.getUserid())
                                .child("followings").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(kmp);

                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers")
                                .child(noti.getUserid()).setValue(kmp);
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify")
                                .child("requests").child("follows").child(noti.getUserid()).removeValue();
                    }else{
                        Toast.makeText(context, "This Notification doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                    holder.acc.setEnabled(true);
                }
            });
            holder.rej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.rej.setEnabled(false);
                    if ((holder.istimg.getTag()+"").equals("yes")) {
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify")
                                .child("requests").child("follows").child(noti.getUserid()).removeValue();
                    }else{
                        Toast.makeText(context, "This Notification doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                    holder.rej.setEnabled(true);

                }
            });

        }
        else if(noti.getSubject().equals("friendreq")){
            effecy.instance.notifyreqexist(noti,holder.istimg);
            holder.acc.setVisibility(View.VISIBLE);
            holder.rej.setVisibility(View.VISIBLE);
            if(currgender.equals("male")){
                holder.rea.setText("Gf request");
            }else{
                holder.rea.setText("Bf request");
            }

            holder.acc.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    holder.acc.setEnabled(false);
                    if ((holder.istimg.getTag()+"").equals("yes")) {
                        HashMap<String, String> mp = new HashMap<>();
                        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                        String dat = effecy.instance.gettime();

                        mp.put("notify", " ");
                        mp.put("postid", " ");
                        mp.put("publisherid", " ");
                        mp.put("userid", noti.getUserid());
                        mp.put("notifyid", key);
                        mp.put("time", dat.substring(11));
                        mp.put("subject", "friend");
                        FirebaseDatabase.getInstance().getReference().child("info").child(noti.getUserid()).child("notify")
                                .child("others").child(dat.substring(0, 10)).child(key).setValue(mp);

                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("notify").child("others").child(dat.substring(0, 10)).child(key).setValue(mp);


                        HashMap kmp = new HashMap();
                        kmp.put("key", key);
                        kmp.put("time", dat.substring(11));
                        kmp.put("date", dat.substring(0, 10));
                        if (currgender.equals("male")) {
                            effecy.instance.sendnotii(context, noti.getUserid(), "Accepted Bf Request", "accfrireq", null,key,null);
                            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("gfs").child(noti.getUserid()).setValue(kmp);
                            FirebaseDatabase.getInstance().getReference().child("info").child(noti.getUserid())
                                    .child("bfs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(kmp);
                        } else {
                            effecy.instance.sendnotii(context, noti.getUserid(), "Accepted Gf Request", "accfrireq", null,key,null);
                            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("bfs").child(noti.getUserid()).setValue(kmp);
                            FirebaseDatabase.getInstance().getReference().child("info").child(noti.getUserid())
                                    .child("gfs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(kmp);

                        }
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify")
                                .child("requests").child("friends").child(noti.getUserid()).removeValue();
                    }else{
                        Toast.makeText(context, "This Notification doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.rej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.rej.setEnabled(false);
                    if ((holder.istimg.getTag()+"").equals("yes")) {
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify")
                                .child("requests").child("friends").child(noti.getUserid()).removeValue();
                    }else{
                        Toast.makeText(context, "This Notification doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                    holder.rej.setEnabled(true);

                }
            });

        }
        else if(noti.getSubject().equals("likes")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");
            holder.tothimag.setVisibility(View.VISIBLE);
            holder.rea.setText("Likes your post");
            getpost(noti.getPublisherid(),noti.getPostid(),holder.tothimag,false);
        }
        else if (noti.getSubject().equals("commented")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");

            holder.tothimag.setVisibility(View.VISIBLE);
            holder.rea.setText("commented " +noti.getNotify());
            getpost(noti.getPublisherid(),noti.getPostid(),holder.tothimag,false);
        }
        else if(noti.getSubject().equals("replied")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");

            holder.tothimag.setVisibility(View.VISIBLE);
            holder.rea.setText("replied " +noti.getNotify());
            getpost(noti.getPublisherid(),noti.getPostid(),holder.tothimag,false);
        }
        else if(noti.getSubject().equals("tag")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");

            holder.tothimag.setVisibility(View.VISIBLE);
            holder.rea.setText("tagged you  " +noti.getNotify());
            getpost(noti.getPublisherid(),noti.getPostid(),holder.tothimag,false);
        }
        else if (noti.getSubject().equals("crush")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");

            holder.rea.setText("Marked you as Crush");
        }
        else if (noti.getSubject().equals("follows")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");

            holder.rea.setText(" follows you");
        }
        else if (noti.getSubject().equals("friend")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"friends");

            holder.rea.setText("added you as a friend ");
        }
        else if (noti.getSubject().equals("match")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");

            holder.rea.setText("had a match with you");
        }
        else if(noti.getSubject().equals("commentliked")){
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");

            holder.tothimag.setVisibility(View.VISIBLE);
            holder.rea.setText("commented liked:" +noti.getNotify());
            getpost(noti.getPublisherid(),noti.getPostid(),holder.tothimag,false);
        }
        else{
            effecy.instance.notifyotherexist(noti,holder.istimg,"others");
            holder.rea.setText("error");
        }
        holder.bcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bcg.setEnabled(false);
                if(holder.bcg.getTag().equals("yes")){
                    Intent inte = new Intent(context, story_see.class);
                    inte.putExtra("userid", noti.getUserid());
                    context.startActivity(inte);
                }else{
                    Toast.makeText(context, "This Notification doesn't exist", Toast.LENGTH_SHORT).show();
                }
                holder.bcg.setEnabled(true);
            }
        });
        holder.usss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.usss.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",noti.getUserid());
                context.startActivity(inte);
                holder.usss.setEnabled(true);
            }
        });
        holder.tothimag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tothimag.setEnabled(false);
                if ((holder.istimg.getTag()+"").equals("yes")) {
                    getpost(noti.getPublisherid(), noti.getPostid(), holder.tothimag, true);
                    holder.tothimag.setEnabled(true);
                }else{
                    Toast.makeText(context, "This Notification doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getpost(String publisherid,String postid,ImageView tohtimg,boolean isnext){
        FirebaseDatabase.getInstance().getReference().child("info").child(publisherid).child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dt : snapshot.getChildren()){
                    if(dt.getKey().equals(postid)) {
                        pdel = dt.getValue(postmodel.class);
                        Picasso.get().load(pdel.getUri()).into(tohtimg);
                        if(isnext){
                            Intent inte = new Intent(context,Mainactivity.class);
                            inte.putExtra("postid",pdel.getPostid());
                            inte.putExtra("publisherid",pdel.getPublisherid());
                            inte.putExtra("time",pdel.getTime());
                            inte.putExtra("tags",pdel.getTags());
                            inte.putExtra("description",pdel.getDescription());
                            inte.putExtra("uri",pdel.getUri());
                            inte.putExtra("location",pdel.getLocation());
                            inte.putExtra("type",pdel.getType());

                            context.startActivity(inte);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getting(String userid, ImageView istimg, TextView usrname){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dt : snapshot.getChildren()){
                    if(dt.getKey().equals(userid)) {
                        usermodel udol = dt.getValue(usermodel.class);
                        Picasso.get().load(udol.getImageurl()).into(istimg);
                        usrname.setText(udol.getUsername());
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
        return notylst.size();
    }

    public class viewolder extends RecyclerView.ViewHolder{
        private ImageView istimg,tothimag,acc,rej,bcg,annofri,annocru,annoadm;
        private TextView usss,rea;


        public viewolder(@NonNull View itemView) {
            super(itemView);

            bcg=itemView.findViewById(R.id.cardview);
            istimg=itemView.findViewById(R.id.chatpro);
            tothimag=itemView.findViewById(R.id.pst);
            acc=itemView.findViewById(R.id.acc);
            rej=itemView.findViewById(R.id.rej);
            usss=itemView.findViewById(R.id.chatusernme);
            rea=itemView.findViewById(R.id.chatlin);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);

        }
    }
}
