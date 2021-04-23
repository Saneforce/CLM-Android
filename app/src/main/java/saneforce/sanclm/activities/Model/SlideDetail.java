package saneforce.sanclm.activities.Model;

public class SlideDetail {
    String slideName;
    String startTime;
    String endTime;
    String inputName,iqty;
    String slideingname;

    public SlideDetail(String slideName,String startTime,String endTime){
        this.slideName=slideName;
        this.startTime=startTime;
        this.endTime=endTime;
    }
    public SlideDetail(String slideName,String startTime,String endTime,String slideingname){
        this.slideName=slideName;
        this.startTime=startTime;
        this.endTime=endTime;
        this.slideingname=slideingname;
    }

    public SlideDetail(String inputName, String iqty) {
        this.inputName = inputName;
        this.iqty = iqty;
    }
    public SlideDetail(String inputName){
        this.inputName=inputName;
    }
    @Override
    public boolean equals(Object obj) {
        SlideDetail dt = (SlideDetail)obj;

        if(inputName == null) return false;
        if(dt.inputName.equals(inputName)) return true;

        return false;
    }
    public String getSlideingname() {
        return slideingname;
    }

    public void setSlideingname(String slideingname) {
        this.slideingname = slideingname;
    }

    public String getSlideName() {
        return slideName;
    }

    public void setSlideName(String slideName) {
        this.slideName = slideName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getIqty() {
        return iqty;
    }

    public void setIqty(String iqty) {
        this.iqty = iqty;
    }
}
