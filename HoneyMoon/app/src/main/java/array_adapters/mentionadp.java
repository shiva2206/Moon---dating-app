package array_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.honeymoon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapters.effecy;
import model.usermodel;

public class mentionadp extends ArrayAdapter<usermodel> {

    private List<usermodel> usrlst;

//    public mentionadp(@NonNull Context context, int resource, @NonNull List<usermodel> usermdelst) {
//        super(context, 0, usermdelst);
//    }

//    public mentionadp(@NonNull Context context, @NonNull List<usermodel> objects, List<usermodel> selecusrlst) {
//        super(context, 0, objects);
//        this.selecusrlst = selecusrlst;
//    }

    public mentionadp(@NonNull Context context, List<usermodel> usrlst) {
        super(context, 0    , usrlst);
        this.usrlst = new ArrayList<>(usrlst);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adap_search_two, parent, false);
        }

        usermodel udell = usrlst.get(position);
        Toast.makeText(getContext(), udell.getUsername(), Toast.LENGTH_SHORT).show();

        CardView cd = convertView.findViewById(R.id.cdgh);
//        cd.setRadius(0);
        ImageView prof = convertView.findViewById(R.id.vpro);
        ImageView backimg = convertView.findViewById(R.id.backimag);
        ImageView annoadm = convertView.findViewById(R.id.adm);
        ImageView annofri = convertView.findViewById(R.id.fri);
        ImageView annocru = convertView.findViewById(R.id.cru);
        TextView usernme = convertView.findViewById(R.id.vusme);
        TextView naem = convertView.findViewById(R.id.vnme);
        TextView time = convertView.findViewById(R.id.tme);


        Picasso.get().load(udell.getImageurl()).into(prof);
        effecy anno = new effecy(getContext(), udell.getUserid(), annocru, annoadm, annofri);

        usernme.setText(udell.getUsername());
        naem.setText(udell.getName());


        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return userfilter;
    }

    private Filter userfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<usermodel> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(usrlst);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (usermodel ussmdel : usrlst) {
                    if (ussmdel.getUsername().contains(filterpattern)) {
                        suggestions.add(ussmdel);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((usermodel) resultValue).getUsername();
        }
    };
}
