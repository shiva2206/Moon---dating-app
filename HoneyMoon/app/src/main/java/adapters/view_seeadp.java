

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
import com.squareup.picasso.Picasso;

import java.util.List;

import model.postmodel;
import model.statusmodel;

public class view_seeadp extends PagerAdapter {

    private Context context;
    private List<statusmodel> lstst;

    public view_seeadp(Context context, List<statusmodel> lstst) {
        this.context = context;
        this.lstst = lstst;
    }

    @Override
    public int getCount() {
        return lstst.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.adap_image,container,false);
        statusmodel stdel = lstst.get(position);
        ImageView img  = view.findViewById(R.id.imst);
        TextView tme = view.findViewById(R.id.ttmie);

        tme.setText(stdel.getTime());
        Picasso.get().load(stdel.getUri()).into(img);


        container.addView(view,position);
        return view;
    }
}
