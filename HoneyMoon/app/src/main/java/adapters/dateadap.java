package adapters;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.callmodel;
import model.chatmodel;
import model.highmodel;
import model.notifymodel;
import model.statusmodel;

//date adp
public class dateadap extends RecyclerView.Adapter<dateadap.viewh> {
    private Context context;
    private List<String> dateslst;
    private String what;
    private String userid;
    private List<statusmodel> selectedstadel = new ArrayList<>();
    private chatmodel repmdel;

//    private CardView repcd;
//    private ImageView repimg;
//    private VideoView repvid;
//    private TextView repcht,repnme;
//    private NestedScrollView nested;

    public List<statusmodel> getSelectedstadel() {
        return selectedstadel;
    }

    public chatmodel getrepmdel() {
        return repmdel;
    }

    public dateadap(Context context, List<String> dateslst, String what) {
        this.context = context;
        this.dateslst = dateslst;
        this.what = what;
    }

    public dateadap(Context context, List<String> dateslst, String userid, String what) {
        this.context = context;
        this.dateslst = dateslst;
        this.userid = userid;
        this.what = what;
    }
//    public dateadap(Context context, List<String> dateslst, String userid, String what, CardView cd, ImageView iv, VideoView vv, TextView tv, TextView nme, NestedScrollView nested) {
//        this.context = context;
//        this.dateslst = dateslst;
//        this.userid = userid;
//        this.what = what;
//        this.repcd = cd;
//        this.repimg = iv;
//        this.repvid = vv;
//        this.repcht = tv;
//        this.repnme = nme;
//        this.nested=nested;
//    }


