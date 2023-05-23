package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.honeymoon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class arrayadap extends ArrayAdapter<String> {

    private Context context;
    public arrayadap(@NonNull Context context, int resource,  List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        String imguri = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adap_grid,parent,false);
        }
        ImageView img = convertView.findViewById(R.id.ig);

        Picasso.get().load(imguri).placeholder(R.drawable.logo_background).into(img);

        return convertView;
    }
}
