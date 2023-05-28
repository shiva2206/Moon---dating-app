package array_adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.comments;
import com.example.honeymoon.posttags;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialEditText;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.hendraanggrian.appcompat.widget.SocialView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import model.postmodel;
import model.usermodel;

public class homepost_arradp extends ArrayAdapter<postmodel> {

    private Context context;
    private List<postmodel> postmodels;

    public homepost_arradp(@NonNull Context context, int resource, List<postmodel> postmodels) {
        super(context, resource,postmodels);
        this.context = context;
        this.postmodels = postmodels;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adap_post,parent,false);
        }

        ImageView prof=convertView.findViewById(R.id.pro);
        ImageView save=convertView.findViewById(R.id.save);
        ImageView like=convertView.findViewById(R.id.like);
        ImageView comm=convertView.findViewById(R.id.comm);
        SocialTextView descrip=convertView.findViewById(R.id.desc);
        TextView time=convertView.findViewById(R.id.nmea);
        ImageView tgs=convertView.findViewById(R.id.tg);
        ImageView share=convertView.findViewById(R.id.share);
        ImageView post=convertView.findViewById(R.id.post);
        VideoView vvvv=convertView.findViewById(R.id.postv);
        TextView loc=convertView.findViewById(R.id.loc);
        TextView usernme=convertView.findViewById(R.id.usrnme);
        TextView nolikes=convertView.findViewById(R.id.nolik);
        ImageView bcg=convertView.findViewById(R.id.cardview);
        ImageView mennn=convertView.findViewById(R.id.mennn);
        TextView nocomm = convertView.findViewById(R.id.nocom);
        TextView noshare=convertView.findViewById(R.id.noshare);
        SocialEditText editdes=convertView.findViewById(R.id.editdesc);
        TextView done=convertView.findViewById(R.id.dne);

        ImageView annocru = convertView.findViewById(R.id.cru);
        ImageView annofri=convertView.findViewById(R.id.fri);
        ImageView annoadm=convertView.findViewById(R.id.adm);

        postmodel pl = postmodels.get(position);
        effecy sen=new effecy(pl.getPublisherid(),bcg);
        effecy anno = new effecy(context,pl.getPublisherid(),annocru,annoadm,annofri);
        if(pl.getTags().isEmpty()){
            tgs.setVisibility(View.GONE);
        }

        if(pl.getType().equals("image")) {
            Picasso.get().load(pl.getUri()).placeholder(R.drawable.logo_background).into(post);
        }else{
            MediaController mcon = new MediaController(context);
            mcon.setAnchorView(vvvv);
            vvvv.setMediaController(mcon);
            vvvv.setVideoURI(Uri.parse(pl.getUri()));
            vvvv.start();
        }
        time.setText(pl.getTime().substring(0,17));
        descrip.setText(pl.getDescription());
        loc.setText(pl.getLocation());
        getinfo(pl.getPublisherid(),usernme,prof,mennn);
        settinglike(like,pl,nolikes);
        settingsaced(save,pl);
        getcomcount(nocomm,pl);
        getshrecount(noshare,pl);

        tgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(context, posttags.class);
                inty.putExtra("tags",pl.getTags());
                context.startActivity(inty);
            }
        });
        bcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bcg.getTag().equals("yes")){

                }Intent inte = new Intent(context, story_see.class);
                inte.putExtra("userid", pl.getPublisherid());
                context.startActivity(inte);
            }
        });
        mennn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pmen = new PopupMenu(context,mennn);
                pmen.getMenuInflater().inflate(R.menu.editpst,pmen.getMenu());
                pmen.show();
                pmen.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                editdes.setVisibility(View.VISIBLE);
                                descrip.setVisibility(View.INVISIBLE);
                                done.setVisibility(View.VISIBLE);
                                editdes.setText(descrip.getText());
                                break;
                            case R.id.dell:
                                AlertDialog.Builder alt = new AlertDialog.Builder(context);
                                alt.setTitle("Delete !").setMessage("Do you want to delete the Post")
                                        .setCancelable(true)
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for(String m : descrip.getHashtags()){
                                            FirebaseDatabase.getInstance().getReference().child("hashs").child(m).child(pl.getPostid()).removeValue();
                                        }
                                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                                                .child("posts").child(pl.getPostid()).removeValue();
                                    }
                                }).show();
                                break;
                        }
                        return true;
                    }
                });

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editdes.equals(descrip)){

                    for(String i : descrip.getHashtags()){
                        FirebaseDatabase.getInstance().getReference().child("hashs").child(i).removeValue();
                    }
                    for(String j :editdes.getHashtags()){
                        FirebaseDatabase.getInstance().getReference().child("hashs").child(j).child(pl.getPostid()).setValue(pl);
                    }
                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("posts").child(pl.getPostid())
                            .child("description").setValue(editdes.getText().toString());
                    descrip.setText(editdes.getText().toString());

                }
                done.setVisibility(View.GONE);
                descrip.setVisibility(View.VISIBLE);

                editdes.setVisibility(View.GONE);
            }
        });

        bcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(context, story_see.class);
                inte.putExtra("userid",pl.getPublisherid());
                context.startActivity(inte);
            }
        });

        usernme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(context, Mainactivity.class);
                inte.putExtra("userid",pl.getPublisherid());
                context.startActivity(inte);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (like.getTag().equals("liked")) {
                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                            .child("likes").child(pl.getPostid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                } else {
                    HashMap<String,String> notymp = new HashMap<>();
                    String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                    notymp.put("subject","likes");
                    notymp.put("notify"," ");
                    notymp.put("postid",pl.getPostid());
                    notymp.put("publisherid",pl.getPublisherid());
                    notymp.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    notymp.put("notifyid", key);
                    notymp.put("time", DateFormat.getDateTimeInstance().format(new Date()));

                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                            .child("notify").child("others").child(DateFormat.getDateTimeInstance().format(new Date()).substring(0,11)).child(key).setValue(notymp);
                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                            .child("likes").child(pl.getPostid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                }

            }
        });

        descrip.setOnHashtagClickListener(new SocialView.OnClickListener() {
            @Override
            public void onClick(@NonNull SocialView view, @NonNull CharSequence text) {
                Intent into = new Intent(context,Mainactivity.class);
                into.putExtra("hashtag","#"+text);
                context.startActivity(into);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (save.getTag().equals("saved")) {
                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("saved").child(pl.getPostid()).removeValue();
//                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("posts").child(pl.getPostid())
//                            .child("saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                } else {
                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("saved").child(pl.getPostid()).setValue(pl);
//                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("posts").child(pl.getPostid())
//                            .child("saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(DateFormat.getDateTimeInstance().format(new Date()));
                }

            }
        });
        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(context, comments.class);
                inte.putExtra("description",  pl.getDescription());
                inte.putExtra("uri",  pl.getUri());
                inte.putExtra("location",  pl.getLocation());
                inte.putExtra("postid",  pl.getPostid());
                inte.putExtra("publisherid",  pl.getPublisherid());
                inte.putExtra("tags",  pl.getTags());
                inte.putExtra("time",  pl.getTime());
                inte.putExtra("type",pl.getType());
                context.startActivity(inte);
            }
        });


        return convertView;
    }
    public void getshrecount(TextView noshre,postmodel pl){
        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("shares")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(pl.getPostid()).exists()) {
                            for (DataSnapshot dss : snapshot.getChildren()) {
                                if (dss.getKey().equals(pl.getPostid())) {
                                    noshre.setText(dss.getValue() + "");
                                }
                            }
                        }else{
                            noshre.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getcomcount(TextView nocom,postmodel pl){
        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("comments")
                .child(pl.getPostid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nocom.setText(snapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void settingsaced(ImageView save, postmodel pel){

        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saved")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.child(pel.getPostid()).exists()) {
                                save.setImageResource(R.drawable.saved);
                                save.setTag("saved");
                            } else {
                                save.setImageResource(R.drawable.save);
                                save.setTag("save");
                            }

                        }else {
                            save.setImageResource(R.drawable.save);
                            save.setTag("save");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void settinglike(ImageView like,postmodel pel,TextView nolikes){

        FirebaseDatabase.getInstance().getReference().child("info")
                .child(pel.getPublisherid()).child("likes").child(pel.getPostid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                        like.setImageResource(R.drawable.thumbup);
                        like.setTag("liked");
                    } else {
                        like.setImageResource(R.drawable.thumbupoutline);
                        like.setTag("like");
                    }

                    nolikes.setText(snapshot.getChildrenCount() + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    };

    public void getinfo(String publisherid, TextView usernme, ImageView imagevw,ImageView igvm){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dl : snapshot.getChildren()){
                    if(dl.getKey().equals(publisherid)){
                        usermodel ul = dl.getValue(usermodel.class);
                        usernme.setText(ul.getUsername());
                        Picasso.get().load(ul.getImageurl()).placeholder(R.drawable.logo_background).into(imagevw);
                        if(ul.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            igvm.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
