package com.example.honeymoon;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import adapters.effecy;
import model.usermodel;
import utility.NetworkChangeListener;

public class login extends AppCompatActivity {
    private ImageView logo;
    private EditText email,password;
    private TextView forgot,create,word;
    Button login,facelgn;
    private RelativeLayout rt;
    private FirebaseAuth auth;
//    private searchadp adp;
//    private RecyclerView recy;
//    private List<usermodel> cdel;
    private static final String EMAIL = "email";
    FirebaseAuth mAuth;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_login);


        login=findViewById(R.id.register);
        facelgn=findViewById(R.id.login_button);
        logo=findViewById(R.id.logo);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        forgot=findViewById(R.id.forgot);
        create=findViewById(R.id.logn);
        word=findViewById(R.id.honeytext);
        rt=findViewById(R.id.relativeLayout);
//        recy=findViewById(R.id.recyyy);
        auth=FirebaseAuth.getInstance();
//        callbackManager = CallbackManager.Factory.create();


//        startActivity(new Intent(login.this,encrption.class));

        effecy sec= new effecy("sec");
//        cdel = new ArrayList<>();
//        adp = new searchadp(login.this,cdel,"login","none");
//        recy.setAdapter(adp);
//        recy.setLayoutManager(new LinearLayoutManager(login.this));
        login.setEnabled(true);
        if(getIntent().getStringExtra("kick") != null){
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(auth.getCurrentUser().getUid()).child("devicetoken").setValue("null").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            auth.signOut();
                        }
                    });

        }

        getSharedPreferences("profile",MODE_PRIVATE).getAll().clear();

        rt.setVisibility(View.INVISIBLE);
        TranslateAnimation trans = new TranslateAnimation(0,0,0,-2000);
        trans.setFillAfter(false);
        trans.setDuration(2000);
        trans.setAnimationListener(new ani());
        logo.setAnimation(trans);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setEnabled(false);
                password.setEnabled(false);
                login.setEnabled(false);
                getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("userid").commit();
                getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("email").commit();
                getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("password").commit();
                getSharedPreferences("profile",Context.MODE_PRIVATE).edit().remove("gender").commit();
                if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){

                    Toast.makeText(login.this, "email or password is empty", Toast.LENGTH_SHORT).show();

                }
                else{
                    auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {


                            if(auth.getCurrentUser().isEmailVerified()){
                                FirebaseDatabase.getInstance().getReference().child("users").child(auth.getCurrentUser().getUid())
                                        .child("verified").setValue("yes");
                                Intent intr = new Intent(login.this,Mainactivity.class);
//                                intr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        cdel.clear();
                                        for (DataSnapshot dt : snapshot.getChildren()) {
                                            if (dt.getKey().equals(authResult.getUser().getUid())) {


                                                usermodel uel = dt.getValue(usermodel.class);

//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("userid",udell.getUserid()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("password", udell.getPassword()).commit();
////                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("dob", udell.getDob()).commit();
////
////                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("about",udell.getAbout()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("email", udell.getEmail()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("gender", udell.getGender()).commit();
//                                                cdel.add(udell);

                                                System.out.println("login " + login.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("userid","none")
                                                + login.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("password","none")
                                                + login.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("email","none")
                                                        + login.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender","none"));

                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("userid",uel.getUserid()).commit();
                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("password", uel.getPassword()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("dob", udell.getDob()).commit();
//
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("about",udell.getAbout()).commit();
                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("email", uel.getEmail()).commit();
                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("gender", uel.getGender()).commit();
                                                System.out.println("im waiting");
                                                Intent intro = new Intent(login.this,Mainactivity.class);
                                                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(login.this,word, ViewCompat.getTransitionName(word));

                                                startActivity(intro,options.toBundle());


//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("verified", udell.getVerified()).commit();
//
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("username", udell.getUsername()).commit();
//
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("devicetoken",udell.getDevicetoken()).commit();
//
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("status",udell.getStatus()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("mode",udell.getMode()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlastseen",udell.getShowlastseen()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showlocation",udell.getShowlocation()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showmat",udell.getShowmat()).commit();
//
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showcru",udell.getShowcru()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showadm",udell.getShowadm()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showfri",udell.getShowfri()).commit();
//                                                getSharedPreferences("profile", MODE_PRIVATE).edit().putString("showaddcrubut",udell.getShowaddcrubut()).commit();


//                                                intr.putExtra("cuserid",udell.getUserid());
//                                                intr.putExtra("cpassword",udell.getPassword());
//                                                intr.putExtra("cdob",udell.getDob());
//                                                intr.putExtra("cabout",udell.getAbout());
//                                                intr.putExtra("cemail",udell.getEmail());
//                                                intr.putExtra("cgender",udell.getGender());
//                                                intr.putExtra("cmium",udell.getMium());
//                                                intr.putExtra("cimageurl",udell.getImageurl());
//                                                intr.putExtra("cname",udell.getName());
//                                                intr.putExtra("cstatus",udell.getStatus());
//                                                intr.putExtra("cline",udell.getLine());
//                                                intr.putExtra("cverified",udell.getVerified());
//                                                intr.putExtra("cusername",udell.getUsername());
//                                                intr.putExtra("cmode",udell.getMode());
//                                                intr.putExtra("cshowlastseen",udell.getShowlastseen());
//                                                intr.putExtra("cshowlocation",udell.getShowlocation());
//                                                intr.putExtra("ccoverimage",udell.getCoverimage());
//                                                intr.putExtra("clatitude",udell.getLatitude());
//                                                intr.putExtra("clongitude",udell.getLongitude());
//                                                intr.putExtra("cdevicetoken",udell.getDevicetoken());
//                                                intr.putExtra("cshowmat",udell.getShowmat());
//                                                intr.putExtra("cshowcru",udell.getShowcru());
//                                                intr.putExtra("cshowadm",udell.getShowadm());
//                                                intr.putExtra("cshowfri",udell.getShowfri());

                                            }
                                        }
//                                        adp.notifyDataSetChanged();

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//                                if(!login.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("gender","none").equals("none") ) {
//
//                                    login.setEnabled(false);
//                                    startActivity(intr);
//                                    ;
//                                }else{
//                                    Toast.makeText(login.this, "wait", Toast.LENGTH_SHORT).show();
//                                }


                            }else {
                                Toast.makeText(login.this, "verify your email first", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthException) {
                                FirebaseAuthException firebaseAuthException = (FirebaseAuthException) e;
                                String errorCode = firebaseAuthException.getErrorCode();
                                String errorMessage;
                                switch (errorCode) {
                                    case "ERROR_INVALID_CUSTOM_TOKEN":
                                        errorMessage = "The custom token format is incorrect.";
                                        break;
                                    case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                        errorMessage = "The custom token corresponds to a different audience.";
                                        break;
                                    case "ERROR_INVALID_CREDENTIAL":
                                        errorMessage = "The supplied auth credential is malformed or has expired.";
                                        break;
                                    case "ERROR_INVALID_EMAIL":
                                        errorMessage = "The email address is badly formatted.";
                                        break;
                                    case "ERROR_WRONG_PASSWORD":
                                        errorMessage = "The password is invalid or the user does not have a password.";
                                        break;
                                    case "ERROR_USER_MISMATCH":
                                        errorMessage = "The supplied credentials do not correspond to the previously signed in user.";
                                        break;
                                    case "ERROR_USER_NOT_FOUND":
                                        errorMessage = "There is no user record corresponding to this identifier. The user may have been deleted.";
                                        break;
                                    case "ERROR_USER_DISABLED":
                                        errorMessage = "The user account has been disabled by an administrator.";
                                        break;
                                    case "ERROR_TOO_MANY_REQUESTS":
                                        errorMessage = "Too many requests. Try again later.";
                                        break;
                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        errorMessage = "This operation is not allowed. You must enable this service in the console.";
                                        break;
                                    default:
                                        errorMessage = "Authentication failed: " + firebaseAuthException.getMessage();
                                }
                                Toast.makeText(login.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle generic exception
                                Toast.makeText(login.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                login.setEnabled(true);
                email.setEnabled(true);
                password.setEnabled(true);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.setEnabled(false);
                Intent tnnt = new Intent(login.this,create.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(login.this,word, ViewCompat.getTransitionName(word));
                startActivity(tnnt,options.toBundle());
                create.setEnabled(true);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot.setEnabled(false);
                Intent tnnt = new Intent(login.this,forgot.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(login.this,word, ViewCompat.getTransitionName(word));
                startActivity(tnnt,options.toBundle());
                forgot.setEnabled(true);
            }
        });
        facelgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot.setEnabled(false);
                Intent intent = new Intent(login.this,facebooklogin.class);
                startActivity(intent);
                forgot.setEnabled(true);
            }
        });


    }
    private class ani implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {



        }

        @Override
        public void onAnimationEnd(Animation animation) {
            logo.setVisibility(View.GONE);
            if(login.this.getSharedPreferences("profile", Context.MODE_PRIVATE).getString("userid","none").equals("none")) {
                rt.setVisibility(View.VISIBLE);
            }else{
                Intent intt = new Intent(login.this,login_two.class);
                String where = getIntent().getStringExtra("where");
                if (where!=null) {
                    intt.putExtra("where", where);
                    intt.putExtra("userid",getIntent().getStringExtra("userid"));
                    intt.putExtra("postid",getIntent().getStringExtra("postid"));
                }
                startActivity(intt);
                finish();

            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
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