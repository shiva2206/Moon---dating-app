package adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.comments;
import com.example.honeymoon.likes;
import com.example.honeymoon.posttags;
import com.example.honeymoon.story_see;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import model.commentsmodel;
import model.keymodel;
import model.postmodel;
import model.replymodel;
import model.usermodel;

public class homepostadp extends RecyclerView.Adapter<homepostadp.viewholder> {
    private Context context;
    private List<postmodel> postmodels;



    public homepostadp(Context context, List<postmodel> postmodels) {
        this.context = context;
        this.postmodels = postmodels;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_post, parent, false);
        return new homepostadp.viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);

        postmodel pl = postmodels.get(position);
        System.out.println(pl.toString());
        effecy sen = new effecy(pl.getPublisherid(), holder.bcg);
        effecy anno = new effecy(context, pl.getPublisherid(), holder.annocru, holder.annoadm, holder.annofri);
        effecy.instance.postexist(pl, holder.comm);
//        if( (pl.getTags() == null || pl.getTags().isEmpty()) && !pl.getPublisherid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())  ){
//            holder.tgs.setVisibility(GONE);
//        }else{
//            holder.tgs.setVisibility(VISIBLE);
//        }


//        if( pl.getTags() != null && pl.getTags().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//            holder.remtag.setVisibility(VISIBLE);
//        }else{
//            holder.remtag.setVisibility(View.INVISIBLE);
//        }

        if (pl.getType().equals("image")) {
            Picasso.get().load(pl.getUri()).into(holder.post);
        } else {
            holder.vvvv.setVisibility(VISIBLE);
            MediaController mcon = new MediaController(context);
            mcon.setAnchorView(holder.vvvv);
            holder.vvvv.setMediaController(mcon);
            holder.vvvv.setVideoURI(Uri.parse(pl.getUri()));
//            holder.vvvv.start();
        }

        holder.time.setText(effecy.instance.getzonetime(pl.getTime().substring(0, 16)));
        holder.descrip.setText(pl.getDescription());
        holder.loc.setText(pl.getLocation());

        if (pl.getTags() != null) {
            holder.notg.setText(pl.getTags().size() + "");
        } else {
            holder.notg.setText("0");
        }
        getinfo(pl.getPublisherid(), holder.usernme, holder.prof, holder.mennn);
        settinglike(holder.like, pl, holder.nolikes);
        settingsaced(holder.save, pl);
        getcomcount(holder.nocomm, pl);
        getshrecount(holder.noshare, pl);

