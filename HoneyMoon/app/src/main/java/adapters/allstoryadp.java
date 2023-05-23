package adapters;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.seen;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.statusmodel;

// for viewing all the stories of the user(only currentuser)
public class allstoryadp extends RecyclerView.Adapter<allstoryadp.holderview> {
    private Context context;
    private List<statusmodel> stalst;
    private List<String> ishighlst;
    private List<statusmodel> selectedstalst;

    public allstoryadp(Context context, List<statusmodel> stalst,List<String> ishighlst,List<statusmodel> selectedstalst) {
        this.context = context;
        this.stalst = stalst;
        this.ishighlst=ishighlst;
        this.selectedstalst=selectedstalst;

    }

    public List<statusmodel> getSelectedstalst() {
        return selectedstalst;
    }

    @NonNull
    @Override
    public holderview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_allstories,parent ,false);

        return new allstoryadp.holderview(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull holderview holder, @SuppressLint("RecyclerView") int position) {
         statusmodel statmo = stalst.get(position);
         holder.tme.setVisibility(View.VISIBLE);
         holder.tme.setText(statmo.getTime().substring(11,17));
         if(statmo.getType().equals("image")){
             Picasso.get().load(statmo.getUri()).into(holder.imgv);
         }

         if(ishighlst.get(position).equals("true")){
             holder.alredypree.setVisibility(View.VISIBLE);
         }else{
             holder.alredypree.setVisibility(GONE);
         }
         holder.cd.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 System.out.println(ishighlst.get(position));
                 if(ishighlst.get(position).equals("false")) {
                     if (holder.chek.getVisibility() == View.VISIBLE) {
                         holder.chek.setVisibility(View.INVISIBLE);
                         //alsmdel.removesta(statmo.getStatusid());
                         selectedstalst.remove(statmo);

                     } else {
                         holder.chek.setVisibility(View.VISIBLE);
                         selectedstalst.add(statmo);

                     }
                 }else{
                     Toast.makeText(context, holder.cd.getCardBackgroundColor().toString(), Toast.LENGTH_SHORT).show();
                 }


                 return true;
             }
         });


         holder.cd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 holder.cd.setEnabled(false);
                 Intent inte = new Intent(context, seen.class);
                 inte.putExtra("purpose","allstories");
                 inte.putExtra("position",String.valueOf(position));
                 inte.putExtra("date",stalst.get(position).getTime().substring(0,10));
                 context.startActivity(inte);
                 holder.cd.setEnabled(true);
             }
         });

    }

    @Override
    public int getItemCount() {
        return stalst.size();
    }

    public class holderview extends RecyclerView.ViewHolder{
        private ImageView imgv,chek,alredypree;
        private VideoView vv;
        private TextView tme;
        private CardView cd;
        public holderview(@NonNull View itemView) {
            super(itemView);

            imgv=itemView.findViewById(R.id.ig);
            tme=itemView.findViewById(R.id.ttme);
            cd=itemView.findViewById(R.id.relllls);
            chek = itemView.findViewById(R.id.chek);
            alredypree=itemView.findViewById(R.id.alreadypre);
        }
    }
}
