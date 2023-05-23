package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.chatmodel;

public class docsadp extends RecyclerView.Adapter<docsadp.viehoder> {

    private Context context;
    private List<chatmodel> lstcht;

    public docsadp(Context context, List<chatmodel> lstcht) {
        this.context = context;
        this.lstcht = lstcht;
    }

    @NonNull
    @Override
    public viehoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viehoder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstcht.size();
    }

    public class viehoder extends RecyclerView.ViewHolder{

        public viehoder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
