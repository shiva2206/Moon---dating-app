package adapters;

import static android.view.View.GONE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import NotificationPack.APIService;
import NotificationPack.Client;
import NotificationPack.Data;
import NotificationPack.MyResponse;
import NotificationPack.NotificationSender;
import model.commentsmodel;
import model.notifymodel;
import model.postmodel;
import model.statusmodel;
import model.usermodel;
import model.varymodel;
import retrofit2.Callback;
import retrofit2.Response;


public class effecy {
    public static final String APP_KEY = "29b46f4a-2acd-4d3d-bf26-1b90359db7be";
    public static final String APP_SECRET = "YAyz5F+Ua0Kt3jijImEmWw==";
    public static final String ENVIRONMENT = "clientapi.sinch.com";

    public static String AES = "AES";
    public static int qaz = 0;
    public static String encryptpassword = "shiva prasad";

    private String userid, what, mode, ulatt, ulong;
    private List<statusmodel> stalst;

    public void setSinchClient(SinchClient sinchClient) {
        this.sinchClient = sinchClient;
    }

    public Context context;
    public static effecy instance, security;
    private Dialog dialog;
    public int summa = 0;
    private ImageView send, annofri, annocru, annoadm, bcimg;
    private int p = 0;
    private usermodel usrdel, curuserdel;
    private TextView distt, age, lastseen;
    private varymodel currvarymodel;
    String friend;

    private Call call;
    private TextView callState, usrnme;
    private ImageView prof;
    private SinchClient sinchClient;
    private Button button, decline, pick, reject;
    private String callerId;
    private String recipientId;

    public effecy() {

    }

    public effecy(Context context, String what) {
        this.context = context;
        this.what = what;
        if (effecy.instance == null) {
            effecy.instance = new effecy();
        }
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        caal();
    }

    public effecy(String what) {
        if (effecy.security == null) {
            effecy.security = new effecy();
        }
    }

    public effecy(Context context) {
        this.context = context;
        if (effecy.instance == null) {
            effecy.instance = new effecy();
        }
//        dialog = new Dialog(context);
//        dialog.setCancelable(false);
//        caal();
    }

    public effecy(Context context, String userid, ImageView send) {
        this.context = context;
        this.userid = userid;
        this.send = send;
        getbc();

    }

    public effecy(Context context, String ulatt, String ulong, TextView distt, varymodel currvarymdel) {
        this.context = context;
        this.ulatt = ulatt;
        this.ulong = ulong;
        this.distt = distt;
        this.currvarymodel = currvarymdel;
        getdist();
    }

    public effecy(Context context, String userid, TextView lastseen, String what) {
        this.context = context;
        this.userid = userid;
        this.lastseen = lastseen;
        this.what = what;

    }

    //geting age
    public effecy(Context context, String userid, TextView age) {
        this.context = context;
        this.userid = userid;
        this.age = age;
        getage();
    }

