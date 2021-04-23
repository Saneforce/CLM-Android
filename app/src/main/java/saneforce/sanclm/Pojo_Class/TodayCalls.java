package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayCalls {
    @SerializedName("ADetSLNo")
    @Expose
    private String aDetSLNo;
    @SerializedName("CustCode")
    @Expose
    private String custCode;
    @SerializedName("CustName")
    @Expose
    private String custName;
    @SerializedName("vstTime")
    @Expose
    private String vstTime;
    @SerializedName("CustType")
    @Expose
    private String custType;
    @SerializedName("Synced")
    @Expose
    private Integer synced;

    public String getADetSLNo() {
        return aDetSLNo;
    }

    public void setADetSLNo(String aDetSLNo) {
        this.aDetSLNo = aDetSLNo;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getVstTime() {
        return vstTime;
    }

    public void setVstTime(String vstTime) {
        this.vstTime = vstTime;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public Integer getSynced() {
        return synced;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }
}