//        holder.descrip.setOnHashtagClickListener(new SocialView.OnClickListener() {
//            @Override
//            public void onClick(@NonNull SocialView view, @NonNull CharSequence text) {
//                holder.descrip.setEnabled(false);
//                if ((holder.comm.getTag()+"").equals("yes")) {
//                    Intent inte = new Intent(context, Mainactivity.class);
//                    inte.putExtra("hashtag", text);
//                    context.startActivity(inte);
//                }else{
//                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();
//
//                }
//                holder.descrip.setEnabled(true);
//
//            }
//        });
        holder.tgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tgs.setEnabled(false);
                if ((holder.comm.getTag() + "").equals("yes")) {

                    Intent inty = new Intent(context, posttags.class);
                    inty.putExtra("postid", pl.getPostid());
                    inty.putExtra("publisherid", pl.getPublisherid());
                    context.startActivity(inty);

                } else {
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();
                }
                holder.tgs.setEnabled(true);
            }
        });
        holder.notg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.notg.setEnabled(false);
                if ((holder.comm.getTag() + "").equals("yes")) {

                    Intent inty = new Intent(context, posttags.class);
                    inty.putExtra("postid", pl.getPostid());
                    inty.putExtra("publisherid", pl.getPublisherid());
                    context.startActivity(inty);

                } else {
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.notg.setEnabled(true);
            }
        });
        holder.remtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.remtag.setEnabled(false);
                if ((holder.comm.getTag() + "").equals("yes")) {
                    android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
                    alt.setInverseBackgroundForced(true);
                    alt.setTitle("Remove Your Tag").setMessage("Are u Sure ")
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    effecy.instance.sendnotii(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),null,null,null,pl.getPostid(),"del");

                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify")
                                            .child("others").child(pl.getTime().substring(0, 10)).child(pl.getPostid()).removeValue();
                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("tags")
                                            .child(pl.getPostid()).removeValue();
                                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("posts").child(pl.getPostid()).child("tags")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.remtag.setEnabled(true);
            }
        });
        holder.bcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.bcg.setEnabled(false);
                Intent inte = new Intent(context, story_see.class);
                inte.putExtra(pl.getPublisherid(), pl.getPublisherid());
                context.startActivity(inte);
                holder.bcg.setEnabled(true);
            }
        });
        holder.mennn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mennn.setEnabled(false);
                if ((holder.comm.getTag()+"").equals("yes")) {
                    PopupMenu pmen = new PopupMenu(context, holder.mennn);
                    pmen.getMenuInflater().inflate(R.menu.editpst, pmen.getMenu());
                    pmen.show();
                    pmen.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    holder.editdes.setVisibility(VISIBLE);
                                    holder.descrip.setVisibility(View.INVISIBLE);
                                    holder.done.setVisibility(VISIBLE);
                                    holder.editdes.setText(holder.descrip.getText());
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
//                                        for(String m : holder.descrip.getHashtags()){
//                                            FirebaseDatabase.getInstance().getReference().child("hashs").child(m).child(pl.getPostid()).removeValue();
//                                        }
                                                    if (pl.getTags() != null) {
                                                        for (String sg : pl.getTags().keySet()) {

                                                            effecy.instance.sendnotii(context,sg,null,null,null,pl.getPostid(),"del");

                                                            FirebaseDatabase.getInstance().getReference().child("info").child(sg)
                                                                    .child("notify").child("others").child(pl.getTime().substring(0, 10)).child(pl.getPostid())
                                                                    .removeValue();

                                                            FirebaseDatabase.getInstance().getReference().child("info").child(sg)
                                                                    .child("tags").child(pl.getPostid()).removeValue();


                                                        }
                                                    }

                                                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("mypostsaved").child(pl.getPostid()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot das : snapshot.getChildren()) {
                                                                FirebaseDatabase.getInstance().getReference().child("info").child(das.getKey())
                                                                        .child("saved").child(pl.getPostid()).removeValue();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("mypostsaved").child(pl.getPostid())
                                                            .removeValue();

                                                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("likes").child(pl.getPostid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dsa : snapshot.getChildren()) {
                                                                keymodel kdel = dsa.getValue(keymodel.class);
                                                                effecy.instance.sendnotii(context,pl.getPublisherid(),null,null,null,kdel.getKey(),"del");
                                                                FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("notify").child("others")
                                                                        .child(kdel.getDate()).child(kdel.getKey()).removeValue();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("likes").child(pl.getPostid()).removeValue();

                                                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("comments")
                                                            .child(pl.getPostid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                                                        commentsmodel commm = ds.getValue(commentsmodel.class);
                                                                        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid())
                                                                                .child("replys").child(commm.getPostid()).child(commm.getCommentid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                                                                            replymodel rel = ds.getValue(replymodel.class);
                                                                                            effecy.instance.sendnotii(context,commm.getUserid(),null,null,null,rel.getReplyid(),"del");

                                                                                            effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,rel.getReplyid(),"del");
                                                                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("notify")
                                                                                                    .child("others").child(rel.getTime().substring(0, 10)).child(rel.getReplyid()).removeValue();

                                                                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify")
                                                                                                    .child("others").child(rel.getTime().substring(0, 10)).child(rel.getReplyid()).removeValue();

                                                                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("replys")
                                                                                                    .child(rel.getPostid())
                                                                                                    .child(rel.getCommentid())
                                                                                                    .child(rel.getReplyid()).removeValue();
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });
                                                                        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("notify")
                                                                                .child("others").child(commm.getDate()).child(commm.getCommentid()).removeValue();
                                                                        effecy.instance.sendnotii(context,commm.getPublisherid(),null,null,null,commm.getCommentid(),"del");


                                                                        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("commlikes").child(commm.getPostid())
                                                                                .child(commm.getCommentid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        for (DataSnapshot dspp : snapshot.getChildren()) {
                                                                                            keymodel kdelll = dspp.getValue(keymodel.class);
                                                                                            effecy.instance.sendnotii(context,commm.getUserid(),null,null,null,kdelll.getKey(),"del");

                                                                                            FirebaseDatabase.getInstance().getReference().child("info").child(commm.getUserid()).child("notify").child("others")
                                                                                                    .child(kdelll.getDate()).child(kdelll.getKey()).removeValue();
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                                    }
                                                                                });

                                                                        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("commlikes").child(commm.getPostid())
                                                                                .child(commm.getCommentid()).removeValue();

                                                                        FirebaseDatabase.getInstance().getReference().child("info").child(commm.getPublisherid()).child("comments")
                                                                                .child(commm.getPostid()).child(commm.getCommentid()).removeValue();

                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });

                                                    FirebaseStorage.getInstance().getReference().child(pl.getPublisherid())
                                                            .child("posts").child(pl.getPostid()).delete();

                                                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                                                            .child("posts").child(pl.getPostid()).removeValue();

