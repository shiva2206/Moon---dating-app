package array_adapters;

import static android.view.View.GONE;
import static com.example.honeymoon.Mainactivity.currfri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import adapters.effecy;
import model.keymodel;
import model.usermodel;

public class search_arradp extends ArrayAdapter<usermodel> {
    private Context context;
    private List<usermodel> allusers;
    private List<String> distlst;


    public search_arradp(@NonNull Context context, int resource,List<usermodel> allusers,List<String> distlst) {
        super(context, resource,allusers);
        this.context=context;
        this.allusers=allusers;
        this.distlst =distlst;
    }

    public List<usermodel> getAllusers() {
        return allusers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adap_arr_search,parent,false);
        }

        ImageView imgvw=convertView.findViewById(R.id.vpa);
        TextView usrnme=convertView.findViewById(R.id.hnae);
        TextView age=convertView.findViewById(R.id.ag);
        TextView stats=convertView.findViewById(R.id.stats);
        TextView dists=convertView.findViewById(R.id.dists);
        TextView nmee = convertView.findViewById(R.id.nme);
        Button foll=convertView.findViewById(R.id.buts);
        CardView follcd=convertView.findViewById(R.id.ss);



        ImageView annocru = convertView.findViewById(R.id.cru);
        ImageView annofri=convertView.findViewById(R.id.fri);
        ImageView annoadm=convertView.findViewById(R.id.adm);
        ImageView bltk = convertView.findViewById(R.id.bltk);

        usermodel uel= allusers.get(position);
        effecy anno = new effecy(context,uel.getUserid(),annocru,annoadm,annofri);
        effecy aeff = new effecy(context,uel.getUserid(),age);
        age.setText(age.getTag()+"");

        if(uel.getBluetick().equals("true")){
            bltk.setVisibility(View.VISIBLE);
        }else{
            bltk.setVisibility(GONE);
        }

        stats.setVisibility(View.VISIBLE);
        stats.setText(uel.getStatus());
        usrnme.setText(uel.getUsername());
        nmee.setText(uel.getName());
        Picasso.get().load(uel.getImageurl()).into(imgvw);
//        dists.setText("( "+String.format("%.0f",lststr.get(position))+"Km Away )");

        dists.setText(" ("+distlst.get(position).substring(0,3)+" Km Away)");
//        age.setText(","+distlst.get(position));

        usrnme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrnme.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",uel.getUserid());
                context.startActivity(inte);
                usrnme.setEnabled(true);
            }
        });

        if(uel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            follcd.setVisibility(View.INVISIBLE);
        }


        foll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (foll.getTag().toString().startsWith("following")) {
                    String folltag = foll.getTag().toString().substring(9);

                    String s = "";
                    if(imgvw.getTag() != null || (imgvw.getTag()+"").length() > 4){
                        s="Removing Following Will Remove "+uel.getUsername()+" as "+currfri+".Are U Sure";
                    }else{
                        s="Do U Want To Remove Following";
                    }


                    android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
                    alt.setInverseBackgroundForced(true);
                    alt.setTitle("Remove Following!!!").setMessage(s)
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    effecy eff = new effecy(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),uel.getUserid(),
                                            "following",folltag,imgvw.getTag()+"");

                                }
                            })
                            .show();




                } else if(foll.getTag().equals("request")){
                    effecy eff = new effecy(context,uel.getUserid(),"requestcancel",uel.getMode());
                }
                else {

                    effecy eff = new effecy(context,uel.getUserid(),"follows",uel.getMode());

                }

            }
        });

        getfoll(uel.getUserid(),foll);
        getfri(uel,imgvw);

        return convertView;
    }
    public void getfri(usermodel uudl,ImageView remove){
        String oppfri = "";
        if(currfri == "gfs"){
            oppfri="bfs";
        }else{
            oppfri="gfs";
        }
        FirebaseDatabase.getInstance().getReference().child("info").child(uudl.getUserid()).child(oppfri).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            keymodel kkkel = snapshot.getValue(keymodel.class);
                            remove.setTag(kkkel.getDate()+kkkel.getKey());
                        }else{
                            remove.setTag(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getfoll(String useid,Button bon){
        FirebaseDatabase.getInstance().getReference().child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(useid).exists()){
                    keymodel kydell = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(useid).getValue(keymodel.class);
                    bon.setText("Following");
                    System.out.println(kydell.getDate()+kydell.getKey()+"ppp");
                    bon.setTag("following"+kydell.getDate()+ kydell.getKey());
                }else if(snapshot.child(useid).child("notify").child("requests").child("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    bon.setText("Request Sent");
                    bon.setTag("request");

                }else if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers").child(useid).exists() && !snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(useid).exists()){
                    bon.setText("Follow Back");
                    bon.setTag("follow");

                }else{
                    bon.setText("Follow");
                    bon.setTag("follow");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
