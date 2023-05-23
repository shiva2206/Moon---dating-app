package com.example.honeymoon;

import static android.view.View.GONE;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import adapters.effecy;
import model.usermodel;
import utility.NetworkChangeListener;

public class attach extends AppCompatActivity {

    private ImageView prof, send, img;
    private TextView username;
    private EditText mess;
    private VideoView vvs;
    private String type,userid;
    private Uri uri;

    private String pushid;
    private effecy cl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_attach);

        prof=findViewById(R.id.chatpro);
        send=findViewById(R.id.chasend);
        img=findViewById(R.id.attimg);
        mess=findViewById(R.id.chatmes);
        vvs=findViewById(R.id.attvv);
        username=findViewById(R.id.chatusernme);



        Intent inte = getIntent();
        type = inte.getStringExtra("type");
        userid=inte.getStringExtra("userid");


//        cl=new effecy(attach.this);
        if (!type.equals("image/*")) {
            vvs.setVisibility(View.VISIBLE);
            img.setVisibility(GONE);
        }else{
            img.setVisibility(View.VISIBLE);
            vvs.setVisibility(GONE);
        }

        Intent iny = new Intent(Intent.ACTION_PICK);
        iny.setType(type);
        startActivityForResult(iny, 1);

        getprod();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send.setEnabled(false);
                mess.setEnabled(false);
                if(uri!=null) {
                    StorageReference sto = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("chats").child(userid).child(pushid);
                    StorageTask tak = sto.putFile(uri);

                    tak.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            } else {
                                return sto.getDownloadUrl();
                            }
                        }
                    }).addOnCompleteListener(new OnCompleteListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdfff = new SimpleDateFormat("HH:mm:ss");


                            DatabaseReference sef = FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("chats")
                                    .child(userid);

                            HashMap<String, Object> vp = new HashMap<>();
                            vp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            vp.put("chatid", pushid);
                            vp.put("uri",task.getResult().toString());
                            vp.put("chat", mess.getText().toString());
                            vp.put("liked","false");
                            vp.put("type", type.substring(0, 5));
                            vp.put("time", sdfff.format(cal.getTime()));
                            sef.child(effecy.instance.gettime().substring(0, 10)).child(pushid).setValue(vp);

                            HashMap<String, Object> mp = new HashMap<>();
                            mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mp.put("chatid", pushid);
                            mp.put("uri",task.getResult().toString());
                            mp.put("chat", mess.getText().toString());
                            mp.put("liked","false");
                            mp.put("type", type.substring(0, 5));
                            mp.put("time", sdfff.format(cal.getTime()));
                            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("chats")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(effecy.instance.gettime().substring(0, 10))
                                    .child(pushid).setValue(mp);

                        }
                    });
                }
                mess.setEnabled(true);
                send.setEnabled(true);
                finish();
            }
        });

    }
    public void getprod(){
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot dt : snapshot.getChildren()){
                        if(dt.getKey().equals(userid)){
                            usermodel udmo = dt.getValue(usermodel.class);
                            Picasso.get().load(udmo.getImageurl()).placeholder(R.drawable.logo_background).into(prof);
                            username.setText(udmo.getUsername());

                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String filextension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            uri = data.getData();
//
            Picasso.get().load(uri).into(img);
            if(type != "image/*"){

                MediaController mcon = new MediaController(attach.this);
                mcon.setAnchorView(vvs);
                vvs.setMediaController(mcon);
                vvs.setVideoURI(uri);
            }else{
                Picasso.get().load(uri).into(img);
            }
            DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
            pushid = dataref.push().getKey();
//            StorageReference sto = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .child("chats").child(userid).child(pushid);
//            StorageTask tak = sto.putFile(uri);
//            tak.continueWithTask(new Continuation() {
//                @Override
//                public Object then(@NonNull Task task) throws Exception {
//                    if(!task.isSuccessful()){
//                        throw task.getException();
//                    }
//                    return sto.getDownloadUrl();
//                }
//            });
//            if (type.equals("image/*")) {
//                img.setImageURI(uri);
//            } else {
//                vvs.setVideoURI(uri);
//            }

        } else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

//        cl = new effecy(attach.this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue(DateFormat.getDateTimeInstance().format(new Date()));
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }
}