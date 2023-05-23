package fragment;

import static android.view.View.GONE;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.honeymoon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.hashadp;
import adapters.searchadp;
import model.usermodel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search#} factory method to
 * create an instance of this fragment.
 */
public class search extends Fragment {

    private List<usermodel> lstuser;
    private List<String> strlst,hshlst,summlst,fritaglst;
    private String what,userid,difftag,fritag;
    private boolean isnavsearch;
    private EditText etx;
    private Query que;
    private searchadp sadp;
    private hashadp hadp;
    private RecyclerView urecy;
    private RecyclerView hrecy;
    private TextView nn;
    private SwipeRefreshLayout srl;
    public static String sg;


    public search(boolean isnavsearch) {
        this.isnavsearch = isnavsearch;

    }
    public search(boolean isnavsearch,List<usermodel> aftertag,String userid,String what,RecyclerView recy) {
        this.isnavsearch = isnavsearch;
        this.what = what;
        this.userid=userid;

    }


    public search(boolean isnavsearch,String userid,String what, String difftag,String fritag){
        this.what = what;
        this.userid=userid;
        this.isnavsearch = isnavsearch;
    }

    public search(boolean isnavsearch,String userid,String what){
        this.what = what;
        this.userid=userid;
        this.isnavsearch = isnavsearch;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        hshlst =new ArrayList<>();
        lstuser=new ArrayList<>();
        strlst=new ArrayList<>();
        summlst=new ArrayList<>();
        fritaglst=new ArrayList<>();
        etx=view.findViewById(R.id.etx);
        nn=view.findViewById(R.id.nn);
        srl=view.findViewById(R.id.srl);
        sadp = new searchadp(getContext(),lstuser,what,userid);
        hadp = new hashadp(getContext(),hshlst);

        hrecy = view.findViewById(R.id.hrecy);
        urecy = view.findViewById(R.id.urecy);


        hrecy.setAdapter(hadp);
        hrecy.setLayoutManager(new LinearLayoutManager(getContext()));

        urecy.setAdapter(sadp);
        urecy.setLayoutManager(new LinearLayoutManager(getContext()));

        if(isnavsearch){
//            que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt("").endAt(""+"\uf8ff");
            getallque("");
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                    que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username")
//                            .startAt(etx.getText()+"").endAt(etx.getText()+""+"\uf8ff");
                    getallque(etx.getText()+"");
                    srl.setRefreshing(false);
                }
            });
        }else {
            getque();

            getuser("");
            srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getque();
//                    que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username")
//                            .startAt(etx.getText()+"").endAt(etx.getText()+"\uf8ff");

                    getuser(etx.getText()+"");
                    srl.setRefreshing(false);
                }
            });
        }

        etx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sg = s +"";
                if(isnavsearch){
                    getallque(sg);
                }else{
                    getuser(s+"");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
    public void getque(){

        FirebaseDatabase.getInstance().getReference().child("info").child(userid).child(what).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              hshlst.clear();
              strlst.clear();
              fritaglst.clear();
              for(DataSnapshot dss : snapshot.getChildren()){

                  strlst.add(dss.getKey());
//                  if(what.equals("gfs") || what.equals("bfs")){
//                      keymodel kdel = snapshot.child(dss.getKey()).getValue(keymodel.class);
//                      fritaglst.add(kdel.getDate()+kdel.getTime());
//                  }

              }




          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }
    public void getuser(String ss){
        que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(ss).endAt(ss+"\uf8ff");

        que.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               lstuser.clear();
               for(DataSnapshot dss : snapshot.getChildren()){
                   if(strlst.contains(dss.getKey())){
                       lstuser.add(dss.getValue(usermodel.class));
                   }
               }
//               if(aftertag == null){
               hadp.notifyDataSetChanged();
               sadp.notifyDataSetChanged();

               if(lstuser.isEmpty()){
                   nn.setVisibility(View.VISIBLE);
               }else{
                   nn.setVisibility(GONE);
               }
               //               }else{
//                   beforetagadp adp = new beforetagadp(getContext(),lstuser,beforerecy);
//
//                   urecy.setAdapter(adp);
//                   urecy.setLayoutManager(new LinearLayoutManager(getContext()));
//               }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       Query qy =FirebaseDatabase.getInstance().getReference().child("hashs").orderByKey().startAt(ss).endAt(ss+"\uf8ff");

       qy.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               hshlst.clear();
               for(DataSnapshot dss : snapshot.getChildren()){
                   if(summlst.contains(dss.getKey())){
                       hshlst.add(dss.getKey());
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
    public void getallque(String st){
        que = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").startAt(st).endAt(st+"\uf8ff");
        que.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  lstuser.clear();
                  for(DataSnapshot sh : snapshot.getChildren()){
                        usermodel udel = sh.getValue(usermodel.class);
                        lstuser.add(udel);
                  }
                  urecy.setAdapter(sadp);
                  urecy.setLayoutManager(new LinearLayoutManager(getContext()));

                  sadp.notifyDataSetChanged();

                  if(lstuser.isEmpty()){
                      nn.setVisibility(View.VISIBLE);
                  }else{
                      nn.setVisibility(GONE);
                  }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(st == ""){
            FirebaseDatabase.getInstance().getReference().child("hashs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    hshlst.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot dss : snapshot.getChildren()) {
                            hshlst.add(dss.getKey());
                        }
                        hadp.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {

            Query qry = FirebaseDatabase.getInstance().getReference().child("hashs").orderByKey().startAt(st).endAt(st + "\uf8ff");
            qry.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    hshlst.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot dss : snapshot.getChildren()) {
                            hshlst.add(dss.getKey());
                        }
                        hadp.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}