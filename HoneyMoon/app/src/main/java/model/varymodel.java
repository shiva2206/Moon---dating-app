package model;

import adapters.effecy;

public class varymodel {
    private String userid,latitude,longitude,line;

    public varymodel(String userid, String latitude, String longitude, String line) throws Exception {

//        this.userid = effecy.security.decrypt(userid);
//        this.latitude = effecy.security.decrypt(latitude);
//        this.longitude = effecy.security.decrypt(longitude);
//        this.line = effecy.security.decrypt(line);

        this.userid = userid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.line = line;
    }
    public varymodel(String userid, String latitude, String longitude, String line,String crypt) throws Exception {
        if(crypt == null) {
            this.userid = userid;
            this.latitude = latitude;
            this.longitude = longitude;
            this.line = line;
        }else{
            this.userid = effecy.security.encrypt(userid);
            this.latitude = effecy.security.encrypt(latitude);
            this.longitude = effecy.security.encrypt(longitude);
            this.line = effecy.security.encrypt(line);
        }
    }

    public varymodel() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