    //to exit following
    public effecy(Context context, String firstuserid, String userid, String what, String folltag, String fritag) {
        this.userid = userid;
        this.what = what;
        this.context = context;
        checkfoll(firstuserid, folltag, fritag);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public effecy(Context context, String userid, String what, String mode) {
        this.userid = userid;
        this.what = what;
        this.context = context;
        this.mode = mode;
        setnoti();
    }

    public effecy(String userid, ImageView bcimg) {
        this.userid = userid;
        this.bcimg = bcimg;
        stalst = new ArrayList<>();
        try {
            isfollw();
        } catch (NullPointerException e) {

        }
    }

    public effecy(Context context, String userid, ImageView annocru, ImageView annoadm, ImageView annofri) {
        this.userid = userid;
        this.annofri = annofri;
        this.annocru = annocru;
        this.annoadm = annoadm;
        getuserinfo();

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void getage() {
        FirebaseDatabase.getInstance().getReference().child("users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String dob = snapshot.child("dob").getValue() + "";
                calculateage(dob,age,true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void calculateage(String dob,TextView age,boolean issettext){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int mon = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String today = day + "/" + mon + "/" + year;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date bdate = simpleDateFormat.parse(dob);
            Date now = simpleDateFormat.parse(today);

            long startdate = bdate.getTime();
            long endate = now.getTime();

            Period period = new Period(startdate, endate, PeriodType.yearMonthDay());
            if (issettext) {
                age.setText(period.getYears() + "");
            }
            age.setTag(period.getYears());

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void getbc() {
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("blocked").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
//                    send.setVisibility(View.INVISIBLE);
                    send.setTag("blocked");
                } else {
//                    send.setVisibility(View.VISIBLE);
                    send.setTag("notblocked");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkfoll(String firstuserid, String firsttag, String secondtag) {

        if (what.equals("following")) {
            System.out.println(firsttag);
            String folldat = firsttag.substring(0, 10);
            String follkey = firsttag.substring(10);


            sendnotii(context,firstuserid,null,null,null,follkey,"del");
            sendnotii(context,userid,null,null,null,follkey,"del");

            FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid).child("notify")
                    .child("others").child(folldat).child(follkey).removeValue();

            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").
                    child("others").child(folldat).child(follkey).removeValue();


            FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid).child("followings")
                    .child(userid).removeValue();

            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("followers")
                    .child(firstuserid).removeValue();


            friremve(firstuserid, secondtag);

//        }else if(what.equals("matchremove")){
//           try {
//               String dat = firsttag.substring(0, 10);
//               String ky = firsttag.substring(10);
//
//               System.out.println(firsttag);
//
//               FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("others")
//                       .child(dat).child(ky).removeValue();
//               FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify").child("others")
//                       .child(dat).child(ky).removeValue();
//
//               FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("matches").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                       .removeValue();
//               FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("matches")
//                       .child(userid).removeValue();
//           }catch (Exception e){
//
//           }
        } else if (what.equals("crushremove")) {
            FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid)
                    .child("crushs").child(userid).removeValue();

            String datt = firsttag.substring(0, 10);
            String kyt = firsttag.substring(10);

            sendnotii(context,firstuserid,null,null,null,kyt,"del");

            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify")
                    .child("others").child(datt).child(kyt).removeValue();

            FirebaseDatabase.getInstance().getReference().child("info").child(userid)
                    .child("admirers").child(firstuserid).removeValue();

            try {
                String dat = secondtag.substring(0, 10);
                String ky = secondtag.substring(10);

                System.out.println(secondtag);

                sendnotii(context,firstuserid,null,null,null,ky,"del");
                sendnotii(context,userid,null,null,null,ky,"del");

                FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("others")
                        .child(dat).child(ky).removeValue();
                FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid).child("notify").child("others")
                        .child(dat).child(ky).removeValue();



                FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("matches").child(firstuserid)
                        .removeValue();
                FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid).child("matches")
                        .child(userid).removeValue();
            } catch (Exception e) {

            }
//        }else if(what.equals("admirerremove")){
//            FirebaseDatabase.getInstance().getReference().child("info").child(userid)
//                    .child("crushs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
//
//            String datt = firsttag.substring(0,10);
//            String kyt = firsttag.substring(10);
//
//            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify")
//                    .child("others").child(datt).child(kyt).removeValue();
//
//            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .child("admirers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        } else if (what.equals("friendremove")) {

            friremve(firstuserid, secondtag);

        }
    }

    public void friremve(String firstuserid, String tag) {

        if (tag != null) {
            if (tag.length() > 4) {
                System.out.println(tag + "f");
                String fridat = tag.substring(0, 10);
                String frikey = tag.substring(10);
                System.out.println(fridat + " " + frikey);


                sendnotii(context,firstuserid,null,null,null,frikey,"del");
                sendnotii(context,userid,null,null,null,frikey,"del");

                FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid).child("notify")
                        .child("others").child(fridat).child(frikey).removeValue();
                FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").
                        child("others").child(fridat).child(frikey).removeValue();


                FirebaseDatabase.getInstance().getReference().child("info").child(userid)
                        .child("bfs").child(firstuserid).removeValue();
                FirebaseDatabase.getInstance().getReference().child("info").child(userid)
                        .child("gfs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid)
                        .child("bfs").child(userid).removeValue();
                FirebaseDatabase.getInstance().getReference().child("info").child(firstuserid)
                        .child("gfs").child(userid).removeValue();
            }
        }
    }

    public void caal() {
        sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .userId(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        requestpermission();
        if (what != null) {
            if (call == null) {

                effecy.instance.summa = 1;
                opendialog();

                decline.setVisibility(View.VISIBLE);
                call = sinchClient.getCallClient().callUser(what);
                gety(what, call);
                call.addCallListener(new effecy.SinchCallListener());
//                button.setText("Hang Up");


            } else {
                call.hangup();
            }
        }
        if (effecy.instance.summa == 0) {
            sinchClient.getCallClient().addCallClientListener((CallClientListener) new SinchCallClientListener());

        }
    }

    public class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
//            button.setText("Call");
            callState.setText("callended");
            dialog.dismiss();
            effecy.instance.summa = 0;
            ((Activity) context).setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            callState.setText("connected");
            System.out.println("estabblished");
            ((Activity) context).setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            callState.setText("ringing");

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
    }

    public class SinchCallClientListener implements CallClientListener {


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            effecy.instance.summa = 1;
            opendialog();
            pick.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
            call = incomingCall;

            String ddat = effecy.instance.gettime();
            String keyy = FirebaseDatabase.getInstance().getReference().push().getKey();

//            HashMap<String,String> hmp = new HashMap<>();
//            hmp.put("userid",call.getRemoteUserId());
//            hmp.put("going","outgoing");
//            hmp.put("type","audio");
//            hmp.put("time",ddat);
//            hmp.put("talktime","30");
//            hmp.put("callid",keyy);
//
//            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .child("calls").child(keyy).setValue(hmp);
//
//            hmp.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
//            hmp.put("going","ingoing");
//            FirebaseDatabase.getInstance().getReference().child("info").child(call.getRemoteUserId())
//                    .child("calls").child(keyy).setValue(hmp);

            gety(incomingCall.getRemoteUserId(), call);

        }
    }

    public void gety(String userid, Call cll) {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (call != null) {
                    for (DataSnapshot ss : snapshot.getChildren()) {

                        if (ss.getKey().equals(userid)) {
                            usermodel caller = ss.getValue(usermodel.class);

                            usrnme.setText(caller.getUsername());
                            Picasso.get().load(caller.getImageurl()).into(prof);

                        }
                    }
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void requestpermission() {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission
                (context, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setnoti() {
        if (what.equals("friendreq")) {

        } else if (what.equals("frirequestcancel")) {

            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("requests")
                    .child("friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        } else if (what.equals("requestcancel")) {
            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("requests")
                    .child("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        } else if (what.equals("follows")) {
            String dat = effecy.instance.gettime();
            if (mode.equals("private")) {
                HashMap<String, String> mp = new HashMap<>();
                String key = FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("requests").push().getKey();
                mp.put("subject", "followreq");
                mp.put("notify", " ");
                mp.put("postid", " ");
                mp.put("publisherid", " ");
                mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                mp.put("notifyid", key);
                mp.put("time", dat);
                FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("requests").child("follows")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mp);
                sendnotii(context, userid, "Follow Request", "followreq", null,key,null);
            } else {
                HashMap<String, String> mp = new HashMap<>();
                String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                mp.put("subject", "follows");
                mp.put("notify", " ");
                mp.put("postid", " ");
                mp.put("publisherid", " ");
                mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                mp.put("notifyid", key);
                mp.put("time", dat.substring(11));
                FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("others")
                        .child(dat.substring(0, 10)).child(key).setValue(mp);


                HashMap kmp = new HashMap();
                kmp.put("key", key);
                kmp.put("time", dat.substring(11));
                kmp.put("date", dat.substring(0, 10));

                FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("followings").child(userid).setValue(kmp);

                FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("followers")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(kmp);
                sendnotii(context, userid, "Follows You", "follows", null,key,null);
            }
        }
    }

    public void getuserinfo() {
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dss : snapshot.getChildren()) {

                    if (dss.getKey().equals(userid)) {
                        usrdel = dss.getValue(usermodel.class);
                    }
                    if (dss.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        curuserdel = dss.getValue(usermodel.class);
                    }
                }
                if (!curuserdel.getGender().equals(usrdel.getGender())) {
                    if (curuserdel.getGender().equals("male")) {
                        friend = "gfs";
                    } else {
                        friend = "bfs";
                    }
                    getanno();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getanno() {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("crushs").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(userid).exists()) {
                            annocru.setVisibility(View.VISIBLE);
                        } else {
                            annocru.setVisibility(GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("admirers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(userid).exists()) {
                            annoadm.setVisibility(View.VISIBLE);
                        } else {
                            annoadm.setVisibility(GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(friend).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(userid).exists()) {
                            annofri.setVisibility(View.VISIBLE);
                        } else {
                            annofri.setVisibility(GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void isfollw() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("users").child(userid).child("mode").getValue().equals("public")
                        || userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        || snapshot.child("info").child(userid).child("followers")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    seenornot();
                } else if (snapshot.child("users").child(userid).child("mode").getValue().equals("private")) {
                    bcimg.setBackgroundColor(Color.parseColor("#000000"));
                    bcimg.setBackgroundResource(R.drawable.blacksolidcircle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void seenornot() {
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stalst.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dss : snapshot.getChildren()) {
                        statusmodel sta = dss.getValue(statusmodel.class);
                        stalst.add(sta);
                    }
                    bcimg.setTag("yes");
                    bcimg.setBackgroundResource(R.drawable.blacksolidcircle);
                    getsen();

                } else {
                    bcimg.setTag("no");
//                    bcimg.setBackgroundColor(Color.parseColor("#000000"));
                    bcimg.setBackgroundResource(R.drawable.pinksolidcircle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getsen() {


        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("seen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    p = 0;
                    for (statusmodel i : stalst) {
                        if (snapshot.child(i.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                            p = p + 1;
                        }
                    }
                    if (p != stalst.size()) {
                        bcimg.setTag("yes");
                        bcimg.setBackgroundColor(Color.parseColor("#FF00DD"));
                    } else {
                        bcimg.setTag("yes");
                        bcimg.setBackgroundColor(Color.parseColor("#B8B8B8"));
                    }

                } else {
                    bcimg.setBackgroundColor(Color.parseColor("#FF00DD"));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System.out.println(bcimg.getTag() + "");


    }

    public void getdist() {

        double ulat = Double.parseDouble(currvarymodel.getLatitude());
        double ulongi = Double.parseDouble(currvarymodel.getLongitude());

        double lat = Double.parseDouble(ulatt);
        double longi = Double.parseDouble(ulong);

        double longdiff = ulongi - longi;
        double distance = Math.sin(deg2rad(ulat))
                * Math.sin(deg2rad(lat))
                + Math.cos(deg2rad(ulat))
                * Math.cos(deg2rad(lat))
                * Math.cos(deg2rad(longdiff));

        distance = Math.acos(distance);
        distance = rad2deg(distance);

        distance = distance * 60 * 1.1515;
        distance = distance * 1.61;

//        dist.setText("( "+String.format("%.0f",distance)+"Km Away )");
        distt.setTag(distance);

    }

    private double rad2deg(double dis) {
        return (dis * 180.0 / Math.PI);
    }

    private double deg2rad(double la) {
        return (la * Math.PI / 180.0);
    }

    public void opendialog() {
        dialog.setContentView(R.layout.custom_dialog);
        prof = dialog.findViewById(R.id.callpro);
        usrnme = dialog.findViewById(R.id.callusername);
        callState = dialog.findViewById(R.id.callstate);
        decline = dialog.findViewById(R.id.decl);
        pick = dialog.findViewById(R.id.pick);
        reject = dialog.findViewById(R.id.reject);
        dialog.show();

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline.setEnabled(false);
                if (call != null) {
                    call.hangup();
                    dialog.dismiss();
                    call = null;
                } else {
                    dialog.dismiss();
                }
                decline.setEnabled(true);
            }
        });
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick.setEnabled(false);
                call.answer();
                call.addCallListener(new effecy.SinchCallListener());
//                    button.setText("Hang Up");
                pick.setVisibility(GONE);
                reject.setVisibility(GONE);
                decline.setVisibility(View.VISIBLE);
                pick.setEnabled(true);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject.setEnabled(false);
                call.hangup();
                call = null;
                dialog.dismiss();
                reject.setEnabled(true);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String gettime() {
        Instant instant = Instant.now();
        ZoneId z = ZoneId.of("Asia/Kolkata");
        ZonedDateTime zdt = instant.atZone(z);
        String hu = zdt.toString();
        System.out.println(hu);
        return hu.substring(8, 10) + hu.substring(4, 8) + hu.substring(0, 4) + " " + hu.substring(11, 19);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getzonetime(String isttime){

        Clock clock = Clock.systemDefaultZone();

       try {
           SimpleDateFormat formatter6 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
           Date date = formatter6.parse(isttime);


//           ZoneId z = ZoneId.of();
           TimeZone pstTimezone = TimeZone.getTimeZone(String.valueOf(clock.getZone()));

           formatter6.setTimeZone(pstTimezone);
//           ZonedDateTime zdt = instant.atZone(z);
           String PSTTime = formatter6.format(date);

           return PSTTime.substring(0,16);

       } catch (ParseException e) {
           e.printStackTrace();
       }


       return "";
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getmilltime() {
        Instant instant = Instant.now();
        ZoneId z = ZoneId.of("Asia/Kolkata");
        ZonedDateTime zdt = instant.atZone(z);
        String hu = zdt.toString();
        System.out.println(hu);
        return hu.substring(8, 10) + hu.substring(4, 8) + hu.substring(0, 4) + " " + hu.substring(11, 23);
    }

    public List<String> getdalst(List<String> dalst) {
        List<String> orderlst = new ArrayList<>();
        List<Date> lstdate = new ArrayList<>();
        SimpleDateFormat formatter6 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for (String dat : dalst) {
            try {
                Date date = formatter6.parse(dat);
                System.out.println(date);
                lstdate.add(date);
            } catch (ParseException e) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
                    Date date = formatter.parse(dat);
                    lstdate.add(date);
                } catch (ParseException r) {

                }
            }
        }
        Collections.sort(lstdate);
        for (Date date : lstdate) {
            for (String strdat : dalst) {
                try {
                    Date dat = formatter6.parse(strdat);
                    if (dat.equals(date)) {
                        orderlst.add(strdat);
                        break;
                    }
                } catch (ParseException e) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
                        Date dat = formatter.parse(strdat);
                        if (dat.equals(date)) {
                            orderlst.add(strdat);
                            break;
                        }
                    } catch (ParseException pa) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return orderlst;
    }

    public String decrypt(String outputstr) throws Exception {
        SecretKeySpec key = generateKey();
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedvalue = Base64.decode(outputstr, Base64.DEFAULT);
        byte[] decvalue = c.doFinal(decodedvalue);
        String decryptedvalue = new String(decvalue);
        return decryptedvalue;
    }

    public String encrypt(String Data) throws Exception {
        SecretKeySpec key = generateKey();
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encval = c.doFinal(Data.getBytes());
        String encryptedvalue = Base64.encodeToString(encval, Base64.DEFAULT);
        return encryptedvalue;
    }

    public SecretKeySpec generateKey() throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = encryptpassword.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    public void sendnotii(Context contxt, String usrtid, String message, String type, String postid,String hashid,String what) {


        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel udell = snapshot.child(usrtid).getValue(usermodel.class);
                usermodel currusrmodel = snapshot.child(FirebaseAuth.getInstance().getUid()).getValue(usermodel.class);
                if (udell != null) {

                    String usertkn = udell.getDevicetoken();

                    Data data = new Data(currusrmodel.getUsername(), message, type, FirebaseAuth.getInstance().getUid(), udell.getImageurl(), usrtid, postid,hashid.hashCode());
                    if (what!=null){
                        data = new Data("", "", "delete", FirebaseAuth.getInstance().getCurrentUser().getUid(), "", usrtid, "",hashid.hashCode());

                    }
                    NotificationSender sender = new NotificationSender(data, usertkn);
                    Toast.makeText(contxt, ""+hashid.hashCode(), Toast.LENGTH_SHORT).show();
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success != 1) {
                                    Toast.makeText(contxt, response.errorBody()+"", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(contxt, "Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<MyResponse> call, Throwable t) {
                            Toast.makeText(contxt, t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String nextprevioustime(String timetochange, Integer minutes) {
        SimpleDateFormat formatter6 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Calendar nxtdate = Calendar.getInstance();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                nxtdate.setTime(formatter6.parse(timetochange));
                nxtdate.add(Calendar.MINUTE, minutes);// number of days to add
                return formatter6.format(nxtdate.getTime());
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "error";
    }

    public boolean isold(String checktime, Integer mintues) {
        SimpleDateFormat formatter6 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Calendar pstdate = Calendar.getInstance();
            Calendar nxtdate = Calendar.getInstance();

            String ddd = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ddd = effecy.instance.gettime();

                pstdate.setTime(formatter6.parse(checktime));
                nxtdate.setTime(formatter6.parse(ddd));
                Calendar or = pstdate;
                pstdate.add(Calendar.MINUTE, mintues);// number of days to add
                if (pstdate.getTime().before(nxtdate.getTime())) {

                    return true;

                } else {
                    return false;

                }
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String compress(Uri imageuri, Context context, ImageView iv) {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageuri);
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            String filename = String.format("%d.jpg", System.currentTimeMillis());
            File finalFile = new File(path, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(finalFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 1, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
//            Picasso.get().load(Uri.fromFile(finalFile)).into(iv);
//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            intent.setData(Uri.fromFile(finalFile));
//            context.sendBroadcast(intent);

            return Uri.fromFile(finalFile) + "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "no";
    }

    public void postexist(postmodel postdel, ImageView iv) {
        FirebaseDatabase.getInstance().getReference().child("info").child(postdel.getPublisherid()).child("posts")
                .child(postdel.getPostid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            iv.setTag("yes");
                        } else {
                            iv.setTag("no");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void commentexist(postmodel postdel, commentsmodel commentdel, ImageView iv) {
        FirebaseDatabase.getInstance().getReference().child("info").child(postdel.getPublisherid()).child("comments")
                .child(postdel.getPostid()).child(commentdel.getCommentid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            iv.setTag("yes");
                        } else {
                            iv.setTag("no");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void notifyotherexist(notifymodel notidel, ImageView iv,String wh) {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("notify").child(wh).child(notidel.getTime().substring(0,10)).child(notidel.getNotifyid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            iv.setTag("yes");
                        }else{
                            iv.setTag("no");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void notifyreqexist(notifymodel notidel, ImageView iv) {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("notify").child("requests").child(notidel.getNotifyid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            iv.setTag("yes");
                        }else{
                            iv.setTag("no");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void onlinesta(String userid,String showlastseen,TextView line,boolean issettext){
        FirebaseDatabase.getInstance().getReference().child("vary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot st : snapshot.getChildren()) {
                    if (st.getKey().equals(userid)) {
                        varymodel vydel = st.getValue(varymodel.class);
                        if(showlastseen.equals("true")) {
                            if (issettext) {
                                line.setText(vydel.getLine());
                            }else{
                                line.setTag(vydel.getLine());
                            }
                        }else{
                            if(vydel.getLine().equals("online")){
                                if (issettext) {
                                    line.setText(vydel.getLine());
                                }else{
                                    line.setTag(vydel.getLine());
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void layoutanime(RecyclerView recy, int layout){
        Context context = recy.getContext();
        LayoutAnimationController layoutAnimationController
                = AnimationUtils.loadLayoutAnimation(context,layout);
        recy.setLayoutAnimation(layoutAnimationController);
        recy.getAdapter().notifyDataSetChanged();
        recy.scheduleLayoutAnimation();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setoffline(){
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue(effecy.instance.gettime());
    }
    public void setonline(){
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");

    }

}

