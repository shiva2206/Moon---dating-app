package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.honeymoon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.postmodel;

public class hashadp extends RecyclerView.Adapter<hashadp.viehder> {
    private Context context;
    private List<String> hashlst;


    public hashadp(Context context, List<String> hashlst) {
        this.context = context;
        this.hashlst = hashlst;
    }

    @NonNull
    @Override
    public viehder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_hashs,parent,false);
        return new hashadp.viehder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viehder holder, int position) {

        String hashtag = hashlst.get(position);
        List<postmodel> pstlst = new ArrayList<>();

        search_viewpageradp adp = new search_viewpageradp(context,pstlst);
        holder.viewpag.setAdapter(adp);

        holder.hnme.setText(hashtag);
        getallpsts(hashtag,holder.totpsts,pstlst, adp);
    }public void getallpsts(String htgs,TextView tpsts,List<postmodel> pstlst,search_viewpageradp adp){
        FirebaseDatabase.getInstance().getReference().child("hashs").child(htgs).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pstlst.clear();
                tpsts.setText(snapshot.getChildrenCount()+"");
                for(DataSnapshot st : snapshot.getChildren()){
                    pstlst.add(st.getValue(postmodel.class));
                }
                adp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return hashlst.size();
    }

    public class viehder extends RecyclerView.ViewHolder{

        private ViewPager viewpag;
        private TextView hnme,totpsts;
        private Button foll;
        private CardView follcd;
        public viehder(@NonNull View itemView) {
            super(itemView);

            viewpag = itemView.findViewById(R.id.vpa);
            hnme=itemView.findViewById(R.id.hnae);
            totpsts=itemView.findViewById(R.id.totpsts);
            foll=itemView.findViewById(R.id.buts);
            follcd=itemView.findViewById(R.id.ss);
        }
    }
}
