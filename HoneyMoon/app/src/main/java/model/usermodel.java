package model;

import adapters.effecy;

public class usermodel {
    private String userid,password,dob,about,email,gender,mium,imageurl,name,status,verified,username,mode,showlastseen,
            showlocation,coverimage,devicetoken,showmat,showcru,showadm,showfri,showaddcrubut,istagpermitted,bluetick,height,weight,swipes;

    public String getSwipes() {
        return swipes;
    }

    public void setSwipes(String swipes) {
        this.swipes = swipes;
    }

    public String getBluetick() {
        return bluetick;
    }

    public void setBluetick(String bluetick) {
        this.bluetick = bluetick;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public String getIstagpermitted() {
        return istagpermitted;
    }

    public void setIstagpermitted(String istagpermitted) {
        this.istagpermitted = istagpermitted;
    }

    public usermodel(String userid, String password, String dob, String about, String email, String gender, String mium, String imageurl, String name, String status, String verified, String username, String mode, String showlastseen, String showlocation, String coverimage, String devicetoken, String showmat, String showcru, String showadm, String showfri, String showaddcrubut, String istagpermitted, String bluetick,String height,String weight,String swipes) {
        this.userid = userid;
        this.password = password;
        this.dob = dob;
        this.about = about;
        this.email = email;
        this.gender = gender;
        this.mium = mium;
        this.imageurl = imageurl;
        this.name = name;
        this.status = status;
        this.verified = verified;
        this.username = username;
        this.mode = mode;
        this.showlastseen = showlastseen;
        this.showlocation = showlocation;
        this.coverimage = coverimage;
        this.devicetoken = devicetoken;
        this.showmat = showmat;
        this.showcru = showcru;
        this.showadm = showadm;
        this.showfri = showfri;
        this.showaddcrubut = showaddcrubut;
        this.istagpermitted = istagpermitted;
        this.bluetick = bluetick;
        this.height=height;
        this.weight=weight;
        this.swipes=swipes;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public usermodel(String userid, String password, String dob, String about, String email, String gender, String mium, String imageurl, String name, String status, String verified, String username, String mode, String showlastseen, String showlocation, String coverimage, String devicetoken, String showmat, String showcru, String showadm, String showfri, String showaddcrubut, String istagpermitted, String bluetick, String height, String weight,String swipes, String crypt) throws Exception {

        if(crypt != null){
            this.userid = userid;
            this.password = password;
            this.dob = dob;
            this.about = about;
            this.email = email;
            this.gender = gender;
            this.mium = mium;
            this.imageurl = imageurl;
            this.name = name;
            this.status = status;
            this.verified = verified;
            this.username = username;
            this.mode = mode;
            this.showlastseen = showlastseen;
            this.showlocation = showlocation;
            this.coverimage = coverimage;
            this.devicetoken = devicetoken;
            this.showmat = showmat;
            this.showcru = showcru;
            this.showadm = showadm;
            this.showfri = showfri;
            this.showaddcrubut = showaddcrubut;
            this.istagpermitted = istagpermitted;
            this.bluetick = bluetick;
            this.height=height;
            this.weight=weight;
            this.swipes=swipes;
        }else{
            this.userid = effecy.security.decrypt(userid);
            this.password = effecy.security.decrypt(password);
            this.dob = dob;
            this.about = about;
            this.email = email;
            this.gender = gender;
            this.mium = mium;
            this.imageurl = imageurl;
            this.name = name;
            this.status = status;
            this.verified = verified;
            this.username = username;
            this.mode = mode;
            this.showlastseen = showlastseen;
            this.showlocation = showlocation;
            this.coverimage = coverimage;
            this.devicetoken = devicetoken;
            this.showmat = showmat;
            this.showcru = showcru;
            this.showadm = showadm;
            this.showfri = showfri;
            this.showaddcrubut = showaddcrubut;
            this.istagpermitted = istagpermitted;
            this.bluetick = bluetick;
            this.height=height;
            this.weight=weight;
            this.swipes=swipes;
        }

    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getShowmat() {
        return showmat;
    }

    public usermodel(String status,String mode, String showlastseen, String showlocation, String showmat, String showcru, String showadm, String showfri, String showaddcrubut) {
        this.status = status;
        this.mode=mode;
        this.showlastseen = showlastseen;
        this.showlocation = showlocation;
        this.showmat = showmat;
        this.showcru = showcru;
        this.showadm = showadm;
        this.showfri = showfri;
        this.showaddcrubut = showaddcrubut;
    }

    public void setShowmat(String showmat) {
        this.showmat = showmat;
    }

    public String getShowcru() {
        return showcru;
    }

    public void setShowcru(String showcru) {
        this.showcru = showcru;
    }

    public String getShowadm() {
        return showadm;
    }

    public void setShowadm(String showadm) {
        this.showadm = showadm;
    }

    public String getShowfri() {
        return showfri;
    }

    public void setShowfri(String showfri) {
        this.showfri = showfri;
    }

    public usermodel(String userid, String password, String dob, String about, String email, String gender, String mium, String imageurl, String name, String status, String verified, String username,
                     String mode, String showlastseen, String showlocation, String coverimage, String devicetoken, String showmat, String showcru, String showadm, String showfri,String showaddcrubut) {
        this.userid = userid;
        this.password = password;
        this.dob = dob;

        this.about = about;
        this.email = email;
        this.gender = gender;

        this.mium = mium;
        this.imageurl = imageurl;
        this.name = name;

        this.status = status;

        this.verified = verified;

        this.username = username;
        this.mode = mode;
        this.showlastseen = showlastseen;

        this.showlocation = showlocation;
        this.coverimage = coverimage;
        this.devicetoken = devicetoken;
        this.showmat = showmat;

        this.showcru = showcru;
        this.showadm = showadm;
        this.showfri = showfri;
        this.showaddcrubut=showaddcrubut;
    }

    public String getMium() {
        return mium;
    }

    public String getShowaddcrubut() {
        return showaddcrubut;
    }

    public void setShowaddcrubut(String showaddcrubut) {
        this.showaddcrubut = showaddcrubut;
    }

    public void setMium(String mium) {
        this.mium = mium;
    }

    public String getShowlocation() {
        return showlocation;
    }

    public void setShowlocation(String showlocation) {
        this.showlocation = showlocation;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        this.coverimage = coverimage;
    }

    public String getShowlastseen() {
        return showlastseen;
    }


    public void setShowlastseen(String showlastseen) {
        this.showlastseen = showlastseen;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public usermodel() {
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "usermodel{" +
                "userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", dob='" + dob + '\'' +
                ", about='" + about + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", mium='" + mium + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", verified='" + verified + '\'' +
                ", username='" + username + '\'' +
                ", mode='" + mode + '\'' +
                ", showlastseen='" + showlastseen + '\'' +
                ", showlocation='" + showlocation + '\'' +
                ", coverimage='" + coverimage + '\'' +
                ", devicetoken='" + devicetoken + '\'' +
                ", showmat='" + showmat + '\'' +
                ", showcru='" + showcru + '\'' +
                ", showadm='" + showadm + '\'' +
                ", showfri='" + showfri + '\'' +
                ", showaddcrubut='" + showaddcrubut + '\'' +
                '}';
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
