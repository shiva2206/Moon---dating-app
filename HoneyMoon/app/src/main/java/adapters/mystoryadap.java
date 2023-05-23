package adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.login;
import com.example.honeymoon.seen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.statusmodel;

//my story
public class mystoryadap extends RecyclerView.Adapter<mystoryadap.viewh>{

    private List<statusmodel> stalst;
    private Context context;

    public mystoryadap(List<statusmodel> stalst, Context context) {
        this.stalst = stalst;
        this.context = context;
    }

    @NonNull
    @Override
    public viewh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_story_view,parent,false);
        return new mystoryadap.viewh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewh holder, @SuppressLint("RecyclerView") int position) {
     statusmodel sta = stalst.get(position);
     holder.tme.setText(sta.getTime());

     getnoviews(sta.getStatusid(),holder.novie);
     Picasso.get().load(sta.getUri()).into(holder.sto);
     FirebaseDatabase.getInstance().getReference().child("info").child(sta.getPublisherid()).child("seen")
             .child(sta.getStatusid()).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists()){
                 holder.novie.setText(snapshot.getChildrenCount() + " Views");
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {

         }
     });
     holder.lllll.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             holder.lllll.setEnabled(false);
             Intent inte = new Intent(context, seen.class);
             inte.putExtra("purpose","story");
             inte.putExtra("position",String.valueOf(position));
             context.startActivity(inte);
             holder.lllll.setEnabled(true);
         }
     });
     holder.men.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             holder.men.setEnabled(false);
             PopupMenu pmne = new PopupMenu(context,holder.men);
             pmne.getMenuInflater().inflate(R.menu.storymen,pmne.getMenu());
             pmne.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                 @Override
                 public boolean onMenuItemClick(MenuItem item) {
                     switch(item.getItemId()){
                         case R.id.forward:
                             break;
                         case R.id.delete:
                             android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
                             alt.setTitle("Delete !!!").setMessage("Do you want to Delete")
                                     .setCancelable(true)
                                     .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     })
                                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             FirebaseStorage.getInstance().getReference().child(sta.getPublisherid()).child("highlights").child(sta.getStatusid()).delete();
                                             FirebaseDatabase.getInstance().getReference().child("info").child(sta.getPublisherid()).child("seen").child(sta.getStatusid()).removeValue();
                                             FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                     .child("story").child(sta.getStatusid()).removeValue();


                                         }
                                     })
                                     .show();

                             break;
                     }
                     return false;
                 }
             });
             pmne.show();
             holder.men.setEnabled(true);

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
            lllll=itemView.findViewById(R.id.rel);


        }
    }
}
