package adapters;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.chats;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.chatmodel;
import model.usermodel;
import model.varymodel;


//message log
public class messadap extends RecyclerView.Adapter<messadap.iehdr>{
    private Context context;
    private List<usermodel> uselst;
    private List<Integer> newmesslst;
    private List<chatmodel> lastchtlst;


    public messadap(Context context, List<usermodel> uselst, List<Integer> newmesslst, List<chatmodel> lastchtlst) {
        this.context = context;
        this.uselst = uselst;
        this.newmesslst = newmesslst;
        this.lastchtlst = lastchtlst;
    }

    @NonNull
    @Override
    public iehdr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_mess,parent,false);
        return new messadap.iehdr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull iehdr holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setDuration(1000);
        usermodel udel = uselst.get(position);
        chatmodel cdel = lastchtlst.get(position);
        String nomes = String.valueOf(newmesslst.get(position));

        holder.unames.setText(udel.getUsername());
        if(nomes.equals("0")) {
            holder.nomes.setVisibility(GONE);

        }else{
            holder.nomes.setVisibility(View.VISIBLE);
            holder.nomes.setText(nomes);

        }


//        curr(holder.lne,udel);
//
        if(cdel.getChat()!=null){
            if(cdel.getSeen() != null && cdel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                holder.lne.setText(cdel.getChat()+" (Seen)");
            }else{
                holder.lne.setText(cdel.getChat());
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String currdate = effecy.instance.getzonetime(effecy.instance.gettime().substring(0,16)).substring(0,10);
                String yesdate=effecy.instance.getzonetime(effecy.instance.nextprevioustime(effecy.instance.gettime(),-60*24).substring(0,16)).substring(0,10);
                Toast.makeText(context, currdate+"  " +yesdate, Toast.LENGTH_SHORT).show();
                if(currdate.substring(0,10).equals(effecy.instance.getzonetime(cdel.getTime().substring(0,16)).substring(0,10))){
                    holder.timu.setText(cdel.getTime().substring(11,16));
                }else if(yesdate.substring(0,10).equals(effecy.instance.getzonetime(cdel.getTime().substring(0,16)).substring(0,10))){
                    holder.timu.setText("Yesterday");
                }else{
                    holder.timu.setText(effecy.instance.getzonetime(cdel.getTime().substring(0,16)).substring(0,10));
                }
            }
        }else{
            holder.lne.setText("");
        }

        effecy sen=new effecy(udel.getUserid(),holder.bcg);
        effecy anno = new effecy(context,udel.getUserid(),holder.annocru,holder.annoadm,holder.annofri);

        Picasso.get().load(udel.getImageurl()).into(holder.impro);
//        getline(udel,holder);
//        effecy.instance.onlinesta(udel.getUserid(),udel.getShowlastseen(),holder.lne,false);
//        if (holder.lne.getTag()!=null){
//            holder.onimg.setVisibility(View.VISIBLE);
//        }else{
//            holder.onimg.setVisibility(GONE);
//        }
        holder.bcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bcg.setEnabled(false);
                if(holder.bcg.getTag().equals("yes")){
                    Intent inte = new Intent(context, story_see.class);
                    inte.putExtra("userid", udel.getUserid());
                    context.startActivity(inte);
                }
                holder.bcg.setEnabled(true);
            }
        });
        holder.rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rela.setEnabled(false);
                Intent inte = new Intent(context, chats.class);
                inte.putExtra("userid",udel.getUserid());
                context.startActivity(inte);
                holder.rela.setEnabled(true);
            }
        });
        holder.unames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unames.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",udel.getUserid());
                context.startActivity(inte);
                holder.unames.setEnabled(true);
            }
        });
    }

    public void getline(usermodel udel, iehdr holder) {
        FirebaseDatabase.getInstance().getReference().child("vary").child(udel.getUserid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                varymodel vdel = snapshot.getValue(varymodel.class);
                if (vdel!=null && vdel.getLine().equals("online")){
                    holder.onimg.setVisibility(View.VISIBLE);
                }else{
                    holder.onimg.setVisibility(GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void curr(TextView lne,usermodel udell){

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel currdel =snapshot.getValue(usermodel.class);
                if(currdel.getShowlastseen().equals("true")){
                    FirebaseDatabase.getInstance().getReference().child("users").child(udell.getUserid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            usermodel audel = snapshot.getValue(usermodel.class);
                            FirebaseDatabase.getInstance().getReference().child("vary").child(udell.getUserid()).addValueEventListener(new ValueEventListener() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    varymodel vydel = snapshot.getValue(varymodel.class);
                                    lne.setTextColor(R.color.white);
                                    if (audel.getShowlastseen().equals("true")) {
                                        lne.setText(vydel.getLine());
                                    } else {
                                        if (vydel.getLine().equals("online")) {
                                            lne.setTextColor(R.color.red);
                                            lne.setText(vydel.getLine());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return uselst.size();
    }

    public class iehdr extends RecyclerView.ViewHolder{
        private ImageView impro,bcg,annocru,annofri,annoadm,onimg;
        private RelativeLayout rela;
        private TextView unames,lne,nomes,timu;

        public iehdr(@NonNull View itemView) {
            super(itemView);

            onimg=itemView.findViewById(R.id.onimg);
            bcg=itemView.findViewById(R.id.cardview);
            impro=itemView.findViewById(R.id.chatpro);
            unames=itemView.findViewById(R.id.chatusernme);
            lne=itemView.findViewById(R.id.chatlin);
            rela=itemView.findViewById(R.id.messrl);
            nomes=itemView.findViewById(R.id.noofmess);
            timu=itemView.findViewById(R.id.ttmmec);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
        }
    }
}
