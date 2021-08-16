package saneforce.sanclm.activities.Model;

import java.util.ArrayList;

public class FeedbackProductDetail {
    String prdNAme;
    String st_end_time;
    String slideName;
    String rating;
    String feedback,date;
    String sample;
    String remTiming;
    String rxQty;
    ArrayList<PopFeed> prodFb;

    public ArrayList<PopFeed> getProdFb() {
        return prodFb;
    }

    public void setProdFb(ArrayList<PopFeed> prodFb) {
        this.prodFb = prodFb;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public FeedbackProductDetail(String prdNAme, String st_end_time, String rating, String feedback, String date, String sample,String remTiming,String rxQty,ArrayList<PopFeed> prodFb) {
        this.prdNAme = prdNAme;
        this.st_end_time = st_end_time;
        this.rating=rating;
        this.feedback=feedback;
        this.date=date;
        this.sample=sample;
        this.remTiming=remTiming;
        this.rxQty=rxQty;
        this.prodFb=prodFb;
    }
    public FeedbackProductDetail(String sample,String feedback,String rating){
        this.sample=sample;
        this.feedback=feedback;
        this.rating=rating;
    }
  /*  public FeedbackProductDetail(String prdNAme, String st_end_time,String slideName) {
        this.prdNAme = prdNAme;
        this.st_end_time = st_end_time;
        this.slideName=slideName;
    }*/

    public FeedbackProductDetail(String slideNam){
        this.prdNAme=slideNam;
    }

    @Override
    public boolean equals(Object obj) {
        FeedbackProductDetail dt = (FeedbackProductDetail)obj;

        if(prdNAme == null) return false;
        if(dt.prdNAme.equals(prdNAme)) return true;

        return false;
    }
    public String getRemTiming() {
        return remTiming;
    }

    public void setRemTiming(String remTiming) {
        this.remTiming = remTiming;
    }
    public String getPrdNAme() {
        return prdNAme;
    }

    public void setPrdNAme(String prdNAme) {
        this.prdNAme = prdNAme;
    }

    public String getSt_end_time() {
        return st_end_time;
    }

    public void setSt_end_time(String st_end_time) {
        this.st_end_time = st_end_time;
    }

    public String getSlideName() {
        return slideName;
    }

    public void setSlideName(String slideName) {
        this.slideName = slideName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getRxQty() {
        return rxQty;
    }

    public void setRxQty(String rxQty) {
        this.rxQty = rxQty;
    }
}

