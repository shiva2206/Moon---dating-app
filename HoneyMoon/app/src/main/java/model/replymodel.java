package model;

public class replymodel {
    private String commentid,reply,replyid,postid,userid,time;

    public replymodel(String commentid, String reply, String replyid, String postid, String userid, String time) {
        this.commentid = commentid;
        this.reply = reply;
        this.replyid = replyid;
        this.postid = postid;
        this.userid = userid;
        this.time = time;
    }

    public replymodel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
