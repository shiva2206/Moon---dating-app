package fragment;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapters.effecy;
import adapters.nearbyadp;
import adapters.searchadp;
import array_adapters.search_arradp;
import model.nearbymodel;
import model.usermodel;
import model.varymodel;


public class nearby extends Fragment {
    private nearbyadp naerbyadp;
    private List<nearbymodel> currnermdelst;
    private nearbymodel currnearbymdel;
    private RecyclerView recy,userrecy;
    private List<String> stragelst,userstrlst;
    private List<varymodel> currvarymdel;
    private searchadp nsearadp;
    private List<usermodel> lstuser,filtuser;
    private usermodel currusermdel;
    private search_arradp sadp;
    private TextView dist,nn;
    private Intent intu;
    private SwipeFlingAdapterView swie;

    public nearby(List<String> userstrlst,List<String> stragelst){
        this.userstrlst = userstrlst;
        this.stragelst = stragelst;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_nearby, container, false);

        dist=view.findViewById(R.id.distxt);
//        recy=view.findViewById(R.id.nerrecy);
        swie= view.findViewById(R.id.uswipe);
        userrecy=view.findViewById(R.id.serrecy);
        nn=view.findViewById(R.id.nn);
        currnermdelst = new ArrayList<>();
        lstuser=new ArrayList<>();
        currvarymdel=new ArrayList<>();
        filtuser=new ArrayList<>();
//        naerbyadp=new nearbyadp(getContext(),currnermdelst,currvarymdel);
        recy.setAdapter(naerbyadp);
        recy.setLayoutManager(new LinearLayoutManager(getContext()));


        intu = new Intent(getContext(),Mainactivity.class);


        if(userstrlst == null){
            userstrlst = new ArrayList<>();
            nsearadp=new searchadp(getContext(),filtuser,"nearby",FirebaseAuth.getInstance().getCurrentUser().getUid());
            userrecy.setAdapter(nsearadp);
            userrecy.setLayoutManager(new LinearLayoutManager(getContext()));
            getcurner();
        }else{
            sadp = new search_arradp(getContext(),R.layout.adap_arr_search,filtuser,stragelst);
            swie.setAdapter(sadp);
            swie.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {

                    filtuser.remove(0);
                    stragelst.remove(0);
                    sadp.notifyDataSetChanged();
                    if(filtuser.isEmpty()){
                        nn.setVisibility(View.VISIBLE);
                    }else{
                        nn.setVisibility(GONE);
                    }
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

            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    filtuser.clear();
                    for(String uid : userstrlst){
                      usermodel udel  = snapshot.child(uid).getValue(usermodel.class);
                      if(udel != null){
                          filtuser.add(udel);
                      }
                    }
                    sadp.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
//        lstuser.add(new usermodel("Jl0vqqfcr1ZFNjoPu9FQ6dkzBZF2", "1234567", "22/06/2003", " poda bittu ",
//                "shivaprasad220603@gmail.com", "male", "freemium", "https://firebasestorage.googleapis.com/v0/b/honey-moon-1e621.appspot.com/o/Jl0vqqfcr1ZFNjoPu9FQ6dkzBZF2%2Fimageurl?alt=media&token=4c4e837e-cf1b-4b41-a064-24cebd38de06",
//                "shiva", "single", "yes", "shiva_daw", "public", "true", "true",
//                "https://firebasestorage.googleapis.com/v0/b/honey-moon-1e621.appspot.com/o/Jl0vqqfcr1ZFNjoPu9FQ6dkzBZF2%2Fcoverimage?alt=media&token=4cc8bb96-c7f7-4d5e-a13a-648b4c7a4df8", "fUNuN-NDRN-ub9BFEjezzz:APA91bF-JvRWDZqS6dn71gk2V06HVwg4FWqZJTCgVbWvir2WxqZIGwBC94dftM-7cWb39I8-3PdwqHYwQSg9uXXaxA7B6DsvrcHN95kEnbaGygwB69c4xOwzKclPdBGoKdZ_izUTK5W-", "all", "all", "all", "all", "true"));


        return view;

    }
    public void getshowloc(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstuser.clear();
                for(DataSnapshot sdh : snapshot.getChildren()){
                    if(!sdh.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        usermodel udel = sdh.getValue(usermodel.class);
                        if (udel.getShowlocation().equals("true"))
                            lstuser.add(udel);
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
                    sadp.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void check(){
        FirebaseDatabase.getInstance().getReference().child("vary").addValueEventListener(new ValueEventListener() {
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
//                                    lststr.add(dist.getTag() + "");
//                                    stragelst.add(period.getYears() + "");

//                                intu.putExtra(usssrd.getUserid(),dist.getTag()+" "+period.getYears());

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

//                startActivity(intu);
//                ((Activity) context).finish();
                nsearadp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getcurner(){

        FirebaseDatabase.getInstance().getReference().child("nearby").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currnermdelst.clear();
                for(DataSnapshot dt :snapshot.getChildren()){
                    if (dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        nearbymodel curmdel = dt.getValue(nearbymodel.class);
                        currnermdelst.add(curmdel);
                        currnearbymdel=curmdel;

                    }
                }

                if(currnermdelst.isEmpty()){
                    currnermdelst.add(new nearbymodel("all","all","10","18","25"));
                }
                getvarydel();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getvarydel(){
        FirebaseDatabase.getInstance().getReference().child("vary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currvarymdel.clear();
                for(DataSnapshot doss : snapshot.getChildren()){
                    if(doss.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        varymodel vydel = doss.getValue(varymodel.class);
                        currvarymdel.add(vydel);
                    }
                }
                naerbyadp.notifyDataSetChanged();
                getshowloc();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}