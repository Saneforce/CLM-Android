package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stockists {

    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Town_Code")
    @Expose
    private String townCode;
    @SerializedName("Town_Name")
    @Expose
    private String townName;
    @SerializedName("Addr")
    @Expose
    private String addr;
    @SerializedName("Stockiest_Phone")
    @Expose
    private String stockiestPhone;
    @SerializedName("Stockiest_Mobile")
    @Expose
    private String stockiestMobile;
    @SerializedName("Stockiest_Email")
    @Expose
    private String stockiestEmail;
    @SerializedName("Stockiest_Cont_Desig")
    @Expose
    private String stockiestContDesig;
    @SerializedName("Sto_Credit_Days")
    @Expose
    private String stoCreditDays;
    @SerializedName("Sto_Credit_Limit")
    @Expose
    private String stoCreditLimit;
    @SerializedName("Division_Code")
    @Expose
    private String divisionCode;
    @SerializedName("SF_code")
    @Expose
    private String sFCode;
    @SerializedName("GEOTagCnt")
    @Expose
    private String tagCnt;
    @SerializedName("MaxGeoMap")
    @Expose
    private String maxCnt;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String longi;

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

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getStockiestPhone() {
        return stockiestPhone;
    }

    public void setStockiestPhone(String stockiestPhone) {
        this.stockiestPhone = stockiestPhone;
    }

    public String getStockiestMobile() {
        return stockiestMobile;
    }

    public void setStockiestMobile(String stockiestMobile) {
        this.stockiestMobile = stockiestMobile;
    }

    public String getStockiestEmail() {
        return stockiestEmail;
    }

    public void setStockiestEmail(String stockiestEmail) {
        this.stockiestEmail = stockiestEmail;
    }

    public String getStockiestContDesig() {
        return stockiestContDesig;
    }

    public void setStockiestContDesig(String stockiestContDesig) {
        this.stockiestContDesig = stockiestContDesig;
    }

    public String getStoCreditDays() {
        return stoCreditDays;
    }

    public void setStoCreditDays(String stoCreditDays) {
        this.stoCreditDays = stoCreditDays;
    }

    public String getStoCreditLimit() {
        return stoCreditLimit;
    }

    public void setStoCreditLimit(String stoCreditLimit) {
        this.stoCreditLimit = stoCreditLimit;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getSFCode() {
        return sFCode;
    }

    public void setSFCode(String sFCode) {
        this.sFCode = sFCode;
    }

    public String getTagCnt() {
        return tagCnt;
    }

    public void setTagCnt(String tagCnt) {
        this.tagCnt = tagCnt;
    }

    public String getMaxCnt() {
        return maxCnt;
    }

    public void setMaxCnt(String maxCnt) {
        this.maxCnt = maxCnt;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }
}
