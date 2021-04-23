package com.example.myapplication.Order_Report.modelclass;

public class TargetPrimary_Class {
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

    private String Sl_pcpm;

    private String Sl_Primary;

    private String Sl_Target;
    private String div_code;

    private boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public TargetPrimary_Class(String sno, String sf_code, String sf_name, String hq, String sf_type, String SF_Cat_Code, String sf_TP_Active_Flag, String SF_VacantBlock, String sl_Achieve, String sl_growth, String sl_Primary, String sl_Target,String div_code,String Sl_pcpm) {
        this.sno = sno;
        this.sf_code = sf_code;
        this.sf_name = sf_name;
        this.hq = hq;
        this.sf_type = sf_type;
        this.SF_Cat_Code = SF_Cat_Code;
        this.sf_TP_Active_Flag = sf_TP_Active_Flag;
        this.SF_VacantBlock = SF_VacantBlock;
        this.Sl_Achieve = sl_Achieve;
        this.Sl_growth = sl_growth;
        this.Sl_Primary = sl_Primary;
        this.Sl_Target = sl_Target;
        this.Sl_pcpm = Sl_pcpm;
        this.div_code=div_code;
        this.expanded=false;
    }
    public TargetPrimary_Class(){

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
    public void setSl_Primary(String Sl_Primary){
        this.Sl_Primary = Sl_Primary;
    }
    public String getSl_Primary(){
        return this.Sl_Primary;
    }
    public void setSl_Target(String Sl_Target){
        this.Sl_Target = Sl_Target;
    }
    public String getSl_Target(){
        return this.Sl_Target;
    }

    public String getDiv_code() {
        return div_code;
    }

    public void setDiv_code(String div_code) {
        this.div_code = div_code;
    }

    public String getSl_pcpm() {
        return Sl_pcpm;
    }

    public void setSl_pcpm(String sl_pcpm) {
        Sl_pcpm = sl_pcpm;
    }
}
