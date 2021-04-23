package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chemists {


    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Addr")
    @Expose
    private String addr;
    @SerializedName("Town_Code")
    @Expose
    private String townCode;
    @SerializedName("Town_Name")
    @Expose
    private String townName;
    @SerializedName("Chemists_Phone")
    @Expose
    private String chemistsPhone;
    @SerializedName("Chemists_Mobile")
    @Expose
    private String chemistsMobile;
    @SerializedName("Chemists_Fax")
    @Expose
    private String chemistsFax;
    @SerializedName("Chemists_Email")
    @Expose
    private String chemistsEmail;
    @SerializedName("Chemists_Contact")
    @Expose
    private String chemistsContact;
    @SerializedName("SF_Code")
    @Expose
    private String sFCode;
    @SerializedName("GEOTagCnt")
    @Expose
    private String tagCnt;
    @SerializedName("MaxGeoMap")
    @Expose
    private String maxCnt;

    @SerializedName("Hospital_Code")
    @Expose
    private String hosCode;

    public String getHosCode() {
        return hosCode;
    }

    public void setHosCode(String hosCode) {
        this.hosCode = hosCode;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
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

    public String getChemistsPhone() {
        return chemistsPhone;
    }

    public void setChemistsPhone(String chemistsPhone) {
        this.chemistsPhone = chemistsPhone;
    }

    public String getChemistsMobile() {
        return chemistsMobile;
    }

    public void setChemistsMobile(String chemistsMobile) {
        this.chemistsMobile = chemistsMobile;
    }

    public String getChemistsFax() {
        return chemistsFax;
    }

    public void setChemistsFax(String chemistsFax) {
        this.chemistsFax = chemistsFax;
    }

    public String getChemistsEmail() {
        return chemistsEmail;
    }

    public void setChemistsEmail(String chemistsEmail) {
        this.chemistsEmail = chemistsEmail;
    }

    public String getChemistsContact() {
        return chemistsContact;
    }

    public void setChemistsContact(String chemistsContact) {
        this.chemistsContact = chemistsContact;
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
}
