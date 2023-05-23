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
import com.squareup.picasso.Picasso;

import java.util.List;

import model.statusmodel;

public class z_unwanted2 extends RecyclerView.Adapter<z_unwanted2.vieholder> {

    private Context context;
    private List<statusmodel> staimgs;
    private boolean isstory;

    public z_unwanted2(Context context, List<statusmodel> staimgs, boolean isstory) {
        this.context = context;
        this.staimgs = staimgs;
        this.isstory = isstory;
    }

    public List<statusmodel> getStaimgs() {
        return staimgs;
    }

    @NonNull
    @Override
    public vieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_grid,parent,false);
        return new z_unwanted2.vieholder(view);
    }

    @Override
    public void onBindViewHolder(vieholder holder, int position) {
        statusmodel sdl = staimgs.get(position);
        Picasso.get().load(sdl.getUri()).placeholder(R.drawable.logo_background).into(holder.img);
        holder.tmme.setText(sdl.getTime());

    }

    @Override
    public int getItemCount() {
        return staimgs.size();
    }

    public class vieholder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tmme;

        public vieholder(View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.ig);
            tmme=itemView.findViewById(R.id.ttme);
            tmme.setVisibility(View.VISIBLE);
        }
    }


}
