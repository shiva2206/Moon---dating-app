package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.postmodel;

public class imagadp extends RecyclerView.Adapter<imagadp.viewholdr>{
    private Context context;
    private List<postmodel> postmodels;
    private String userid;

    public imagadp(Context context, List<postmodel> postmodels, String userid) {
        this.context = context;
        this.postmodels = postmodels;
        this.userid = userid;
    }



    @NonNull
    @Override
    public viewholdr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_imag,parent,false);
        return new imagadp.viewholdr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholdr holder, int position) {
        postmodel pdl = postmodels.get(position);
        Toast.makeText(context, postmodels.size() + " ", Toast.LENGTH_SHORT).show();
        Picasso.get().load(pdl.getUri()).placeholder(R.drawable.logo_background).into(holder.igmage);
    }

    @Override
    public int getItemCount() {
        return postmodels.size();
    }

    public class viewholdr extends RecyclerView.ViewHolder{
        private ImageView igmage;
        public viewholdr(@NonNull View itemView) {
            super(itemView);
            igmage = itemView.findViewById(R.id.igmag);
        }
    }
}
