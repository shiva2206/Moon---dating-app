package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.honeymoon.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import adapters.fragadp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment {
    private TabLayout tl;
    private ViewPager vp;
    private List<Fragment> lstfrag;
    private List<String> titles;
    private fragadp adp;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        tl=view.findViewById(R.id.tbb);
        tl.setScrollContainer(true);
        vp=view.findViewById(R.id.homeviewp);
        lstfrag=new ArrayList<>();
        titles=new ArrayList<>();

        lstfrag.add(new home_area(true));
        lstfrag.add(new home_stories());
        lstfrag.add(new home_mess());
        lstfrag.add(new callslog());

        titles.add("Home");
        titles.add("Stories");
        titles.add("Chats");
        titles.add("Calls");

        adp=new fragadp(getChildFragmentManager(),lstfrag,titles);
        vp.setAdapter(adp);
        tl.setupWithViewPager(vp);



        return view;

    }

}