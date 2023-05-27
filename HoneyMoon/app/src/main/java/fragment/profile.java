package fragment;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.honeymoon.Mainactivity.currfri;
import static com.example.honeymoon.Mainactivity.currgender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.honeymoon.R;
import com.example.honeymoon.admirers;
import com.example.honeymoon.chats;
import com.example.honeymoon.crushs;
import com.example.honeymoon.editprof;
import com.example.honeymoon.followers;
import com.example.honeymoon.following;
import com.example.honeymoon.friend;
import com.example.honeymoon.matches;
import com.example.honeymoon.mutualfoll;
import com.example.honeymoon.saved;
import com.example.honeymoon.story_see;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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

import adapters.effecy;
import adapters.fragadp;
import model.chatmodel;
import model.keymodel;
import model.usermodel;
import model.varymodel;


//foll Tag   =  Curruser following/request/follow
//butmut Tag =  mutaual friends waste/show
//nmeg Tag   =  user mode
//username   =  user freemium/premium
//annofr     =  Curr user fri remove/add/request/add
//annocrus   =  Curr user cru add/remove
//fri        =  user friend none/(date,key)
//adm        =  user admired or not    (admiired + date + key)/admire
//cru        =  user cru crushed/crush
//mat        = user mat  matched/match
//covimg     = cuser and user clash in request
//prof      = cuser locked/notlocked
//abt        = user public/private
//
/**
 *
 * A simple {@link Fragment} subclass.
 * Use the {@link profile} factory method to
 * create an instance of this fragment.
 */


//username tag for premium/freemium
public class profile extends Fragment {

    private RelativeLayout abtrels,privrel;
    private FragmentManager chmag;
    private ImageView prof,covimg, annocrus, annofr, annoadmi, backimg,profadd,coveadd,matloc,cruloc,admloc,friloc;
    private TextView username,dist,age,privtxt,bltk;
    private Button mess, foll, editpro, saved, addfri, addcru, abt, nmeg, stat, wri, folg, folw, adm, cru, mats, fri,butmut;
    private String userid;
    private usermodel userinfo,curruserdel;
    private List<Fragment> fraglst;
    private fragadp adp;
    private ViewPager vpag;
    private TabLayout tb;
    private List<String> titlst;
    private String str,child;
    private RelativeLayout  hiadrl;
    private Uri uri;
    private Double distt;
    private Intent intemut;
    private SwipeRefreshLayout srl;
    private List<String> allchtlst,oppchtlst;

    public profile(String userid) {
        this.userid = userid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        profadd=view.findViewById(R.id.profadd);
        coveadd=view.findViewById(R.id.covadrl);
        username = view.findViewById(R.id.profuser);
        dist=view.findViewById(R.id.dist);
        prof = view.findViewById(R.id.pro);
        covimg=view.findViewById(R.id.covimg);
        backimg = view.findViewById(R.id.cardview);

        srl=view.findViewById(R.id.srl);
        bltk=view.findViewById(R.id.bltk);
        hiadrl=view.findViewById(R.id.hiadrl);

        privrel=view.findViewById(R.id.privaterl);
        privtxt=view.findViewById(R.id.pritext);
        saved = view.findViewById(R.id.butsaved);
        mess = view.findViewById(R.id.butmess);
        foll = view.findViewById(R.id.butfoll);
        editpro = view.findViewById(R.id.butedits);

        annoadmi = view.findViewById(R.id.annoadmirer);
        annocrus = view.findViewById(R.id.annocrush);
        annofr = view.findViewById(R.id.annofri);


        abtrels = view.findViewById(R.id.abre);
        chmag=getChildFragmentManager();

        addfri = view.findViewById(R.id.butaddfri);
        addcru = view.findViewById(R.id.butaddcru);
        folg = view.findViewById(R.id.butfollg);
        folw = view.findViewById(R.id.butfollw);
        mats = view.findViewById(R.id.butmats);
        cru = view.findViewById(R.id.butcru);
        adm = view.findViewById(R.id.butadm);
        fri = view.findViewById(R.id.butfri);
        butmut=view.findViewById(R.id.butmut);

        tb=view.findViewById(R.id.tb);
        vpag=view.findViewById(R.id.vpag);

        abt = view.findViewById(R.id.butabt);
        nmeg = view.findViewById(R.id.butnmeg);
        stat = view.findViewById(R.id.butsta);
        wri = view.findViewById(R.id.butwri);
        age= view.findViewById(R.id.ag);

        matloc=view.findViewById(R.id.matloc);
        cruloc=view.findViewById(R.id.cruloc);
        admloc=view.findViewById(R.id.admloc);
        friloc=view.findViewById(R.id.friloc);


        allchtlst=new ArrayList<>();
        oppchtlst=new ArrayList<>();
        intemut = new Intent(getContext(),mutualfoll.class);

        effecy ag = new effecy(getContext(),userid,age);
        effecy sen = new effecy(userid, backimg);



        getuserinfo();




        if (currgender.equals("male")) {
            str = "gfs";
        } else {
            str = "bfs";
        }
        if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            mess.setVisibility(GONE);
            foll.setVisibility(GONE);
            dist.setVisibility(GONE);
            editpro.setVisibility(VISIBLE);
            saved.setVisibility(VISIBLE);
            profadd.setVisibility(VISIBLE);
            coveadd.setVisibility(VISIBLE);
            privtxt.setVisibility(GONE);
            privrel.setVisibility(VISIBLE);
            butmut.setVisibility(GONE);
        } else {
            editpro.setVisibility(GONE);
            saved.setVisibility(GONE);
            dist.setVisibility(VISIBLE);
            profadd.setVisibility(GONE);
            coveadd.setVisibility(GONE);
            butmut.setVisibility(VISIBLE);


        }
        highpostags();



        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getuserinfo();
                highpostags();

