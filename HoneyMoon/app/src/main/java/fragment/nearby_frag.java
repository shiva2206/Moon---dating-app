package fragment;

import static model.constants.SWIPED;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.example.honeymoon.R;
import com.example.honeymoon.nearby;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapters.effecy;
import model.nearbymodel;
import model.usermodel;
import model.varymodel;

//original for search
public class nearby_frag extends Fragment {


    private SeekBar distsek;
    private RadioGroup gendr,status;
    private Intent intentttt ;
    private EditText sagf,sagt;
    private TextView ser,title,nn;
    private nearbymodel currnearbymdel;
//    private RecyclerView userrecy;
    private List<String> distlst;
    private List<varymodel> currvarymdel;
//    private searchadp nsearadp;
    private List<usermodel> lstuser,filtuser;
    private usermodel currusermdel;
    private TextView dist;

    public nearby_frag(nearbymodel currnearbymdel) {
        this.currnearbymdel = currnearbymdel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_nearby, container, false);

        ser=view.findViewById(R.id.ser);
        nn=view.findViewById(R.id.nn);
        dist=view.findViewById(R.id.distxt);
//        userrecy=view.findViewById(R.id.serrecy);
        title=view.findViewById(R.id.nby);
        gendr=view.findViewById(R.id.radsex);
        status=view.findViewById(R.id.showcru);
        sagf=view.findViewById(R.id.sagf);
        sagt=view.findViewById(R.id.sagt);
        distsek=view.findViewById(R.id.distsekbr);

        lstuser=new ArrayList<>();
        currvarymdel=new ArrayList<>();

        filtuser=new ArrayList<>();
        distlst=new ArrayList<>();

