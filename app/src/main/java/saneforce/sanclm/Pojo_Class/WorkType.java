package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkType {
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ETabs")
    @Expose
    private String eTabs;
    @SerializedName("TerrSlFlg")
    @Expose
    private String fWFlg;

    @SerializedName("TP_DCR")
    @Expose
    private String tpdcr;
/*
    @SerializedName("FWFlg")
    @Expose
    private String fWFlg;
*/
    @SerializedName("SF_Code")
    @Expose
    private String sFCode;

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

    public String getETabs() {
        return eTabs;
    }

    public void setETabs(String eTabs) {
        this.eTabs = eTabs;
    }

    public String getFWFlg() {
        return fWFlg;
    }

    public void setFWFlg(String fWFlg) {
        this.fWFlg = fWFlg;
    }

    public String getSFCode() {
        return sFCode;
    }

    public void setSFCode(String sFCode) {
        this.sFCode = sFCode;
    }

    public String getTpdcr() {
        return tpdcr;
    }

    public void setTpdcr(String tpdcr) {
        this.tpdcr = tpdcr;
    }
}