                srl.setRefreshing(false);
            }
        });
        coveadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coveadd.setEnabled(false);
                Intent iny = new Intent(Intent.ACTION_PICK);
                iny.setType("image/*");
                startActivityForResult(iny,1);
                coveadd.setEnabled(true);
            }
        });
        profadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profadd.setEnabled(false);
                Intent iny = new Intent(Intent.ACTION_PICK);
                iny.setType("image/*");
                startActivityForResult(iny,2);
                profadd.setEnabled(true);
            }
        });
        foll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                foll.setEnabled(false);
                String s = "";
                if(fri.getTag() != null || (fri.getTag()+"").length() > 4){
                    s="Removing Following Will Remove "+username.getText().toString()+" From "+currfri+". \n Are U Sure";
                }else{
                    s="Do U Want To Unfollow "+username.getText();
                }
                if ((foll.getTag()+"").startsWith("following")) {
                    android.app.AlertDialog.Builder alt = new AlertDialog.Builder(getContext());
                    alt.setInverseBackgroundForced(true);
                    alt.setTitle("Remove Following!!!").setMessage(s)
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String folltag = foll.getTag().toString().substring(9);
                                    System.out.println(foll.getTag()+"");
                                    effecy eff = new effecy(getContext(),FirebaseAuth.getInstance().getCurrentUser().getUid(),userid,"following",folltag,fri.getTag()+"");

                                }
                            })
                            .show();



                } else if(foll.getTag().equals("request")){
                    effecy eff = new effecy(getContext(),userid,"requestcancel",abt.getTag().toString());
                }else{
                    effecy eff = new effecy(getContext(),userid,"follows",abt.getTag().toString());

                }
                foll.setEnabled(true);
            }
        });
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backimg.setEnabled(false);
                if (backimg.getTag().equals("yes")) {
                    if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        Intent inte = new Intent(getContext(), story_see.class);
                        inte.putExtra("userid", userid);
                        startActivity(inte);
                    }else {
                        Intent inte = new Intent(getContext(), story_see.class);
                        inte.putExtra("userid", userid);
                        startActivity(inte);
                    }
                }
                backimg.setEnabled(true);
            }
        });
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved.setEnabled(false);
                startActivity(new Intent(getContext(), saved.class));
                saved.setEnabled(true);
            }
        });
        editpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editpro.setEnabled(false);
                startActivity(new Intent(getContext(), editprof.class));
                editpro.setEnabled(true);
            }
        });
        mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mess.setEnabled(false);
                Intent tnte = new Intent(getContext(), chats.class);
                tnte.putExtra("userid", userid);
                startActivity(tnte);
                mess.setEnabled(true);
            }
        });
        addcru.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                addcru.setEnabled(false);
                if (annocrus.getTag().equals("add")) {
                    android.app.AlertDialog.Builder alt = new AlertDialog.Builder(getContext());
                    alt.setInverseBackgroundForced(true);
                    alt.setTitle("Crush!!!").setMessage("Do you want to Add "+username.getText() +" as Crush" )
                            .setCancelable(true)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String dat = effecy.instance.gettime();

                                    HashMap<String,String> mp = new HashMap<>();
                                    String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                                    mp.put("subject","crush");
                                    mp.put("notify", " ");
                                    mp.put("postid", " ");
                                    mp.put("publisherid", " ");
                                    mp.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    mp.put("notifyid", key);
                                    mp.put("time", dat.substring(11));



                                    HashMap hm = new HashMap();
                                    hm.put("date",dat.substring(0,10));
                                    hm.put("time",dat.substring(11));
                                    hm.put("key",key);
                                    FirebaseDatabase.getInstance().getReference().child("info").child(userid)
                                            .child("admirers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hm);
                                    FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("crushs").child(userid).setValue(hm);


                                    FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("others")
                                            .child(dat.substring(0,10)).child(key).setValue(mp);

                                    effecy.instance.sendnotii(getContext(),userid,"Your NEW Admirer","crush",null,key,null);
                                    if(cru.getTag().equals("crushed")){

                                        HashMap<String,String> mpp = new HashMap<>();
                                        String kee = FirebaseDatabase.getInstance().getReference().push().getKey();
                                        mpp.put("subject","match");
                                        mpp.put("notify", " ");
                                        mpp.put("postid", " ");
                                        mpp.put("publisherid", " ");
                                        mpp.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mpp.put("notifyid", kee);
                                        mpp.put("time", dat.substring(11));


                                        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("others")
                                                .child(dat.substring(0,10)).child(kee).setValue(mpp);
                                        mpp.remove("userid");
                                        mpp.put("userid",userid);
                                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("notify").child("others").child(dat.substring(0,10)).child(kee).setValue(mpp);

                                        HashMap hmm = new HashMap();
                                        hmm.put("date",dat.substring(0,10));
                                        hmm.put("time",dat.substring(11));
                                        hmm.put("key",kee);

                                        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("matches")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hmm);
                                        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("matches")
                                                .child(userid).setValue(hmm);

                                        effecy.instance.sendnotii(getContext(),userid,"Your NEW Match","match",null,key,null);

                                    }

                                }
                            })
                            .show();


                } else {

                   if(mats.getTag().toString().startsWith("matched")) {

                       android.app.AlertDialog.Builder alt = new AlertDialog.Builder(getContext());
                       alt.setInverseBackgroundForced(true);
                       alt.setTitle("Crush Remove!!!").setMessage("You Are Having a Match with "+username.getText()+" .Removing Crush will remove the Match. \n Are U Sure ")
                               .setCancelable(true)
                               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                   }
                               })
                               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                       String mattag = mats.getTag().toString().substring(7);

                                       effecy eff = new effecy(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(), userid,
                                               "crushremove", adm.getTag().toString().substring(7), mattag);

                                   }
                               })
                               .show();

                   }else {

                       android.app.AlertDialog.Builder alt = new AlertDialog.Builder(getContext());
                       alt.setInverseBackgroundForced(true);
                       alt.setTitle("Remove Crush!!!").setMessage("Do you want to Remove "+username.getText()+" From Crush!!!")
                               .setCancelable(true)
                               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                   }
                               })
                               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                       effecy eff = new effecy(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(), userid,
                                               "crushremove", adm.getTag().toString().substring(7), null);


                                   }
                               })
                               .show();

                   }
                }
                addcru.setEnabled(true);
            }
        });
        addfri.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                addfri.setEnabled(false);
                if(covimg.getTag().equals("true")){
                    Toast.makeText(getContext(), "See your Notification, He/She already sent you Friend Request", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (annofr.getTag().equals("add")) {
                        if (foll.getTag().toString().startsWith("following")) {
                            String dat = effecy.instance.gettime();
                            HashMap<String, String> mp = new HashMap<>();
                            String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                            mp.put("subject", "friendreq");
                            mp.put("notify", " ");
                            mp.put("postid", " ");
                            mp.put("publisherid", " ");
                            mp.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            mp.put("notifyid", key);
                            mp.put("time", dat);

                            FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("requests").child("friends")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mp);
                            if (str.equals("bfs")){
                                effecy.instance.sendnotii(getContext(),userid,"Gf Request","friendreq",null,key,null);
                            }else{
                                effecy.instance.sendnotii(getContext(),userid,"Bf Request","friendreq",null,key,null);
                            }

                        }

                    } else if (annofr.getTag().toString().equals("request")) {
                        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("notify").child("requests")
                                .child("friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                    } else {

                        android.app.AlertDialog.Builder alt = new AlertDialog.Builder(getContext());
                        alt.setInverseBackgroundForced(true);
                        alt.setTitle("Remove "+currfri.substring(0,2)+"!!!").setMessage("Do you want to Remove "+username.getText()+" From " + currfri.substring(0,2))
                                .setCancelable(true)
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String fritag = fri.getTag() + "";
                                        effecy eff = new effecy(getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid(), userid,
                                                "friendremove", null, fritag);

                                    }
                                })
                                .show();
                    }
                }
                addfri.setEnabled(true);
            }
        });
        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abt.setEnabled(false);
                if (abtrels.getVisibility() == GONE) {
                    abtrels.setVisibility(VISIBLE);
                } else {
                    abtrels.setVisibility(GONE);
                }
                abt.setEnabled(true);
            }
        });
        folg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folg.setEnabled(false);
                if(foll.getTag().toString().startsWith("following") || abt.getTag().equals("public") || userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Intent tb = new Intent(getContext(), following.class);
                    tb.putExtra("userid", userid);
                    startActivity(tb);
                }else{
                    Toast.makeText(getContext(), "This Account Is Private ", Toast.LENGTH_SHORT).show();
                }
                folg.setEnabled(true);
            }
        });
        cru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cru.setEnabled(false);
                if(prof.getTag().equals("lock") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                }else if(prof.getTag().equals("number") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                } else if(
///                        username.getTag() =="premium" &&
                    userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userinfo.getShowcru().equals("all")) {
                    Intent tb = new Intent(getContext(), crushs.class);
                    tb.putExtra("userid", userid);
                    startActivity(tb);
                }
