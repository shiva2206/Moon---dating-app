package adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.honeymoon.R;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnChangeListener;

public class custompaperfragment extends PaperOnboardingFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(container.getContext()).inflate(R.layout.adap_custompaperboard, (ViewGroup) container.getParent(),false);
        return view;
    }

    @Override
    public void setOnChangeListener(PaperOnboardingOnChangeListener onChangeListener) {
        super.setOnChangeListener(onChangeListener);
    }
}
