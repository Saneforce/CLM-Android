package saneforce.sanclm.Order_Report.modelclass;

public class secondarysales_class {
    double cnt;
    String prod_name,prod_code,Division_Name,Target_Val,achie,Growth,PC;

    public secondarysales_class(double cnt, String prod_name, String prod_code, String Division_Name, String Target_Val, String achie, String Growth, String PC) {
        this.cnt=cnt;
        this.prod_name=prod_name;
        this.prod_code=prod_code;
        this.Division_Name=Division_Name;
        this.Target_Val=Target_Val;
        this.achie=achie;
        this.Growth=Growth;
        this.PC=PC;
    }

    public double getCnt() {
        return cnt;
    }

    public void setCnt(double cnt) {
        this.cnt = cnt;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public void setTarget_Val(String target_Val) {
        Target_Val = target_Val;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public void setGrowth(String growth) {
        Growth = growth;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public void setAchie(String achie) {
        this.achie = achie;
    }

    public String getProd_code() {
        return prod_code;
    }

    public String getAchie() {
        return achie;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public String getGrowth() {
        return Growth;
    }

    public String getPC() {
        return PC;
    }

    public String getTarget_Val() {
        return Target_Val;
    }
}

