package model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class mainviewmodel extends ViewModel {
    MutableLiveData<statusmodel> mutableLiveata = new MutableLiveData<>();
    MutableLiveData<usermodel> usermutalivdata = new MutableLiveData<>();

    public mainviewmodel(MutableLiveData<statusmodel> mutableLiveData) {
        this.mutableLiveata = mutableLiveData;
    }

    public void setMutableLiveData(statusmodel stas) {
        mutableLiveata.setValue(stas);
    }

    public MutableLiveData<usermodel> getUsermutalivdata() {
        return usermutalivdata;
    }

    public void setUsermutalivdata(MutableLiveData<usermodel> usermutalivdata) {
        this.usermutalivdata = usermutalivdata;
    }

    public MutableLiveData<statusmodel> getMutableLiveData() {
        return mutableLiveata;
    }
}
