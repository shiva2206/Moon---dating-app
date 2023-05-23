package adapters;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.addhighlight;
import com.example.honeymoon.allstories;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import model.highmodel;
import model.statusmodel;
import model.usermodel;

public class story_seeadp extends RecyclerView.Adapter<story_seeadp.stoviewhlder> {

    private Context context;
    private List<String> userlst;
    private List<List<statusmodel>> stalstlst;
    private List<List<highmodel>> highlstlst;
    private List<Integer> counterlst = new ArrayList<>();
    public String what;
    private ViewPager2 vp;

    public story_seeadp(Context context, List<String> userlst, List<List<statusmodel>> stalstlst, List<Integer> counterlst, ViewPager2 vp) {
        this.context = context;
        this.userlst = userlst;
        this.vp=vp;
        this.stalstlst = stalstlst;
        this.counterlst = counterlst;
        this.what = "story";
    }

    public story_seeadp(List<List<highmodel>> highlstlst, Context context, List<String> userlst, List<Integer> counterlst, ViewPager2 vp) {
        this.context = context;
        this.userlst = userlst;
        this.counterlst = counterlst;
        this.highlstlst = highlstlst;
        this.vp=vp;
        this.what = "high";
    }

    @NonNull
    @Override
    public stoviewhlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_storysee, parent, false);
        return new story_seeadp.stoviewhlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stoviewhlder holder, @SuppressLint("RecyclerView") int position) {

//        Toast.makeText(context, vp.getCurrentItem()+"", Toast.LENGTH_SHORT).show();
        String userid = userlst.get(position);
        int counter = counterlst.get(position);
        if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.bott.setVisibility(GONE);
            holder.seevw.setVisibility(View.VISIBLE);
        } else {
            holder.seevw.setVisibility(GONE);
            holder.bott.setVisibility(View.VISIBLE);
        }
        getuserdet(holder, userid);


        if (what.equals("story")) {
            List<statusmodel> stadelst = stalstlst.get(position);

            Toast.makeText(context, stadelst.size() + "", Toast.LENGTH_SHORT).show();
            holder.statlst = stadelst;


            statusmodel stadel = stadelst.get(counter);
            holder.spv.setStoriesCount(stadelst.size());
//            Toast.makeText(context, , Toast.LENGTH_SHORT).show();
            setstoryseen(stadel);
            Picasso.get().load(stadel.getUri()).into(holder.imgg, new Callback() {
                @Override
                public void onSuccess() {
                    holder.spv.resume();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } else {
            List<highmodel> highlst = highlstlst.get(position);
            highmodel hgdl = highlst.get(counter);
            holder.spv.setStoriesCount(highlst.size());
//            holder.spv.pause();
            Picasso.get().load(hgdl.getImageuri()).into(holder.imgg, new Callback() {
                @Override
                public void onSuccess() {
                    holder.spv.resume();
                }

                @Override
                public void onError(Exception e) {

                }
            });

            if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                holder.himen.setVisibility(View.VISIBLE);
            } else {
                holder.himen.setVisibility(GONE);
            }

        }
        holder.spv.setStoryDuration(5000L);
//        Activity act =
        holder.spv.startStories(counter);
        if (vp.getCurrentItem()!=position){
            holder.spv.pause();
        }else{
            holder.spv.resume();
        }

        holder.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.spv.reverse();
            }
        });
        holder.left.setOnTouchListener(holder.onTouchListener);
        holder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.spv.skip();
            }
        });
        holder.right.setOnTouchListener(holder.onTouchListener);

        holder.send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                holder.viewp.setEnabled(false);
                holder.send.setEnabled(false);
                if (!TextUtils.isEmpty(holder.reply.getText() + "") && holder.send.getTag().equals("notblocked")) {
                    String ddd = effecy.instance.getmilltime();
                    DatabaseReference sef = FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("chats")
                            .child(userid);
                    String id = sef.push().getKey();
                    HashMap<String, Object> vp = new HashMap<>();
                    vp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    vp.put("chatid", id);
                    vp.put("chat", holder.reply.getText() + "");
                    vp.put("liked", "false");
                    if (what.equals("high")) {
                        vp.put("uri", highlstlst.get(position).get(counter).getImageuri());
                    } else {
                        vp.put("uri", stalstlst.get(position).get(counter).getUri());
                    }
                    vp.put("type", "story");
                    vp.put("time", ddd.substring(11));


                    sef.child(effecy.instance.gettime().substring(0, 10)).child(id).setValue(vp);

                    HashMap<String, Object> mp = new HashMap<>();
                    mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mp.put("chatid", id);
                    mp.put("chat", holder.reply.getText() + "");
                    mp.put("liked", "false");
                    mp.put("type", "story");
                    if (what.equals("high")) {
                        mp.put("uri", highlstlst.get(position).get(counter).getImageuri());
                    } else {
                        mp.put("uri", stalstlst.get(position).get(counter).getUri());
                    }
                    mp.put("time", ddd.substring(11));


                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("chats")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(effecy.instance.gettime().substring(0, 10))
                            .child(id).setValue(mp);


                    effecy.instance.sendnotii(context, userid, holder.reply.getText() + "", "message", null, FirebaseAuth.getInstance().getCurrentUser().getUid(), null);


                    holder.reply.setText("");
                } else if (holder.send.getTag().equals("blocked")) {
                    Toast.makeText(context, "You have been Blocked", Toast.LENGTH_SHORT).show();
                }
                holder.send.setEnabled(true);
                holder.viewp.setEnabled(true);
            }
        });

        holder.himen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.himen.setEnabled(false);
                PopupMenu pmen = new PopupMenu(context, holder.himen);
                pmen.getMenuInflater().inflate(R.menu.del, pmen.getMenu());
                pmen.show();
                pmen.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.adsto:
                                Intent intte = new Intent(context, allstories.class);
                                intte.putExtra("highid", highlstlst.get(position).get(counter).getHighlightid());
                                (context).startActivity(intte);
                                break;

                            case R.id.edddit:
                                Intent intet = new Intent(context, addhighlight.class);
                                intet.putExtra("highid", highlstlst.get(position).get(counter).getHighlightid());
                                intet.putExtra("from", "edit");
                                (context).startActivity(intet);
                                break;
                            case R.id.remhigh:
                                AlertDialog.Builder alt = new AlertDialog.Builder(context);
                                alt.setTitle("Remove !").setMessage("Do you want to remove this From the Highlight")
                                        .setCancelable(true)
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (highlstlst.size() == 1) {
                                                    FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                                            .child(highlstlst.get(position).get(counter).getHighlightid()).delete();
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("highlights").child(highlstlst.get(position).get(counter).getHighlightid()).removeValue();
                                                    ((Activity) context).finish();
                                                } else {
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("highlights").child(highlstlst.get(position).get(counter).getHighlightid()).child("statusmodelist").child(stalstlst.get(position).get(counter).getStatusid()).removeValue();

                                                }

                                            }
                                        }).show();
                                break;

                        }
                        return true;
                    }
                });
                holder.himen.setEnabled(true);
            }
        });
        holder.topp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.topp.setEnabled(false);
                Intent tent = new Intent(context, Mainactivity.class);
                tent.putExtra("userid", userid);
                (context).startActivity(tent);
                holder.topp.setEnabled(true);
            }
        });
        holder.seevw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.seevw.setEnabled(false);
                if (highlstlst == null) {
                    (context).startActivity(new Intent(context, com.example.honeymoon.view.class));
                } else {
                    Intent ient = new Intent(context, seen.class);
                    ient.putExtra("date", stalstlst.get(position).get(counter).getTime().substring(0, 10));
                    ient.putExtra("purpose", "allstories");
                    (context).startActivity(ient);
                }
                holder.seevw.setEnabled(true);
            }
        });

        holder.spv.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                Toast.makeText(context, counterlst.get(position) + "", Toast.LENGTH_SHORT).show();
                counterlst.set(position, counterlst.get(position) + 1);
                if (what.equals("story")) {

                    Picasso.get().load(stalstlst.get(position).get(counterlst.get(position)).getUri()).into(holder.imgg);
                } else {
                    Picasso.get().load(highlstlst.get(position).get(counterlst.get(position)).getImageuri()).into(holder.imgg);
                }
                setstoryseen(stalstlst.get(position).get(counterlst.get(position)));
            }


            @Override
            public void onPrev() {
                if ((counterlst.get(position) - 1) < 0) {
                    return;
                }

                counterlst.set(position, counterlst.get(position) - 1);
                Toast.makeText(context, counterlst.get(position) + "", Toast.LENGTH_SHORT).show();

                if (what.equals("story")) {

                    Picasso.get().load(stalstlst.get(position).get(counterlst.get(position)).getUri()).into(holder.imgg);
                } else {

                    Picasso.get().load(highlstlst.get(position).get(counterlst.get(position)).getImageuri()).into(holder.imgg);

                }
            }

            @Override
            public void onComplete() {

                ((Activity) context).finish();
//                holder.spv.destroy();
//                int ui = counterlst.get(position)-1;
//                Toast.makeText(context,ui+"", Toast.LENGTH_SHORT).show();

//        Activity act =
//                holder.spv.startStories(0);

            }
        });




    }

    public void getuserdet(stoviewhlder holder, String userid) {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel udel = snapshot.child(userid).getValue(usermodel.class);
                if (udel != null) {
                    holder.username.setText(udel.getUsername());
                    Picasso.get().load(udel.getImageurl()).into(holder.usrprof);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {

        return 1;
    }


    public void setstoryseen(statusmodel stadel) {
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.child(stadel.getPublisherid()).child("seen").child(stadel.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .exists()) {
                    FirebaseDatabase.getInstance().getReference().child("info").child(stadel.getPublisherid()).child("seen")
                            .child(stadel.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(effecy.instance.gettime());

                }

//                        getindi(pp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class stoviewhlder extends RecyclerView.ViewHolder {


//        View fram;

        long pressTime = 0L;
        long limit = 500L;
        private TextView username, title, seevw;
        private CardView topp;
        private RelativeLayout bott;
        private ImageView usrprof, send, annocru, annofri, annoadm, himen, imgg;
        private ViewPager2 viewp;
        private EditText reply;
        private View left, right;
        private StoriesProgressView spv;
        private int counter = 0;
        private int posi;
        private String what;
        private List<statusmodel> statlst;
        private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressTime = System.currentTimeMillis();
                        spv.pause();
                        return false;
                    case MotionEvent.ACTION_UP:
                        long now = System.currentTimeMillis();
                        spv.resume();
                        return limit < now - pressTime;
                }
                return false;
            }
        };

        public stoviewhlder(@NonNull View itemView) {
            super(itemView);
//            fram=itemView.findViewById(R.id.fragg);
            bott = itemView.findViewById(R.id.stobot);
            topp = itemView.findViewById(R.id.topp);
            username = itemView.findViewById(R.id.stousrnme);
            title = itemView.findViewById(R.id.stotit);
            usrprof = itemView.findViewById(R.id.stopro);
            send = itemView.findViewById(R.id.stosen);
            reply = itemView.findViewById(R.id.stoeepl);
            seevw = itemView.findViewById(R.id.sevw);
            himen = itemView.findViewById(R.id.himen);

            viewp = itemView.findViewById(R.id.sevie);

            annocru = itemView.findViewById(R.id.cru);
            annofri = itemView.findViewById(R.id.fri);
            annoadm = itemView.findViewById(R.id.adm);
            imgg = itemView.findViewById(R.id.imgsto);
            left = itemView.findViewById(R.id.lft);
            right = itemView.findViewById(R.id.rft);
            spv = itemView.findViewById(R.id.spv);


        }


    }
}