//                else if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){ Toast.makeText(getContext(), "Get 'Premium' to view Crush", Toast.LENGTH_SHORT).show();}
                else{
                        Toast.makeText(getContext(), "You can't view someone else Crush", Toast.LENGTH_SHORT).show();
                    }
                cru.setEnabled(true);
            }
        });
        adm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adm.setEnabled(false);
                if(prof.getTag().equals("lock") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                }else if(prof.getTag().equals("number") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                } else if(
//                        username.getTag() =="premium" &&
                        userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userinfo.getShowadm().equals("all")) {
                    Intent tb = new Intent(getContext(), admirers.class);
                    tb.putExtra("userid", userid);
                    startActivity(tb);
                }
//                else if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){ Toast.makeText(getContext(), "Get 'Premium' to view Admirers", Toast.LENGTH_SHORT).show(); }
                else{
                    Toast.makeText(getContext(), "You can't view someone else Admirers", Toast.LENGTH_SHORT).show();
                }
                adm.setEnabled(true);
            }
        });
        folw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folw.setEnabled(false);
                if(foll.getTag().toString().startsWith("following") || abt.getTag().equals("public") || userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Intent tb = new Intent(getContext(), followers.class);
                    tb.putExtra("userid", userid);
                    startActivity(tb);
                }else{
                    Toast.makeText(getContext(), "This Account Is Private ", Toast.LENGTH_SHORT).show();
                }
                folw.setEnabled(true);
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fri.setEnabled(false);
                if(prof.getTag().equals("lock") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                }else if(prof.getTag().equals("number") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                } else if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userinfo.getShowfri().equals("all")) {
                    Intent tb = new Intent(getContext(), friend.class);
                    tb.putExtra("userid", userid);
                    startActivity(tb);
                }else{
                    Toast.makeText(getContext(), "You can't view someone else "+currfri, Toast.LENGTH_SHORT).show();
                }
                fri.setEnabled(true);
            }
        });
        mats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mats.setEnabled(false);
                if(prof.getTag().equals("lock") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                }else if(prof.getTag().equals("number") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Toast.makeText(getContext(), "Set Your (Show (Crush/Admmirer/"+currfri+"/matches)) to 'all'  to view others", Toast.LENGTH_SHORT).show();
                } else if(
//                        username.getTag() =="premium" &&
                          userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userinfo.getShowmat().equals("all")) {
                    Intent tb = new Intent(getContext(), matches.class);
                    tb.putExtra("userid", userid);
                    startActivity(tb); }
