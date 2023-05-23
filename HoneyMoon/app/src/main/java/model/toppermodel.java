package model;

import java.util.List;

public class toppermodel {
    private usermodel usermodel;
    private List<Integer> matadmfri;
    private Double points;

    public toppermodel(model.usermodel usermodel, List<Integer> matadmfri, Double points) {
        this.usermodel = usermodel;
        this.matadmfri = matadmfri;
        this.points = points;
    }



    public model.usermodel getUsermodel() {
        return usermodel;
    }

    public void setUsermodel(model.usermodel usermodel) {
        this.usermodel = usermodel;
    }

    public List<Integer> getMatadmfri() {
        return matadmfri;
    }

    public void setMatadmfri(List<Integer> matadmfri) {
        this.matadmfri = matadmfri;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }
}
