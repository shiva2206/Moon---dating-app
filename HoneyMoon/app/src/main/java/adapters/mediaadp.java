package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.viewchatmedia;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.chatmodel;

public class mediaadp extends RecyclerView.Adapter<mediaadp.vhoder>{
    private Context context;
    private List<chatmodel> lstcht;
    private Intent inte;
    private String secuserid;

    public mediaadp(Context context, List<chatmodel> lstcht,String secuserid) {
        this.context = context;
        this.lstcht = lstcht;
        this.secuserid=secuserid;
        this.inte = new Intent(context, viewchatmedia.class);
    }

    @NonNull
    @Override
    public vhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_media,parent,false);
        return new mediaadp.vhoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhoder holder, int position) {
        chatmodel cel = lstcht.get(position);




        if(cel.getType().equals("image")){
            Picasso.get().load(cel.getUri()).placeholder(R.drawable.logo_background).into(holder.imgv);
        }else{
            holder.vodvw.setVisibility(View.VISIBLE);
            holder.imgv.setVisibility(View.GONE);
            MediaController mcon = new MediaController(context);
            mcon.setAnchorView(holder.vodvw);
            holder.vodvw.setMediaController(mcon);
            holder.vodvw.setVideoURI(Uri.parse(cel.getUri()));
//            holder.vodvw.start();
        }

        holder.imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgv.setEnabled(false);
                inte.putExtra("userid",secuserid);
                inte.putExtra("chatid",cel.getChatid());
                context.startActivity(inte);
                holder.imgv.setEnabled(true);
            }
        });
        holder.vodvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.vodvw.setEnabled(false);
                inte.putExtra("userid",secuserid);
                inte.putExtra("chatid",cel.getChatid());
                context.startActivity(inte);
                holder.vodvw.setEnabled(true);
            }
        });


    }


    @Override
    public int getItemCount() {
        return lstcht.size();
    }

    public class vhoder extends RecyclerView.ViewHolder{
        private ImageView imgv;
        private VideoView vodvw;
        public vhoder(@NonNull View itemView) {
            super(itemView);
            imgv=itemView.findViewById(R.id.docimg);
            vodvw=itemView.findViewById(R.id.docvid);

        }
    }
}
