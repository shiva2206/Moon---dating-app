package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;

import java.util.List;

public class indiadap extends RecyclerView.Adapter<indiadap.viehold> {
    private Context context;
    private List<Integer> images;

    public indiadap(Context context, List<Integer> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public viehold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_indicators,parent,false);
        return new indiadap.viehold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viehold holder, int position) {

        holder.img.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class viehold extends RecyclerView.ViewHolder{

        private ImageView img;
        public viehold(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.rund);
        }
    }
}
