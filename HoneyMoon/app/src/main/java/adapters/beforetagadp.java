package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.posttags;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.mainviewmodel;
import model.usermodel;

import static android.content.ContentValues.TAG;
import static android.view.View.VISIBLE;


public class beforetagadp extends RecyclerView.Adapter<beforetagadp.vhlder> {

    private Context context;
    private List<usermodel> userlst,allusrlst;
    private RecyclerView recy;
    private mainviewmodel mainviewmodel;
    boolean isenabled = false;


    public beforetagadp(Context context, List<usermodel> userlst, RecyclerView recy) {
        this.context = context;
        this.userlst = userlst;
        this.recy = recy;
    }

    @NonNull
    @Override
    public vhlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_search_two,parent,false);

        mainviewmodel= ViewModelProviders.of((FragmentActivity) context).get(mainviewmodel.class);
        return new beforetagadp.vhlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhlder holder, int position) {
        usermodel udel  = userlst.get(position);
        effecy eff = new effecy(context,udel.getUserid(),holder.annocru,holder.annoadm,holder.annofri);

        holder.usernme.setText(udel.getUsername());
        holder.name.setText(udel.getName());
        Picasso.get().load(udel.getImageurl()).placeholder(R.drawable.logo_background).into(holder.prof);


        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isenabled) {
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            MenuInflater menuInflater = mode.getMenuInflater();
                            menuInflater.inflate(R.menu.selectedusr, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            isenabled = true;
                            ClickItem(holder);

                            mainviewmodel.getUsermutalivdata().observe((LifecycleOwner) context, new Observer<usermodel>() {
                                @Override
                                public void onChanged(usermodel usermodel) {

                                    mode.setTitle(String.format("%s Selected",usermodel));
                                }
                            });
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            int id = item.getItemId();
                            switch (id){
                                case R.id.selectall:

                            }
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {

                        }
                    };
                }
            }
        });
//        holder.rr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!alstuser.contains(udel)){
//                    alstuser.add(udel);
//                    holder.rr.setVisibility(View.INVISIBLE);
//                }else{
//                    holder.rr.setVisibility(View.VISIBLE);
//                }
//                aftertagadp adp = new aftertagadp(context);
//                recy.setAdapter(adp);
//                recy.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
////              inte.putExtra("tagged", (Serializable) afusrlst.toArray());
//            }
//        });

    }
    private void ClickItem(vhlder holder){
        usermodel udel =userlst.get(holder.getAdapterPosition());
        if(!holder.cd.getCardBackgroundColor().equals(R.color.grey)){
            holder.cd.setCardBackgroundColor(Color.parseColor("#FF00DD"));
            allusrlst.add(udel);
        }else{
            holder.cd.setCardBackgroundColor(Color.parseColor("#000000"));
            allusrlst.remove(udel);
        }
        mainviewmodel.setUsermutalivdata((MutableLiveData<usermodel>) allusrlst);
    }

    @Override
    public int getItemCount() {
        return userlst.size();
    }

    public class vhlder extends RecyclerView.ViewHolder{

        private ImageView annofri,annoadm,annocru,prof;
        private TextView usernme,name;
        private RelativeLayout rr;
        private CardView cd;
        public vhlder(@NonNull View itemView) {
            super(itemView);

            annoadm=itemView.findViewById(R.id.adm);
            annocru=itemView.findViewById(R.id.cru);
            annofri=itemView.findViewById(R.id.fri);
            prof=itemView.findViewById(R.id.vpro);
            usernme=itemView.findViewById(R.id.vusme);
            name = itemView.findViewById(R.id.vnme);
            rr = itemView.findViewById(R.id.rr);
            cd = itemView.findViewById(R.id.cdgh);
        }
    }
}
