package com.example.honeymoon;

import static android.view.View.GONE;
import static adapters.effecy.instance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import adapters.effecy;
import adapters.imageadp;
import model.highmodel;
import model.statusmodel;
import utility.NetworkChangeListener;

public class addhighlight extends AppCompatActivity {

    private TextView add,textu;
    private List<String> stalst,dalst,funstalst,fundalst;
    private List<statusmodel> stamdelst;
    private ImageView img,remm;
    private EditText ttle;
    private Uri uri;
    private effecy cl;
    private ViewPager2 viewp;
    private imageadp adp;
    private String from,highid;
    private Integer pos=0;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_addhighlight);

        ttle=findViewById(R.id.ttil);
        add=findViewById(R.id.adhigh);
        img=findViewById(R.id.aadimg);
        viewp=findViewById(R.id.allsta);
        textu=findViewById(R.id.textu);
        remm=findViewById(R.id.remm);

        stalst = new ArrayList<>();
        dalst = new ArrayList<>();
        funstalst=new ArrayList<>();
        fundalst=new ArrayList<>();
        stamdelst=new ArrayList<>();


//        cl = new effecy(addhighlight.this);
        adp=new imageadp(addhighlight.this,stamdelst);

        Bundle bundle = getIntent().getExtras();
        Set<String> bundleKeySet = bundle.keySet(); // string key set
        for(String key : bundleKeySet) { // traverse and print pairs
            if (key.equals("from")){
                from = bundle.get(key)+"";
            }else if(key.equals("highid")){
                highid= bundle.get(key)+"";
            }else{
                funstalst.add(key);
                fundalst.add(bundle.get(key).toString());
            }
        }
        dalst=effecy.instance.getdalst(fundalst);
        Toast.makeText(this, effecy.instance.getdalst(fundalst).size()+"", Toast.LENGTH_SHORT).show();
        for(String da : dalst) {
            for (int q = 0; q < dalst.size(); q++) {
                if (da.equals(fundalst.get(q))){
                    stalst.add(funstalst.get(q));
                    break;
                }
            }
        }
//        effecy eff = new effecy(addhighlight.this);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setEnabled(false);
                Intent ik = new Intent(Intent.ACTION_PICK);
                ik.setType("image/*");
                startActivityForResult(ik,1);
                img.setEnabled(true);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setEnabled(false);
                ttle.setEnabled(false);
                remm.setVisibility(GONE);
                if(from.equals("add")) {
                    if (img.getTag() != null && img.getTag().equals("ok")) {
                        add.setVisibility(GONE);
                        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference().child("info");
                        String key = dataref.push().getKey();
                        StorageReference sto = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("highlights").child(key);
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
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task task) {
                                HashMap mp = new HashMap();
                                if (TextUtils.isEmpty(ttle.getText().toString())) {
                                    mp.put("title", "highlights");
                                } else {
                                    mp.put("title", ttle.getText().toString().trim());
                                }
                                mp.put("imageuri", task.getResult().toString());
                                mp.put("highlightid", key);
                                mp.put("time", instance.gettime());


                                dataref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                        .child(key).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(addhighlight.this, "Successful", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                for (int r = 0; r < stalst.size(); r++) {

                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                            .child(key).child("statusmodelist").child(stalst.get(r)).setValue(dalst.get(r).substring(0, 10));

                                }
                                finish();
                            }
                        });

                    } else {
                        Toast.makeText(addhighlight.this, "Set image", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (img.getTag() != null && img.getTag().equals("ok")){
                        FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                .child(highid).delete();
                        StorageReference sto = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("highlights").child(highid);
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
                                FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("highlights").child(highid).child("imageuri").setValue(task.getResult().toString());
                            }
                        });
                    }

                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("highlights").child(highid).child("title").setValue(ttle.getText().toString().trim());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("highlights").child(highid).child("time").setValue(instance.gettime());
                    }
//                    Toast.makeText(addhighlight.this, "Successfully Done", Toast.LENGTH_SHORT).show();
                    finish();
                }
                remm.setVisibility(View.VISIBLE);
                ttle.setEnabled(true);
                add.setEnabled(true);
            }
        });
        remm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remm.setEnabled(false);
                AlertDialog.Builder alt = new AlertDialog.Builder(addhighlight.this);
                alt.setTitle("Remove !").setMessage("Do you want to remove this From the Highlight")
                        .setCancelable(true)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (from.equals("add")) {
                                    dalst.remove(0+pos);
                                    stalst.remove(0+pos);
//                                    stamdelst.clear();
                                    getstaa();
                                }else{

                                    if(adp.getStalst().size() == 1){
                                        FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights")
                                                .child(highid).delete();
                                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("highlights").child(highid).removeValue();
                                        finish();
                                    }else {
                                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("highlights").child(highid).child("statusmodelist").child(stamdelst.get(pos).getStatusid()).removeValue();

                                    }
                                }
                            }
                        }).show();
                remm.setEnabled(true);
            }
        });

        viewp.setAdapter(adp);


        viewp.canScrollVertically(100);
        viewp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pos=position;

            }
        });
        if(from.equals("add")){
            textu.setText("ADD HIGHLIGHT");
            getstaa();
        }else{
            textu.setText("EDIT HIGHLIGHT");
            gethigh();
        }

    }
    public void gethigh(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("highlights").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stalst.clear();
                dalst.clear();
                for(DataSnapshot df : snapshot.getChildren()){
                    if(df.getKey().equals(highid)){
                        highmodel ghb = df.getValue(highmodel.class);
                        ttle.setText(ghb.getTitle());
                        Picasso.get().load(ghb.getImageuri()).into(img);
                        if(ghb.getStatusmodelist() != null) {

                            for (String sta : ghb.getStatusmodelist().keySet()) {
//                                stalst.add(sta);

                                stalst.add(sta);
                                dalst.add(ghb.getStatusmodelist().get(sta));

                            }
                        }


                    }

                }
                getstatmdel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getstatmdel(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("allstories")
                .addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        stamdelst.clear();
                        List<String> orddalst = new ArrayList<>();
                        List<statusmodel>  unordstalst = new ArrayList<>();
                        for(int u = 0 ; u< stalst.size();u++) {
                            statusmodel stdel = snapshot.child(dalst.get(u)).child(stalst.get(u)).getValue(statusmodel.class);
                            if (stdel != null) {
                                unordstalst.add(stdel);
                                orddalst.add(stdel.getTime());

                            }
                        }
                        for(String das : effecy.instance.getdalst(orddalst)){
                            for(statusmodel stdell : unordstalst){
                                if(stdell.getTime().equals(das) && !stalst.contains(stdell)){
                                    stamdelst.add(stdell);
                                    break;
                                }
                            }
                        }

                        adp.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void getstaa(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("allstories").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        stamdelst.clear();
                        for(int y=0;y<dalst.size();y++){
                            for(DataSnapshot dsp : snapshot.child(dalst.get(y).substring(0,10)).getChildren()){
                                statusmodel stdel = dsp.getValue(statusmodel.class);
                                if(stdel.getStatusid().equals(stalst.get(y))){
                                    stamdelst.add(stdel);
                                }
                            }
                        }
                        if (stamdelst.size()==0){
                            finish();
                        }
                        adp.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            uri=data.getData();
            img.setImageURI(uri);
            img.setTag("ok");
        }
    }
    private  String filextension(Uri uri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }


    @Override
    protected void onResume() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue("online");
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

//        cl = new effecy(addhighlight.this);
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        FirebaseDatabase.getInstance().getReference().child("vary").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("line").setValue(instance.gettime());
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }


}