package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("pSlNo")
    @Expose
    private String pSlNo;
    @SerializedName("cateid")
    @Expose
    private String cateid;
    @SerializedName("Division_Code")
    @Expose
    private String divisionCode;
    @SerializedName("ActFlg")
    @Expose
    private String actFlg;
    @SerializedName("DRate")
    @Expose
    private String dRate;

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

    public String getPSlNo() {
        return pSlNo;
    }

    public void setPSlNo(String pSlNo) {
        this.pSlNo = pSlNo;
    }

    public String getCateid() {
        return cateid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getActFlg() {
        return actFlg;
    }

    public void setActFlg(String actFlg) {
        this.actFlg = actFlg;
    }

    public String getDRate() {
        return dRate;
    }

    public void setDRate(String dRate) {
        this.dRate = dRate;
    }
}
