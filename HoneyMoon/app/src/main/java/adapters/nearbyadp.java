package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.List;

import model.nearbymodel;

public class nearbyadp extends RecyclerView.Adapter<nearbyadp.vhder>{

    private Context context;
    private List<nearbymodel> nearlst;

    private nearbymodel currnearbymdel;
    private String genderstr,statusstr;

    public nearbyadp(Context context, List<nearbymodel> nearlst) {
        this.context = context;
        this.nearlst = nearlst;
    }

    @NonNull
    @Override
    public vhder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_nearby,parent,false);
        return new nearbyadp.vhder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull vhder holder, int position) {

        currnearbymdel = nearlst.get(position);



        genderstr="";
        statusstr="";
        holder.title.setText("Distance Within "+currnearbymdel.getDistance()+"Km");

//        search_arradp sadp = new search_arradp(context,R.layout.adap_search,filtuser,new ArrayList<>());
//        holder.swie.setAdapter(sadp);
//
//        holder.swie.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
//            @Override
//            public void removeFirstObjectInAdapter() {
//
//                if(!lstuser.isEmpty()) {
//                    lstuser.remove(0);
//                }
//                sadp.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onLeftCardExit(Object o) {
//
//            }
//
//            @Override
//            public void onRightCardExit(Object o) {
//
//            }
//
//            @Override
//            public void onAdapterAboutToEmpty(int i) {
//
//            }
//
//            @Override
//            public void onScroll(float v) {
//
//            }
//        });


        holder.distsek.setProgress((Integer.parseInt(currnearbymdel.getDistance()) - 1) * 10);


        if (currnearbymdel.getGender().equals("male")) {
            holder.gendr.check(R.id.male);
        } else if (currnearbymdel.getGender().equals("female")) {
            holder.gendr.check(R.id.female);
        } else {
            holder.gendr.check(R.id.allrd);
        }
        if (currnearbymdel.getStatus().equals("single")) {
            holder.status.check(R.id.sinle);
        } else if (currnearbymdel.getStatus().equals("married")) {
            holder.status.check(R.id.married);
        } else if (currnearbymdel.getStatus().equals("committed")) {
            holder.status.check(R.id.commited);
        } else {
            holder.status.check(R.id.sallr);
        }
        holder.sagf.setText(currnearbymdel.getAgefrom());
        holder.sagt.setText(currnearbymdel.getAgeto());




        holder.ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ser.setEnabled(false);
                if (genderstr.equals("")){
                    genderstr=currnearbymdel.getGender();
                }
                if (statusstr.equals("")){
                    statusstr=currnearbymdel.getStatus();
                }
                if(!TextUtils.isEmpty(holder.sagf.getText().toString()) && !TextUtils.isEmpty(holder.sagt.getText().toString()) ) {
                    FirebaseDatabase.getInstance().getReference().child("nearby").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("agefrom").setValue(holder.sagf.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("nearby").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("ageto").setValue(holder.sagt.getText().toString());


                    FirebaseDatabase.getInstance().getReference().child("nearby").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("status").setValue(statusstr);

                    FirebaseDatabase.getInstance().getReference().child("nearby").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("gender").setValue(genderstr);


                }else{
                    Toast.makeText(context, "Kindly Fill all Forms", Toast.LENGTH_SHORT).show();
                }

                Intent inty = new Intent(context,Mainactivity.class);
                inty.putExtra("nearby","nearby");
                context.startActivity(inty);
                ((Activity)context).finish();
                holder.ser.setEnabled(true);
            }
        });
        holder.gendr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.male:
                        genderstr="male";
                        break;
                    case R.id.female:
                        genderstr="female";
                        break;
                    case R.id.allrd:
                        genderstr="all";
                        break;
                    default:
                        break;

                }

                Toast.makeText(context, currnearbymdel.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        holder.status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sinle:
                        statusstr= "single";
                        break;
                    case R.id.commited:
                        statusstr = "committed";
                        break;
                    case R.id.married:
                        statusstr="married";
                        break;
                    case R.id.sallr:
                        statusstr = "all";
                        break;
                    default:
                        break;

                }

            }
        });
        holder.distsek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                FirebaseDatabase.getInstance().getReference().child("nearby").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("distance").setValue(String.valueOf((progress/10)+1));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return nearlst.size();
    }

    public class vhder extends RecyclerView.ViewHolder{


        private SeekBar distsek;
        private RadioGroup gendr,status;

        private EditText sagf,sagt;
        private TextView ser,title;
        private SwipeFlingAdapterView swie;

        public vhder(@NonNull View itemView) {
            super(itemView);


            title=itemView.findViewById(R.id.nby);
            gendr=itemView.findViewById(R.id.radsex);
            status=itemView.findViewById(R.id.showcru);
            sagf=itemView.findViewById(R.id.sagf);
            sagt=itemView.findViewById(R.id.sagt);
            distsek=itemView.findViewById(R.id.distsekbr);
            ser=itemView.findViewById(R.id.ser);
            swie=itemView.findViewById(R.id.uswipe);
        }

    }
}
