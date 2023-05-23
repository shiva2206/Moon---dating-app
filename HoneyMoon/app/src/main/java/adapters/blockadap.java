package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.usermodel;

public class blockadap extends RecyclerView.Adapter<blockadap.vdowhlder> {

    private Context context;
    private List<usermodel> lstudel;

    public blockadap(Context context, List<usermodel> lstudel) {
        this.context = context;
        this.lstudel = lstudel;
    }

    @NonNull
    @Override
    public vdowhlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_blockedconts,parent,false);
        return new blockadap.vdowhlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vdowhlder holder, int position) {

        usermodel uel = lstudel.get(position);

        effecy sen=new effecy(uel.getUserid(),holder.bcg);
        effecy anno = new effecy(context,uel.getUserid(),holder.annocru,holder.annoadm,holder.annofri);

//        holder.sno.setText(position+"");
        Picasso.get().load(uel.getImageurl()).into(holder.impro);
        holder.unames.setText(uel.getUsername());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("blocked")
                        .child(uel.getUserid()).removeValue();
            }
        });

        holder.unames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unames.setEnabled(false);
                Intent ine = new Intent(context, Mainactivity.class);
                ine.putExtra("userid",uel.getUserid());
                ((Activity)context).startActivity(ine);
                holder.unames.setEnabled(true);
            }
        });



    }

    @Override
    public int getItemCount() {
        return lstudel.size();
    }

    public class vdowhlder extends RecyclerView.ViewHolder{

        private ImageView impro,bcg,annocru,annofri,annoadm;
        private RelativeLayout rela;
        private CardView cdvie;
        private TextView unames,sno,remove;

        public vdowhlder(@NonNull View itemView) {
            super(itemView);
            bcg=itemView.findViewById(R.id.cardview);
            impro=itemView.findViewById(R.id.chatpro);
            unames=itemView.findViewById(R.id.chatusernme);
            rela=itemView.findViewById(R.id.messrl);
            sno=itemView.findViewById(R.id.lp);
            cdvie= itemView.findViewById(R.id.cdmess);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
            remove=itemView.findViewById(R.id.ere);

        }
    }
}
