package saneforce.sanclm.Order_Report.modelclass;

public class TargetSecondary_class {
    private String sno;

    private String sf_code;

    private String sf_name;

    private String hq;

    private String sf_type;

    private String SF_Cat_Code;

    private String sf_TP_Active_Flag;

    private String SF_VacantBlock;

    private String Sl_Achieve;

    private String Sl_growth;

    private String Sl_Secondary;

    private String Sl_Target;
    private boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public TargetSecondary_class(String sno, String sf_code, String sf_name, String hq, String sf_type,
                                 String SF_Cat_Code, String sf_TP_Active_Flag, String SF_VacantBlock, String sl_Achieve,
                                 String sl_growth, String sl_Secondary, String sl_Target) {
        this.sno = sno;
        this.sf_code = sf_code;
        this.sf_name = sf_name;
        this.hq = hq;
        this.sf_type = sf_type;
        this.SF_Cat_Code = SF_Cat_Code;
        this.sf_TP_Active_Flag = sf_TP_Active_Flag;
        this.SF_VacantBlock = SF_VacantBlock;
        Sl_Achieve = sl_Achieve;
        Sl_growth = sl_growth;
        Sl_Secondary = sl_Secondary;
        Sl_Target = sl_Target;
    }
    public TargetSecondary_class(){
    }
    public void setSno(String sno){
        this.sno = sno;
    }
    public String getSno(){
        return this.sno;
    }
    public void setSf_code(String sf_code){
        this.sf_code = sf_code;
    }
    public String getSf_code(){
        return this.sf_code;
    }
    public void setSf_name(String sf_name){
        this.sf_name = sf_name;
    }
    public String getSf_name(){
        return this.sf_name;
    }
    public void setHq(String hq){
        this.hq = hq;
    }
    public String getHq(){
        return this.hq;
    }
    public void setSf_type(String sf_type){
        this.sf_type = sf_type;
    }
    public String getSf_type(){
        return this.sf_type;
    }
    public void setSF_Cat_Code(String SF_Cat_Code){
        this.SF_Cat_Code = SF_Cat_Code;
    }
    public String getSF_Cat_Code(){
        return this.SF_Cat_Code;
    }
    public void setSf_TP_Active_Flag(String sf_TP_Active_Flag){
        this.sf_TP_Active_Flag = sf_TP_Active_Flag;
    }
    public String getSf_TP_Active_Flag(){
        return this.sf_TP_Active_Flag;
    }
    public void setSF_VacantBlock(String SF_VacantBlock){
        this.SF_VacantBlock = SF_VacantBlock;
    }
    public String getSF_VacantBlock(){
        return this.SF_VacantBlock;
    }
    public void setSl_Achieve(String Sl_Achieve){
        this.Sl_Achieve = Sl_Achieve;
    }
    public String getSl_Achieve(){
        return this.Sl_Achieve;
    }
    public void setSl_growth(String Sl_growth){
        this.Sl_growth = Sl_growth;
    }
    public String getSl_growth(){
        return this.Sl_growth;
    }

    public void setSl_Secondary(String sl_Secondary) {
        Sl_Secondary = sl_Secondary;
    }

    public String getSl_Secondary() {
        return Sl_Secondary;
    }

    public void setSl_Target(String Sl_Target){
        this.Sl_Target = Sl_Target;
    }
    public String getSl_Target(){
        return this.Sl_Target;
    }
}
