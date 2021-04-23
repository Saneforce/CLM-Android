package saneforce.sanclm.activities.Model;

public class ModelInnerReportDetail {
    String prd,sample,rx,feedback;

    public ModelInnerReportDetail(String prd, String sample, String rx, String feedback) {
        this.prd = prd;
        this.sample = sample;
        this.rx = rx;
        this.feedback = feedback;
    }

    public ModelInnerReportDetail(String prd,String rx){
        this.prd = prd;
        this.rx = rx;
    }

    public String getPrd() {
        return prd;
    }

    public void setPrd(String prd) {
        this.prd = prd;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getRx() {
        return rx;
    }

    public void setRx(String rx) {
        this.rx = rx;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
