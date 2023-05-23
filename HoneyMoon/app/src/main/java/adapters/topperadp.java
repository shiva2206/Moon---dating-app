package adapters;

import android.annotation.SuppressLint;
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
import com.example.honeymoon.story_see;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.toppermodel;
import model.usermodel;

public class topperadp  extends RecyclerView.Adapter<topperadp.viwhlder>{

    private Context context;
    private List<toppermodel> toppmdel;
    private String usrfri;

    public topperadp(Context context, List<toppermodel> toppmdel) {
        this.context = context;
        this.toppmdel = toppmdel;
    }

    @NonNull
    @Override
    public viwhlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_topper,parent ,false);
        return new topperadp.viwhlder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull viwhlder holder, int position) {


        usermodel udel = toppmdel.get(position).getUsermodel();

        List<Integer> admmatfri = toppmdel.get(position).getMatadmfri();

//        if(udel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//            holder.cdvie.setCardBackgroundColor(R.color.grey);
//        }else{
//            holder.cdvie.setCardBackgroundColor(R.color.black);
//        }
        System.out.println(udel.getUserid());
//        if(admmatfri.get(0).equals(0) && admmatfri.get(1).equals(0) && admmatfri.get(2).equals(0)){
//            holder.cdvie.setVisibility(GONE);
//        }else{
//            holder.cdvie.setVisibility(View.VISIBLE);
//        }


        if (udel.getGender().equals("male")){
            usrfri="GFs";
        }else{
            usrfri="BFs";
        }
        holder.unames.setText(udel.getUsername());

        effecy sen=new effecy(udel.getUserid(),holder.bcg);
        effecy anno = new effecy(context,udel.getUserid(),holder.annocru,holder.annoadm,holder.annofri);

        holder.sno.setText((position+1)+"");
        Picasso.get().load(udel.getImageurl()).into(holder.impro);

        System.out.println(admmatfri);
        holder.matnum.setText(admmatfri.get(0)+"M");
        holder.admnum.setText(admmatfri.get(1)+"A");
        holder.frinum.setText(admmatfri.get(2)+usrfri);



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
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",udel.getUserid());
                context.startActivity(inte);
                holder.rela.setEnabled(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return toppmdel.size();
    }

    public class viwhlder extends RecyclerView.ViewHolder{

        private ImageView impro,bcg,annocru,annofri,annoadm;
        private RelativeLayout rela;
        private CardView cdvie;
        private TextView unames,sno,matnum,admnum,frinum;

        public viwhlder(@NonNull View itemView) {
            super(itemView);

            bcg=itemView.findViewById(R.id.cardview);
            impro=itemView.findViewById(R.id.chatpro);
            unames=itemView.findViewById(R.id.chatusernme);
            rela=itemView.findViewById(R.id.messrl);
            sno=itemView.findViewById(R.id.lp);
            cdvie= itemView.findViewById(R.id.cdmess);
            matnum=itemView.findViewById(R.id.matnum);
            admnum=itemView.findViewById(R.id.admnum);
            frinum=itemView.findViewById(R.id.frinum);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
        }
    }
}
