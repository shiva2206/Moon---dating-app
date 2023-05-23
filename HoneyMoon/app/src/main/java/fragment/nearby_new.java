package fragment;

import static android.view.View.GONE;

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

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import array_adapters.search_arradp;
import model.nearbymodel;
import model.usermodel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nearby_new#} factory method to
 * create an instance of this fragment.
 */
public class nearby_new extends Fragment {

    private String gend,stat;
    private SeekBar distsek;
    private RadioGroup gendr,status;
    private TextView nn,neaby,ser,dist,imgitxt;
    private List<usermodel> lstuser,allusers,lstran;;

    private search_arradp sadp;
    private EditText sagf,sagt;
    private SwipeFlingAdapterView swie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_new, container, false);

        nn=view.findViewById(R.id.nn);
        ser=view.findViewById(R.id.ser);
        neaby=view.findViewById(R.id.nby);
        imgitxt=view.findViewById(R.id.imgitxt);
        dist=view.findViewById(R.id.distxt);
        gendr=view.findViewById(R.id.radsex);
        status=view.findViewById(R.id.showcru);
        sagf=view.findViewById(R.id.sagf);
        sagt=view.findViewById(R.id.sagt);
        distsek=view.findViewById(R.id.distsekbr);
        lstuser =new ArrayList<>();
        allusers=new ArrayList<>();
        lstran=new ArrayList<>();
//        sadp = new search_arradp(getContext(),R.layout.adap_search,lstuser,new ArrayList<>(),new ArrayList<>());

        swie=view.findViewById(R.id.uswipe);
        swie.setAdapter(sadp);

        swie.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

                lstuser.remove(0);
                sadp.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {

                  }

            @Override
            public void onRightCardExit(Object o) {

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        geting();
        gendr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male:
                        gend = "male";
                        break;
                    case R.id.female:
                        gend = "female";
                        break;
                    case R.id.allrd:
                        gend="all";
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
                        stat= "single";
                        break;
                    case R.id.commited:
                        stat = "committed";
                        break;
                    case R.id.married:
                        stat="married";
                        break;
                    case R.id.sallr:
                        stat = "all";
                        break;
                    default:
                        break;

                }
            }
        });
        distsek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ser.setEnabled(false);
                if(gend!=null && !TextUtils.isEmpty(sagf.getText().toString()) && !TextUtils.isEmpty(sagt.getText().toString()) ) {
                    HashMap<String, String> mp = new HashMap<>();
                    mp.put("gender", gend);
                    mp.put("status", stat);
                    mp.put("agefrom", sagf.getText().toString());
                    mp.put("ageto", sagt.getText().toString());
                    mp.put("distance",String.valueOf((distsek.getProgress()/10)+1) );
                    FirebaseDatabase.getInstance().getReference().child("nearby").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mp);
                    Intent inte = new Intent(getContext(),Mainactivity.class);
                    inte.putExtra("nearby","nearby");
                    getContext().startActivity(inte);
                    getActivity().finish();

                }else{
                    Toast.makeText(getContext(), "Kindly Fill all Forms", Toast.LENGTH_SHORT).show();
                }
                ser.setEnabled(true);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("nearby").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int yy = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        nearbymodel nearedel = ds.getValue(nearbymodel.class);
                        neaby.setVisibility(View.VISIBLE);
                        neaby.setText("Nearby " + nearedel.getDistance()+" Km");
//                        finds();
                        yy=1;
                    }

                }
                if(yy==0){
                    neaby.setVisibility(GONE);
                    nn.setText("Turn 'ON' Show Location In The Settings ");
                    nn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
    public void geting(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstran=new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    usermodel udelll = ds.getValue(usermodel.class);
                    if (udelll.getShowlocation().equals("true") && !udelll.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        lstran.add(udelll);
                    }
                }
//                finds();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void finds(){
//        FirebaseDatabase.getInstance().getReference().child("vary").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                lstuser.clear();
//
//                varymodel currvarymdel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(varymodel.class);
//                for(usermodel udel : lstran){
//                    varymodel vydel = snapshot.child(udel.getUserid()).getValue(varymodel.class);
//                    System.out.println(vydel.toString());
//
////                  effecy ag = new effecy(getContext(),udel.getUserid(),imgitxt);
//
//                    effecy dis = new effecy(getContext(),vydel.getLatitude(),vydel.getLongitude(),dist,currvarymdel);
//                    Calendar calendar = Calendar.getInstance();
//                    final int year = calendar.get(Calendar.YEAR);
//                    final int mon = calendar.get(Calendar.MONTH);
//                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                    String today = day+"/"+mon+"/"+year;
//
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                    try {
//                        Date bdate = simpleDateFormat.parse(udel.getDob()+"");
//                        Date now = simpleDateFormat.parse(today);
//
//                        long startdate = bdate.getTime();
//                        long endate = now.getTime();
//
//                        Period period = new Period(startdate,endate, PeriodType.yearMonthDay());
////                        imgitxt.setText(period.getYears()+"");
////                            imgitxt.setTag(period.getYears());
//                        if(Double.parseDouble(dist.getTag()+"") <=
//                                Double.parseDouble(currnearbymdel.getDistance()+"") &&
//                                (gend.equals(udel.getGender()) || gend.equals("all"))  &&
//                                (Integer.parseInt(sagf.getText()+"") <=
//                                        Integer.parseInt(period.getYears()+"")) &&
//                                (Integer.parseInt(sagt.getText()+"") >=
//                                        Integer.parseInt(period.getYears()+"")) &&
//                                (stat.equals(udel.getStatus()) || stat.equals("all"))){
//
//                            lstuser.add(udel);
//                        }
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//
//                }
//                if(lstuser.isEmpty()){
//                    nn.setVisibility(View.VISIBLE);
//                }else{
//                    nn.setVisibility(View.INVISIBLE);
//                }
//
//                sadp.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    public void set(){
//        if(currnearbymdel.getGender() != null) {
//            distsek.setProgress((Integer.parseInt(currnearbymdel.getDistance()) - 1) * 10);
//            gend = currnearbymdel.getGender();
//            if (gend.equals("male")) {
//                gendr.check(R.id.male);
//            } else if (gend.equals("female")) {
//                gendr.check(R.id.female);
//            } else {
//                gendr.check(R.id.allrd);
//            }
//            stat = currnearbymdel.getStatus();
//            if (stat.equals("single")) {
//                status.check(R.id.sinle);
//            } else if (stat.equals("married")) {
//                status.check(R.id.married);
//            } else if (stat.equals("committed")) {
//                status.check(R.id.commited);
//            } else {
//                status.check(R.id.sallr);
//            }
//            sagf.setText(currnearbymdel.getAgefrom());
//            sagt.setText(currnearbymdel.getAgeto());
//        }
//        else {
//            distsek.setProgress(100);
//            dist.setText("11");
//            gendr.check(R.id.female);
//            status.check(R.id.sinle);
//            sagf.setText("18");
//            sagt.setText("30");
//        }
//    }
}