    @NonNull
    @Override
    public viewh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_date,parent,false);
        return new dateadap.viewh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewh holder, int position) {
      String da = dateslst.get(position);

      holder.date.setText(da);
      if(what.equals("chats")) {
//          getallchts(da, holder.recy, position);
      }else if(what.equals("calls")){
          getcalldetails(da,holder.recy,holder.date);
      }else if(what.equals("notify")){
          getnotify(da,holder.recy,holder.date);
      }else if( what.equals("allstories")){

          getstst(da,holder.recy);

      }else if (what.equals("media")){
          getmedi(da,holder.recy,holder.date);
      }

    }
    public void getmedi(String da,RecyclerView recy,TextView datetxt){
        List<chatmodel> ordchtmedl = new ArrayList<>();
        mediaadp adp  = new mediaadp(context,ordchtmedl,userid);

        recy.setAdapter(adp);
        recy.setLayoutManager(new GridLayoutManager(context,3));
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("chats")
                .child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<chatmodel> lstchtmdl = new ArrayList<>();
                ordchtmedl.clear();
                List<String> unord = new ArrayList<>();
                for(DataSnapshot sp : snapshot.child(da).getChildren()){
                    chatmodel chtmdl = sp.getValue(chatmodel.class);
                    if(chtmdl != null) {
                        if (chtmdl.getType().equals("image") || chtmdl.getType().equals("video")) {
                            lstchtmdl.add(chtmdl);
                            unord.add(da + " " + chtmdl.getTime());
                        }
                    }
                }

                for(String strdat : effecy.instance.getdalst(unord)){
                    for(chatmodel cdel : lstchtmdl){
                        if(cdel.getTime().equals(strdat.substring(11)) && !ordchtmedl.contains(cdel)){
                            cdel.setTime(strdat);
                            ordchtmedl.add(cdel);
                            break;
                        }
                    }
                }


                if(ordchtmedl.isEmpty()){
                    datetxt.setVisibility(GONE);
                }else{
                    datetxt.setVisibility(View.VISIBLE);
                }
                adp.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getstst(String date,RecyclerView recy) {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("allstories").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<statusmodel> stalst = new ArrayList<>();
                List<statusmodel> ordstalst = new ArrayList<>();
                List<String> unorddalst = new ArrayList<>();


                for (DataSnapshot dsf : snapshot.getChildren()) {
                        statusmodel stts = dsf.getValue(statusmodel.class);
                        stalst.add(stts);
                        unorddalst.add(stts.getTime());
                }


                for(String strdat : effecy.instance.getdalst(unorddalst)){
                    for(statusmodel stadel : stalst){
                        if(stadel.getTime().equals(strdat) && !ordstalst.contains(stadel)){
                            ordstalst.add(stadel);
                            break;
                        }
                    }
                }



                alreadyinhigh(ordstalst,recy);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
    public void alreadyinhigh(List<statusmodel> stamdellst,RecyclerView recy){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> isahighlghtst = new ArrayList<>();
                        for(statusmodel stadel : stamdellst){
                            int yi = 0;
                            for(DataSnapshot dssp : snapshot.getChildren()){
                                highmodel hdel = dssp.getValue(highmodel.class);
                                if(hdel.getStatusmodelist() != null) {
                                    for (String stausidid : hdel.getStatusmodelist().keySet()) {
                                        System.out.println(stausidid);
                                        if (stadel.getStatusid().equals(stausidid)) {
                                            yi = yi + 1;
                                        }
                                    }
                                }
                            }
                            if(yi==1) {
                                isahighlghtst.add("true");
                            }else{
                                isahighlghtst.add("false");
                            }
                        }
                        allstoryadp adp = new allstoryadp(context, stamdellst,isahighlghtst,selectedstadel);

                        recy.setAdapter(adp);
                        recy.setLayoutManager(new GridLayoutManager(context, 2));

//                        for (statusmodel stttmdell : adp.getSelectedstalst()){
//                            Toast.makeText(context, stttmdell.getStatusid(), Toast.LENGTH_SHORT).show();
//                            if(!selectedstadel.contains(stttmdell)){
//                                selectedstadel.add(stttmdell);
//                            }
//                        }
//                        selectedstadel = adp.getSelectedstalst();
//                        adp.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getnotify(String date , RecyclerView rview, TextView datetxt){

        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("notify").child("others").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<notifymodel> ndelst  = new ArrayList<>();
                List<notifymodel> ordnotidel = new ArrayList<>();
                List<String> unorddalst = new ArrayList<>();

                for(DataSnapshot sd : snapshot.getChildren()){
                    notifymodel ndel = sd.getValue(notifymodel.class);

                    if (ndel.getSubject().equals("likes") || ndel.getSubject().equals("commentliked")){
                        if (effecy.instance.isold(date+" "+ndel.getTime(),2*60*24)){
                            effecy.instance.sendnotii(context,userid,null,null,null,ndel.getNotifyid(),"del");

                            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("notify").child("others").child(date).child(ndel.getNotifyid()).removeValue();

                        }
                    }
                }
                for(DataSnapshot sd : snapshot.getChildren()){
                    notifymodel ndel = sd.getValue(notifymodel.class);

                    ndelst.add(ndel);
                    unorddalst.add(date+" "+ndel.getTime());
                }



                for(String strdat : effecy.instance.getdalst(unorddalst)){
                    for(notifymodel noadel : ndelst){
                        if(noadel.getTime().equals(strdat.substring(11)) && !ordnotidel.contains(noadel)){
                            noadel.setTime(strdat);
                            ordnotidel.add(noadel);
                            break;
                        }
                    }
                }

                Collections.reverse(ordnotidel);
                notifyadp adp = new notifyadp(context,ordnotidel);
                rview.setAdapter(adp);
                rview.setLayoutManager(new LinearLayoutManager(context));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getcalldetails(String date,RecyclerView rv,TextView datetxt){
            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("calls").child(date).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<callmodel> lstcal = new ArrayList<>();
                    List<callmodel> ordcalldel = new ArrayList<>();
                    List<String> unorddalst = new ArrayList<>();


                    for (DataSnapshot df : snapshot.getChildren()) {
                        callmodel del = df.getValue(callmodel.class);
                        lstcal.add(del);
                        unorddalst.add(del.getTime());
                    }


                    for(String strdat : effecy.instance.getdalst(unorddalst)){
                        for(callmodel caldel : lstcal){
                            if(caldel.getTime().equals(strdat)  &&  !ordcalldel.contains(caldel)){
                                ordcalldel.add(caldel);
                                break;
                            }
                        }
                    }


//                    calladp adp = new calladp(context,ordcalldel);
//                    rv.setAdapter(adp);
//                    rv.setLayoutManager(new LinearLayoutManager(context));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

//    public void getallchts(String da,RecyclerView recy,int position){
//        List<chatmodel> chlst=new ArrayList<>();
//        List<chatmodel> ordchdel = new ArrayList<>();
//        List<String> unorddalst = new ArrayList<>();
//        List<String> islastdatest = new ArrayList<>();
//        chatadap adp = new chatadap(context, ordchdel,islastdatest,userid,nested);
//        recy.setAdapter(adp);
//        recy.setLayoutManager(new LinearLayoutManager(context));
////        scrollView.smoothScrollTo(0, scrollView.getChildAt(0).getHeight());
//// scrollview has always only one child
//        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("chats").child(userid).child(da).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                chlst.clear();
//                ordchdel.clear();
//                unorddalst.clear();
//                for(DataSnapshot ds : snapshot.getChildren()){
//                    chatmodel cho = ds.getValue(chatmodel.class);
//                    chlst.add(cho);
//                    unorddalst.add(da+" "+cho.getTime());
//                }
//                System.out.println(effecy.instance.getdalst(unorddalst));
//                for(String strdat : effecy.instance.getdalst(unorddalst)){
//                    for(chatmodel chdel : chlst){
//                        if(chdel.getTime().equals(strdat.substring(11))
//                                && !ordchdel.contains(chdel)){
//                            ordchdel.add(chdel);
//                            islastdatest.add("false");
//                            break;
//
//                        }
//                    }
//                }
//                if(dateslst.get(dateslst.size()-1) == da) {
//                    islastdatest.remove(islastdatest.size() - 1);
//                    islastdatest.add("true");
//
//
//                }
//                adp.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                repmdel = ordchdel.get(viewHolder.getAdapterPosition());
//                repmdel.setTime(da);
//                repcd.setVisibility(VISIBLE);
//                FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot dsp : snapshot.getChildren()){
//                            if(dsp.getKey().equals(repmdel.getUserid())) {
//                                usermodel udel = dsp.getValue(usermodel.class);
//                                repnme.setText(udel.getUsername());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                if (repmdel.getType().equals("image")){
//                    repimg.setVisibility(VISIBLE);
//                    repvid.setVisibility(GONE);
//                    repcht.setVisibility(GONE);
//                    Picasso.get().load(repmdel.getUri()).into(repimg);
//
//                }else if(repmdel.getType().equals("video")){
//                    repimg.setVisibility(INVISIBLE);
//                    repvid.setVisibility(VISIBLE);
//                    repcht.setVisibility(GONE);
//                    repvid.setVideoURI(Uri.parse(repmdel.getUri()));
//
//                }else{
//                    repimg.setVisibility(GONE);
//                    repvid.setVisibility(GONE);
//                    repcht.setVisibility(VISIBLE);
//                    repcht.setText(repmdel.getChat());
//                }
//
//                adp.notifyDataSetChanged();
//            }
//        }).attachToRecyclerView(recy);
//
//    }

    @Override
    public int getItemCount() {
        return dateslst.size();
    }

    public class viewh extends RecyclerView.ViewHolder{
        private TextView date;
        private RecyclerView recy;


        public viewh(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.datedat);
            recy = itemView.findViewById(R.id.datchat);
        }
    }
}
