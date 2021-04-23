package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodayTp {
    @SerializedName("SFCode")
    @Expose
    private String sFCode;
    @SerializedName("TPDt")
    @Expose
    private TPDt tPDt;
    @SerializedName("WT")
    @Expose
    private String wT;
    @SerializedName("WTNm")
    @Expose
    private String wTNm;
    @SerializedName("FWFlg")
    @Expose
    private String fWFlg;
    @SerializedName("SFMem")
    @Expose
    private String sFMem;
    @SerializedName("HQNm")
    @Expose
    private String hQNm;
    @SerializedName("Pl")
    @Expose
    private String pl;
    @SerializedName("PlNm")
    @Expose
    private String plNm;
    @SerializedName("Rem")
    @Expose
    private String rem;
    @SerializedName("TpVwFlg")
    @Expose
    private String tpVwFlg;

    public String getSFCode() {
        return sFCode;
    }

    public void setSFCode(String sFCode) {
        this.sFCode = sFCode;
    }

    public TPDt getTPDt() {
        return tPDt;
    }

    public void setTPDt(TPDt tPDt) {
        this.tPDt = tPDt;
    }

    public String getWT() {
        return wT;
    }

    public void setWT(String wT) {
        this.wT = wT;
    }

    public String getWTNm() {
        return wTNm;
    }

    public void setWTNm(String wTNm) {
        this.wTNm = wTNm;
    }

    public String getFWFlg() {
        return fWFlg;
    }

    public void setFWFlg(String fWFlg) {
        this.fWFlg = fWFlg;
    }

    public String getSFMem() {
        return sFMem;
    }

    public void setSFMem(String sFMem) {
        this.sFMem = sFMem;
    }

    public String getHQNm() {
        return hQNm;
    }

    public void setHQNm(String hQNm) {
        this.hQNm = hQNm;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getPlNm() {
        return plNm;
    }

    public void setPlNm(String plNm) {
        this.plNm = plNm;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public String getTpVwFlg() {
        return tpVwFlg;
    }

    public void setTpVwFlg(String tpVwFlg) {
        this.tpVwFlg = tpVwFlg;
    }


}
