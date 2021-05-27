package saneforce.sanclm.activities.Model;

public class Brandexpolist {
    String brand;
    String tcount;
    String barclr;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTcount() {
        return tcount;
    }

    public void setTcount(String tcount) {
        this.tcount = tcount;
    }

    public String getBarclr() {
        return barclr;
    }

    public void setBarclr(String barclr) {
        this.barclr = barclr;
    }

    public String getIvalue() {
        return ivalue;
    }

    public void setIvalue(String ivalue) {
        this.ivalue = ivalue;
    }

    public Brandexpolist(String brand, String tcount, String barclr, String ivalue) {
        this.brand = brand;
        this.tcount = tcount;
        this.barclr = barclr;
        this.ivalue = ivalue;
    }

    String ivalue;
}
