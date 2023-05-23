package com.example.honeymoon;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import utility.NetworkChangeListener;

public class forgot extends AppCompatActivity {
    private EditText eml;
    private Button send;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_forgot);
        
        eml=findViewById(R.id.eml);
        send=findViewById(R.id.send);
        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eml.setEnabled(false);
                send.setEnabled(false);
                if(!TextUtils.isEmpty(eml.getText())) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(eml.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(forgot.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            send.setText("");
                            Toast.makeText(forgot.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(forgot.this, "Email given is empty", Toast.LENGTH_SHORT).show();
                }
                send.setEnabled(true);
                eml.setEnabled(true);
                
            }
        });

    }

    @Override
    protected void onResume() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);

        super.onResume();

    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeListener);
        super.onPause();
    }
}