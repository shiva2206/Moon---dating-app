package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.honeymoon.R;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import adapters.searchadp;
import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search_username# factory method to
 * create an instance of this fragment.
 */
public class search_username extends Fragment {

    private List<usermodel> lstusermdl;
    private searchadp adp;
    private RecyclerView recyvw;
    public search_username(List<usermodel> lstusermdl) {
        this.lstusermdl = lstusermdl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_username, container, false);
        adp = new searchadp(getContext(),lstusermdl,"ds0","SDfsf");
        recyvw = view.findViewById(R.id.recyi);
        recyvw.setAdapter(adp);
        recyvw.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;


    }
}