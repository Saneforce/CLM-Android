package saneforce.sanclm.User_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerMe {
    @SerializedName("SF_Code")
    @Expose
    private String sfCode;
    @SerializedName("SF_Name")
    @Expose
    private String sfName;
    @SerializedName("divisionCode")
    @Expose
    private String divCode;
    @SerializedName("desigCode")
    @Expose
    private String desigCode;

    @SerializedName("SFStat")
    @Expose
    private String sfStat;

    @SerializedName("UserN")
    @Expose
    private String username;

    @SerializedName("Pass")
    @Expose
    private String password;

    @SerializedName("call_report")
    @Expose
    private String callreport;

    @SerializedName("quote_Text")
    @Expose
    private String quotetext;

    @SerializedName("call_report_from_date")
    @Expose
    private String call_report_from_date;

    @SerializedName("call_report_to_date")
    @Expose
    private String call_report_to_date;

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

    public String getDivisionCode() {
        return divCode;
    }

    public void setDivisionCode(String divCode) {
        this.divCode = divCode;
    }
    public String getDesigCode() {
        return desigCode;
    }

    public void setDesigCode(String desigCode) {
        this.desigCode = desigCode;
    }
    public String getSFStat() {
        return sfStat;
    }

    public void setSFStat(String sfStat) {
        this.sfStat = sfStat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCallreport() {
        return callreport;
    }

    public void setCallreport(String callreport) {
        this.callreport = callreport;
    }

    public String getQuotetext() {
        return quotetext;
    }

    public void setQuotetext(String quotetext) {
        this.quotetext = quotetext;
    }

    public String getCall_report_from_date() {
        return call_report_from_date;
    }

    public void setCall_report_from_date(String call_report_from_date) {
        this.call_report_from_date = call_report_from_date;
    }

    public String getCall_report_to_date() {
        return call_report_to_date;
    }

    public void setCall_report_to_date(String call_report_to_date) {
        this.call_report_to_date = call_report_to_date;
    }
}