//                else if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){ Toast.makeText(getContext(), "Get 'Premium' to view Matches", Toast.LENGTH_SHORT).show(); }
                else{
                    Toast.makeText(getContext(), "You can't view since it is locked by the user", Toast.LENGTH_SHORT).show();
                }
                mats.setEnabled(true);
            }
        });
        butmut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                butmut.setEnabled(false);
                intemut.putExtra("userid",userid);

                startActivity(intemut);
                butmut.setEnabled(true);
            }
        });

        return view;

    }
    public void highpostags(){
        fraglst=new ArrayList<>();
        titlst=new ArrayList<>();

        fraglst.add(new profile_highlights(userid));
        fraglst.add(new profile_posts(userid));
        fraglst.add(new profile_tags(userid));
        titlst.add("HIGHLIGHTS");
        titlst.add("POSTS");
        titlst.add("TAGS");
        adp = new fragadp(chmag,fraglst,titlst);
        vpag.setAdapter(adp);
        tb.setupWithViewPager(vpag);
    }
    public void getotherreq(){
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notify").child("requests").child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    covimg.setTag("true");
                }else{
                    covimg.setTag("false");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getfoll() {
        FirebaseDatabase.getInstance().getReference().child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(userid).exists()){
                    keymodel kydell = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(userid).getValue(keymodel.class);
                    foll.setText("Following");
                    System.out.println(kydell.getDate()+kydell.getKey());
                    foll.setTag("following"+kydell.getDate()+ kydell.getKey());

                    privrel.setVisibility(VISIBLE);
                    privtxt.setVisibility(GONE);
                }else if(snapshot.child(userid).child("notify").child("requests").child("follows").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    foll.setText("Request Sent");
                    foll.setTag("request");

                    privrel.setVisibility(GONE);
                    privtxt.setVisibility(VISIBLE);
                }else if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers").child(userid).exists() && !snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followings").child(userid).exists()){
                    foll.setText("Follow Back");
                    foll.setTag("follow");


                    if(userinfo.getMode().equals("private")){
                        privrel.setVisibility(GONE);
                        privtxt.setVisibility(VISIBLE);
                    }else{
                        privrel.setVisibility(VISIBLE);
                        privtxt.setVisibility(GONE);
                    }
                }else{
                    foll.setText("Follow");
                    foll.setTag("follow");


                    if(userinfo.getMode().equals("private") && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        privrel.setVisibility(GONE);
                        privtxt.setVisibility(VISIBLE);
                    }else{
                        privrel.setVisibility(VISIBLE);
                        privtxt.setVisibility(GONE);
                    }
                }
                getfolgnum();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getuserinfo() {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dss : snapshot.getChildren()) {
                    if(dss.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        curruserdel = dss.getValue(usermodel.class);
                        if(curruserdel.getShowfri().equals("lock") || curruserdel.getShowadm().equals("lock") ||curruserdel.getShowmat().equals("lock") || curruserdel.getShowadm().equals("lock") ){
                            prof.setTag("lock");
                        }else if(curruserdel.getShowfri().equals("all") && curruserdel.getShowadm().equals("all") && curruserdel.getShowmat().equals("all") && curruserdel.getShowadm().equals("all") ){
                            prof.setTag("all");
                        }
                        else {
                            prof.setTag("number");
                        }


                    }
                    if (dss.getKey().equals(userid)) {
                        userinfo = dss.getValue(usermodel.class);
                        System.out.println(userinfo);
                        Picasso.get().load(userinfo.getImageurl()).into(prof);
                        Picasso.get().load(userinfo.getCoverimage()).into(covimg);
                        nmeg.setText(userinfo.getName());
                        username.setText(userinfo.getUsername());
                        abt.setTag(userinfo.getMode());
                        stat.setText(userinfo.getStatus());
                        wri.setText(userinfo.getAbout());
                        intemut.putExtra("gender",userinfo.getGender());
                        if(userinfo.getMium().equals("freemium")){
                            username.setTag("freemium");
                        }else{
                            username.setTag("premium");
                        }
                        if(userinfo.getShowaddcrubut().equals("true") && !userinfo.getGender().equals(currgender) && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            addcru.setVisibility(VISIBLE);
                        }else{
                            addcru.setVisibility(GONE);
                        }


                        if(userinfo.getBluetick().equals("true")){
                            bltk.setVisibility(VISIBLE);
                        }else{
                            bltk.setVisibility(GONE);
                        }

//                            if (userinfo.getGender().equals("male")) {
//                                addfri.setText("add as gf");
//
//                            }else{
//                                addfri.setText("add as bf");
//                            }
//                        }

                    }

                }

                getfollwnum();
                getfoll();
                check();
                getfrinum();
                getotherreq();

                getadmnum();
                getcrunum();



                getmatsnum();
                if(curruserdel.getShowlocation().equals("true") && userinfo.getShowlocation().equals("true")){
                    getlatlong();
                }else{
                    dist.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void check() {
        FirebaseDatabase.getInstance().getReference().child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(str).exists()) {

                    if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(str).child(userid).exists()) {
                        annofr.setVisibility(VISIBLE);
                        annofr.setTag("remove");
                        if (currfri == "bfs") {
                            addfri.setText("remove Bf");
                        } else {
                            addfri.setText("remove Gf");
                        }
                    } else {
                        annofr.setVisibility(GONE);
                        annofr.setTag("add");
                        if (currfri == "bfs") {
                            addfri.setText("add as Bf");
                        } else {
                            addfri.setText("add as Gf");
                        }
                    }
                }else if(snapshot.child(userid).child("notify").child("requests").child("friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    annofr.setTag("request");
                    annofr.setVisibility(GONE);
                    if (currfri.toString() == "gfs") {
                        addfri.setText("Gf Request Sent");
                    } else {
                        addfri.setText("Bf Request Sent");
                    }
                } else {
                    annofr.setVisibility(GONE);
                    annofr.setTag("add");
                    if (currfri.toString() == "bfs") {
                        addfri.setText("add as Bf");
                    } else {
                        addfri.setText("add as Gf");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("admirers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child(userid).exists()) {
                        annoadmi.setVisibility(VISIBLE);
                    } else {
                        annoadmi.setVisibility(GONE);
                    }

                } else {
                    annoadmi.setVisibility(GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("crushs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(userid).exists()) {
                        annocrus.setVisibility(VISIBLE);
                        annocrus.setTag("remove");
                        addcru.setText("remove crush");
                    } else {
                        annocrus.setVisibility(GONE);
                        annocrus.setTag("add");
                        addcru.setText("add as crush");
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getfrinum() {
        String ufri="";

        if(userinfo.getGender().equals("male")){
            ufri = "gfs";
        }else{
            ufri = "bfs";
        }
        String finalUfri = ufri;
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child(ufri).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || !userinfo.getShowfri().equals("lock") || !prof.getTag().equals("lock")) {
                    friloc.setVisibility(GONE);
                    fri.setText(finalUfri + " \n \n " + snapshot.getChildrenCount());

                }else{
                    friloc.setVisibility(VISIBLE);
                    fri.setText(finalUfri);

                }


                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() && !userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    keymodel ky = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(keymodel.class);
                    fri.setTag(ky.getDate()+ky.getKey());
                }else{
                    fri.setTag(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getadmnum() {
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("admirers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || !userinfo.getShowadm().equals("lock") || !prof.getTag().equals("lock")
//                            && username.getTag() == "premium"
                ) {
                    adm.setText("ADMIRERS \n \n " + snapshot.getChildrenCount());
                    admloc.setVisibility(GONE);
                }else{
                    admloc.setVisibility(VISIBLE);
                }



                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    keymodel kdelll = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(keymodel.class);
                    adm.setTag("admired"+kdelll.getDate()+kdelll.getKey());
                }else{
                    adm.setTag("admire");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getcrunum() {
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("crushs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || !userinfo.getShowcru().equals("lock") || !prof.getTag().equals("lock")
//                            && username.getTag() == "premium"
                ) {
                    cru.setText("CRUSHS \n \n " + snapshot.getChildrenCount());
                    cruloc.setVisibility(GONE);
                }else{
                    cruloc.setVisibility(VISIBLE);
                }

                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    cru.setTag("crushed");
                }else{
                    cru.setTag("crush");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getmatsnum() {
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("matches").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || !userinfo.getShowmat().equals("lock") || !prof.getTag().equals("lock")
//                            && username.getTag() == "premium"
                ) {
                    mats.setText("MATCHES \n \n " + snapshot.getChildrenCount());
                    matloc.setVisibility(GONE);
                }else{
                    matloc.setVisibility(VISIBLE);
                }


                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    keymodel kdel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(keymodel.class);
                    mats.setTag("matched"+kdel.getDate()+kdel.getKey());

                }else{
                    mats.setTag("match");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getfollwnum() {
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                folw.setText("FOLLOWERS \n \n " + snapshot.getChildrenCount());

                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    folw.setTag("cuser_a_follower");
                }else{
                    folw.setTag("cuser_not_a_follower");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getfolgnum() {
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("followings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                System.out.println(folw.getTag().toString());
                System.out.println(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists());
                System.out.println(!userinfo.getGender().equals(currgender));

                folg.setText("FOLLOWINGS \n \n " + snapshot.getChildrenCount());
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() &&
                        folw.getTag().toString().equals("cuser_a_follower")  &&
                        !userinfo.getGender().equals(currgender)){


                    addfri.setVisibility(VISIBLE);

                }else{

                    addfri.setVisibility(GONE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getgend(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                oppchtlst.clear();
                for(String userid : allchtlst){
                    usermodel udel = snapshot.child(userid).getValue(usermodel.class);
                    if(udel != null && (udel.getGender() != userinfo.getGender())){
                        oppchtlst.add(udel.getUsername());
                    }
                }
                getallchtlst("next");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getallchtlst(String st){
        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!st.equals("next")) {
                    allchtlst.clear();
                    for (DataSnapshot dst : snapshot.getChildren()) {
                        allchtlst.add(dst.getKey());
                    }
                    getgend();
                }else{
                    int moons = 0;
                    for(String uder : oppchtlst){
                        int fst =0 ,sec = 0;
                        List<String> dalst = new ArrayList<>();
                        for (DataSnapshot dst : snapshot.child(uder).getChildren()) {
                            dalst.add(dst.getKey());
                        }
                        for(String dat : dalst){
                            for (DataSnapshot dstt : snapshot.child(uder).child(dat).getChildren()) {
                                chatmodel chdel = dstt.getValue(chatmodel.class);
                                if (chdel.getUserid().equals(userid)){
                                    fst=fst+1;
                                }else{
                                    sec=sec+1;
                                }
                            }
                        }
                        if (sec >= 10 && fst >=10){
                            moons = moons + fst + sec;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getlatlong(){
        FirebaseDatabase.getInstance().getReference().child("vary").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                varymodel currvarymdel = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(varymodel.class);
                for(DataSnapshot sh : snapshot.getChildren()){
                    if(sh.getKey().equals(userid)){
                        varymodel vydel = sh.getValue(varymodel.class);
                        effecy getdis = new effecy(getContext(),vydel.getLatitude(),vydel.getLongitude(),dist,currvarymdel);
                        dist.setText("( "+String.format("%.0f",dist.getTag())+"Km Away )");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 1 || requestCode == 2 ) && resultCode == RESULT_OK){
            uri = data.getData();

            if (requestCode==1){
                child="coverimage";
            }else{
                child="imageurl";
            }
            DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
            FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(child).delete();
            StorageReference sto = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(child);
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
                @Override
                public void onComplete(@NonNull Task task) {


                    dataref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(child)
                            .setValue(task.getResult().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });


        }
    }
    private  String filextension(Uri uri){
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContext().getContentResolver().getType(uri));
    }

}