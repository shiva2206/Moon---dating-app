package adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


//tablayout with frg
public class fragadp extends FragmentPagerAdapter {
    private List<Fragment> lstfrg;
    private List<String> titles;

    public fragadp(@NonNull FragmentManager fm, List<Fragment> lstfrg, List<String> titles) {
        super(fm);
        this.lstfrg = lstfrg;
        this.titles = titles;
    }

    public List<Fragment> getLstfrg() {
        return lstfrg;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return lstfrg.get(0);
            case 1:
                return lstfrg.get(1);

            case 2:
                return lstfrg.get(2);
            case 3:
                return lstfrg.get(3);
            case 4:
                return lstfrg.get(4);

            case 5:
                return lstfrg.get(5);


        }
        return null;
    }

    @Override
    public int getCount() {
        return lstfrg.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
