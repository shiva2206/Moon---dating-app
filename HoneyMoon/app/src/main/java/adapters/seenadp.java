package adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.List;

import model.usermodel;

//seen adp
public class seenadp extends RecyclerView.Adapter<seenadp.vidher> {
    private Context context;
    private List<String> lstuser,timelst;
    private String what;
    private String publisherid,postid;

    public seenadp(Context context, List<String> lstuser, List<String> timelst, String what, String publisherid, String postid) {
        this.context = context;
        this.lstuser = lstuser;
        this.timelst = timelst;
        this.what = what;
        this.publisherid = publisherid;
        this.postid = postid;
    }

    public seenadp(Context context, List<String> lstuser, List<String> timelst, String what) {
        this.context = context;
        this.lstuser = lstuser;
        this.timelst = timelst;
        this.what = what;
    }

    @NonNull
    @Override
    public vidher onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_search_two,parent,false);
        return new seenadp.vidher(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vidher holder, int position) {
        String userid = lstuser.get(position);
        String time = timelst.get(position);
        if (what.equals("likes")){
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(time);
        }else if(what.equals("posttags")){
            if(publisherid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                holder.time.setVisibility(View.VISIBLE);
            }else{
                holder.time.setVisibility(View.INVISIBLE);
            }
            holder.time.setText("Remove Tag");

            holder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.time.setEnabled(false);
                    android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
                    alt.setInverseBackgroundForced(true);
                    alt.setTitle("Remove  Tag").setMessage("Are u Sure ")
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    effecy.instance.sendnotii(context,userid,null,null,null,postid,"del");

                                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify")
                                            .child("others").child(time).child(postid).removeValue();
                                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("tags")
                                            .child(postid).removeValue();
                                    FirebaseDatabase.getInstance().getReference().child("info").child(publisherid).child("posts").child(postid).child("tags")
                                            .child(userid).removeValue();
                                }
                            })
                            .show();
                    holder.time.setEnabled(true);
                }
            });
        } else {
            holder.time.setText(time.substring(10));
        }

        effecy bc = new effecy(userid,holder.backimg);
        effecy anno = new effecy(context,userid,holder.annocru,holder.annoadm,holder.annofri);

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dss : snapshot.getChildren()){
                    if(dss.getKey().equals(userid)){
                        usermodel udel = dss.getValue(usermodel.class);
                        Picasso.get().load(udel.getImageurl()).into(holder.prof);
                        holder.usernme.setText(udel.getUsername());
                        holder.naem.setText(udel.getName());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.usernme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.usernme.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",userid);
                context.startActivity(inte);
                holder.usernme.setEnabled(true);
            }
        });
        holder.prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.prof.setEnabled(false);
                if(holder.backimg.getTag().equals("yes")) {
                    Intent inte = new Intent(context, story_see.class);
                    inte.putExtra("userid", userid);
                    context.startActivity(inte);
                }
                holder.prof.setEnabled(true);
            }
        });
    }


    @Override
    public int getItemCount() {
        return lstuser.size();
    }

    public class vidher extends RecyclerView.ViewHolder{
        private ImageView prof,backimg,annocru,annofri,annoadm;
        private TextView usernme,naem,time;
        public vidher(@NonNull View itemView) {
            super(itemView);
            prof = itemView.findViewById(R.id.vpro);
            backimg=itemView.findViewById(R.id.backimag);
            annoadm=itemView.findViewById(R.id.adm);
            annofri=itemView.findViewById(R.id.fri);
            annocru = itemView.findViewById(R.id.cru);
            usernme=itemView.findViewById(R.id.vusme);
            naem=itemView.findViewById(R.id.vnme);
            time=itemView.findViewById(R.id.tme);
        }
    }
}