//                                        Intent inte = new Intent(context, comments.class);
//                                        inte.putExtra("description",  pl.getDescription());
//                                        inte.putExtra("uri",  pl.getUri());
//                                        inte.putExtra("location",  pl.getLocation());
//                                        inte.putExtra("postid",  pl.getPostid());
//                                        inte.putExtra("publisherid",  pl.getPublisherid());
//                                        inte.putExtra("tags",  pl.getTags());
//                                        inte.putExtra("time",  pl.getTime());
//                                        inte.putExtra("type",pl.getType());
//                                        inte.putExtra("delete","delete");
//                                        context.startActivity(inte);
//                                        ((Activity)context).finish();
//                                        List<commentsmodel> comdellst = new ArrayList<>();
//                                        effecyadp effadp = new effecyadp(context,comdellst);
//                                        holder.effrecy.setAdapter(effadp);
//                                        holder.effrecy.setLayoutManager(new LinearLayoutManager(context));
//                                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("comments")
//                                                .child(pl.getPostid()).addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                comdellst.clear();
//                                                for(DataSnapshot ds  : snapshot.getChildren()) {
//                                                    commentsmodel cdel = ds.getValue(commentsmodel.class);
//                                                    comdellst.add(cdel);
//                                                }
//
//
//                                                effadp.notifyDataSetChanged();
//
//    //                                                FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("replys").child(pl.getPostid()).removeValue();
//    //
//    //                                                FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("comments").child(pl.getPostid()).removeValue();
//    //
//    //                                                FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("commlikes").child(pl.getPostid()).removeValue();
//    //
//    //                                                FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("likes").child(pl.getPostid()).removeValue();
//
//
//    //                                                FirebaseStorage.getInstance().getReference().child(pl.getPublisherid()).child("posts").child(pl.getPostid()).delete();
//    //
//    //                                                FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
//    //                                                        .child("posts").child(pl.getPostid()).removeValue();
//
//                                                ((Activity) context).finish();
//
//                                            }
//
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });


                                                }
                                            }).show();
                                    break;

                            }
                            return true;
                        }
                    });
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.mennn.setEnabled(true);
            }
        });
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.done.setEnabled(false);
                if ((holder.comm.getTag()+"").equals("yes")) {
                    holder.editdes.setEnabled(false);
                    if (!holder.editdes.equals(holder.descrip)) {

//                        for (String i : holder.descrip.getHashtags()) {
//                            FirebaseDatabase.getInstance().getReference().child("hashs").child(i).removeValue();
//                        }
//                        for (String j : holder.editdes.getHashtags()) {
//                            FirebaseDatabase.getInstance().getReference().child("hashs").child(j).child(pl.getPostid()).setValue(pl);
//                        }
                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("posts").child(pl.getPostid())
                                .child("description").setValue(holder.editdes.getText().toString());
                        holder.descrip.setText(holder.editdes.getText().toString());

                    }
                    holder.done.setVisibility(GONE);
                    holder.descrip.setVisibility(VISIBLE);
                    holder.editdes.setVisibility(GONE);
                    holder.done.setEnabled(true);
                    holder.editdes.setEnabled(true);
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
            }
        });

        holder.usernme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.usernme.setEnabled(false);
                Intent inte = new Intent(context, Mainactivity.class);
                Activity act = (Activity) context;
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(act,holder.usernme, ViewCompat.getTransitionName(holder.usernme));
                inte.putExtra("userid", pl.getPublisherid());
                context.startActivity(inte,options.toBundle());
                holder.usernme.setEnabled(true);
            }
        });
        holder.nolikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.nolikes.setEnabled(false);
                if ((holder.comm.getTag()+"").equals("yes")) {
                    Intent inty = new Intent(context, likes.class);
                    inty.putExtra("publisherid", pl.getPublisherid());
                    inty.putExtra("postid", pl.getPostid());
                    context.startActivity(inty);
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.nolikes.setEnabled(true);
            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.like.setEnabled(false);
                if ((holder.comm.getTag()+"").equals("yes")) {
                    if (holder.like.getTag().toString().startsWith("liked")) {
                        String ky = holder.like.getTag().toString().substring(15);
                        String dat = holder.like.getTag().toString().substring(5, 15);
                        effecy.instance.sendnotii(context,pl.getPublisherid(),null,null,null,ky,"del");

                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("notify").child("others")
                                .child(dat).child(ky).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                                .child("likes").child(pl.getPostid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                    } else {
                        HashMap<String, String> notymp = new HashMap<>();
                        String dat = effecy.instance.gettime();
                        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                        notymp.put("subject", "likes");
                        notymp.put("notify", " ");
                        notymp.put("postid", pl.getPostid());
                        notymp.put("publisherid", pl.getPublisherid());
                        notymp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        notymp.put("notifyid", key);
                        notymp.put("time", dat.substring(11));

                        if (!pl.getPublisherid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                            FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                                    .child("notify").child("others").child(dat.substring(0, 10)).child(key).setValue(notymp);
                        }
                        HashMap hm = new HashMap();
                        hm.put("date", dat.substring(0, 10));
                        hm.put("time", dat.substring(11));
                        hm.put("key", key);
                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid())
                                .child("likes").child(pl.getPostid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hm);

                        effecy.instance.sendnotii(context, pl.getPublisherid(), "Liked Your Post", "liked", pl.getPostid(),key,null);
                    }
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.like.setEnabled(true);
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.save.setEnabled(false);
                if ((holder.comm.getTag()+"").equals("yes")) {
                    if (holder.save.getTag().equals("saved")) {
                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("mypostsaved")
                                .child(pl.getPostid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("saved").child(pl.getPostid()).removeValue();
//                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("posts").child(pl.getPostid())
//                            .child("saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                    } else {
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("saved").child(pl.getPostid()).setValue(pl.getPublisherid());
                        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("mypostsaved").child(pl.getPostid())
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("true");
//                    FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("posts").child(pl.getPostid())
//                            .child("saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(DateFormat.getDateTimeInstance().format(new Date()));
                    }
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.save.setEnabled(true);
            }
        });
        holder.comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.comm.setEnabled(false);
                if ((holder.comm.getTag()+"").equals("yes")) {
                    Intent inte = new Intent(context, comments.class);
                    inte.putExtra("description", pl.getDescription());
                    inte.putExtra("uri", pl.getUri());
                    inte.putExtra("location", pl.getLocation());
                    inte.putExtra("postid", pl.getPostid());
                    inte.putExtra("publisherid", pl.getPublisherid());
                    inte.putExtra("tags", pl.getTags());
                    inte.putExtra("time", pl.getTime());
                    inte.putExtra("type", pl.getType());
                    context.startActivity(inte);
                }else{
                    Toast.makeText(context, "This Post doesn't exist", Toast.LENGTH_SHORT).show();

                }
                holder.comm.setEnabled(true);
            }
        });


    }

    public void getshrecount(TextView noshre, postmodel pl) {
        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("shares")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(pl.getPostid()).exists()) {
                            for (DataSnapshot dss : snapshot.getChildren()) {
                                if (dss.getKey().equals(pl.getPostid())) {
                                    noshre.setText(dss.getValue() + "");
                                }
                            }
                        } else {
                            noshre.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getcomcount(TextView nocom, postmodel pl) {
        FirebaseDatabase.getInstance().getReference().child("info").child(pl.getPublisherid()).child("comments")
                .child(pl.getPostid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nocom.setText(snapshot.getChildrenCount() + "");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void settingsaced(ImageView save, postmodel pel) {

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

                        } else {
                            save.setImageResource(R.drawable.save);
                            save.setTag("save");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void settinglike(ImageView like, postmodel pel, TextView nolikes) {

        FirebaseDatabase.getInstance().getReference().child("info")
                .child(pel.getPublisherid()).child("likes").child(pel.getPostid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                            keymodel kkdel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(keymodel.class);
                            like.setImageResource(R.drawable.thumbup);
                            like.setTag("liked" + kkdel.getDate() + kkdel.getKey());
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

    }

    ;

    public void getinfo(String publisherid, TextView usernme, ImageView imagevw, ImageView igvm) {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dl : snapshot.getChildren()) {
                    if (dl.getKey().equals(publisherid)) {
                        usermodel ul = dl.getValue(usermodel.class);
                        usernme.setText(ul.getUsername());
                        Picasso.get().load(ul.getImageurl()).into(imagevw);
                        if (ul.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            igvm.setVisibility(VISIBLE);
                        }
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
        return postmodels.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView prof, save, like, comm, share, post, mennn, tgs, annocru, annofri, annoadm;
        private TextView usernme, time, loc, nolikes, nocomm, noshare, remtag, notg;
        private ImageView bcg;
        private VideoView vvvv;
        private EditText editdes;
        private Button done;
        private TextView descrip;
        private RecyclerView effrecy;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            prof = itemView.findViewById(R.id.pro);
            save = itemView.findViewById(R.id.save);
            like = itemView.findViewById(R.id.like);
            comm = itemView.findViewById(R.id.comm);
            descrip = itemView.findViewById(R.id.desc);
            time = itemView.findViewById(R.id.nmea);
            tgs = itemView.findViewById(R.id.tg);
            share = itemView.findViewById(R.id.share);
            post = itemView.findViewById(R.id.post);
            vvvv = itemView.findViewById(R.id.postv);
            loc = itemView.findViewById(R.id.loc);
            notg = itemView.findViewById(R.id.notg);
            usernme = itemView.findViewById(R.id.usrnme);
            nolikes = itemView.findViewById(R.id.nolik);
            bcg = itemView.findViewById(R.id.cardview);
            mennn = itemView.findViewById(R.id.mennn);
            nocomm = itemView.findViewById(R.id.nocom);
            noshare = itemView.findViewById(R.id.noshare);
            editdes = itemView.findViewById(R.id.editdesc);
            remtag = itemView.findViewById(R.id.removetg);
            done = itemView.findViewById(R.id.dne);
            effrecy = itemView.findViewById(R.id.effrecy);

            annocru = itemView.findViewById(R.id.cru);
            annofri = itemView.findViewById(R.id.fri);
            annoadm = itemView.findViewById(R.id.adm);
        }
    }
}
