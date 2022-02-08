package saneforce.sanclm.Sales_Report.Modelclass;

public class PSalesHQBasedBrandModel {
    String SF_Cat_Code, HQ_Name, Brand, Brand_Code, Division_Code, Division_Name, Primary, Target, PrevSale, achie, Growth, PC;

    public PSalesHQBasedBrandModel(String SF_Cat_Code, String HQ_Name, String Brand, String Brand_Code, String Division_Code, String Division_Name,
                                   String Primary, String Target, String PrevSale, String achie, String Growth, String PC){
        this.SF_Cat_Code = SF_Cat_Code;
        this.HQ_Name = HQ_Name;
        this.Brand = Brand;
        this.Brand_Code = Brand_Code;
        this.Division_Code = Division_Code;
        this.Division_Name = Division_Name;
        this.Primary = Primary;
        this.Target = Target;
        this.PrevSale = PrevSale;
        this.achie = achie;
        this.Growth = Growth;
        this.PC = PC;
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

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getBrand_Code() {
        return Brand_Code;
    }

    public void setBrand_Code(String brand_Code) {
        Brand_Code = brand_Code;
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

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getPrevSale() {
        return PrevSale;
    }

    public void setPrevSale(String prevSale) {
        PrevSale = prevSale;
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
}
