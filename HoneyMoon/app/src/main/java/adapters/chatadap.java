package adapters;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.story_see;
import com.example.honeymoon.viewchatmedia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.chatmodel;
import model.doubleclick;
import model.usermodel;

//chats adp
public class chatadap extends RecyclerView.Adapter<chatadap.viehdr> {
    private Context context;
    private List<chatmodel> chatlst,selectedchtlst;
    private String txtdate,zonetim;
    private List<String> islastdatest;
    private String secuserid;
    private ImageView dell;
    private PopupMenu pmenu;
    private StorageReference storef;
    private NestedScrollView nested;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    public List<chatmodel> getChatlst() {
        return chatlst;
    }
    public List<chatmodel> getSelectedchtlst(){
        return selectedchtlst;
    }

    public chatadap(Context context, List<chatmodel> chatlst, List<String> islastdatest, String secuserid, NestedScrollView nested,ImageView dell) {
        this.context = context;
        this.chatlst = chatlst;
        this.nested = nested;
        this.islastdatest = islastdatest;
        this.secuserid = secuserid;
        this.selectedchtlst=new ArrayList<>();
        this.dell=dell;
    }

    @NonNull
    @Override
    public viehdr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.adap_chatsright, parent, false);
            return new chatadap.viehdr(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.adap_chatsleft, parent, false);
            return new chatadap.viehdr(view);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull viehdr holder, int position) {

        chatmodel ch = chatlst.get(position);
        String date = ch.getTime().substring(0,10);
        zonetim=effecy.instance.getzonetime(ch.getTime().substring(0,16));
        if(position==0){
            txtdate = effecy.instance.getzonetime(ch.getTime().substring(0,16)).substring(0,10);
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
        if (ch.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
//            animation.start();
//           holder.secoper.setVisibility(GONE);
//           holder.cdf.setVisibility(VISIBLE);

//            holder.cdf.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anime_one));
            holder.fitime.setText(zonetim.substring(11,16));

            if (ch.getLiked().equals("true")) {
                holder.firlik.setVisibility(VISIBLE);
            } else {
                holder.firlik.setVisibility(GONE);
            }


            if (ch.getType().equals("text")) {
                holder.firimag.setVisibility(GONE);
                holder.firch.setText(ch.getChat());

            } else if (ch.getType().equals("image") || ch.getType().equals("story")) {
                holder.firimag.setVisibility(VISIBLE);
                holder.firch.setText(ch.getChat());
                Picasso.get().load(ch.getUri()).placeholder(R.mipmap.blackypic).into(holder.firimag);

            } else {
                holder.firch.setText(ch.getChat());
                holder.firimag.setVisibility(GONE);
                holder.firvido.setVisibility(VISIBLE);
                holder.firvido.setVideoURI(Uri.parse(ch.getUri()));
            }

            if (islastdatest.get(position).equals("true") && (chatlst.size() - 1) == position) {

                if (ch.getSeen() != null) {
                    holder.seene.setVisibility(VISIBLE);
                } else {
                    holder.seene.setVisibility(GONE);
                }

            }else{
                holder.seene.setVisibility(GONE);
            }

            if (ch.getReplyid() != null) {
                String da = ch.getReplyid().substring(0, 10);
                String repid = ch.getReplyid().substring(17);
                FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("chats").child(secuserid).child(da).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dss : snapshot.getChildren()) {
                                    if (dss.getKey().equals(repid)) {
                                        chatmodel repdel = dss.getValue(chatmodel.class);

                                        if (repdel.getType().equals("image")) {

                                            holder.rovid.setVisibility(GONE);
                                            holder.rocht.setVisibility(GONE);
                                            Picasso.get().load(repdel.getUri()).into(holder.roimg);
                                            holder.roimg.setVisibility(VISIBLE);

                                        } else if (repdel.getType().equals("video")) {
                                            holder.roimg.setVisibility(GONE);
                                            holder.rocht.setVisibility(GONE);
                                            holder.rovid.setVideoURI(Uri.parse(repdel.getUri()));
                                            holder.rovid.setVisibility(VISIBLE);

                                        } else {
                                            holder.roimg.setVisibility(GONE);
                                            holder.rovid.setVisibility(GONE);
                                            holder.rocht.setText(repdel.getChat());
                                            holder.rocht.setVisibility(VISIBLE);
                                        }
                                        getusernme(repdel.getUserid(), holder.ronme);
                                        holder.ro.setVisibility(VISIBLE);
                                    }
                                }
                                if (holder.ro.getVisibility() == GONE) {

                                    holder.roimg.setVisibility(GONE);
                                    holder.rovid.setVisibility(GONE);
                                    holder.rocht.setText("This Message is deleted");
                                    holder.rocht.setVisibility(VISIBLE);
                                    holder.ro.setVisibility(VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            } else {
               holder.ro.setVisibility(GONE);
            }


            holder.firimag.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ch.getType().equals("story")) {
                        savefil(ch, holder, "firimg");
                    }
                    return true;
                }
            });
            holder.firvido.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ch.getType().equals("story")) {
                        savefil(ch, holder, "firvid");
                    }
                    return true;
                }
            });
            holder.cdf.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ch.getType().equals("story")) {
                        savefil(ch, holder, "firch");
                    }
                    return true;
                }
            });
            holder.firimag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.firimag.setEnabled(false);
                    if (!ch.getType().equals("story")) {
                        newact(ch);
                    } else {
                        Intent inty = new Intent(context, story_see.class);
                        inty.putExtra(secuserid, secuserid);
                    }
                    holder.firimag.setEnabled(true);
                }
            });
            holder.firvido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.firvido.setEnabled(false);
                    newact(ch);
                    holder.firvido.setEnabled(true);
                }
            });

        } else {
//            holder.cds.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anime_one));

//           holder.cds.setVisibility(VISIBLE);
            Toast.makeText(context, "yup", Toast.LENGTH_SHORT).show();
            if (ch.getLiked().equals("true")) {
                holder.seclik.setVisibility(VISIBLE);
            } else {
                holder.seclik.setVisibility(GONE);
            }

//           getseentag(holder,ch);
            holder.sectime.setText(zonetim.substring(11,16));
            if (ch.getSeen() == null) {
                String dddt = effecy.instance.gettime();
                FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("chats").child(ch.getUserid()).child(date).child(ch.getChatid()).child("seen").setValue(dddt);
                FirebaseDatabase.getInstance().getReference().child("info").child(secuserid)
                        .child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date)
                        .child(ch.getChatid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    FirebaseDatabase.getInstance().getReference().child("info").child(secuserid)
                                            .child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date)
                                            .child(ch.getChatid()).child("seen").setValue(dddt);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
            getu(ch.getUserid(), holder.prof);
//           holder.firpers.setVisibility(GONE);
            if (ch.getType().equals("text")) {
                holder.secimag.setVisibility(GONE);
                holder.secch.setText(ch.getChat());

            } else if (ch.getType().equals("image") || ch.getType().equals("story")) {
                holder.secch.setText(ch.getChat());
                holder.secimag.setVisibility(VISIBLE);
                Picasso.get().load(ch.getUri()).placeholder(R.mipmap.blackypic).into(holder.secimag);
            } else {
                holder.secimag.setVisibility(GONE);
                holder.secch.setText(ch.getChat());
                holder.secvideo.setVisibility(VISIBLE);
                holder.secvideo.setVideoURI(Uri.parse(ch.getUri()));
            }

            if (ch.getReplyid() != null) {
                String da = ch.getReplyid().substring(0, 10);
                String repid = ch.getReplyid().substring(11);
                FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("chats").child(secuserid).child(da).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dss : snapshot.getChildren()) {

                                    if (dss.getKey().equals(repid)) {
                                        chatmodel repdel = dss.getValue(chatmodel.class);
                                        if (repdel.getType().equals("image")) {
                                            holder.rtvid.setVisibility(GONE);
                                            holder.rtcht.setVisibility(GONE);
                                            Picasso.get().load(repdel.getUri()).into(holder.rtimg);
                                            holder.rtimg.setVisibility(VISIBLE);

                                        } else if (repdel.getType().equals("video")) {
                                            holder.rtimg.setVisibility(GONE);

                                            holder.rtcht.setVisibility(GONE);
                                            holder.rtvid.setVideoURI(Uri.parse(repdel.getUri()));
                                            holder.rtvid.setVisibility(VISIBLE);
                                        } else {
                                            holder.rtimg.setVisibility(GONE);
                                            holder.rtvid.setVisibility(GONE);
                                            holder.rtcht.setText(repdel.getChat());
                                            holder.rtcht.setVisibility(VISIBLE);
                                        }
                                        getusernme(repdel.getUserid(), holder.rtnme);
                                        holder.rt.setVisibility(VISIBLE);
                                    }
                                }
                                if (holder.rt.getVisibility() == GONE) {

                                    holder.rtimg.setVisibility(GONE);
                                    holder.rtvid.setVisibility(GONE);
                                    holder.rtcht.setVisibility(VISIBLE);
                                    holder.rtcht.setText("This message was deleted");
                                    holder.rt.setVisibility(VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            } else {
                holder.rt.setVisibility(GONE);
            }
            holder.secimag.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ch.getType().equals("story")) {
                        savefil(ch, holder, "secimg");
                    }
                    return true;
                }
            });
            holder.secvideo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ch.getType().equals("story")) {
                        savefil(ch, holder, "secvid");
                    }
                    return true;
                }
            });
            holder.cds.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!ch.getType().equals("story")) {
                        savefil(ch, holder, "secch");
                    }
                    return true;
                }
            });

            holder.secimag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.secimag.setEnabled(false);
                    if (!ch.getType().equals("story")) {
                        newact(ch);
                    } else {
                        Intent inty = new Intent(context, story_see.class);
                        inty.putExtra( FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }
                    holder.secimag.setEnabled(true);
                }
            });
            holder.secvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.secvideo.setEnabled(false);
                    newact(ch);
                    holder.secvideo.setEnabled(true);
                }
            });

            holder.cds.setOnClickListener(new doubleclick() {
                @Override
                public void onSingleClick(View v) {

                }

                @Override
                public void onDoubleClick(View v) {
                    holder.cds.setEnabled(false);
                    if (ch.getLiked().equals("false")) {
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("chats").child(secuserid).child(date).child(ch.getChatid()).child("liked").setValue("true");
                        FirebaseDatabase.getInstance().getReference().child("info").child(secuserid).child("chats")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date).child(ch.getChatid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.child(ch.getChatid()).exists()) {
                                            FirebaseDatabase.getInstance().getReference().child("info").child(secuserid).child("chats")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date).child(ch.getChatid()).child("liked").setValue("true");

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        effecy.instance.sendnotii(context,secuserid,"liked your message","chatliked",null,ch.getChatid(),null);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("chats").child(secuserid).child(date).child(ch.getChatid()).child("liked").setValue("false");
                        FirebaseDatabase.getInstance().getReference().child("info").child(secuserid).child("chats")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.child(ch.getChatid()).exists()) {
                                            FirebaseDatabase.getInstance().getReference().child("info").child(secuserid).child("chats")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(date).child(ch.getChatid()).child("liked").setValue("false");

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        effecy.instance.sendnotii(context,secuserid,null,null,null,ch.getChatid(),"del");

                    }
                    holder.cds.setEnabled(true);
                }
            });

        }

