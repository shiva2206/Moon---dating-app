package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.example.honeymoon.blockedconts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import java.util.List;

import model.usermodel;

public class settingadp extends RecyclerView.Adapter<settingadp.svhder> {

    private Context context;
    private List<usermodel> currusrmdelst;

    public settingadp(Context context, List<usermodel> currusrmdelst) {
        this.context = context;
        this.currusrmdelst = currusrmdelst;
    }

    @NonNull
    @Override
    public svhder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_settings,parent,false);
        return new settingadp.svhder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull svhder holder, int position) {

        usermodel settmodel = currusrmdelst.get(position);


        if(settmodel.getMode().equals("private")){
            holder.priva.setChecked(true);
        }else{
            holder.priva.setChecked(false);
        }

        if(settmodel.getShowlastseen().equals("true")){
            holder.lstsce.setChecked(true);
        }else{
            holder.lstsce.setChecked(false);
        }

        if(settmodel.getShowlocation().equals("true")){
            holder.loc.setChecked(true);
        }else{
            holder.loc.setChecked(false);
        }




        if(settmodel.getShowaddcrubut().equals("true")){
            holder.showcrubut.setChecked(true);
        }else {
            holder.showcrubut.setChecked(false);
        }

        if(settmodel.getIstagpermitted().equals("true")){
            holder.tg.setChecked(true);
        }else{
            holder.tg.setChecked(false);
        }

        if(settmodel.getShowmat().equals("all")){
            holder.mat.check(R.id.sall);
        }else if (settmodel.getShowmat().equals("number")){
            holder.mat.check(R.id.son);
        }else {
            holder.mat.check(R.id.lck);
        }

        if(settmodel.getShowcru().equals("all")){
            holder.cru.check(R.id.csall);
        }else if (settmodel.getShowcru().equals("number")){
            holder.cru.check(R.id.cson);
        }else {
            holder.cru.check(R.id.clck);
        }

        if(settmodel.getShowadm().equals("all")){
            holder.adm.check(R.id.asall);
        }else if (settmodel.getShowadm().equals("number")){
            holder.adm.check(R.id.ason);
        }else {
            holder.adm.check(R.id.aclck);
        }

        if(settmodel.getShowfri().equals("all")){
            holder.fri.check(R.id.fall);
        }else if (settmodel.getShowfri().equals("number")){
            holder.fri.check(R.id.fson);
        }else {
            holder.fri.check(R.id.fclck);
        }

        holder.tg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              holder.tg.setEnabled(false);
              if(holder.tg.isChecked()){
                  FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("istagpermitted")
                          .setValue("true");
              }else{
                  FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("istagpermitted")
                          .setValue("false");
              }
              holder.tg.setEnabled(true);
            }
        });
        holder.showcrubut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showcrubut.setEnabled(false);
                if(holder.showcrubut.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showaddcrubut").setValue("true");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showaddcrubut","true").commit();

                }else {
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showaddcrubut").setValue("false");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showaddcrubut","false").commit();

                }
                holder.showcrubut.setEnabled(true);
//                sete();
            }
        });
        holder.blckc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.blckc.setEnabled(false);
                context.startActivity(new Intent(context, blockedconts.class));
                holder.blckc.setEnabled(true);
            }
        });
        holder.mat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.sall:
                        wht = "all";
                        break;
                    case R.id.son:
                        wht="number";
                        break;
                    case R.id.lck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showmat").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showmat",wht).commit();
//                sete();
            }
        });
        holder.cru.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.csall:
                        wht = "all";
                        break;
                    case R.id.cson:
                        wht="number";
                        break;
                    case R.id.clck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showcru").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showcru",wht).commit();
//                sete();
            }
        });
        holder.adm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.asall:
                        wht = "all";
                        break;
                    case R.id.ason:
                        wht="number";
                        break;
                    case R.id.aclck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showadm").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showadm",wht).commit();
//                sete();
            }
        });
        holder.fri.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String wht ="";
                switch (checkedId){
                    case R.id.fall:
                        wht = "all";
                        break;
                    case R.id.fson:
                        wht="number";
                        break;
                    case R.id.fclck:
                        wht="lock";
                        break;
                    default:
                        break;

                }
                FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("showfri").setValue(wht);
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showfri",wht).commit();
//                sete();
            }
        });

        holder.priva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.priva.setEnabled(false);
                if(!holder.priva.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("mode").setValue("public");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("mode","public").commit();

                }else{
                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("notify").child("requests").child("follows").removeValue();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("mode").setValue("private");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("mode","private").commit();

                }
                holder.priva.setEnabled(true);
//                sete();
            }
        });
        holder.lstsce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lstsce.setEnabled(false);
                if(holder.lstsce.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlastseen").setValue("true");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlastseen","true").commit();

                }else{
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlastseen").setValue("false");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlastseen","false").commit();

                }
                holder.lstsce.setEnabled(true);
//                sete();
            }
        });
        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.loc.setEnabled(false);
                if(holder.loc.isChecked()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlocation").setValue("true");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlocation","true").commit();


                }else{
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("showlocation").setValue("false");
//                    getContext().getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlocation","false").commit();

                }
                holder.loc.setEnabled(true);
//                sete();
            }
        });

    }

    @Override
    public int getItemCount() {
        return currusrmdelst.size();
    }

    public class svhder extends RecyclerView.ViewHolder{
        private SwitchCompat lstsce,priva,loc,tg,showcrubut;
        private Button dele,blckc;
        private RadioGroup mat,adm,cru,fri;
        private SocialTextView soci;
        private usermodel settmodel;

        public svhder(@NonNull View itemView) {
            super(itemView);

            lstsce =itemView.findViewById(R.id.shon);
            priva=itemView.findViewById(R.id.priacc);
            loc=itemView.findViewById(R.id.loc);
            mat=itemView.findViewById(R.id.showmat);
            cru=itemView.findViewById(R.id.showcru);
            fri=itemView.findViewById(R.id.showfri);
            adm=itemView.findViewById(R.id.showadm);
            dele=itemView.findViewById(R.id.delete);
            soci=itemView.findViewById(R.id.funpa);
            tg=itemView.findViewById(R.id.alltotg);
            blckc=itemView.findViewById(R.id.blcans);
            showcrubut=itemView.findViewById(R.id.showadcru);

        }
    }
}
