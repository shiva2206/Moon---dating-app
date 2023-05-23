package com.example.honeymoon;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.util.List;

import adapters.effecy;
import model.usermodel;
import utility.NetworkChangeListener;

public class call extends AppCompatActivity {

    private Call call;
    private TextView callState,usrnme;
    private ImageView prof;
//    private SinchClient sinchClient;
    private Button button,pick,reject;
    private String callerId;
    private String recipientId;
//    private AudioPlayer maudplay;
    private Chronometer chrometr;
    public static final String APP_KEY = "a8784d8a-1b12-4751-8983-61ef5a7b8a8b";
    public static final String APP_SECRET = "EfVrYOTke0eoAtHxbLC9Ew==";
    public static final String ENVIRONMENT = "clientapi.sinch.com";

    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_call);
        if (ContextCompat.checkSelfPermission(call.this,
                android.Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission
                (call.this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(call.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        usrnme = findViewById(R.id.callusername);
        prof = findViewById(R.id.callpro);
        pick=findViewById(R.id.pick);
        reject=findViewById(R.id.reject);
        chrometr=findViewById(R.id.chrm);
        Intent inte = getIntent();
        recipientId = inte.getStringExtra("userid");
        callerId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        SinchClient sinchClient = Sinch.getSinchClientBuilder().context(this)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .userId(callerId)
                .enableVideoCalls(true)
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener((CallClientListener) new SinchCallClientListener());
//        sinchClient.getCallClient().addVideo

        button = (Button) findViewById(R.id.button);
        callState = (TextView) findViewById(R.id.callState);

        getuser();
        if (inte.getStringExtra("tocall")!=null){
//            call = sinchClient.getCallClient().callUser(recipientId);
//            call.addCallListener(new SinchCallListener());
////            effecy.instance.sendnotii(call.this,recipientId,"call","call",null);
//            button.setText("Hang Up");
            button.setVisibility(View.VISIBLE);
            pick.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
        }else{
            pick.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);

        }
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                if (call == null) {
                    call = sinchClient.getCallClient().callUser(recipientId);
                    call.addCallListener(new SinchCallListener());
                    button.setText("Hang Up");
                } else {
                    call.hangup();
                    button.setVisibility(View.GONE);
                }
                button.setEnabled(true);
            }
        });

    }
    public void getuser(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel udel = snapshot.child(recipientId).getValue(usermodel.class);
                Picasso.get().load(udel.getImageurl()).into(prof);
                usrnme.setText(udel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private class SinchCallListener implements CallListener {

        public SinchCallListener() {
        }

        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            Toast.makeText(call.this, a+"", Toast.LENGTH_SHORT).show();
//            if ((button.getText()+"").equals("ringing")){
//
//            }else{
//
//            }
            button.setText("Call");
            callState.setText("");

            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
//            effecy.instance.sendnotii(call.this,recipientId,null,null,null,ky,null);

        }

        @Override
        public void onCallEstablished(Call establishedCall) {

            callState.setText("connected");
            chrometr.start();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

//            effecy.instance.sendnotii(call.this,recipientId,null,null,null,ky,null);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            callState.setText("ringing");
//            String datme = effecy.instance.gettime();
//            String ky = FirebaseDatabase.getInstance().getReference().push().getKey();
//
//            HashMap<String, Object> mp = new HashMap<>();
//            mp.put("userid", recipientId);
//            mp.put("going", "out");
//            mp.put("type", "audio");
//            mp.put("time",datme.substring(11));
//            mp.put("type", "text");
//            mp.put("callid",ky);
//            FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .child("calls").child(datme.substring(0,10)).child(ky).setValue(mp);
//            mp.put("going","in");
//            mp.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
//            FirebaseDatabase.getInstance().getReference().child("info").child(recipientId)
//                    .child("calls").child(datme.substring(0,10)).child(ky).setValue(mp);
//            effecy.instance.sendnotii(call.this,recipientId,"call","call",null,ky,null);
//

        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            call.answer();
            call.addCallListener(new SinchCallListener());
            button.setText("Hang Up");
            button.setVisibility(View.VISIBLE);

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call.hangup();
                    call=null;
                }
            });

            pick.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ss : snapshot.getChildren()){

                            if(ss.getKey().equals(incomingCall.getRemoteUserId())){
                                usermodel caller = ss.getValue(usermodel.class);
//                                call = incomingCall;

//                                AlertDialog alertDialog = new AlertDialog.Builder(call.this).create();
//                                alertDialog.setTitle("InComing Call");
//
//
//                                alertDialog.setMessage(caller.getUsername());

//                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reject", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        call.hangup();
//                                        call=null;
//                                    }
//                                });

                                reject.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        call.hangup();
                                        call=null;
                                    }
                                });
//                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Pick", new DialogInterface.OnClickListener() {
//                                    @RequiresApi(api = Build.VERSION_CODES.O)
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        call.answer();
//                                        call.addCallListener(new SinchCallListener());
//                                        button.setText("Hang Up");
//                                    }
//                                });
//                                pick.setOnClickListener(new View.OnClickListener() {
//                                    @RequiresApi(api = Build.VERSION_CODES.O)
//                                    @Override
//                                    public void onClick(View v) {
//                                        call.answer();
//                                        call.addCallListener(new SinchCallListener());
//                                        button.setText("Hang Up");
//                                    }
//                                });

//                                alertDialog.show();
                                ;
                                pick.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onClick(View v) {
                                        call.answer();
                                        call.addCallListener(new SinchCallListener());
                                        button.setText("Hang Up");
                                        button.setVisibility(View.VISIBLE);
                                    }
                                });
                                reject.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        call.hangup();
                                        call=null;
                                    }
                                });
                            }

                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }

    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue(effecy.instance.gettime());
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }


}