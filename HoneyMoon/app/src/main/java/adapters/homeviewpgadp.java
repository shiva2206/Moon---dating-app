package adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class homeviewpgadp extends FragmentStateAdapter {
    private List<Fragment> lstfrag;
    private List<String> titles;


    public homeviewpgadp(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return lstfrag.get(position);
    }

    @Override
    public int getItemCount() {
        return lstfrag.size();
    }




}
