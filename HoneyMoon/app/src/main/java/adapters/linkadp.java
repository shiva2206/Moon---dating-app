package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;

import java.util.List;

import model.chatmodel;

public class linkadp extends RecyclerView.Adapter<linkadp.vihsder> {
    private Context context;
    private List<chatmodel> lstcht;

    public linkadp(Context context, List<chatmodel> lstcht) {
        this.context = context;
        this.lstcht = lstcht;
    }

    @NonNull
    @Override
    public vihsder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_links,parent,false);
        return new linkadp.vihsder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vihsder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstcht.size();
    }

    public class vihsder extends RecyclerView.ViewHolder{


        public vihsder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
