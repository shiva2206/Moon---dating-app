package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.honeymoon.R;
import com.google.firebase.auth.FirebaseAuth;

import adapters.searchadp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nearby_old#} factory method to
 * create an instance of this fragment.
 */
public class nearby_old extends Fragment {

   private Fragment fra;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_nearby_old, container, false);

        fra = new search(false, FirebaseAuth.getInstance().getCurrentUser().getUid(),"nearbyold");
        getChildFragmentManager().beginTransaction().replace(R.id.frag,fra).commitAllowingStateLoss();

        return view;
    }
}