package saneforce.sanclm.activities.Model;

public class ModelTpApproval {
    String code,name,mnth,yr,mn;

    public ModelTpApproval(String code, String name, String mnth, String yr,String mn) {
        this.code = code;
        this.name = name;
        this.mnth = mnth;
        this.yr = yr;
        this.mn=mn;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMnth() {
        return mnth;
    }

    public void setMnth(String mnth) {
        this.mnth = mnth;
    }

    public String getYr() {
        return yr;
    }

    public void setYr(String yr) {
        this.yr = yr;
    }
}
