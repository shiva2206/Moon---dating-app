package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.postmodel;

public class z_unwanted1 extends RecyclerView.Adapter<z_unwanted1.veihder>{

    private Context context;
    private List<postmodel> imagests;

    public z_unwanted1(Context context, List<postmodel> imagests) {
        this.context = context;
        this.imagests = imagests;
    }

    @NonNull
    @Override
    public veihder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.z_unwanted_adap_slider,parent,false);
        return new z_unwanted1.veihder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull veihder holder, int position) {
        Picasso.get().load(imagests.get(position).getUri()).placeholder(R.drawable.logo_background).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagests.size();
    }

    public class veihder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public veihder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.sli);
        }
    }
}
