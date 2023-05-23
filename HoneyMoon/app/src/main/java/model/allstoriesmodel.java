package model;

import java.util.ArrayList;
import java.util.List;

public class allstoriesmodel {
    private static allstoriesmodel Instance;
    private static ArrayList<String> selected;


    public static allstoriesmodel getInstance() {
        if(Instance == null){
            Instance = new allstoriesmodel();
            return Instance;
        }else{
            Instance = Instance;
            return Instance;
        }

    }

    public static void setInstance(allstoriesmodel instance) {
        Instance = instance;
    }


    public allstoriesmodel() {
        if(selected == null){
            selected = new ArrayList<>();
        }else{
            selected = selected;
        }
    }

    public static ArrayList<String> getSelected() {
        return selected;
    }

    public static void setSelected(ArrayList<String> selected) {
        allstoriesmodel.selected = selected;
    }
}
