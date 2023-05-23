
package fragment;

import static android.view.View.INVISIBLE;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.effecy;
import adapters.messadap;
import model.chatmodel;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_mess# method to
 * create an instance of this fragment.
 */
public class home_mess extends Fragment {
    private List<usermodel> uselst,othlst;
    private List<String> peolst;
    private List<Integer> finalnewmesslst,otherfinalnewmesslst;
    private RecyclerView ecyc,othrecy;
    private List<chatmodel> lastchtlst,dummylst;
    private messadap adp,othadp;
    private EditText ed;
    private TextView nn,oth;
    private String s;

    private List<Integer> newmes = new ArrayList<>();
    private List<chatmodel> finchtlst = new ArrayList<>();
    private List<String> leftoutst = new ArrayList<>();

    private List<String> ulst = new ArrayList<>();
    private List<Integer> ordnewmess = new ArrayList<>();
    private List<chatmodel> chtlst = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_mess, container, false);
        uselst=new ArrayList<>();
        peolst=new ArrayList<>();
        othlst=new ArrayList<>();
        lastchtlst=new ArrayList<>();
        dummylst=new ArrayList<>();
        finalnewmesslst=new ArrayList<>();
        otherfinalnewmesslst=new ArrayList<>();

        ed = view.findViewById(R.id.etx);
        ecyc = view.findViewById(R.id.ecycc);
        oth=view.findViewById(R.id.dd);
        othrecy=view.findViewById(R.id.othrecy);

        nn=view.findViewById(R.id.nn);
        adp=new messadap(getContext(),uselst,finalnewmesslst,lastchtlst);
        othadp=new messadap(getContext(),othlst,otherfinalnewmesslst,dummylst);

        othrecy.setAdapter(othadp);
        othrecy.setLayoutManager(new LinearLayoutManager(getContext()));
        ecyc.setAdapter(adp);
        ecyc.setLayoutManager(new LinearLayoutManager(getContext()));
        s="";
        getmess();
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence ss, int start, int before, int count) {
                s=ss+"";
                getmess();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        adp.notifyDataSetChanged();
        return view;

    }
    public void getuser(){
        ulst.clear();
        ordnewmess.clear();
        chtlst.clear();


        List<String> dalst = new ArrayList<>();

        for(chatmodel chdel : finchtlst){
             dalst.add(chdel.getTime());
        }
        for(String stg : effecy.instance.getdalst(dalst)){
             for(int y =0 ; y<finchtlst.size();y++){
                 if(stg.equals(finchtlst.get(y).getTime()) && !chtlst.contains(finchtlst.get(y))){
                     ulst.add(peolst.get(y));
                     ordnewmess.add(newmes.get(y));
                     chtlst.add(finchtlst.get(y));
//                     Toast.makeText(getContext(), finchtlst.get(y).getChat(), Toast.LENGTH_SHORT).show();
//                     Toast.makeText(getContext(),"2----" +finchtlst.get(y).getChat(), Toast.LENGTH_SHORT).show();
                     break;
                 }
             }
        }
        for(String uid : leftoutst){
             ulst.add(uid);
             ordnewmess.add(0);
             dummylst.add(new chatmodel());
        }
        Collections.reverse(ulst);
        Collections.reverse(ordnewmess);
        Collections.reverse(chtlst);

        Query que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");
//        System.out.println(chtlst.toArray());
        que.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     uselst.clear();
                     othlst.clear();
                     finalnewmesslst.clear();
                     otherfinalnewmesslst.clear();
                     lastchtlst.clear();
                     for(String userid : ulst){
                         usermodel uel = snapshot.child(userid).getValue(usermodel.class);
                         if(uel !=null) {
                             uselst.add(uel);
                             finalnewmesslst.add(ordnewmess.get(ulst.indexOf(uel.getUserid())));
                             lastchtlst.add(chtlst.get(ulst.indexOf(uel.getUserid())));
//                             Toast.makeText(getContext(), finchtlst.get(ulst.indexOf(uel.getUserid())).getChat(), Toast.LENGTH_SHORT).show();
//                             Toast.makeText(getContext(),chtlst.size()+"  "+chtlst.get(ulst.indexOf(uel.getUserid())).getChatid(), Toast.LENGTH_SHORT).show();
//                             Toast.makeText(getContext(),"0----"+ uel.getUserid()+" "+finalChtlst.get(finalUlst.indexOf(uel.getUserid())).toString(), Toast.LENGTH_SHORT).show();
                         }
                     }
                     for (DataSnapshot ds : snapshot.getChildren()) {
                         usermodel uel = ds.getValue(usermodel.class);
                         if (!uel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&
                                 !ulst.contains(ds.getKey()) && (othlst.size() != 10)) {

                             othlst.add(uel);
                             otherfinalnewmesslst.add(0);
                             dummylst.add(new chatmodel());
                         }
                     }
                     if (othlst.isEmpty()) {
                         oth.setVisibility(INVISIBLE);
                     } else {
                         oth.setVisibility(View.VISIBLE);
                     }

                     if(othlst.isEmpty() && ulst.isEmpty()){
                         nn.setVisibility(View.VISIBLE);
                     }else{
                         nn.setVisibility(INVISIBLE);
                     }

                     othadp.notifyDataSetChanged();
                     adp.notifyDataSetChanged();
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
        });

    }
    public void getmess(){

        FirebaseDatabase.getInstance().getReference().child("info").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                peolst.clear();
                newmes.clear();
                finchtlst.clear();
                leftoutst.clear();
//                Toast.makeText(getContext(), "11111", Toast.LENGTH_SHORT).show();
                for (DataSnapshot dh : snapshot.getChildren()) {
                    int pl = 0;
                    List<chatmodel> chtlst = new ArrayList<>();
                    List<String> dalst = new ArrayList<>();

                    for(DataSnapshot dsp : snapshot.child(dh.getKey()).getChildren()){

                        for(DataSnapshot  dsn : snapshot.child(dh.getKey()).child(dsp.getKey()).getChildren()){
                            chatmodel chdel = dsn.getValue(chatmodel.class);
                            System.out.println(chdel.getChatid());
                            chdel.setTime(dsp.getKey()+" "+chdel.getTime());
//                            chdel.setChat(chdel.getChatid());
                            dalst.add(chdel.getTime());
                            chtlst.add(chdel);
                            if(chdel.getSeen() == null && !chdel.getUserid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                                pl=pl+1;

                            }
                        }

                    }
                    List<String> gotit =effecy.instance.getdalst(dalst);
//
                    for(chatmodel chd : chtlst){
                        if(chd.getTime().equals(gotit.get(gotit.size()-1))){
//
//                            Toast.makeText(getContext(), "1---"+chd.getUserid()+" "+chd.getChatid(), Toast.LENGTH_SHORT).show();
//                            System.out.println(chd.getUserid()+" "+chd.getChatid());
                            finchtlst.add(chd);
//                            break;
                        }
                    }
//                    finchtlst.add(chtlst.get(chtlst.size()-1));
//                    Toast.makeText(getContext(), chtlst.get(chtlst.size()-1).getChatid(), Toast.LENGTH_SHORT).show();
                    newmes.add(pl);
                    peolst.add(dh.getKey());
                }
                getuser();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}