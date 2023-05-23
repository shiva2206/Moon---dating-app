package NotificationPack;

public class Data {
    private String Title;
    private String Message;
    private String Type;
    private String Whoid;
    private String Imageurl;
    private String Towhom;
    private String Postid;
    private int Hashid;

    public Data(String title, String message, String type, String whoid, String imageurl, String towhom, String postid, int hashid) {
        Title = title;
        Message = message;
        Type = type;
        Whoid = whoid;
        Imageurl = imageurl;
        Towhom = towhom;
        Postid = postid;
        Hashid = hashid;
    }

    public int getHashid() {
        return Hashid;
    }

    public void setHashid(int hashid) {
        Hashid = hashid;
    }

    public String getTowhom() {
        return Towhom;
    }

    public void setTowhom(String towhom) {
        Towhom = towhom;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public String getPostid() {
        return Postid;
    }

    public void setPostid(String postid) {
        Postid = postid;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }



    public Data() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getWhoid() {
        return Whoid;
    }

    public void setWhoid(String whoid) {
        Whoid = whoid;
    }

    public String getMessage() {
        return Message;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