        intentttt=new Intent(getContext(), nearby.class);
        if(currnearbymdel == null) {

            currnearbymdel = new nearbymodel("all", "all", "11", "18", "25");
            sagf.setText(currnearbymdel.getAgefrom());
            sagt.setText(currnearbymdel.getAgeto());

        }else{
            distsek.setProgress((Integer.parseInt(currnearbymdel.getDistance()) - 1) * 10);


            if (currnearbymdel.getGender().equals("male")) {
                gendr.check(R.id.male);
            } else if (currnearbymdel.getGender().equals("female")) {
                gendr.check(R.id.female);
            } else {
                gendr.check(R.id.allrd);
            }
            if (currnearbymdel.getStatus().equals("single")) {
                status.check(R.id.sinle);
            } else if (currnearbymdel.getStatus().equals("married")) {
                status.check(R.id.married);
            } else if (currnearbymdel.getStatus().equals("committed")) {
                status.check(R.id.commited);
            } else {
                status.check(R.id.sallr);
            }
            sagf.setText(currnearbymdel.getAgefrom());
            sagt.setText(currnearbymdel.getAgeto());

        }
        title.setText("Distance Within "+currnearbymdel.getDistance()+"Km");

//        nsearadp=new searchadp(getContext(),filtuser,"nearby",FirebaseAuth.getInstance().getCurrentUser().getUid(),distlst,currnearbymdel);
//        userrecy.setAdapter(nsearadp);
//        userrecy.setLayoutManager(new LinearLayoutManager(getContext()));

        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ser.setEnabled(false);
                if(!TextUtils.isEmpty(sagf.getText().toString()) && !TextUtils.isEmpty(sagt.getText().toString()) ) {

                    currnearbymdel.setAgefrom(sagf.getText().toString());
                    currnearbymdel.setAgeto(sagt.getText().toString());
                    getvarydel();

                }else{
                    Toast.makeText(getContext()
                            , "Kindly Fill all Forms", Toast.LENGTH_SHORT).show();
                }
                ser.setEnabled(true);
            }
        });

        gendr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.male:
                        currnearbymdel.setGender("male");
                        break;
                    case R.id.female:
                        currnearbymdel.setGender("female");
                        break;
                    case R.id.allrd:
                        currnearbymdel.setGender("all");
                        break;
                    default:
                        break;

                }
            }
        });
        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sinle:
                        currnearbymdel.setStatus("single");
                        break;
                    case R.id.commited:
                        currnearbymdel.setStatus("committed");
                        break;
                    case R.id.married:
                        currnearbymdel.setStatus("married");
                        break;
                    case R.id.sallr:
                        currnearbymdel.setStatus("all");
                        break;
                    default:
                        break;

                }

            }
        });
        distsek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

              currnearbymdel.setDistance(String.valueOf((progress/10)+1));
              title.setText("Distance Within "+currnearbymdel.getDistance()+"Km");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return view;

    }
    public void getshowloc(){
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstuser.clear();

                for(DataSnapshot sdh : snapshot.child("users").getChildren()){
                    if(!sdh.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        usermodel udel = sdh.getValue(usermodel.class);
                        Toast.makeText(getContext(), snapshot.child("qwe").getValue()+"", Toast.LENGTH_SHORT).show();

                        if (udel.getShowlocation().equals("true")) {

                                Toast.makeText(getContext(),  snapshot.child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(SWIPED).child(udel.getUserid()).getValue()+"", Toast.LENGTH_SHORT).show();
                                if ((snapshot.child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(SWIPED).child(udel.getUserid()).getValue()+"").equals("null")){
                                    Toast.makeText(getContext(),  " 3", Toast.LENGTH_SHORT).show();

                                    lstuser.add(udel);

                                }





                        }
                    }else{
                        currusermdel= sdh.getValue(usermodel.class);
                    }
                }

                if(currusermdel.getShowlocation().equals("true")) {
                    nn.setVisibility(View.INVISIBLE);
                    check();
                }else{
                    nn.setText("Turn 'ON' Your show location in settings to view nearby people");
                    nn.setVisibility(View.VISIBLE);
                    filtuser.clear();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void check(){
        FirebaseDatabase.getInstance().getReference().child("vary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filtuser.clear();


                for(usermodel usssrd : lstuser){
                    varymodel vydel = snapshot.child(usssrd.getUserid()).getValue(varymodel.class);
                    if (vydel != null) {
                        effecy dis = new effecy(getContext(), vydel.getLatitude(), vydel.getLongitude(), dist, currvarymdel.get(0));
                        Calendar calendar = Calendar.getInstance();
                        final int year = calendar.get(Calendar.YEAR);
                        final int mon = calendar.get(Calendar.MONTH);
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);

                        String today = day + "/" + mon + "/" + year;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date bdate = simpleDateFormat.parse(usssrd.getDob() + "");
                            Date now = simpleDateFormat.parse(today);

                            long startdate = bdate.getTime();
                            long endate = now.getTime();

                            Period period = new Period(startdate, endate, PeriodType.yearMonthDay());
//                        imgitxt.setText(period.getYears()+"");
//                            imgitxt.setTag(period.getYears());

                            System.out.println(usssrd);

                            System.out.println(Double.parseDouble(dist.getTag() + "") <=
                                    Double.parseDouble(currnearbymdel.getDistance() + ""));

                            System.out.println(currnearbymdel.getGender().equals(usssrd.getGender()) || currnearbymdel.getGender().equals("all"));

                            System.out.println(Integer.parseInt(currnearbymdel.getAgefrom()) <=
                                    Integer.parseInt(period.getYears() + ""));

                            System.out.println(Integer.parseInt(currnearbymdel.getAgeto()) >=
                                    Integer.parseInt(period.getYears() + ""));

                            System.out.println(currnearbymdel.getStatus().equals(usssrd.getStatus()) || currnearbymdel.getStatus().equals("all"));



                            if (Double.parseDouble(dist.getTag() + "") <=
                                    Double.parseDouble(currnearbymdel.getDistance() + "") &&
                                    (currnearbymdel.getGender().equals(usssrd.getGender()) || currnearbymdel.getGender().equals("all")) &&
                                    (Integer.parseInt(currnearbymdel.getAgefrom()) <=
                                            Integer.parseInt(period.getYears() + "")) &&
                                    (Integer.parseInt(currnearbymdel.getAgeto()) >=
                                            Integer.parseInt(period.getYears() + "")) &&
                                    (currnearbymdel.getStatus().equals(usssrd.getStatus()) || currnearbymdel.getStatus().equals("all"))) {



                                    filtuser.add(usssrd);
                                    distlst.add(dist.getTag()+"");
                                    intentttt.putExtra(usssrd.getUserid(),dist.getTag()+"");



                                    startActivity(intentttt);
                                    effecy.instance.qaz = 1;




                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(filtuser.isEmpty()){
                    nn.setText("No Nearby Users");
                    nn.setVisibility(View.VISIBLE);
                }else{
                    nn.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getvarydel(){
        FirebaseDatabase.getInstance().getReference().child("vary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currvarymdel.clear();
                for(DataSnapshot doss : snapshot.getChildren()){
                    if(doss.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        varymodel vydel = doss.getValue(varymodel.class);
                        currvarymdel.add(vydel);
                        intentttt.putExtra("nearby", "nearby");
                        intentttt.putExtra("gender", currnearbymdel.getGender());
                        intentttt.putExtra("status", currnearbymdel.getStatus());
                        intentttt.putExtra("agefrom", currnearbymdel.getAgefrom());
                        intentttt.putExtra("ageto", currnearbymdel.getAgeto());
                        intentttt.putExtra("distance", currnearbymdel.getDistance());

                    }
                }
                getshowloc();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}