package saneforce.sanclm.Pojo_Class;

public class DetailingList {
    String brand;
    String percnt;
    String barclr;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPercnt() {
        return percnt;
    }

    public void setPercnt(String percnt) {
        this.percnt = percnt;
    }

    public String getLblClr() {
        return lblClr;
    }

    public void setLblClr(String lblClr) {
        this.lblClr = lblClr;
    }

    public String getBarclr() {
        return barclr;
    }

    public void setBarclr(String barclr) {
        this.barclr = barclr;
    }

    public DetailingList(String brand, String percnt, String lblClr, String barclr) {
        this.brand = brand;
        this.percnt = percnt;
        this.lblClr = lblClr;
        this.barclr=barclr;
    }

    String lblClr;

}
