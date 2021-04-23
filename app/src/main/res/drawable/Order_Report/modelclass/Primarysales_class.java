package com.example.myapplication.Order_Report.modelclass;

public class Primarysales_class {
    double cnt,Target_Val,Prev_Sale,PC;
    String prod_name;
    String prod_code;
    String Division_Name;
    String achie;
    String Growth;

    public Primarysales_class(double cnt,double Target_Val,double Prev_Sale,Double PC,String prod_name,String prod_code,String Division_Name,String achie,String Growth) {
        this.cnt=cnt;
        this.prod_name=prod_name;
        this.Target_Val=Target_Val;
        this.Prev_Sale=Prev_Sale;
        this.PC=PC;
        this.prod_name=prod_name;
        this.prod_code=prod_code;
        this.Division_Name=Division_Name;
        this.achie=achie;
        this.Growth=Growth;

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

    public void setPC(double PC) {
        this.PC = PC;
    }

    public double getPC() {
        return PC;
    }

    public void setGrowth(String growth) {
        Growth = growth;
    }

    public String getGrowth() {
        return Growth;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public double getPrev_Sale() {
        return Prev_Sale;
    }

    public double getTarget_Val() {
        return Target_Val;
    }

    public String getAchie() {
        return achie;
    }

    public String getProd_code() {
        return prod_code;
    }

    public void setAchie(String achie) {
        this.achie = achie;
    }

    public void setPrev_Sale(double prev_Sale) {
        Prev_Sale = prev_Sale;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
    }

    public void setTarget_Val(double target_Val) {
        Target_Val = target_Val;
    }
}
