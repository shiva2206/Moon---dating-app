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
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.allstories;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.highmodel;
//highlights adp
public class highlightadp extends RecyclerView.Adapter<highlightadp.vie>{

    private Context context;
    private String userid;
    private List<highmodel> highlst;

    public highlightadp(Context context, String userid, List<highmodel> highlst) {
        this.context = context;
        this.userid = userid;
        this.highlst = highlst;

    }

    @NonNull
    @Override
    public vie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_high,parent,false);
        return new highlightadp.vie(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vie holder, @SuppressLint("RecyclerView") int position) {
        highmodel hg = highlst.get(position);
        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && position == 0){
            Picasso.get().load("pods").placeholder(R.drawable.add).into(holder.prol);
            holder.title.setText("Add New");
//            holder.cd.setCardBackgroundColor(Color.parseColor("#b70505"));
        }else {
            holder.title.setText(hg.getTitle());
            if(hg.getImageuri() != null){
                Picasso.get().load(hg.getImageuri()).into(holder.prol);
            }

        }
        holder.rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rela.setEnabled(false);
                if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && position == 0){
                   context.startActivity(new Intent(context, allstories.class));
                }else{
                    Intent inte = new Intent(context, story_see.class);
                    inte.putExtra("userid",userid);
                    inte.putExtra("highid",hg.getHighlightid());
                    inte.putExtra("type","highlight");
//                    for(highmodel hmdl : highlst){
//                        inte.putExtra(hmdl.getHighlightid(),"g");
//                    }
                    context.startActivity(inte);
                }
                holder.rela.setEnabled(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return highlst.size();
    }

    public class vie extends RecyclerView.ViewHolder{
        private ImageView prol;
        private RelativeLayout rela;
        private TextView title;

        public vie(@NonNull View itemView) {
            super(itemView);

            prol=itemView.findViewById(R.id.highlgpro);
            rela=itemView.findViewById(R.id.rrrr);
            title=itemView.findViewById(R.id.titl);

        }
    }
}
