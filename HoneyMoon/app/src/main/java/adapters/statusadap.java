package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.story_see;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.usermodel;


//the user foloowing status to see adp
//usedin home_status
public class statusadap extends RecyclerView.Adapter<statusadap.viewhder> {
    private Context context;
    private List<usermodel> usdmdl;


    public statusadap(Context context, List<usermodel> usdmdl) {
        this.context = context;
        this.usdmdl = usdmdl;
    }

    public List<usermodel> getUsdmdl() {
        return usdmdl;
    }

    @NonNull
    @Override
    public viewhder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_status,parent,false);
        return new statusadap.viewhder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewhder holder, int position) {
        usermodel ud = usdmdl.get(position);
        effecy sen=new effecy(ud.getUserid(),holder.bacg);
        effecy anno = new effecy(context,ud.getUserid(),holder.annocru,holder.annoadm,holder.annofri);
        holder.usernme.setText(ud.getUsername());
        holder.nme.setText(ud.getName());
        Picasso.get().load(ud.getImageurl()).placeholder(R.drawable.profile).into(holder.image);

//        FirebaseDatabase.getInstance().getReference().child("info").child(ud.getUserid()).child("story").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dss : snapshot.getChildren()){
//                    statusmodel hm = dss.getValue(statusmodel.class);
//                    Picasso.get().load(hm.getUri()).placeholder(R.drawable.ic_launcher_background).into(holder.mid);
//                    break;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        holder.rllll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rllll.setEnabled(false);
                if(holder.bacg.getTag().equals("yes")) {
                    Intent inte = new Intent(context, story_see.class);
//                    List<usermodel> intusrlst = new ArrayList<>();
//                    for (usermodel userdel : usdmdl){
//                        intusrlst.add(userdel);
//                    }
                    inte.putExtra("userid",ud.getUserid());

                    for (usermodel userdel : usdmdl){
                        inte.putExtra(userdel.getUserid(),"userid");

                    }
//                    inte.putExtra(ud.getUserid(),"userid");
                    context.startActivity(inte);
                }
                holder.rllll.setEnabled(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usdmdl.size();
    }

    public class viewhder extends RecyclerView.ViewHolder{
        private ImageView image,bacg,mid,annocru,annofri,annoadm;
        private TextView usernme,nme;
        private RelativeLayout rllll;
        public viewhder(@NonNull View itemView) {
            super(itemView);

//            mid=itemView.findViewById(R.id.frontcd);
            image=itemView.findViewById(R.id.fireus);
            usernme=itemView.findViewById(R.id.chatusernme);
            bacg=itemView.findViewById(R.id.backimag);
            nme= itemView.findViewById(R.id.nmea);
            rllll=itemView.findViewById(R.id.rlllll);

            annocru = itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            annoadm=itemView.findViewById(R.id.adm);
        }


    }
}
