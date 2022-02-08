package saneforce.sanclm.Sales_Report.Modelclass;

public class PSalesFFBasedBrandModel {
    String sf_code, Brand_Name, Brand_Code, Division_Name, Division_Code, Sal_Val, Target_Val, Prev_Sale, achie, Growth, PC, selectedValue;

    public PSalesFFBasedBrandModel(String sf_code, String brand_Name, String brand_Code, String division_Name, String division_Code, String sal_Val, String target_Val, String prev_Sale, String achie, String growth, String PC, String selectedValue) {
        this.sf_code = sf_code;
        Brand_Name = brand_Name;
        Brand_Code = brand_Code;
        Division_Name = division_Name;
        Division_Code = division_Code;
        Sal_Val = sal_Val;
        Target_Val = target_Val;
        Prev_Sale = prev_Sale;
        this.achie = achie;
        Growth = growth;
        this.PC = PC;
        this.selectedValue = selectedValue;
    }

    public String getSf_code() {
        return sf_code;
    }

    public void setSf_code(String sf_code) {
        this.sf_code = sf_code;
    }

    public String getBrand_Name() {
        return Brand_Name;
    }

    public void setBrand_Name(String brand_Name) {
        Brand_Name = brand_Name;
    }

    public String getBrand_Code() {
        return Brand_Code;
    }

    public void setBrand_Code(String brand_Code) {
        Brand_Code = brand_Code;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getDivision_Code() {
        return Division_Code;
    }

    public void setDivision_Code(String division_Code) {
        Division_Code = division_Code;
    }

    public String getSal_Val() {
        return Sal_Val;
    }

    public void setSal_Val(String sal_Val) {
        Sal_Val = sal_Val;
    }

    public String getTarget_Val() {
        return Target_Val;
    }

    public void setTarget_Val(String target_Val) {
        Target_Val = target_Val;
    }

    public String getPrev_Sale() {
        return Prev_Sale;
    }

    public void setPrev_Sale(String prev_Sale) {
        Prev_Sale = prev_Sale;
    }

    public String getAchie() {
        return achie;
    }

    public void setAchie(String achie) {
        this.achie = achie;
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

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}
