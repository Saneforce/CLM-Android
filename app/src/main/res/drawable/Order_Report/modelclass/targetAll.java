package com.example.myapplication.Order_Report.modelclass;

public class targetAll {
    private String sf_code;

    private String division_code;

    private String Division_Name;

    private String Target;

    private String Sale;

    private String achiec;

    private String PSale;

    private String Growth;

    private String PC;

    private boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public targetAll( String sf_code, String division_code, String Division_Name, String Target,
                                 String Sale, String achiec, String PSale, String Growth,
                                 String PC) {
        this.sf_code = sf_code;
        this.division_code = division_code;
        this.Division_Name = Division_Name;
        this.Target = Target;
        this.Sale = Sale;
        this.achiec = achiec;
        this.PSale = PSale;
        this.Growth = Growth;
        this.PC = PC;

    }
    public targetAll(){
    }
    public void setSf_code(String sf_code){
        this.sf_code = sf_code;
    }
    public String getSf_code(){
        return this.sf_code;
    }

    public String getDivision_code() {
        return division_code;
    }

    public void setDivision_code(String division_code) {
        this.division_code = division_code;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getSale() {
        return Sale;
    }

    public void setSale(String sale) {
        Sale = sale;
    }

    public String getAchiec() {
        return achiec;
    }

    public void setAchiec(String achiec) {
        this.achiec = achiec;
    }

    public String getPSale() {
        return PSale;
    }

    public void setPSale(String PSale) {
        this.PSale = PSale;
    }

    public String getGrowth() {
        return Growth;
    }

    public void setGrowth(String growth) {
        Growth = growth;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }
}
