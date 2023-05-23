package model;

import java.util.HashMap;
import java.util.List;

public class postmodel {
    private String publisherid,postid,uri,time,location,description,type;
    private HashMap<String,String> tags;

    public postmodel(String uri, String type) {
        this.uri = uri;
        this.type = type;
    }

    @Override
    public String toString() {
        return "postmodel{" +
                "publisherid='" + publisherid + '\'' +
                ", postid='" + postid + '\'' +
                ", uri='" + uri + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", tags=" + tags +
                '}';
    }

    public postmodel(String publisherid, String postid, String uri, String time, String location, String description, String type, HashMap<String, String> tags) {
        this.publisherid = publisherid;
        this.postid = postid;
        this.uri = uri;
        this.time = time;
        this.location = location;
        this.description = description;
        this.type = type;
        this.tags = tags;
        if(tags == null){
            this.tags = new HashMap<>();
        }

    }


    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public postmodel(String uri) {
        this.uri = uri;
    }

    public postmodel() {
    }

    public String getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
