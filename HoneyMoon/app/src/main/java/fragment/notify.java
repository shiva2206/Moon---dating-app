package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.honeymoon.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.fragadp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notify#} factory method to
 * create an instance of this fragment.
 */
public class notify extends Fragment {

    private TabLayout tb;
    private List<Fragment> fralst;
    private List<String> titlst;
    private ViewPager vp;
    private fragadp adp;
    private SwipeRefreshLayout srl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        fralst=new ArrayList<>();
        titlst=new ArrayList<>();


        tb=view.findViewById(R.id.tably);
        vp=view.findViewById(R.id.vpage);
        srl=view.findViewById(R.id.srl);

        seet();


        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                seet();
                srl.setRefreshing(false);
            }
        });

        return view;
    }
    public void seet(){
        fralst.clear();
        fralst.add(new notify_others());
        fralst.add(new notify_requests());


        titlst.add("Others");
        titlst.add("Requests");


        adp =new fragadp(getChildFragmentManager(),fralst,titlst);
        vp.setAdapter(adp);
        tb.setupWithViewPager(vp);


    }

}
