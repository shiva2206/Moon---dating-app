package model;

import java.util.HashMap;
import java.util.List;

public class highmodel {
    private String highlightid,title,imageuri,time;
    private HashMap<String,String> statusmodelist;

    public highmodel(String highlightid, String title, String imageuri, String time, HashMap<String, String> statusmodelist) {
        this.highlightid = highlightid;
        this.title = title;
        this.imageuri = imageuri;
        this.time = time;
        this.statusmodelist = statusmodelist;
    }

    public HashMap<String, String> getStatusmodelist() {
        return statusmodelist;
    }

    public void setStatusmodelist(HashMap<String, String> statusmodelist) {
        this.statusmodelist = statusmodelist;
    }

    public highmodel() {
    }

    public String getHighlightid() {
        return highlightid;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public void setHighlightid(String highlightid) {
        this.highlightid = highlightid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
