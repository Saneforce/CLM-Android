package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnListedDoctorList {@SerializedName("Code")
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
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("SpecialtyName")
    @Expose
    private String specialtyName;
    @SerializedName("Specialty")
    @Expose
    private String specialty;
    @SerializedName("SF_Code")
    @Expose
    private String sFCode;
    @SerializedName("Qual")
    @Expose
    private String qual;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Addrs")
    @Expose
    private String addrs;
    @SerializedName("GEOTagCnt")
    @Expose
    private String tagCnt;
    @SerializedName("MaxGeoMap")
    @Expose
    private String maxCnt;
    @SerializedName("Doc_hospcode")
    @Expose
    private String Doc_hospcode;
    @SerializedName("Doc_hospname")
    @Expose
    private String Doc_hospname;



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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSFCode() {
        return sFCode;
    }

    public void setSFCode(String sFCode) {
        this.sFCode = sFCode;
    }

    public String getQual() {
        return qual;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddrs() {
        return addrs;
    }

    public void setAddrs(String addrs) {
        this.addrs = addrs;
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

    public String getDoc_hospcode() {
        return Doc_hospcode;
    }

    public String getDoc_hospname() {
        return Doc_hospname;
    }
}