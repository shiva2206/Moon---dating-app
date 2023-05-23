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




public class aftertagadp extends RecyclerView.Adapter<aftertagadp.vhdr>{

    private Context context;
    public aftertagadp(Context context) {
        this.context = context;
    }

    private List<usermodel> alstuser;

    @NonNull

    @Override
    public vhdr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_aftertag,parent,false);
        return new aftertagadp.vhdr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhdr holder, int position) {

        usermodel userr  = alstuser.get(position);

        holder.usernme.setText(userr.getUsername());
        Picasso.get().load(userr.getImageurl()).placeholder(R.drawable.logo_background).into(holder.prof);

        holder.clo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alstuser.remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return alstuser.size();
    }

    public class vhdr extends RecyclerView.ViewHolder{

        private ImageView prof,clo;
        private TextView usernme;
        public vhdr(@NonNull View itemView) {
            super(itemView);

            prof = itemView.findViewById(R.id.prf);
            usernme=itemView.findViewById(R.id.urme);
            clo = itemView.findViewById(R.id.cls);

        }
    }
}
