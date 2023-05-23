package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.statusmodel;

public class autostoryadap extends SliderViewAdapter<autostoryadap.vieholder> {

    private Context context;
    private List<statusmodel> staimgs;

    public autostoryadap(Context context, List<statusmodel> staimgs) {
        this.context = context;
        this.staimgs = staimgs;
    }

    @Override
    public vieholder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_grid,parent,false);
        return new autostoryadap.vieholder(view);
    }

    @Override
    public void onBindViewHolder(vieholder holder, int position) {
        statusmodel sdl = staimgs.get(position);
        Picasso.get().load(sdl.getUri()).placeholder(R.drawable.logo_background).into(holder.img);
        holder.tmme.setText(sdl.getTime());
        FirebaseDatabase.getInstance().getReference().child("info").child(sdl.getPublisherid()).child("seen")
                .child(sdl.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
    }

    @Override
    public int getCount() {
        return staimgs.size();
    }

    public class vieholder extends SliderViewAdapter.ViewHolder{
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
