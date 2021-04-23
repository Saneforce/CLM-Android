package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JointWork {
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("SfName")
    @Expose
    private String sfName;
    @SerializedName("Reporting_To_SF")
    @Expose
    private String reportingToSF;
    @SerializedName("OwnDiv")
    @Expose
    private String ownDiv;
    @SerializedName("Division_Code")
    @Expose
    private String divisionCode;
    @SerializedName("SF_Status")
    @Expose
    private String sFStatus;
    @SerializedName("ActFlg")
    @Expose
    private String actFlg;
    @SerializedName("UsrDfd_UserName")
    @Expose
    private String usrDfdUserName;
    @SerializedName("sf_type")
    @Expose
    private String sfType;
    @SerializedName("Desig")
    @Expose
    private String desig;

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

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public String getReportingToSF() {
        return reportingToSF;
    }

    public void setReportingToSF(String reportingToSF) {
        this.reportingToSF = reportingToSF;
    }

    public String getOwnDiv() {
        return ownDiv;
    }

    public void setOwnDiv(String ownDiv) {
        this.ownDiv = ownDiv;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getSFStatus() {
        return sFStatus;
    }

    public void setSFStatus(String sFStatus) {
        this.sFStatus = sFStatus;
    }

    public String getActFlg() {
        return actFlg;
    }

    public void setActFlg(String actFlg) {
        this.actFlg = actFlg;
    }

    public String getUsrDfdUserName() {
        return usrDfdUserName;
    }

    public void setUsrDfdUserName(String usrDfdUserName) {
        this.usrDfdUserName = usrDfdUserName;
    }

    public String getSfType() {
        return sfType;
    }

    public void setSfType(String sfType) {
        this.sfType = sfType;
    }

    public String getDesig() {
        return desig;
    }

    public void setDesig(String desig) {
        this.desig = desig;
    }

}