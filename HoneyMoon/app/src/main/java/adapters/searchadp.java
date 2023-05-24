package adapters;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.honeymoon.Mainactivity.currfri;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.nearby;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.keymodel;
import model.nearbymodel;
import model.usermodel;

public class searchadp extends RecyclerView.Adapter<searchadp.viewholder> {
    private Context context;
    private List<usermodel> allusers;
    private String what,userid;
    private List<String> distlst;

    private nearbymodel currnearbymdel;
    private Intent intentttt;
    public List<usermodel> getAllusers() {
        return allusers;
    }

    public searchadp(Context context, List<usermodel> allusers, String what, String userid) {
        this.context = context;
        this.allusers = allusers;
        this.userid = userid;
        if(what == null){
            this.what = "all";
        }else {
            this.what = what;

        }
        this.intentttt = new Intent(this.context, nearby.class);

    }
    public searchadp(Context context, List<usermodel> allusers, String what, String userid,List<String> distlst,nearbymodel currnearbymdel) {
        this.context = context;
        this.allusers = allusers;
        this.userid = userid;
        this.distlst= distlst;
        this.currnearbymdel = currnearbymdel;
        if(what == null){
            this.what = "all";
        }else {
            this.what = what;

        }
        this.intentttt = new Intent(this.context, nearby.class);

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_search,parent,false);
        return new searchadp.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        if((what.equals("followers") || what.equals("crushs") || what.equals("gfs") || what.equals("admirers") || what.equals("bfs")) && userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ){
            holder.remove.setVisibility(View.VISIBLE);
        }else{
            holder.remove.setVisibility(View.INVISIBLE);
        }

        usermodel uel = allusers.get(position);
        effecy ag = new effecy(context,uel.getUserid(),holder.age);

