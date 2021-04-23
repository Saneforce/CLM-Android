package saneforce.sanclm.adapter_class;

public class Custom_Todaycalls_contents {

    private String DrCode,DrName,ARCd,VstDt,VstTyp,type,common;
    Integer Sync;
    boolean local=false;

    public Custom_Todaycalls_contents(String drCode, String drName, String ARCd, String vstDt, String vstTyp, Integer sync) {
        this.DrCode = drCode;
        this.DrName = drName;
        this.ARCd = ARCd;
        this.VstDt = vstDt;
        this.VstTyp = vstTyp;
        this.Sync = sync;
    }
    public Custom_Todaycalls_contents(String drCode, String drName, String ARCd, String vstDt, String vstTyp, Integer sync,String type,String common,boolean local) {
        this.DrCode = drCode;
        this.DrName = drName;
        this.ARCd = ARCd;
        this.VstDt = vstDt;
        this.VstTyp = vstTyp;
        this.Sync = sync;
        this.type=type;
        this.common=common;
        this.local=local;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getDrCode() {
        return DrCode;
    }

    public void setDrCode(String drCode) {
        DrCode = drCode;
    }

    public String getDrName() {
        return DrName;
    }

    public void setDrName(String drName) {
        DrName = drName;
    }

    public String getARCd() {
        return ARCd;
    }

    public void setARCd(String ARCd) {
        this.ARCd = ARCd;
    }

    public String getVstDt() {
        return VstDt;
    }

    public void setVstDt(String vstDt) {
        VstDt = vstDt;
    }

    public String getVstTyp() {
        return VstTyp;
    }

    public void setVstTyp(String vstTyp) {
        VstTyp = vstTyp;
    }

    public Integer getSync() {
        return Sync;
    }

    public void setSync(Integer sync) {
        Sync = sync;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }
}
