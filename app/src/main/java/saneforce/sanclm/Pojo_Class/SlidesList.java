package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlidesList {

    @SerializedName("SlideId")
    @Expose
    private String slideId;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Product_Detail_Code")
    @Expose
    private String productDetailCode;
    @SerializedName("FilePath")
    @Expose
    private String filePath;
    @SerializedName("FileTyp")
    @Expose
    private String fileTyp;
    @SerializedName("Speciality_Code")
    @Expose
    private String specialityCode;
    @SerializedName("Category_Code")
    @Expose
    private String categoryCode;
    @SerializedName("Eff_from")
    @Expose
    private DoctorEffFrom effFrom;
    @SerializedName("EffTo")
    @Expose
    private EffTo effTo;
    @SerializedName("Group")
    @Expose
    private Integer group;
    @SerializedName("Camp")
    @Expose
    private String camp;
    @SerializedName("OrdNo")
    @Expose
    private Integer ordNo;
    @SerializedName("Priority")
    @Expose
    private Integer priority;

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
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

    public String getProductDetailCode() {
        return productDetailCode;
    }

    public void setProductDetailCode(String productDetailCode) {
        this.productDetailCode = productDetailCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileTyp() {
        return fileTyp;
    }

    public void setFileTyp(String fileTyp) {
        this.fileTyp = fileTyp;
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public DoctorEffFrom getEffFrom() {
        return effFrom;
    }

    public void setEffFrom(DoctorEffFrom effFrom) {
        this.effFrom = effFrom;
    }

    public EffTo getEffTo() {
        return effTo;
    }

    public void setEffTo(EffTo effTo) {
        this.effTo = effTo;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getCamp() {
        return camp;
    }

    public void setCamp(String camp) {
        this.camp = camp;
    }

    public Integer getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(Integer ordNo) {
        this.ordNo = ordNo;
    }
}
