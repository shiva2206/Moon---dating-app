//package com.example.honeymoon;
//
//import static android.view.View.GONE;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//import androidx.preference.PreferenceManager;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//
//import org.jitsi.meet.sdk.JitsiMeetActivity;
//import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
//
//import java.net.URL;
//
//import adapters.effecy;
//
//public class jitsiaudvidcall extends AppCompatActivity {
//
//    private Button call,dec,pic,rej;
//    private ImageView dp;
//    private TextView usrnme;
//    private String what,token;
//    private Intent getintet;
//    private String userid,type;
//
//    private PreferenceManager preferenceManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jitsiaudvidcall);
//
//        dp=findViewById(R.id.callpro);
//        call=findViewById(R.id.call);
//        dec=  findViewById(R.id.decl);
//        pic=findViewById(R.id.pick);
//        rej=findViewById(R.id.reject);
//
//        getintet = getIntent();
//        what = getintet.getStringExtra("from");
//        userid=getintet.getStringExtra("userid");
//        type=getintet.getStringExtra("type");
//
//        if(what.equals("in")){
//
//            startjitsi();
//            dec.setVisibility(GONE);
//            call.setVisibility(GONE);
//
//            pic.setVisibility(View.VISIBLE);
//            rej.setVisibility(View.VISIBLE);
//
//        }else{
//
////            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
////                @Override
////                public void onComplete(@NonNull Task<InstanceIdResult> task) {
////                        if(task.isSuccessful() && task.getResult()!=task){
////                            token=task.getResult().getToken();
////                        }
////                }
////            });
//            token= FirebaseInstanceId.getInstance().getToken();
//            if(token!=null) {
//                effecy.instance.sendnotii(this, userid, "call", type, null, token, null);
//            }
//            dec.setVisibility(View.VISIBLE);
//            call.setVisibility(View.VISIBLE);
//
//            pic.setVisibility(GONE);
//            rej.setVisibility(GONE);
//
//        }
//
//        pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pic.setVisibility(GONE);
//                rej.setVisibility(GONE);
//                dec.setVisibility(View.VISIBLE);
//
//            }
//        });
//        rej.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        dec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//    public void startjitsi(){
//        try {
//            URL serverurl = new URL("https://meet.jit.si");
//
//            JitsiMeetConferenceOptions.Builder build = new JitsiMeetConferenceOptions.Builder();
//            build.setServerURL(serverurl);
////            build.setWelcomePageEnabled(false);
//            build.setRoom(getIntent().getStringExtra("meeting room"));
//            if(type.equals("audio")){
//                build.setVideoMuted(true);
//            }
//
////            JitsiMeetConferenceOptions conferenceOptions =
////                    new JitsiMeetConferenceOptions.Builder()
////                            .setServerURL(serverurl)
////                            .setWelcomePageEnabled(false)
////                            .setRoom(getIntent().getStringExtra("meeting room"))
////                            .build();
////            JitsiMeetActivity.launch(this, conferenceOptions);
//            JitsiMeetActivity.launch(this, build.build());
//            finish();
//        }catch (Exception e){
//            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//    private BroadcastReceiver invitaresponcereceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String type = intent.getStringExtra("responce");
//            if(type!=null){
//                if(type.equals("Accepted")){
//                    startjitsi();
//                }else{
//
//                }
//            }
//        }
//    };
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(what.equals("out")){
//            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
//                    invitaresponcereceiver,new IntentFilter("responce")
//            );
//        }else{
//
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(what.equals("out")){
//            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
//                    invitaresponcereceiver
//            );
//        }
//    }
//}