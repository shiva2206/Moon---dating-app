package adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import model.highmodel;
import model.statusmodel;
import model.usermodel;

public class story_seeadp extends RecyclerView.Adapter<story_seeadp.stoviewhlder> {

    private Context context;
    private List<String> userlst;
    private List<List<statusmodel>> stalstlst;
    private List<List<highmodel>> highlstlst;
    private List<Integer> counterlst = new ArrayList<>();
    public String what;
    private ViewPager2 vp;

    public story_seeadp(Context context, List<String> userlst, List<List<statusmodel>> stalstlst, List<Integer> counterlst, ViewPager2 vp) {
        this.context = context;
        this.userlst = userlst;
        this.vp=vp;
        this.stalstlst = stalstlst;
        this.counterlst = counterlst;
        this.what = "story";
    }

    public story_seeadp(List<List<highmodel>> highlstlst, Context context, List<String> userlst, List<Integer> counterlst, ViewPager2 vp) {
        this.context = context;
        this.userlst = userlst;
        this.counterlst = counterlst;
        this.highlstlst = highlstlst;
        this.vp=vp;
        this.what = "high";
    }

    @NonNull
    @Override
    public stoviewhlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_storysee, parent, false);
        return new story_seeadp.stoviewhlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stoviewhlder holder, @SuppressLint("RecyclerView") int position) {

//        Toast.makeText(context, vp.getCurrentItem()+"", Toast.LENGTH_SHORT).show();
        String userid = userlst.get(position);
        int counter = counterlst.get(position);


    }

    public void getuserdet(stoviewhlder holder, String userid) {
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodel udel = snapshot.child(userid).getValue(usermodel.class);
                if (udel != null) {
                    holder.username.setText(udel.getUsername());
                    Picasso.get().load(udel.getImageurl()).into(holder.usrprof);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {

        return 1;
    }


    public void setstoryseen(statusmodel stadel) {
        FirebaseDatabase.getInstance().getReference().child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.child(stadel.getPublisherid()).child("seen").child(stadel.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .exists()) {
                    FirebaseDatabase.getInstance().getReference().child("info").child(stadel.getPublisherid()).child("seen")
                            .child(stadel.getStatusid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(effecy.instance.gettime());

                }

//                        getindi(pp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class stoviewhlder extends RecyclerView.ViewHolder {


//        View fram;

        long pressTime = 0L;
        long limit = 500L;
        private TextView username, title, seevw;
        private CardView topp;
        private RelativeLayout bott;
        private ImageView usrprof, send, annocru, annofri, annoadm, himen, imgg;
        private ViewPager2 viewp;
        private EditText reply;
        private View left, right;
        private StoriesProgressView spv;
        private int counter = 0;
        private int posi;
        private String what;
        private List<statusmodel> statlst;
        private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressTime = System.currentTimeMillis();
                        spv.pause();
                        return false;
                    case MotionEvent.ACTION_UP:
                        long now = System.currentTimeMillis();
                        spv.resume();
                        return limit < now - pressTime;
                }
                return false;
            }
        };

        public stoviewhlder(@NonNull View itemView) {
            super(itemView);
//            fram=itemView.findViewById(R.id.fragg);
            bott = itemView.findViewById(R.id.stobot);
            topp = itemView.findViewById(R.id.topp);
            username = itemView.findViewById(R.id.stousrnme);
            title = itemView.findViewById(R.id.stotit);
            usrprof = itemView.findViewById(R.id.stopro);
            send = itemView.findViewById(R.id.stosen);
            reply = itemView.findViewById(R.id.stoeepl);
            seevw = itemView.findViewById(R.id.sevw);
            himen = itemView.findViewById(R.id.himen);

            viewp = itemView.findViewById(R.id.sevie);

            annocru = itemView.findViewById(R.id.cru);
            annofri = itemView.findViewById(R.id.fri);
            annoadm = itemView.findViewById(R.id.adm);
            imgg = itemView.findViewById(R.id.imgsto);
            left = itemView.findViewById(R.id.lft);
            right = itemView.findViewById(R.id.rft);
            spv = itemView.findViewById(R.id.spv);


        }


    }
}
