package fragment;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import adapters.mediaadp;
import model.chatmodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link media#} factory method to
 * create an instance of this fragment.
 */
public class media extends Fragment {

    private String userid;
    private RecyclerView recv;
    private mediaadp adp;
    private TextView nn;
    private List<chatmodel> chtlst;

    public media(String userid) {
        this.userid = userid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        chtlst = new ArrayList<>();
        nn = view.findViewById(R.id.nn);
        recv = view.findViewById(R.id.medrecy);

        adp = new mediaadp(getContext(), chtlst, userid);

        recv.setAdapter(adp);
        recv.setLayoutManager(new GridLayoutManager(getContext(), 3));

        getdt();

        return view;
    }

    public void getdt() {
        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        chtlst.clear();
                        List<String> dalst = new ArrayList<>();
                        List<chatmodel> chhhlst = new ArrayList<>();
                        for (DataSnapshot dss : snapshot.getChildren()) {
                            for (DataSnapshot qwe : snapshot.child(dss.getKey()).getChildren()) {
                                chatmodel chdel = qwe.getValue(chatmodel.class);
                                if (chdel.getType().equals("image") || chdel.getType().equals("video")) {
                                    chdel.setTime(dss.getKey() + " " + chdel.getTime());
                                    chhhlst.add(chdel);
                                    dalst.add(chdel.getTime());
                                }
                            }
                        }

                        for (String datt : effecy.instance.getdalst(dalst)) {
                            for (chatmodel chdel : chhhlst) {
                                if (chdel.getTime().equals(datt) && !chtlst.contains(chdel)) {
                                    chdel.setTime(datt.substring(0, 16));
                                    chtlst.add(chdel);
                                }
                            }
                        }


                        if (chtlst.isEmpty()) {
                            nn.setVisibility(View.VISIBLE);
                        } else {
                            nn.setVisibility(GONE);
                        }

                        adp.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}