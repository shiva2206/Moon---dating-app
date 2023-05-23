package model;

public class chatmodel {
    private String userid,type,chat,chatid,liked,time,seen,uri,replyid;

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public chatmodel(String userid, String time) {
        this.userid = userid;
        this.time = time;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public chatmodel(String userid, String type, String chat, String chatid, String liked, String time, String seen, String uri, String replyid) {
        this.userid = userid;
        this.type = type;
        this.chat = chat;
        this.chatid = chatid;
        this.liked = liked;
        this.time = time;
        this.seen = seen;
        this.uri = uri;
        this.replyid = replyid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public chatmodel(String userid, String type, String chat, String chatid, String time, String seen) {
        this.userid = userid;
        this.type = type;
        this.chat = chat;
        this.chatid = chatid;
        this.time = time;
        this.seen = seen;
    }

    public chatmodel() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
