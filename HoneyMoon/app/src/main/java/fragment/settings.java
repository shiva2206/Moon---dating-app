package fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.honeymoon.Mainactivity;
import com.example.honeymoon.R;
import com.example.honeymoon.blockedconts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import java.util.ArrayList;
import java.util.List;

import adapters.settingadp;
import model.usermodel;

import static android.content.Context.MODE_PRIVATE;
import static com.example.honeymoon.Mainactivity.frag;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settings} factory method to
 * create an instance of this fragment.
 */
public class settings extends Fragment {

    private RecyclerView recy;
    private settingadp adp;
    private List<usermodel> lstcurrdel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        recy =view.findViewById(R.id.setrecy);
        lstcurrdel=new ArrayList<>();

        adp=new settingadp(getContext(),lstcurrdel);

        recy.setAdapter(adp);
        recy.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstcurrdel.clear();
                for(DataSnapshot dsp : snapshot.getChildren()){
                    if(dsp.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        usermodel cudel = dsp.getValue(usermodel.class);
                        lstcurrdel.add(cudel);
                    }
                }
                adp.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        settmodel=new usermodel(
//
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("status","none"),
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("mode","none"),
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("showlastseen","none"),
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("showlocation","none"),
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("showmat","none"),
//
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("showcru","none"),
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("showadm","none"),
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("showfri","none"),
//                getContext().getSharedPreferences("profile", MODE_PRIVATE).getString("showaddcrubut","none")
//
//        );


//        dele.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                android.app.AlertDialog.Builder alt = new AlertDialog.Builder(getContext());
//                alt.setTitle("Delete Account").setMessage("Do you want to Delete your Account")
//                        .setCancelable(true)
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                FirebaseDatabase.getInstance().getReference().child("users").child(mauth.getCurrentUser().getUid())
//                                        .removeValue();
//                                FirebaseDatabase.getInstance().getReference().child("info").child(mauth.getCurrentUser().getUid())
//                                        .child("posts").addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        for(DataSnapshot dss : snapshot.getChildren()){
//                                            postmodel pdel = dss.getValue(postmodel.class);
//                                            soci.setText(pdel.getDescription());
//                                            for(String i : soci.getHashtags()){
//                                                FirebaseDatabase.getInstance().getReference().child("hashs")
//                                                        .child(i).child(pdel.getPostid()).removeValue();
//                                            }
//                                            for(String i : pdel.getTags().keySet()){
//                                                if(!i.equals("none")) {
//                                                    FirebaseDatabase.getInstance().getReference().child("info").child(i).child("tags")
//                                                            .child(pdel.getPostid()).removeValue();
//                                                }
//                                            }
//                                            for(String i : pdel.getSaves().keySet()){
//                                                if(!i.equals("none")) {
//                                                    FirebaseDatabase.getInstance().getReference().child("info").child(i).child("saved")
//                                                            .child(pdel.getPostid()).removeValue();
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//
//                                startActivity(new Intent(getContext(), login.class));
//
//                            }
//                        })
//                        .show();
//
//            }
//        });

        return view;
    }


}