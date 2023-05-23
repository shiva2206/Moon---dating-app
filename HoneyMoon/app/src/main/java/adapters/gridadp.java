package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.postact;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.postmodel;

public class gridadp extends RecyclerView.Adapter<gridadp.viewholder> {
    private Context context;
    private List<postmodel> postmodels;
    private String userid;
    private boolean isprofile;


    public gridadp(Context context, String userid, List<postmodel> postmodls,boolean isprofile) {
        this.context = context;
        this.userid = userid;
        this.postmodels=postmodls;
        this.isprofile=isprofile;

    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_grid,parent,false);
        return new gridadp.viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, @SuppressLint("RecyclerView") int position) {
        postmodel pd = postmodels.get(position);

        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && position == 0 && isprofile ){
             Picasso.get().load("hello").placeholder(R.drawable.add).into(holder.imag);
             holder.adn.setVisibility(View.VISIBLE);

        }
        else{
            effecy.instance.postexist(pd,holder.imag);
            holder.adn.setVisibility(View.INVISIBLE);
            if(pd.getType().equals("image")) {
                Picasso.get().load(pd.getUri()).into(holder.imag);
            }else{
                holder.vid.setVisibility(View.VISIBLE);
                holder.imag.setVisibility(View.GONE);

//                MediaController mcon = new MediaController(context);
//                mcon.setAnchorView(holder.vid);
//                holder.vid.setMediaController(mcon);
//                holder.vid.setVideoURI(Uri.parse(pd.getUri()));
//                holder.vid.start();

            }
        }
        holder.vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.vid.setEnabled(false);
                if ((holder.imag.getTag()+"").equals("yes")) {
                    Intent inte = new Intent(context, Mainactivity.class);
                    inte.putExtra("postid", pd.getPostid());
                    inte.putExtra("publisherid", pd.getPublisherid());
                    inte.putExtra("time", pd.getTime());
                    inte.putExtra("tags", pd.getTags());
                    inte.putExtra("description", pd.getDescription());
                    inte.putExtra("uri", pd.getUri());
                    inte.putExtra("location", pd.getLocation());
                    inte.putExtra("type", pd.getType());

                    context.startActivity(inte);
                }else {
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();
                }
                holder.vid.setEnabled(true);
            }
        });

        holder.imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imag.setEnabled(false);
                if(isprofile && position == 0 && userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                    PopupMenu pmenu = new PopupMenu(context,holder.imag);
                    pmenu.getMenuInflater().inflate(R.menu.vidorph,pmenu.getMenu());
                    pmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.photo:
                                    Intent iny =new Intent(context, postact.class);
                                    iny.putExtra("type","image/*");
                                    context.startActivity(iny);
                                    break;
                                case R.id.video:
                                    Intent iney =new Intent(context, postact.class);
                                    iney.putExtra("type","video/*");
                                    context.startActivity(iney);
                                    break;

                            }
                            return false;
                        }
                    });
                    pmenu.show();
                }
                else{
                    if ((holder.imag.getTag()+"").equals("yes")) {
                        Intent inte = new Intent(context, Mainactivity.class);
                        inte.putExtra("postid", pd.getPostid());
                        inte.putExtra("publisherid", pd.getPublisherid());
                        inte.putExtra("time", pd.getTime());
                        inte.putExtra("tags", pd.getTags());
                        inte.putExtra("description", pd.getDescription());
                        inte.putExtra("uri", pd.getUri());
                        inte.putExtra("location", pd.getLocation());
                        inte.putExtra("type", pd.getType());

                        context.startActivity(inte);
                    }else {
                        Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }
                holder.imag.setEnabled(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postmodels.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        private ImageView imag;
        private VideoView vid;
        private TextView adn;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            adn=itemView.findViewById(R.id.adn);
            imag=itemView.findViewById(R.id.ig);
            vid=itemView.findViewById(R.id.iv);
        }
    }

}
