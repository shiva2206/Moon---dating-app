package model;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;

import com.example.honeymoon.login;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null && locationResult.getLastLocation() != null) {
                double lat = locationResult.getLastLocation().getLatitude();
                double longi = locationResult.getLastLocation().getLongitude();
//                Log.d("location",String.valueOf(lat) +"," + String.valueOf(longi));

                try {
                    FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("latitude").setValue(String.format("%.2f", lat));
                    FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("longitude").setValue(String.format("%.2f", longi));
                }catch (NullPointerException e){
                    System.out.println("null");
                    onDestroy();
                }
//                FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot dss : snapshot.child("users").getChildren() ){
//                            if(dss.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                curr = dss.getValue(usermodel.class);
//                                for(DataSnapshot ds : snapshot.child("users").getChildren() ){
//                                    if(!ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    && !snapshot.child("info").child(curr.getUserid()).child("followings").child(ds.getKey()).exists()
//                                            && !snapshot.child("info").child(curr.getUserid()).child("followers").child(ds.getKey()).exists()
//                                            && !snapshot.child("info").child(curr.getUserid()).child("crushs").child(ds.getKey()).exists()
//                                            && !snapshot.child("info").child(curr.getUserid()).child("admirers").child(ds.getKey()).exists()){
//                                        userinfo = ds.getValue(usermodel.class);
//                                        if(userinfo.getShowlocation().equals("true")) {
//                                            String dob = userinfo.getDob();
//                                            Calendar calendar = Calendar.getInstance();
//                                            final int year = calendar.get(Calendar.YEAR);
//                                            final int mon = calendar.get(Calendar.MONTH);
//                                            final int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//                                            String today = day+"/"+mon+"/"+year;
//
//                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                            try {
//                                                Date bdate = simpleDateFormat.parse(dob);
//                                                Date now = simpleDateFormat.parse(today);
//
//                                                long startdate = bdate.getTime();
//                                                long endate = now.getTime();
//
//                                                Period period = new Period(startdate,endate, PeriodType.yearMonthDay());
//
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//
//
//                                        }
//                                    }
//                                }break;
//
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        }
    };
//    public void getdist(){
//
//        double ulat =Double.parseDouble(curr.getLatitude());
//        double ulongi = Double.parseDouble(curr.getLongitude());
//
//        double lat = Double.parseDouble(userinfo.getLatitude());
//        double longi = Double.parseDouble(userinfo.getLongitude());
//
//        double longdiff = ulongi - longi;
//        double distance = Math.sin(deg2rad(ulat))
//                *  Math.sin(deg2rad(lat))
//                +  Math.cos(deg2rad(ulat))
//                *  Math.cos(deg2rad(lat))
//                * Math.cos(deg2rad(longdiff));
//
//        distance = Math.acos(distance);
//        distance=rad2deg(distance);
//
//        distance=distance * 60 * 1.1515;
//        distance = distance * 1.61;
//
//        if (distance <= Integer.parseInt(curr.getDistance())){
//            FirebaseDatabase.getInstance().getReference().child("info").child(curr.getUserid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(!snapshot.child("nearbyold").child(userinfo.getUserid()).exists()){
//                        if(!snapshot.child("nearbynew").child(userinfo.getUserid()).exists()){
//                            FirebaseDatabase.getInstance().getReference().child("info").child(curr.getUserid()).child("nearbynew")
//                                    .child(userinfo.getUserid()).setValue("true");
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//
//
//    }
//    private double rad2deg(double dis){
//        return (dis * 180.0 / Math.PI);
//    }
//    private double deg2rad(double la){
//        return (la*Math.PI/180.0);
//    }


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startlocationservice() {
        String Channelid = "location_notification_channel";
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }
    private void stoplocationservice(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            if(action != null){
                if(action.equals(constants.ACTION_START_LOCATION_SERVICE)){
                    startlocationservice();
                }else if (action.equals(constants.ACTION_STOP_LOCATION_SERVICE)){
                    stoplocationservice();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}