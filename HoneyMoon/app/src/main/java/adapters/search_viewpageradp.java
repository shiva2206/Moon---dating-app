package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.honeymoon.R;
import com.example.honeymoon.view;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.postmodel;
import model.statusmodel;


//viewpager in search
public class search_viewpageradp extends PagerAdapter {
    private Context context;
    private List<postmodel> lstpst;

    public search_viewpageradp(Context context, List<postmodel> lstpst) {
        this.context = context;
        this.lstpst = lstpst;
    }

    @Override
    public int getCount() {
        return lstpst.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_image_sec,container,false);

        postmodel pmdel = lstpst.get(position);
        ImageView img = view.findViewById(R.id.imst);

        Picasso.get().load(pmdel.getUri()).into(img);
        container.addView(view,position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