        if(what.equals("login")){
            context.getSharedPreferences("profile", MODE_PRIVATE).edit().putString("userid",uel.getUserid()).commit();
            context.getSharedPreferences("profile", MODE_PRIVATE).edit().putString("password", uel.getPassword()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("dob", udell.getDob()).commit();
//
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("about",udell.getAbout()).commit();
            context.getSharedPreferences("profile", MODE_PRIVATE).edit().putString("email", uel.getEmail()).commit();
            context.getSharedPreferences("profile", MODE_PRIVATE).edit().putString("gender", uel.getGender()).commit();
            System.out.println("im waiting");
            Intent intro = new Intent(context,Mainactivity.class);
            ((Activity)context).startActivity(intro);

        }
        else if(what.equals("nearby")){
            intentttt.putExtra(uel.getUserid(),distlst.get(position));
            if(allusers.size() == position+1 && effecy.instance.qaz == 0){
                intentttt.putExtra("nearby","nearby");
                intentttt.putExtra("gender",currnearbymdel.getGender());
                intentttt.putExtra("status",currnearbymdel.getStatus());
                intentttt.putExtra("agefrom",currnearbymdel.getAgefrom());
                intentttt.putExtra("ageto",currnearbymdel.getAgeto());
                intentttt.putExtra("distance",currnearbymdel.getDistance());


                context.startActivity(intentttt);
                effecy.instance.qaz =1;


                ((Activity)context).finish();

            }
        }
        if(uel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.foll.setVisibility(View.INVISIBLE);
        }else{
            holder.foll.setVisibility(View.VISIBLE);
        }

        if(uel.getBluetick().equals("true")){
            holder.bltk.setVisibility(VISIBLE);
        }else{
            holder.bltk.setVisibility(GONE);
        }


        effecy anno = new effecy(context,uel.getUserid(),holder.annocru,holder.annoadm,holder.annofri);
        effecy bcv = new effecy(uel.getUserid(),holder.bcimg);
        Picasso.get().load(uel.getImageurl()).into(holder.prof);

        if(what.equals("followers") || what.equals("all") || what.equals("bfs") || what.equals("gfs")){
            getfri(uel,holder.remove);
            if(what.equals("followers")){
                getfolw(uel,holder.prof);
            }
        }else if(what.equals("crushs")){
            getcru(uel,holder.prof);
            getmat(uel,holder.remove);
        }else if(what.equals("admirers")){
            getadm(uel,holder.prof);
            getmat(uel,holder.remove);
        }


        holder.cddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cddd.setEnabled(false);
                Intent inte = new Intent(context,Mainactivity.class);
                inte.putExtra("userid",uel.getUserid());
                context.startActivity(inte);
                holder.cddd.setEnabled(true);
            }
        });
        holder.bcimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bcimg.setEnabled(false);
                if (holder.bcimg.getTag() != null  && holder.bcimg.getTag().equals("yes")) {
                    if(uel.getUserid() == FirebaseAuth.getInstance().getCurrentUser().getUid()){
                        Intent inte = new Intent(context, story_see.class);
                        context.startActivity(inte);
                    }else {
                        Intent inte = new Intent(context, story_see.class);
                        inte.putExtra("userid", uel.getUserid());
                        context.startActivity(inte);
                    }
                }
                holder.bcimg.setEnabled(true);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.remove.setEnabled(false);
                if(what.equals("followers")){
                    setbuilder(uel,holder.prof.getTag()+"",holder.remove.getTag()+"");
                }else if(what.equals("bfs") || what.equals("gfs")){
                    setbuilder(uel,null,holder.remove.getTag()+"");
                }else if(what.equals("crushs")){
                    setbuilder(uel,holder.prof.getTag()+"",holder.remove.getTag()+"");
                }else if(what.equals("admirers")){
                    setbuilder(uel,holder.prof.getTag()+"",holder.remove.getTag()+"");
                }
                holder.remove.setEnabled(true);
            }
        });

        holder.stats.setText(uel.getStatus());
        holder.usrnme.setText(uel.getUsername()+",");

        holder.foll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.foll.setEnabled(false);
                if (holder.foll.getTag().toString().startsWith("following")) {
                    String folltag = holder.foll.getTag().toString().substring(9);

                    String s = "";
                    if(holder.remove.getTag() != null || (holder.remove.getTag()+"").length() > 4){
                        s="Removing Following Will Remove "+uel.getUsername()+" From "+currfri.substring(0,2)+".\n Are U Sure";
                    }else{
                        s="Do U Want To Unfollow "+uel.getUsername();
                    }


                    android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
                    alt.setInverseBackgroundForced(true);
                    alt.setTitle("Remove Following!!!").setMessage(s)
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    effecy eff = new effecy(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),uel.getUserid(),
                                            "following",folltag,holder.remove.getTag()+"");

                                }
                            })
                            .show();




                } else if(holder.foll.getTag().equals("request")){
                    effecy eff = new effecy(context,uel.getUserid(),"requestcancel",uel.getMode());
                }
                else {

                    effecy eff = new effecy(context,uel.getUserid(),"follows",uel.getMode());

                }
                holder.foll.setEnabled(true);

            }
        });

        getfoll(uel.getUserid(),holder.foll);

    }
    public void setbuilder(usermodel udel,String firsttag,String secondtag){

        if(what.equals("followers") ){

            String s = "";
            if(secondtag != null || secondtag.length() > 4){
                s="Removing Follower Will Remove "+udel.getUsername()+" From "+currfri.substring(0,2)+". \n Are U Sure";
            }else{
                s="Do U Want To Remove "+udel.getUsername()+" as a Follower";
            }


            android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
            alt.setInverseBackgroundForced(true);
            alt.setTitle("Remove Follower!!!").setMessage(s)
                    .setCancelable(true)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            effecy eff = new effecy(context,udel.getUserid(),FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    "following",firsttag,secondtag);

                        }
                    })
                    .show();



        }
        else if( what.equals("gfs") || what.equals("bfs")) {
            android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
            alt.setInverseBackgroundForced(true);
            alt.setTitle("Remove "+currfri+"!!!").setMessage("Do you want to Remove "+udel.getUsername()+" From " + currfri.substring(0,2))
                    .setCancelable(true)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            effecy eff = new effecy(context, FirebaseAuth.getInstance().getCurrentUser().getUid(), udel.getUserid(),
                                    "friendremove", null, secondtag);

                        }
                    })
                    .show();


        }
        else if(what.equals("crushs")){
            String s ="";
            Toast.makeText(context, secondtag+"", Toast.LENGTH_SHORT).show();
            if(secondtag.length() > 4 || !secondtag.equals("null")){
                s="You Are Having a Match with "+udel.getUsername()+" .Removing Crush will remove the Match. \n Are U Sure";
            }else{
                s="Do U Want To Remove "+udel.getUsername()+" From Crush.";
            }
            android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
            alt.setInverseBackgroundForced(true);
            alt.setTitle("Crush Remove!!!").setMessage(s)
                    .setCancelable(true)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            effecy eff = new effecy(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),udel.getUserid(),
                                    "crushremove",firsttag,secondtag);

                        }
                    })
                    .show();


        }
        else if(what.equals("admirers")){

            String s ="";
            if(secondtag != null){
                s="You Are Having a Match with "+udel.getUsername()+" .Removing Admirer will remove the Match. \n Are U Sure";
            }else{
                s="Do U Want To Remove "+udel.getUsername()+" From Admirer";
            }

            android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
            alt.setInverseBackgroundForced(true);
            alt.setTitle("Admirer Remove!!!").setMessage(s)
                    .setCancelable(true)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            effecy eff = new effecy(context,udel.getUserid(),FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    "crushremove",firsttag,secondtag);

                        }
                    })
                    .show();

        }



    }
    public void getmat(usermodel udelll,ImageView remove){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("matches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(udelll.getUserid()).exists()){
                    keymodel kdel = snapshot.child(udelll.getUserid()).getValue(keymodel.class);
                    remove.setTag(kdel.getDate()+kdel.getKey());
                }else{
                    remove.setTag(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getcru(usermodel udelll,ImageView prof){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("crushs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(udelll.getUserid()).exists()){
                    keymodel kdel = snapshot.child(udelll.getUserid()).getValue(keymodel.class);
                    prof.setTag(kdel.getDate()+kdel.getKey());
                }else{
                    prof.setTag(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getadm(usermodel udelll,ImageView prof){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("admirers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(udelll.getUserid()).exists()){
                    keymodel kdel = snapshot.child(udelll.getUserid()).getValue(keymodel.class);
                    prof.setTag(kdel.getDate()+kdel.getKey());
                }else{
                    prof.setTag(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getfoll(String useid,Button bon){
        FirebaseDatabase.getInstance().getReference().child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(useid).exists()){
                    keymodel kydell = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(useid).getValue(keymodel.class);
                    bon.setText("Following");
                    System.out.println(kydell.getDate()+kydell.getKey()+"ppp");
                    bon.setTag("following"+kydell.getDate()+ kydell.getKey());
                }else if(snapshot.child(useid).child("notify").child("requests").child("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    bon.setText("Request Sent");
                    bon.setTag("request");

                }else if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers").child(useid).exists() && !snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(useid).exists()){
                    bon.setText("Follow Back");
                    bon.setTag("follow");

                }else{
                    bon.setText("Follow");
                    bon.setTag("follow");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getfri(usermodel uudl,ImageView remove){
        String oppfri = "";
        if(currfri == "gfs"){
            oppfri="bfs";
        }else{
            oppfri="gfs";
        }
        FirebaseDatabase.getInstance().getReference().child("info").child(uudl.getUserid()).child(oppfri).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            keymodel kkkel = snapshot.getValue(keymodel.class);
                            remove.setTag(kkkel.getDate()+kkkel.getKey());
                        }else{
                            remove.setTag(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
        });
    }

    public void getfolw(usermodel udelll,ImageView prof){
        FirebaseDatabase.getInstance().getReference().child("info").child(udelll.getUserid()).child("followings").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            keymodel kdel = snapshot.getValue(keymodel.class);
                            prof.setTag(kdel.getDate() + kdel.getKey());
                        }else{
                            prof.setTag(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    public int getItemCount() {
        return allusers.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        private ImageView annocru,annofri,annoadm,prof,bcimg,remove,bltk;
        private TextView usrnme,age,stats;
        private Button foll;
        private RelativeLayout cddd;


        public viewholder(@NonNull View itemView) {
            super(itemView);

            prof=itemView.findViewById(R.id.vpa);
            usrnme=itemView.findViewById(R.id.hnae);
            age=itemView.findViewById(R.id.ag);
            stats=itemView.findViewById(R.id.stats);
            foll=itemView.findViewById(R.id.buts);

            bcimg=itemView.findViewById(R.id.bcvpa);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
            remove=itemView.findViewById(R.id.remove);
            cddd=itemView.findViewById(R.id.cddd);
            bltk=itemView.findViewById(R.id.bltk);

        }

    }
}
