package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.honeymoon.R;

import java.util.List;

import model.usermodel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link alltoppers#} factory method to
 * create an instance of this fragment.
 */
public class alltoppers extends Fragment {
    private String genre;
    private List<usermodel> lstusr;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alltoppers, container, false);
    }
}