package array_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.List;

import adapters.notifyadp;
import model.callmodel;
import model.chatmodel;
import model.notifymodel;
import model.statusmodel;

public class date_arradp extends ArrayAdapter<String> {
    private Context context;
    private List<String> dateslst;
    private String what;
    private String userid;


    public date_arradp(@NonNull Context context, int resource, List<String> dateslst, String what, String userid) {
        super(context, resource,dateslst);
        this.context = context;
        this.dateslst = dateslst;
        this.what = what;
        this.userid = userid;
    }

    public date_arradp(@NonNull Context context, int resource, List<String> dateslst, String what) {
        super(context, resource,dateslst);
        this.context = context;
        this.dateslst = dateslst;
        this.what = what;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adap_arr_date, parent, false);
        }


        TextView date = view.findViewById(R.id.datedat);
        RecyclerView recy = view.findViewById(R.id.datchat);

        String da = dateslst.get(position);

        date.setText(da);
        if (what.equals("chats")) {
            getallchts(da, recy, position);
        } else if (what.equals("calls")) {
            getchatdetails(da, recy);
        } else if (what.equals("notify")) {
            getnotify(da, recy);
        } else if (what.equals("allstories")) {
            getstst(da, recy);
        }

        return view;
    }


    public void getallchts(String da, RecyclerView recy, int position) {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").child(userid).child(da).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<chatmodel> chlst = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        chatmodel cho = ds.getValue(chatmodel.class);
                        chlst.add(cho);
                    }
                    if ((dateslst.size() - 1) == position) {

//                        chatadap adp = new chatadap(context, chlst, da, new ArrayList<>(), userid);
//                        recy.setAdapter(adp);
//                        recy.setLayoutManager(new LinearLayoutManager(context));
//                        recy.smoothScrollToPosition(recy.getAdapter().getItemCount());
                    } else {
//                        chatadap adp = new chatadap(context, chlst, da, new ArrayList<>(), userid);
//                        recy.setAdapter(adp);
//                        recy.setLayoutManager(new LinearLayoutManager(context));

                    }
                }
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
                if (snapshot.exists()) {
                    for (DataSnapshot dsf : snapshot.getChildren()) {
                        statusmodel stts = dsf.getValue(statusmodel.class);
                        stalst.add(stts);
                    }
//                    allstoryadp adp = new allstoryadp(context, stalst,);
//                    recy.setAdapter(adp);
                    recy.setLayoutManager(new GridLayoutManager(context, 2));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getnotify(String date , RecyclerView rview){
        List<notifymodel> ndelst  = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("notify").child("others").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot sd : snapshot.getChildren()){
                    notifymodel ndel = sd.getValue(notifymodel.class);
                    ndelst.add(ndel);
                }

                notifyadp adp = new notifyadp(context,ndelst);
                rview.setAdapter(adp);
                rview.setLayoutManager(new LinearLayoutManager(context));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getchatdetails(String date,RecyclerView rv){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("calls").child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<callmodel> lstcal = new ArrayList<>();
                    for (DataSnapshot df : snapshot.getChildren()) {
                        callmodel del = df.getValue(callmodel.class);
                        lstcal.add(del);
                    }
//                    calladp adp = new calladp(context,lstcal,"poo");
//                    rv.setAdapter(adp);
//                    rv.setLayoutManager(new LinearLayoutManager(context));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}