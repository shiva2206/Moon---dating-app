package com.example.honeymoon;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import adapters.effecy;
import utility.NetworkChangeListener;

public class addstory extends AppCompatActivity {

    public static final int PICK_IMAGE_CODE = 100;
    public static final int DS_PHOTO_EDITOR_REQUEST_CODE = 200;

    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 1000;

    public static final String OUTPUT_PHOTO_DIRECTORY = "ds_photo_editor_sample";

    private ImageView imageView;
    private Button pre;
    private effecy cl;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstory);
        imageView = findViewById(R.id.mainImageView);
        pre=findViewById(R.id.openGalleryButton);


        verifyStoragePermissionsAndPerformOperation(REQUEST_EXTERNAL_STORAGE_CODE);


//        cl = new effecy(addstory.this);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                verifyStoragePermissionsAndPerformOperation(REQUEST_EXTERNAL_STORAGE_CODE);
            }
        });


//        imgv=findViewById();

    }
    private void verifyStoragePermissionsAndPerformOperation(int requestPermissionCode) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_CODE);
        } else {
            // Check if we have storage permission
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // Request the permission.
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, requestPermissionCode);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_CODE);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("The app needs this permission to edit photos on your device.");
            builder.setPositiveButton("Update Permission",  new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    verifyStoragePermissionsAndPerformOperation(REQUEST_EXTERNAL_STORAGE_CODE);
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_CODE:
                    Uri inputImageUri = data.getData();
                    if (inputImageUri != null) {
                        Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
                        dsPhotoEditorIntent.setData(inputImageUri);

                        // This is optional. By providing an output directory, the edited photo
                        // will be saved in the specified folder on your device's external storage;
                        // If this is omitted, the edited photo will be saved to a folder
                        // named "DS_Photo_Editor" by default.
                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, OUTPUT_PHOTO_DIRECTORY);

                        // You can also hide some tools you don't need as below
//                        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_ORIENTATION};
//                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                        startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQUEST_CODE);
                    } else {
                        Toast.makeText(this, "Please select an image from the Gallery", Toast.LENGTH_LONG).show();
                    }
                    break;
                case DS_PHOTO_EDITOR_REQUEST_CODE:
                    Uri uri = data.getData();
                    imageView.setImageURI(uri);
                    DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
                    String key = dataref.push().getKey();
                    StorageReference sto = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("story").child(key);
                    StorageTask tak = sto.putFile(uri);
                    tak.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return sto.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task task) {

                            DatabaseReference def = FirebaseDatabase.getInstance().getReference().child("info")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            HashMap<String,String> mp = new HashMap<>();
                            mp.put("statusid",key);
                            mp.put("publisherid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mp.put("type","image");
                            mp.put("uri",task.getResult().toString());
                            mp.put("time",effecy.instance.gettime());
                            def.child("story").child(key).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addstory.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(addstory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    });

                    Toast.makeText(this, "Story added and Photo saved in " + OUTPUT_PHOTO_DIRECTORY + " folder.", Toast.LENGTH_LONG).show();
                    break;
            }
        }else{
            finish();
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

//        cl = new effecy(addstory.this);
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