package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.usermodel;

public class tagadp extends RecyclerView.Adapter<tagadp.vviehdr>{

    private Context context;
    private List<String> usenamelst;
    private List<usermodel> usrmdelst,alluserdellst;

    public tagadp(Context context, List<String> usenamelst, List<usermodel> usrmdelst,List<usermodel> alluserdellst) {
        this.context = context;
        this.usenamelst = usenamelst;
        this.usrmdelst = usrmdelst;
        this.alluserdellst=alluserdellst;
    }


    public List<String> getUsenamelst() {
        return usenamelst;
    }

    public List<usermodel> getUsrmdelst() {
        return usrmdelst;
    }


    @NonNull
    @Override
    public vviehdr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_tags,parent,false);
        return new tagadp.vviehdr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vviehdr holder, int position) {

        String usrnme = usenamelst.get(position);
        String infodata=usenamelst.get(position);
        Toast.makeText(context, alluserdellst.toArray().length+"", Toast.LENGTH_SHORT).show();
        for(usermodel udel : alluserdellst){
            if(udel.getUsername().equals(usrnme)){
                usrmdelst.add(udel);

                Picasso.get().load(udel.getImageurl()).into(holder.prof);
                holder.name.setText(udel.getName());

            }
        }
        holder.soci.setText(usrnme);

        holder.clos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.clos.setEnabled(false);
                removeitem(infodata);
                holder.clos.setEnabled(true);
            }
        });

    }
    public void removeitem(String infodata){
        int pooo = usenamelst.indexOf(infodata);
        usrmdelst.remove(pooo);
        usenamelst.remove(pooo);
        notifyItemRemoved(pooo);
    }

    @Override
    public int getItemCount() {
        return usenamelst.size();
    }

    public class vviehdr extends RecyclerView.ViewHolder{

        private SocialTextView soci;
        private ImageView clos,prof;
        private TextView name;

        public vviehdr(@NonNull View itemView) {
            super(itemView);

            soci = itemView.findViewById(R.id.sstv);
            clos = itemView.findViewById(R.id.cls);
            name=itemView.findViewById(R.id.tgnme);
            prof=itemView.findViewById(R.id.tgpro);
        }
    }
}
