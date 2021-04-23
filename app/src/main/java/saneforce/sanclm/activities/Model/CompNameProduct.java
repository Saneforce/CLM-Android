package saneforce.sanclm.activities.Model;

public class CompNameProduct {
    String compName;
    String compPrd;
    boolean compSetting;
    String qty,rate,value,choosenPrdName;
    String compCode,compPCode;

    public CompNameProduct(String compName, String compPrd,String compCode,String compPCode) {
        this.compName = compName;
        this.compPrd = compPrd;
        this.compCode=compCode;
        this.compPCode=compPCode;
    }

    public CompNameProduct(String compName, String compPrd, boolean compSetting, String qty, String rate, String value,String choosenPrdName,String compCode,String compPCode) {
        this.compName = compName;
        this.compPrd = compPrd;
        this.compSetting = compSetting;
        this.qty = qty;
        this.rate = rate;
        this.value = value;
        this.choosenPrdName=choosenPrdName;
        this.compCode=compCode;
        this.compPCode=compPCode;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompPrd() {
        return compPrd;
    }

    public void setCompPrd(String compPrd) {
        this.compPrd = compPrd;
    }

    public boolean isCompSetting() {
        return compSetting;
    }

    public void setCompSetting(boolean compSetting) {
        this.compSetting = compSetting;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getChoosenPrdName() {
        return choosenPrdName;
    }

    public void setChoosenPrdName(String choosenPrdName) {
        this.choosenPrdName = choosenPrdName;
    }


    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompPCode() {
        return compPCode;
    }

    public void setCompPCode(String compPCode) {
        this.compPCode = compPCode;
    }
}
