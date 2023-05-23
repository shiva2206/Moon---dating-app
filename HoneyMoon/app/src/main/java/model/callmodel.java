package model;

public class callmodel {
    private String userid,going,type,time,talktime,callid;

    public callmodel(String userid, String going, String type, String time, String callid) {
        this.userid = userid;
        this.going = going;
        this.type = type;
        this.time = time;
        this.talktime = "";
        this.callid = callid;
    }

    public callmodel(String userid, String going, String type, String time, String talktime, String callid) {
        this.userid = userid;
        this.going = going;
        this.type = type;
        this.time = time;
        this.talktime = talktime;
        this.callid = callid;
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }

    public String getGoing() {
        return going;
    }

    public void setGoing(String going) {
        this.going = going;
    }

    public callmodel() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTalktime() {
        return talktime;
    }

    public void setTalktime(String talktime) {
        this.talktime = talktime;
    }
}
