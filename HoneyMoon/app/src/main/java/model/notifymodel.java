package model;

public class notifymodel {
    private String subject,notify,notifyid,postid,publisherid,userid,time;

    public notifymodel(String subject, String notify, String notifyid, String postid, String publisherid, String userid, String time) {
        this.subject = subject;
        this.notify = notify;
        this.notifyid = notifyid;
        this.postid = postid;
        this.publisherid = publisherid;
        this.userid = userid;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public notifymodel() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotify() {
        if(notify == null){
            return "";
        }
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getNotifyid() {
        return notifyid;
    }

    public void setNotifyid(String notifyid) {
        this.notifyid = notifyid;
    }

    public String getPostid() {
        if(postid == null){
            return "";
        }
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisherid() {
        if(publisherid == null){
            return "";
        }
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
