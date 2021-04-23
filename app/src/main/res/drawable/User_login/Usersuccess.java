package com.example.myapplication.User_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usersuccess {
    @SerializedName("SF_Code")
    @Expose
    private String sfCode;
    @SerializedName("SF_Name")
    @Expose
    private String sfName;
    @SerializedName("SFStat")
    @Expose
    private String sfStatus;
    @SerializedName("Division_Code")
    @Expose
    private Integer divisionCode;

    @SerializedName("desigCode")
    @Expose
    private String desigCode;

    public String getSFCode() {
        return sfCode;
    }

    public void setSFCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getSFName() {
        return sfName;
    }

    public void setSFName(String sfName) {
        this.sfName = sfName;
    }

 /*   public int getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(int divisionCode) {
        this.divisionCode = divisionCode;
    }
    public String getDesigCode() {
        return desigCode;
    }

    public void setDesigCode(String desigCode) {
        this.desigCode = desigCode;
    }
    public String getSFStat() {
        return sfStatus;
    }

    public void setSFStat(String sfStatus) {
        this.sfStatus = sfStatus;
    }*/

}
