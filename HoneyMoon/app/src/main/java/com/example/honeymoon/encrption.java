package com.example.honeymoon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class encrption extends AppCompatActivity {

    EditText inputtext,inputpassword;
    TextView outputtext;
    Button encbtn,decbtn;
    String outputstr;
    String AES="AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrption);

        inputtext=findViewById(R.id.inputext);
        inputpassword=findViewById(R.id.password);

        outputtext=findViewById(R.id.outputext);
        encbtn=findViewById(R.id.encbtn);
        decbtn=findViewById(R.id.decbtn);

        encbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputstr = encrypt(inputtext.getText().toString(),inputpassword.getText().toString());
                    outputtext.setText(outputstr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputstr = decrypt(outputstr,inputpassword.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                outputtext.setText(outputstr);
            }
        });
    }
    private String decrypt(String outputstr,String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c =Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedvalue = Base64.decode(outputstr,Base64.DEFAULT);
        byte[] decvalue = c.doFinal(decodedvalue);
        String decryptedvalue = new String(decvalue);
        return decryptedvalue;
    }
    private String encrypt(String Data,String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c =Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encval = c.doFinal(Data.getBytes());
        String encryptedvalue = Base64.encodeToString(encval,Base64.DEFAULT);
        return encryptedvalue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

}