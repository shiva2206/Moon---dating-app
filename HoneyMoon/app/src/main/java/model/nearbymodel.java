package model;

public class nearbymodel {
    @Override
    public String toString() {
        return "nearbymodel{" +
                "gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", distance='" + distance + '\'' +
                ", agefrom='" + agefrom + '\'' +
                ", ageto='" + ageto + '\'' +
                '}';
    }

    private String gender,status,distance,agefrom,ageto;

    public nearbymodel() {
    }

    public nearbymodel(String distance) {
        this.distance = distance;
    }

    public nearbymodel(String gender, String status, String distance, String agefrom, String ageto) {
        this.gender = gender;
        this.status = status;
        this.distance = distance;
        this.agefrom = agefrom;
        this.ageto = ageto;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAgefrom() {
        return agefrom;
    }

    public void setAgefrom(String agefrom) {
        this.agefrom = agefrom;
    }

    public String getAgeto() {
        return ageto;
    }

    public void setAgeto(String ageto) {
        this.ageto = ageto;
    }
}
