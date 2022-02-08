package saneforce.sanclm.Sales_Report.Modelclass;

public class PSalesHQModel {
    String sl_no, SF_Cat_Code, HQ_Name, Division_Code, Division_Name, Primary, PrevPrimary, Target, Achie, Growth;

    public PSalesHQModel(String sl_no, String SF_Cat_Code, String HQ_Name, String Division_Code, String Division_Name,
                         String Primary, String PrevPrimary, String Target, String Achie, String Growth){
        this.sl_no = sl_no;
        this.SF_Cat_Code = SF_Cat_Code;
        this.HQ_Name = HQ_Name;
        this.Division_Code = Division_Code;
        this.Division_Name = Division_Name;
        this.Primary = Primary;
        this.PrevPrimary = PrevPrimary;
        this.Target = Target;
        this.Achie = Achie;
        this.Growth = Growth;
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getSF_Cat_Code() {
        return SF_Cat_Code;
    }

    public void setSF_Cat_Code(String SF_Cat_Code) {
        this.SF_Cat_Code = SF_Cat_Code;
    }

    public String getHQ_Name() {
        return HQ_Name;
    }

    public void setHQ_Name(String HQ_Name) {
        this.HQ_Name = HQ_Name;
    }

    public String getDivision_Code() {
        return Division_Code;
    }

    public void setDivision_Code(String division_Code) {
        Division_Code = division_Code;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getPrimary() {
        return Primary;
    }

    public void setPrimary(String primary) {
        Primary = primary;
    }

    public String getPrevPrimary() {
        return PrevPrimary;
    }

    public void setPrevPrimary(String prevPrimary) {
        PrevPrimary = prevPrimary;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getAchie() {
        return Achie;
    }

    public void setAchie(String achie) {
        Achie = achie;
    }

    public String getGrowth() {
        return Growth;
    }

    public void setGrowth(String growth) {
        Growth = growth;
    }
}
