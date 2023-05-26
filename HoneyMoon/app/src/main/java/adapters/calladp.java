package adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.call;
import com.example.honeymoon.details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.callmodel;
import model.usermodel;
import retrofituu.UsersListener;

//adap for call log
public class calladp extends RecyclerView.Adapter<calladp.vieh>{
    private Context context;
    private List<callmodel> lstcalll;
    private String txtdate,zonetim;
    private UsersListener usersListener;

    public calladp(Context context, List<callmodel> lstcalll,UsersListener usersListener1) {
        this.context = context;
        this.lstcalll = lstcalll;
        this.usersListener=usersListener1;
    }

    @NonNull
    @Override
    public vieh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_call,parent,false);
        return new calladp.vieh(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull vieh holder, int position) {
        callmodel mdl = lstcalll.get(position);
        String date = mdl.getTime().substring(0,10);
        zonetim=effecy.instance.getzonetime(mdl.getTime().substring(0,16));
        if(position==0){
            txtdate = effecy.instance.getzonetime(mdl.getTime().substring(0,16)).substring(0,10);
            holder.ddt.setText(txtdate);
            holder.ddt.setVisibility(VISIBLE);
        }else {
            if (!txtdate.equals(zonetim.substring(0,10))){
                txtdate = zonetim.substring(0,10);
                holder.ddt.setText(txtdate);
                holder.ddt.setVisibility(VISIBLE);
            }else{
                holder.ddt.setVisibility(GONE);
            }
        }
        effecy anno = new effecy(context,mdl.getUserid(),holder.annocru,holder.annoadm,holder.annofri);
        holder.time.setText(effecy.instance.getzonetime(mdl.getTime()));
        getuser(mdl.getUserid(),holder.profle,holder.username);

        if(!mdl.getGoing().equals("missed")){
            holder.talktime.setText(mdl.getTalktime());
        }else{
            holder.talktime.setText("-");

        }

        if(mdl.getType().equals("video")){
            holder.type.setImageResource(R.drawable.video);
        }else{
            holder.type.setImageResource(R.drawable.audio);
        }


        if(mdl.getGoing().equals("in")){
            holder.going.setImageResource(R.drawable.incoming);
        }else if(mdl.getGoing().equals("out")){
            holder.going.setImageResource(R.drawable.outgoing);
        }else{
            holder.talktime.setText(mdl.getTalktime());
            holder.going.setImageResource(R.drawable.whitefilledcircle);
        }

        holder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.type.setEnabled(false);

                if(mdl.getType().equals("video")){
////                    usersListener.initiateVideoMeeting(mdl);
                }else {
////                usersListener.initiateAudioMeeting(mdl);
                }
                Intent inty = new Intent(context, call.class);
                inty.putExtra("userid",mdl.getUserid());
                inty.putExtra("type",mdl.getType());
                context.startActivity(inty);
                holder.type.setEnabled(true);
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.username.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",mdl.getUserid());
                context.startActivity(inte);
                holder.username.setEnabled(true);
            }
        });

        holder.cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cd.setEnabled(false);
                Intent inte = new Intent(context, details.class);
                inte.putExtra("userid",mdl.getUserid());
                context.startActivity(inte);
                holder.cd.setEnabled(true);
            }
        });
        

    }
    public void getuser(String userid, ImageView prof, TextView usr){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dss : snapshot.getChildren()){
                    if(dss.getKey().equals(userid)){
                        usermodel ser = dss.getValue(usermodel.class);
                        Picasso.get().load(ser.getImageurl()).into(prof);
                        usr.setText(ser.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return lstcalll.size();
    }

    public class vieh extends RecyclerView.ViewHolder{

        private RelativeLayout cd;
        private TextView username,time,talktime,ddt;
        private ImageView type,going,profle,annocru,annofri,annoadm;
        public vieh(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.calurnme);
            time=itemView.findViewById(R.id.caltme);
            talktime=itemView.findViewById(R.id.tktme);
            going=itemView.findViewById(R.id.inout);
            type=itemView.findViewById(R.id.caltype);
            profle=itemView.findViewById(R.id.calpro);
            annoadm=itemView.findViewById(R.id.adm);
            annocru=itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            ddt=itemView.findViewById(R.id.datedat);
            cd=itemView.findViewById(R.id.calrl);

        }
    }
}