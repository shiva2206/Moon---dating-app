package model;

import adapters.effecy;

public class statusmodel {
    private String publisherid,type,uri,statusid,time;

    public statusmodel() {
    }

    public statusmodel(String publisherid, String type, String uri, String statusid, String time) {
        this.publisherid = publisherid;
        this.type = type;
        this.uri = uri;
        this.statusid = statusid;
        this.time = time;
    }

    public statusmodel(String publisherid, String type, String uri, String statusid, String time, String seen, String crypt) throws Exception {
        if(crypt == null){
            this.publisherid = publisherid;
            this.type = type;
            this.uri = uri;
            this.statusid = statusid;
            this.time = time;
        }else{
            this.publisherid = effecy.security.decrypt(publisherid);
            this.type = effecy.security.decrypt(type);
            this.uri = uri;
            this.statusid = statusid;
            this.time = time;
        }


    }


    public String getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getStatusid() {
        return statusid;
    }

    public void setStatusid(String statusid) {
        this.statusid = statusid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
