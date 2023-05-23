package com.example.honeymoon;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import adapters.imageadp;
import model.chatmodel;
import utility.NetworkChangeListener;

public class viewchatmedia extends AppCompatActivity {
    private ViewPager2 vpag;
    private imageadp adp;
    private effecy cl;
    private ImageView menn;
    private String userid,chatid;
    private List<chatmodel> chlst;
    private List<String> dalst;
    private PopupMenu pmenu;
    private StorageReference storef;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_viewchatmedia);

        chlst =new ArrayList<>();
        dalst = new ArrayList<>();

        vpag=findViewById(R.id.sevie);
        menn=findViewById(R.id.menn);
        Intent inty = getIntent();
        userid = inty.getStringExtra("userid");
        chatid = inty.getStringExtra("chatid");

        adp = new imageadp(chlst,viewchatmedia.this,userid);
        vpag.setAdapter(adp);
        getdt();

        vpag.canScrollVertically(100);
        vpag.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

        menn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menn.setEnabled(false);
                pmenu = new PopupMenu(viewchatmedia.this,menn);
                pmenu.getMenuInflater().inflate(R.menu.asvefil,pmenu.getMenu());

//                pmenu.getMenu().removeItem(R.id.saev);
                pmenu.getMenu().removeItem(R.id.dell);
                pmenu.getMenu().removeItem(R.id.delforme);
                pmenu.getMenu().removeItem(R.id.seninf);

                pmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        chatmodel chdel = adp.getChlst().get(vpag.getCurrentItem());
                        switch (item.getItemId()){

                            case R.id.saev:
                                if(chdel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    storef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("chats").child(userid).child(chdel.getChatid());
                                }else{
                                    storef = FirebaseStorage.getInstance().getReference(userid)
                                            .child("chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(chdel.getChatid());
                                }
                                storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        downloadfile(viewchatmedia.this,chdel.getTime(),".pdf",DIRECTORY_DOWNLOADS,url);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(viewchatmedia.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            default:
                                break;

                        }
                        return false;
                    }
                });
                pmenu.show();
                menn.setEnabled(true);
            }
        });


    }
    public void downloadfile(Context context, String filename, String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,filename);

        downloadManager.enqueue(request);
        Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();


    }

    //    public void getdate() {
//        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("chats").child(userid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                dalst.clear();
//
//                List<String> sumlst = new ArrayList();
//                for(DataSnapshot dsn : snapshot.getChildren()){
//                    sumlst.add(dsn.getKey()+" 00:00:00");
//                }
//
//                dalst= effecy.instance.getdalst(sumlst);
//                Collections.reverse(dalst);
////                    if(isnew){
////                        if(!dalst.get(0).equals(DateFormat.getDateTimeInstance().format(new Date()).substring(0, 11))) {
////                            Intent it = new Intent(chats.this, chats.class);
////                            it.putExtra("datelist", (Serializable) dalst);
////                            stack.clear();
////                            it.putExtra("stack", (Serializable) stack);
////                            it.putExtra("userid", userid);
////                            startActivity(it);
////                            finish();
////                        }
////                    }
//
//                getallchts();
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        });
//
//    }
    public void getdt(){
    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .child("chats").child(userid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            chlst.clear();
            List<String> dalst = new ArrayList<>();
            List<chatmodel> chhhlst = new ArrayList<>();
            for(DataSnapshot dss : snapshot.getChildren()){
                for(DataSnapshot qwe : snapshot.child(dss.getKey()).getChildren()){
                    chatmodel chdel = qwe.getValue(chatmodel.class);
                    if(chdel.getType().equals("image") || chdel.getType().equals("video")) {
                        chhhlst.add(chdel);
                        dalst.add(dss.getKey() + " " + chdel.getTime());
                    }
                }
            }

            for(String datt : effecy.instance.getdalst(dalst)){
                for(chatmodel chdel : chhhlst){
                    if(chdel.getTime().equals(datt.substring(11)) && !chlst.contains(chdel)){
                        chdel.setTime(datt);
                        chlst.add(chdel);
                    }
                }
            }
            adp.notifyDataSetChanged();


//            if(chlst.isEmpty()){
//                nn.setVisibility(View.VISIBLE);
//            }else{
//                nn.setVisibility(GONE);
//            }

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