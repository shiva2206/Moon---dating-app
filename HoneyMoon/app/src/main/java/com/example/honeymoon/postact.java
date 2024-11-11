package com.example.honeymoon;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
//import com.hendraanggrian.appcompat.widget.SocialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.effecy;
import adapters.tagadp;
import array_adapters.mentionadp;
import model.usermodel;
import utility.NetworkChangeListener;


public class postact extends AppCompatActivity {
    private ImageView iv, close;
    public List<usermodel> alstuser;
    private EditText soedts;
    private TextView post;
    private Uri uri;
    private VideoView vv;
    private String type;
    private tagadp tgadp;
    private effecy cl;
    private AutoCompleteTextView actv;
    private List<usermodel> usrlst, menuserlst, alluserdellst;
    private List<String> usernmelst, selectedlst;
    private mentionadp mentadp;
    private RecyclerView tgrecy;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_postact);

        iv = findViewById(R.id.imm);
        vv = findViewById(R.id.ivv);
        close = findViewById(R.id.clo);
        soedts = findViewById(R.id.soci);
        close = findViewById(R.id.clo);
        post = findViewById(R.id.pst);
        actv = findViewById(R.id.sactv);
        tgrecy = findViewById(R.id.tgecy);


        usrlst = new ArrayList<>();
        alstuser = new ArrayList<>();
        menuserlst = new ArrayList<>();
        usernmelst = new ArrayList<>();
        selectedlst = new ArrayList<>();
        alluserdellst = new ArrayList<>();


        Intent uy = getIntent();
        type = uy.getStringExtra("type");
        usrlst = new ArrayList<>();

        if (!type.equals("image/*")) {
            iv.setVisibility(View.GONE);
            vv.setVisibility(View.VISIBLE);
        }
        Intent iny = new Intent(Intent.ACTION_PICK);
        iny.setType(type);
        startActivityForResult(iny, 1);

//        cl = new effecy(postact.this);


        tgadp = new tagadp(postact.this, selectedlst, menuserlst, alluserdellst);
        tgrecy.setAdapter(tgadp);
        tgrecy.setLayoutManager(new LinearLayoutManager(postact.this));
        mentadp = new mentionadp(postact.this,alluserdellst);
        actv.setAdapter(mentadp);
//        actv.showDropDown();
        getusername();

//        cl=new effecy(postact.this);
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (usernmelst.contains(s + "") && !selectedlst.contains(s + "")) {
                    selectedlst.add(s + "");

                    actv.setText("");
                } else if (selectedlst.contains(s + "")) {
                    Toast.makeText(postact.this, "already tagged", Toast.LENGTH_SHORT).show();
                }


                tgadp.notifyDataSetChanged();
                System.out.println(usernmelst.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

//        addd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent inte = new Intent(postact.this,posttags.class);
//                startActivity(inte);
//            }
//        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                soedts.setEnabled(false);
                post.setEnabled(false);
                if (uri != null) {
                    post.setEnabled(false);
                    DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
                    String key = dataref.push().getKey();
                    String ddat = effecy.instance.gettime();
                    StorageReference sto = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("posts").child(key);
                    StorageTask tak = sto.putFile(uri);
                    tak.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return sto.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            HashMap<String, String> mp = new HashMap<>();
                            HashMap<String, String> tg = new HashMap<>();
                            mp.put("publisherid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mp.put("postid", key);
                            mp.put("description", soedts.getText().toString());
                            mp.put("time", ddat);
                            mp.put("uri", task.getResult().toString());
                            mp.put("location", "");
                            mp.put("type", type.substring(0, 5));
                            dataref.child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts")
                                    .child(key).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(postact.this, "Successful", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(postact.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            for (usermodel userdel : tgadp.getUsrmdelst()) {
                                tg.put(userdel.getUserid(), ddat.substring(0, 10));
                            }
                            dataref.child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("posts").child(key).child("tags").setValue(tg);


//                            for(String i : soedts.getHashtags()){
//                                dataref.child("hashs").child(i).child(key).setValue(mp);
//
//                            }
                            for (usermodel userdel : tgadp.getUsrmdelst()) {
                                FirebaseDatabase.getInstance().getReference().child("info").child(userdel.getUserid()).child("tags")
                                        .child(key).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());


                                HashMap mpp = new HashMap();
                                mpp.put("subject", "tag");
                                mpp.put("notify", " ");
                                mpp.put("postid", key);
                                mpp.put("publisherid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mpp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mpp.put("notifyid", key);
                                mpp.put("time", ddat.substring(11));

                                FirebaseDatabase.getInstance().getReference().child("info").child(userdel.getUserid()).child("notify").child("others")
                                        .child(ddat.substring(0, 10)).child(key).setValue(mpp);
                                FirebaseDatabase.getInstance().getReference().child("info").child(userdel.getUserid()).child("tags").child(key)
                                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                effecy.instance.sendnotii(postact.this, userdel.getUserid(), "You are Tagged in a Post", "tag",key,key,null);
                            }
                        }
                    });
                    finish();
                }
                post.setEnabled(true);
                soedts.setEnabled(true);
            }
        });

//        getallque("");

    }

    public void getusername() {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernmelst.clear();
                alluserdellst.clear();
                for (DataSnapshot dss : snapshot.getChildren()) {
                    usermodel udel = dss.getValue(usermodel.class);
                    alluserdellst.add(udel);

                    if (!udel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && udel.getIstagpermitted().equals("true")) {
                        usernmelst.add(udel.getUsername());

                    }
                }
                mentadp.notifyDataSetChanged();
                tgadp.notifyDataSetChanged();
//                Toast.makeText(postact.this, alluserdellst.size()+" "+usernmelst.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String filextension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            uri = data.getData();
            if (type.equals("image/*")) {
                try {
                    Picasso.get().load(uri).into(iv);
                } catch (Exception e) {
                    Picasso.get().load(effecy.instance.compress(uri, postact.this, iv)).into(iv);
                }

            } else {
                vv.setVideoURI(uri);
            }
        } else {
            finish();
        }
    }

    public void getallque(String st) {
        Query que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(st).endAt(st + "\uf8ff");
        que.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usrlst.clear();
                for (DataSnapshot sh : snapshot.getChildren()) {
                    usermodel udel = sh.getValue(usermodel.class);
                    if (!alstuser.contains(udel)) {
                        usrlst.add(udel);
                    }

                }
//                menadp = new mentionadp(postact.this,R.layout.adap_search_two,usrlst,alstuser);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
//        cl = new effecy(postact.this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

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