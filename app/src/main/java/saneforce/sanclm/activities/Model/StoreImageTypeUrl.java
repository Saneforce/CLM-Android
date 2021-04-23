package saneforce.sanclm.activities.Model;

import java.io.File;

public class StoreImageTypeUrl {
    String slideNam;
    String slideTyp;
    String slideUrl;
    String timing;
    String slideFeed;
    String slideid;
    String brdName;
    String brdCode;
    String remTime;
    int columnid;
    File ffile;
    String rating;
    public StoreImageTypeUrl(String slideNam, String slideTyp, String slideUrl,String brdName,String timing) {
        this.slideNam = slideNam;
        this.slideTyp = slideTyp;
        this.slideUrl = slideUrl;
        this.brdName=brdName;
        this.timing=timing;
    }

    public StoreImageTypeUrl(String slideNam){
        this.slideNam=slideNam;
    }

    @Override
    public boolean equals(Object obj) {
        StoreImageTypeUrl dt = (StoreImageTypeUrl)obj;

        if(slideNam == null) return false;
        if(dt.slideNam.equals(slideNam)) return true;

        return false;
    }

    /*
        public StoreImageTypeUrl(String timing,String slideFeed,String brdName,String slideNam,String slideTyp,String slideUrl){
            this.slideNam = slideNam;
            this.slideTyp = slideTyp;
            this.slideUrl = slideUrl;
            this.timing=timing;
            this.slideFeed=slideFeed;
            this.brdName=brdName;

        }
    */
    public StoreImageTypeUrl(String slideNam, String slideTyp, String slideUrl,String timing,String slideFeed,String remTime) {
        this.slideNam = slideNam;
        this.slideTyp = slideTyp;
        this.slideUrl = slideUrl;
        this.timing=timing;
        this.slideFeed=slideFeed;
        this.remTime=remTime;
    }

    public StoreImageTypeUrl(String slideNam, String slideTyp, String slideUrl,String timing,String slideFeed,String remTime,int columnid,String rating) {
        this.slideNam = slideNam;
        this.slideTyp = slideTyp;
        this.slideUrl = slideUrl;
        this.timing=timing;
        this.slideFeed=slideFeed;
        this.remTime=remTime;
        this.columnid=columnid;
        this.rating=rating;
    }
    public StoreImageTypeUrl(String slideNam, String slideTyp, String slideUrl,String timing,String slideFeed,String remTime,String brdName,boolean xx) {
        this.slideNam = slideNam;
        this.slideTyp = slideTyp;
        this.slideUrl = slideUrl;
        this.timing=timing;
        this.slideFeed=slideFeed;
        this.remTime=remTime;
        this.brdName=brdName;
    }
    public StoreImageTypeUrl(String slideNam, String slideTyp, String slideUrl,String timing,String slideid,String brdName,String brdCode) {
        this.slideNam = slideNam;
        this.slideTyp = slideTyp;
        this.slideUrl = slideUrl;
        this.timing=timing;
        this.slideid=slideid;
        this.brdName=brdName;
        this.brdCode=brdCode;



    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public File getFfile() {
        return ffile;
    }

    public void setFfile(File ffile) {
        this.ffile = ffile;
    }

    public int getColumnid() {
        return columnid;
    }

    public void setColumnid(int columnid) {
        this.columnid = columnid;
    }

    public String getRemTime() {
        return remTime;
    }

    public void setRemTime(String remTime) {
        this.remTime = remTime;
    }

    public String getSlideid() {
        return slideid;
    }

    public void setSlideid(String slideid) {
        this.slideid = slideid;
    }

    public String getBrdName() {
        return brdName;
    }

    public void setBrdName(String brdName) {
        this.brdName = brdName;
    }

    public String getBrdCode() {
        return brdCode;
    }

    public void setBrdCode(String brdCode) {
        this.brdCode = brdCode;
    }

    public String getSlideNam() {
        return slideNam;
    }

    public void setSlideNam(String slideNam) {
        this.slideNam = slideNam;
    }

    public String getSlideTyp() {
        return slideTyp;
    }

    public void setSlideTyp(String slideTyp) {
        this.slideTyp = slideTyp;
    }

    public String getSlideUrl() {
        return slideUrl;
    }

    public void setSlideUrl(String slideUrl) {
        this.slideUrl = slideUrl;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getSlideFeed() {
        return slideFeed;
    }

    public void setSlideFeed(String slideFeed) {
        this.slideFeed = slideFeed;
    }
}

