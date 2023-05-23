package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.usermodel;

public class sec_searchadp extends RecyclerView.Adapter<sec_searchadp.Vehder>{
    private Context context;
    private List<String> lstusr;

    public sec_searchadp(Context context, List<String> lstusr) {
        this.context = context;
        this.lstusr = lstusr;
    }

    @NonNull
    @Override
    public Vehder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_search_two,parent,false);
        return new sec_searchadp.Vehder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vehder holder, int position) {
       String userid = lstusr.get(position);

       effecy anno = new effecy(context,userid,holder.annocru,holder.annoadm,holder.annofri);
       FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dt : snapshot.getChildren()){
                    if(dt.getKey().equals(userid)) {
                        usermodel del = dt.getValue(usermodel.class);
                        Picasso.get().load(del.getImageurl()).placeholder(R.drawable.profile).into(holder.prof);
                        holder.name.setText(del.getName());
                        holder.usenme.setText(del.getUsername());
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
        return lstusr.size();
    }

    public class Vehder extends RecyclerView.ViewHolder{
        private ImageView prof,annocru,annofri,annoadm;
        private TextView usenme,name;

        public Vehder(@NonNull View itemView) {
            super(itemView);
            prof=itemView.findViewById(R.id.vpro);
            usenme=itemView.findViewById(R.id.vusme);
            name=itemView.findViewById(R.id.vnme);

            annocru=itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
        }
    }
}
