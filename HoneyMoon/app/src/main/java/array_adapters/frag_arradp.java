package array_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.honeymoon.R;

import java.util.List;

public class frag_arradp extends ArrayAdapter<Fragment> {
    private Context context;
    private List<Fragment> fralst;
    private FragmentManager fragman;

    public frag_arradp(@NonNull Context context, int resource, List<Fragment> fralst, FragmentManager fragman) {
        super(context, resource,fralst);
        this.context=context;
        this.fralst = fralst;
        this.fragman = fragman;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adap_frag,parent,false);
        }
        FrameLayout fralay = convertView.findViewById(R.id.fra);
        Fragment frag = fralst.get(position);

        fragman.beginTransaction().replace(R.id.fra,frag).commitAllowingStateLoss();

        return convertView;

    }
}
