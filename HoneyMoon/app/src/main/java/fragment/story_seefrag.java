package fragment;

import static android.view.View.GONE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.addhighlight;
import com.example.honeymoon.allstories;
import com.example.honeymoon.seen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import jp.shts.android.storiesprogressview.StoriesProgressView;
import model.highmodel;
import model.statusmodel;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link story_seefrag} factory method to
 * create an instance of this fragment.
 */
public class story_seefrag extends Fragment implements StoriesProgressView.StoriesListener {

    public String userid;
    public boolean islast;
    long pressTime=0L;
    long limit =500L;
    public TextView username,title,seevw;
    public CardView topp;
    public RelativeLayout bott;
    public ImageView usrprof,send,annocru,annofri,annoadm,himen,imgg;
    public ViewPager2 viewp;
    public EditText reply;
    public View left,right;
    public StoriesProgressView spv;
    public int counter;
    public String what;
    public List<statusmodel> statlst;
    public List<highmodel> highlst;
    public Context context;

    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pressTime=System.currentTimeMillis();
                    spv.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    spv.resume();
                    return limit<now-pressTime;
            }
            return false;
        }
    };


    public story_seefrag(Context context,List<statusmodel> statlst,String userid,int counter,boolean islast){
        this.context=context;
        this.statlst=statlst;
        this.userid=userid;
        this.islast=islast;
        this.counter=counter;
        what="story";
    }

    public story_seefrag(List<highmodel> highlst,Context context,String userid,boolean islast){
        this.context=context;
        this.highlst=highlst;
        this.userid=userid;
        this.islast=islast;
        counter=0;
        what="high";
    }


    @Override
    public void onNext() {
        if (what.equals("story")){
            Picasso.get().load(statlst.get(++counter).getUri()).into(imgg);
        }else{
            Picasso.get().load(highlst.get(++counter).getImageuri()).into(imgg);

        }
    }


    @Override
    public void onPrev() {
        if ((counter-1)<0) {
            return;
        }
        if (what.equals("story")){
            Picasso.get().load(statlst.get(--counter).getUri()).into(imgg);
        }else{
            Picasso.get().load(highlst.get(--counter).getImageuri()).into(imgg);

        }
    }

    @Override
    public void onComplete() {
        Toast.makeText(context, "com", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =inflater.inflate(R.layout.fragment_story_see, container, false);
        bott=itemView.findViewById(R.id.stobot);
        topp=itemView.findViewById(R.id.topp);
        username=itemView.findViewById(R.id.stousrnme);
        title=itemView.findViewById(R.id.stotit);
        usrprof=itemView.findViewById(R.id.stopro);
        send=itemView.findViewById(R.id.stosen);
        reply=itemView.findViewById(R.id.stoeepl);
        seevw=itemView.findViewById(R.id.sevw);
        himen=itemView.findViewById(R.id.himen);

        viewp=itemView.findViewById(R.id.sevie);

        annocru = itemView.findViewById(R.id.cru);
        annofri=itemView.findViewById(R.id.fri);
        annoadm=itemView.findViewById(R.id.adm);
        imgg=itemView.findViewById(R.id.imgsto);
        left=itemView.findViewById(R.id.lft);
        right=itemView.findViewById(R.id.rft);
        spv=itemView.findViewById(R.id.spv);

        Toast.makeText(context, userid, Toast.LENGTH_SHORT).show();
        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            bott.setVisibility(GONE);
            seevw.setVisibility(View.VISIBLE);
        }else {
            seevw.setVisibility(GONE);
            bott.setVisibility(View.VISIBLE);
        }
        getuserdet();



        if (what.equals("story")){

//            holder.statlst=stadelst;



            spv.setStoriesCount(statlst.size());
            Picasso.get().load(statlst.get(counter).getUri()).into(imgg, new Callback() {
                @Override
                public void onSuccess() {
                    spv.resume();
                }

                @Override
                public void onError(Exception e) {

                }
            });
//            setstoryseen();
        }else{
            spv.setStoriesCount(highlst.size());
            spv.pause();
            Picasso.get().load(highlst.get(counter).getImageuri()).into(imgg, new Callback() {
                @Override
                public void onSuccess() {
                    spv.resume();
                }

                @Override
                public void onError(Exception e) {

                }
            });

            if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                himen.setVisibility(View.VISIBLE);
            }else{
                himen.setVisibility(GONE);
            }

        }

        spv.setStoryDuration(5000L);
        spv.setStoriesListener((StoriesProgressView.StoriesListener) this);
        spv.startStories(counter);





        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spv.reverse();
            }
        });
        left.setOnTouchListener(onTouchListener);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {spv.skip();
            }
        });
        right.setOnTouchListener(onTouchListener);

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                viewp.setEnabled(false);
                send.setEnabled(false);
                if (!TextUtils.isEmpty(reply.getText()+"") && send.getTag().equals("notblocked"))  {
                    String ddd = effecy.instance.getmilltime();
                    DatabaseReference sef = FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("chats")
                            .child(userid);
                    String id = sef.push().getKey();
                    HashMap<String, Object> vp = new HashMap<>();
                    vp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    vp.put("chatid", id);
                    vp.put("chat", reply.getText()+"");
                    vp.put("liked", "false");
                    if (what.equals("high")) {
                        vp.put("uri", highlst.get(counter).getImageuri());
                    }else{
                        vp.put("uri", statlst.get(counter).getUri());
                    }
                    vp.put("type", "story");
                    vp.put("time", ddd.substring(11));


                    sef.child(effecy.instance.gettime().substring(0, 10)).child(id).setValue(vp);

                    HashMap<String, Object> mp = new HashMap<>();
                    mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mp.put("chatid", id);
                    mp.put("chat", reply.getText()+"");
                    mp.put("liked", "false");
                    mp.put("type", "story");
                    if (what.equals("high")) {
                        mp.put("uri", highlst.get(counter).getImageuri());
                    }else{
                        mp.put("uri", statlst.get(counter).getUri());
                    }
                    mp.put("time", ddd.substring(11));


                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("chats")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(effecy.instance.gettime().substring(0, 10))
                            .child(id).setValue(mp);


                    effecy.instance.sendnotii(context, userid, reply.getText() + "", "message", null, FirebaseAuth.getInstance().getCurrentUser().getUid(),null);


                    reply.setText("");
                }else if(send.getTag().equals("blocked")){
                    Toast.makeText(context, "You have been Blocked", Toast.LENGTH_SHORT).show();
                }
                send.setEnabled(true);
                viewp.setEnabled(true);
            }
        });
        himen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                himen.setEnabled(false);
                PopupMenu pmen = new PopupMenu(context,himen);
                pmen.getMenuInflater().inflate(R.menu.del,pmen.getMenu());
                pmen.show();
                pmen.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.adsto:
                                Intent intte = new Intent(context, allstories.class);
                                intte.putExtra("highid",highlst.get(counter).getHighlightid());
                                (context).startActivity(intte);
                                break;

                            case R.id.edddit:
                                Intent intet = new Intent(context, addhighlight.class);
                                intet.putExtra("highid",highlst.get(counter).getHighlightid());
                                intet.putExtra("from","edit");
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

                                                if(highlst.size() == 1){
                                                    FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                                            .child(highlst.get(counter).getHighlightid()).delete();
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("highlights").child(highlst.get(counter).getHighlightid()).removeValue();
                                                    ((Activity)context).finish();
                                                }else {
                                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("highlights").child(highlst.get(counter).getHighlightid()).child("statusmodelist").child(statlst.get(counter).getStatusid()).removeValue();

                                                }

                                            }
                                        }).show();
                                break;

                        }
                        return true;
                    }
                });
                himen.setEnabled(true);
            }
        });
        topp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topp.setEnabled(false);
                Intent tent = new Intent (context, Mainactivity.class);
                tent.putExtra("userid",userid);
                (context).startActivity(tent);
                topp.setEnabled(true);
            }
        });
        seevw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seevw.setEnabled(false);
                if(highlst == null) {
                    (context).startActivity(new Intent(context, com.example.honeymoon.view.class));
                }else{
                    Intent ient = new Intent(context, seen.class);
                    ient.putExtra("date",statlst.get(counter).getTime().substring(0,10));
                    ient.putExtra("purpose","allstories");
                    (context).startActivity(ient);
                }
                seevw.setEnabled(true);
            }
        });


        return itemView;
    }
    public void getuserdet(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel udel = snapshot.child(userid).getValue(usermodel.class);
                if (udel!=null){
                    username.setText(udel.getUsername());
                    Picasso.get().load(udel.getImageurl()).into(usrprof);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}