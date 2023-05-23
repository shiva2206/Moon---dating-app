package adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.chatmodel;
import model.statusmodel;
import model.usermodel;

//backgroung color red with image match parent
public class imageadp extends RecyclerView.Adapter<imageadp.viewho> {

    private Context context;
    private List<statusmodel> stalst;
    private List<chatmodel> chlst;
    private String secuserid;
    private List<String> issennlst;

    public List<String> getIssennlst() {
        return issennlst;
    }

    public imageadp(Context context, List<statusmodel> stalst) {
        this.context = context;
        this.stalst = stalst;

    }
    public imageadp(Context context, List<statusmodel> stalst,List<String> issennlst) {
        this.context = context;
        this.stalst = stalst;
        this.issennlst=issennlst;

    }

    public imageadp(List<chatmodel> chlst,Context context,String secuserid) {
        this.context = context;
        this.chlst = chlst;
        this.secuserid = secuserid;
    }

    public List<chatmodel> getChlst(){
        return chlst;
    }

    public List<statusmodel> getStalst() {
        return stalst;
    }

    @NonNull
    @Override
    public viewho onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_image,parent,false);
        return new imageadp.viewho(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewho holder, int position) {
        if(stalst != null) {
            statusmodel stt = stalst.get(position);
            Picasso.get().load(stt.getUri()).into(holder.imgv);
            holder.tme.setText(effecy.instance.getzonetime(stt.getTime()));
        }else{
            chatmodel chdel = chlst.get(position);
            holder.usrnmm.setVisibility(View.VISIBLE);
            holder.tme.setText(chdel.getTime());
            if(chdel.getType().equals("image")){
                holder.vido.setVisibility(View.INVISIBLE);
                holder.imgv.setVisibility(View.VISIBLE);
                Picasso.get().load(chdel.getUri()).into(holder.imgv);

            }else{
                holder.imgv.setVisibility(View.INVISIBLE);
                holder.vido.setVisibility(View.VISIBLE);
                MediaController mcon = new MediaController(context);

                mcon.setAnchorView(holder.vido);
                holder.vido.setMediaController(mcon);
                holder.vido.setVideoURI(Uri.parse(chdel.getUri()));
//                holder.vido.start();

            }
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dss : snapshot.getChildren()){
                        usermodel udel = dss.getValue(usermodel.class);
                        if(chdel.getUserid().equals(udel.getUserid())){
                            holder.usrnmm.setText(udel.getUsername());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        if(stalst == null){
            return chlst.size();
        }else{
            return stalst.size();
        }

    }

    public class viewho extends RecyclerView.ViewHolder{
        private ImageView imgv;
        private VideoView vido;
        private TextView tme,usrnmm;

        public viewho(@NonNull View itemView) {
            super(itemView);

           imgv=itemView.findViewById(R.id.imst);
           tme=itemView.findViewById(R.id.ttmie);
           vido = itemView.findViewById(R.id.vidp);
           usrnmm=itemView.findViewById(R.id.usrnmm);
        }
    }
}
