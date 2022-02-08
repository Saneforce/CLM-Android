package saneforce.sanclm.Sales_Report.Modelclass;

public class PSalesBrandBasedProductModel {

    String Division_Name, Product_Code, Product_Brd_Code, Product_Brd_Name, Product_Name, Pack, Ach, CSaleVal, Growth, PCPM, PSaleVal, TargtVal;

    public PSalesBrandBasedProductModel(String division_Name, String product_Code, String product_Brd_Code, String product_Brd_Name, String product_Name, String pack, String ach, String CSaleVal, String growth, String PCPM, String PSaleVal, String targtVal) {
        Division_Name = division_Name;
        Product_Code = product_Code;
        Product_Brd_Code = product_Brd_Code;
        Product_Brd_Name = product_Brd_Name;
        Product_Name = product_Name;
        Pack = pack;
        Ach = ach;
        this.CSaleVal = CSaleVal;
        Growth = growth;
        this.PCPM = PCPM;
        this.PSaleVal = PSaleVal;
        TargtVal = targtVal;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getProduct_Code() {
        return Product_Code;
    }

    public void setProduct_Code(String product_Code) {
        Product_Code = product_Code;
    }

    public String getProduct_Brd_Code() {
        return Product_Brd_Code;
    }

    public void setProduct_Brd_Code(String product_Brd_Code) {
        Product_Brd_Code = product_Brd_Code;
    }

    public String getProduct_Brd_Name() {
        return Product_Brd_Name;
    }

    public void setProduct_Brd_Name(String product_Brd_Name) {
        Product_Brd_Name = product_Brd_Name;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getPack() {
        return Pack;
    }

    public void setPack(String pack) {
        Pack = pack;
    }

    public String getAch() {
        return Ach;
    }

    public void setAch(String ach) {
        Ach = ach;
    }

    public String getCSaleVal() {
        return CSaleVal;
    }

    public void setCSaleVal(String CSaleVal) {
        this.CSaleVal = CSaleVal;
    }

    public String getGrowth() {
        return Growth;
    }

    public void setGrowth(String growth) {
        Growth = growth;
    }

    public String getPCPM() {
        return PCPM;
    }

    public void setPCPM(String PCPM) {
        this.PCPM = PCPM;
    }

    public String getPSaleVal() {
        return PSaleVal;
    }

    public void setPSaleVal(String PSaleVal) {
        this.PSaleVal = PSaleVal;
    }

    public String getTargtVal() {
        return TargtVal;
    }

    public void setTargtVal(String targtVal) {
        TargtVal = targtVal;
    }
}
