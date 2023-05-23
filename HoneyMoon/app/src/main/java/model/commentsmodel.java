package model;

public class commentsmodel {
    private String postid,date,commentid,comment,userid,publisherid,time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public commentsmodel(String postid, String date, String commentid, String comment, String userid, String publisherid, String time) {
        this.postid = postid;
        this.date = date;
        this.commentid = commentid;
        this.comment = comment;
        this.userid = userid;
        this.publisherid = publisherid;
        this.time = time;
    }

    public String getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public commentsmodel() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