//        if (!selectedchtlst.isEmpty()){
//            dell.setVisibility(VISIBLE);
//        }else{
//            dell.setVisibility(GONE);
//        }
        holder.ani.setTag("no");
        holder.ani.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.ani.setEnabled(false);

                changeselected(holder,ch);

                holder.ani.setEnabled(true);
                return true;
            }
        });
        holder.ani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedchtlst.isEmpty()){
                    changeselected(holder,ch);
                }
            }
        });
        if (islastdatest.get(position).equals("true") && (chatlst.size() - 1) == position) {

            nested.post(new Runnable() {
                @Override
                public void run() {
//                    nested.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
//        if (selectedchtlst.isEmpty()) {
//            dell.setVisibility(VISIBLE);
//        }else{
//            dell.setVisibility(GONE);
//        }
//        dell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
//                alt.setInverseBackgroundForced(true);
//                alt.setTitle("Delete").setMessage("Do you want to Delete")
//                        .setCancelable(true)
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                for (chatmodel chdel : selectedchtlst) {
//                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("chats")
//                                            .child(secuserid).child(chdel.getTime().substring(0,10)).child(chdel.getChatid()).removeValue();
//                                }
//
//                            }
//                        })
//                        .show();
//            }
//        });

    }
    public void changeselected(viehdr holder,chatmodel ch){
        if (holder.ani.getTag().equals("no")){

            selectedchtlst.add(ch);
            holder.ani.setBackgroundColor(Color.parseColor("#FD6B6B"));
            holder.ani.setTag("yes");
        }else{
            selectedchtlst.remove(ch);
            holder.ani.setBackgroundColor(Color.parseColor("#FF0000"));
            holder.ani.setTag("no");
        }

    }

    public void getusernme(String uuid, TextView nnme) {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel udel = snapshot.child(uuid).getValue(usermodel.class);
                if (udel != null) {
                    nnme.setText(udel.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getseentag(viehdr holder, chatmodel chdel) {
        FirebaseDatabase.getInstance().getReference().child("info").child(secuserid).child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(chdel.getTime().substring(0, 10)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dsh : snapshot.getChildren()) {
                            chatmodel mdel = dsh.getValue(chatmodel.class);
                            if (chdel.getChatid().equals(mdel.getChatid())) {
                                if (mdel.getSeen() == null) {
                                    holder.firch.setTag("do");
                                } else {
                                    holder.firch.setTag("done");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void newact(chatmodel chdel) {
        Intent inty = new Intent(context, viewchatmedia.class);
        inty.putExtra("posiid", chdel.getChatid());
        inty.putExtra("userid", secuserid);
        context.startActivity(inty);

    }

    public void savefil(chatmodel chdel, viehdr holder, String wht) {
        if (wht.equals("firimg")) {
            pmenu = new PopupMenu(context, holder.firimag);
            pmenu.getMenuInflater().inflate(R.menu.asvefil, pmenu.getMenu());

//            pmenu.getMenu().removeItem(R.id.saev);

        } else if (wht.equals("secimg")) {
            pmenu = new PopupMenu(context, holder.secimag);
            pmenu.getMenuInflater().inflate(R.menu.asvefil, pmenu.getMenu());

            pmenu.getMenu().removeItem(R.id.dell);
            pmenu.getMenu().removeItem(R.id.seninf);
        } else if (wht.equals("firvid")) {
            pmenu = new PopupMenu(context, holder.firvido);
            pmenu.getMenuInflater().inflate(R.menu.asvefil, pmenu.getMenu());
//            pmenu.getMenu().removeItem(R.id.saev);
        } else if (wht.equals("secvid")) {
            pmenu = new PopupMenu(context, holder.secvideo);
            pmenu.getMenuInflater().inflate(R.menu.asvefil, pmenu.getMenu());
            pmenu.getMenu().removeItem(R.id.seninf);
            pmenu.getMenu().removeItem(R.id.dell);
        } else if (wht.equals("firch")) {
            pmenu = new PopupMenu(context, holder.firch);
            pmenu.getMenuInflater().inflate(R.menu.asvefil, pmenu.getMenu());

            pmenu.getMenu().removeItem(R.id.saev);

        } else if (wht.equals("secch")) {
            pmenu = new PopupMenu(context, holder.secch);
            pmenu.getMenuInflater().inflate(R.menu.asvefil, pmenu.getMenu());
            pmenu.getMenu().removeItem(R.id.seninf);
            pmenu.getMenu().removeItem(R.id.saev);
            pmenu.getMenu().removeItem(R.id.dell);
        }

        pmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.saev:
                        download(chdel);
                        break;
                    case R.id.dell:
                        setbuilder(chdel,"delall");
                        break;
                    case R.id.delforme:
                        setbuilder(chdel,"delme");
                        break;
                    case R.id.seninf:
                        if (chdel.getSeen() != null) {
                            Toast.makeText(context, chdel.getSeen(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Not Seen", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });
        pmenu.show();
    }

    public void download(chatmodel chdel) {
        if (chdel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            storef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("chats").child(secuserid).child(chdel.getChatid());
        } else {
            storef = FirebaseStorage.getInstance().getReference(secuserid)
                    .child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(chdel.getChatid());
        }
        storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadfile(context, chdel.getTime(), ".pdf", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void downloadfile(Context context, String filename, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, filename);

        downloadManager.enqueue(request);
        Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();


    }

    public void setbuilder(chatmodel chdel,String what) {
        android.app.AlertDialog.Builder alt = new AlertDialog.Builder(context);
        alt.setInverseBackgroundForced(true);
        alt.setTitle("Delete").setMessage("Do you want to Delete")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (what.equals("delall")){
                            delforall(chdel);
                        }else{
                            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("chats").child(secuserid).child(chdel.getTime().substring(0,10)).child(chdel.getChatid()).removeValue();

                        }


                    }
                })
                .show();


    }
    public void delforall(chatmodel chdel){
        if (chdel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            FirebaseStorage.getInstance().getReference(chdel.getUserid()).child("chats").child(secuserid)
                    .child(chdel.getChatid()).delete();


        } else {
            FirebaseStorage.getInstance().getReference(secuserid).child("chats").child(chdel.getUserid())
                    .child(chdel.getChatid()).delete().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        delchts(chdel);
    }

    public void delchts(chatmodel chdel) {
        effecy.instance.sendnotii(context,secuserid,null,null,null,chdel.getChatid(),FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").child(secuserid).child(chdel.getTime().substring(0,10)).child(chdel.getChatid()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("info").child(secuserid)
                .child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(chdel.getTime().substring(0,10)).child(chdel.getChatid()).removeValue();

    }
    public void getu(String userid, ImageView imag) {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(userid)) {
                        usermodel umo = ds.getValue(usermodel.class);
                        Picasso.get().load(umo.getImageurl()).into(imag);
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
        return chatlst.size();
    }

    public class viehdr extends RecyclerView.ViewHolder {
        private RelativeLayout firpers, secoper, ani;
        private ImageView prof, firimag, secimag, annocru, annofri, annoadm, seclik, firlik, roimg, rtimg;
        private VideoView firvido, secvideo, rovid, rtvid;
        private TextView secch, firch, newmess, seene, fitime, sectime, ronme, rtnme, rocht, rtcht,ddt;
        private CardView cds, cdf, rt, ro;

        public viehdr(@NonNull View itemView) {
            super(itemView);

            cds = itemView.findViewById(R.id.cds);
            cdf = itemView.findViewById(R.id.cdf);
            ani = itemView.findViewById(R.id.ani);
            ddt = itemView.findViewById(R.id.datedat);

            firpers = itemView.findViewById(R.id.firper);
            secoper = itemView.findViewById(R.id.secper);
            prof = itemView.findViewById(R.id.chatpro);
            secch = itemView.findViewById(R.id.chatch);
            firch = itemView.findViewById(R.id.fircht);
            firimag = itemView.findViewById(R.id.firimag);
            secimag = itemView.findViewById(R.id.secimag);
            newmess = itemView.findViewById(R.id.newmsee);
            seene = itemView.findViewById(R.id.seeeen);
            fitime = itemView.findViewById(R.id.fitime);
            sectime = itemView.findViewById(R.id.sectme);
            firvido = itemView.findViewById(R.id.firvido);
            secvideo = itemView.findViewById(R.id.secvide);

            annocru = itemView.findViewById(R.id.cru);
            annofri = itemView.findViewById(R.id.fri);
            annoadm = itemView.findViewById(R.id.adm);
            seclik = itemView.findViewById(R.id.seclik);
            firlik = itemView.findViewById(R.id.firlik);

            ronme = itemView.findViewById(R.id.roname);
            rtnme = itemView.findViewById(R.id.rtname);
            rocht = itemView.findViewById(R.id.rocht);
            rtcht = itemView.findViewById(R.id.rtcht);
            roimg = itemView.findViewById(R.id.roimg);
            rtimg = itemView.findViewById(R.id.rtimg);
            rovid = itemView.findViewById(R.id.rovid);
            rtvid = itemView.findViewById(R.id.rtvid);
            rt = itemView.findViewById(R.id.rt);
            ro = itemView.findViewById(R.id.ro);

//            if (chatlst.get(0).getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//                cdf.setVisibility(GONE);
//                cds.setVisibility(VISIBLE);
//            }else{
//                cdf.setVisibility(VISIBLE);
//                cds.setVisibility(GONE);
//            }

//            if (getItemViewType()==1){
//                cdf.setVisibility(GONE);
//                cds.setVisibility(VISIBLE);
//            }else{
//                cdf.setVisibility(VISIBLE);
//                cds.setVisibility(GONE);
//            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (chatlst.get(position).getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
