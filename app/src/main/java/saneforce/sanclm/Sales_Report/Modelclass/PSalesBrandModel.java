package saneforce.sanclm.Sales_Report.Modelclass;

public class PSalesBrandModel {
    String cnt, prod_code, prod_name, Division_Code, Division_Name, Target_Val, Prev_Sale, achie, Growth, PC, selectedValue;

    public PSalesBrandModel(String cnt, String prod_code, String prod_name, String division_Code, String division_Name, String target_Val, String prev_Sale, String achie, String growth, String PC, String selectedValue) {
        this.cnt = cnt;
        this.prod_code = prod_code;
        this.prod_name = prod_name;
        Division_Code = division_Code;
        Division_Name = division_Name;
        Target_Val = target_Val;
        Prev_Sale = prev_Sale;
        this.achie = achie;
        Growth = growth;
        this.PC = PC;
        this.selectedValue = selectedValue;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
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
