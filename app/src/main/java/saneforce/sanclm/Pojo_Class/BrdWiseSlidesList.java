package saneforce.sanclm.Pojo_Class;

import java.util.ArrayList;

public class BrdWiseSlidesList {

    private String BrdCode;
    private String BrdName;
    private ArrayList<SlidesDetails> slidesDetails;

    public String getBrdCode() {
        return BrdCode;
    }

    public void setBrdCode(String brdCode) {
        BrdCode = brdCode;
    }

    public String getBrdName() {
        return BrdName;
    }

    public void setBrdName(String brdName) {
        BrdName = brdName;
    }

    public ArrayList<SlidesDetails> getSlidesDetails() {
        return slidesDetails;
    }

    public void setSlidesDetails(ArrayList<SlidesDetails> slidesDetails) {
        this.slidesDetails = slidesDetails;
    }
}
