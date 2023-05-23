package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import model.statusmodel;

public class storyadap extends RecyclerView.Adapter<storyadap.viewh>{

    private List<statusmodel> stalst;
    private Context context;

    public storyadap(List<statusmodel> stalst, Context context) {
        this.stalst = stalst;
        this.context = context;
    }

    @NonNull
    @Override
    public viewh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_story_view,parent,false);
        return new storyadap.viewh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewh holder, int position) {
     statusmodel sta = stalst.get(position);
     holder.tme.setText(sta.getTime());
     getnoviews(sta.getStatusid(),holder.novie);
     holder.men.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             PopupMenu pmne = new PopupMenu(context,holder.men);
             pmne.getMenuInflater().inflate(R.menu.storymen,pmne.getMenu());
             pmne.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                 @Override
                 public boolean onMenuItemClick(MenuItem item) {
                     switch(item.getItemId()){
                         case R.id.forward:
                             break;
                         case R.id.delete:
                             break;
                     }
                     return false;
                 }
             });

         }
     });

    }public void getnoviews(String storyid,TextView noviws){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("seen").child(storyid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    noviws.setText(snapshot.getChildrenCount() + " Views");
                }else{
                    noviws.setText("0 Views");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return stalst.size();
    }

    public class viewh extends RecyclerView.ViewHolder{
        private ImageView sto,men;
        private TextView novie,tme;
        private RelativeLayout lllll;
        public viewh(@NonNull View itemView) {
            super(itemView);

            sto=itemView.findViewById(R.id.viewpro);
            men=itemView.findViewById(R.id.men);
            novie=itemView.findViewById(R.id.noviews);
            tme=itemView.findViewById(R.id.viewtime);
            lllll=itemView.findViewById(R.id.lllll);
        }
    }
}
