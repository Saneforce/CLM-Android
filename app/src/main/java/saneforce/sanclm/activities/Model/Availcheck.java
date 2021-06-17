package saneforce.sanclm.activities.Model;

public class Availcheck {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    String name;
    String code;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    String quantity;
    boolean availis;

    public boolean isAvailis() {
        return availis;
    }

    public void setAvailis(boolean availis) {
        this.availis = availis;
    }

    public boolean isIsoos() {
        return isoos;
    }

    public void setIsoos(boolean isoos) {
        this.isoos = isoos;
    }

    boolean isoos;



}
