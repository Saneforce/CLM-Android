package saneforce.sanclm.SFE_report.ModelClass;

public class Multicheckclass {

    String strid,strname;
    String speclity,catogry;
    boolean checked;


    public Multicheckclass() {
    }

    public Multicheckclass(String strid, String strname, boolean checked) {
        this.strid = strid;
        this.strname = strname;
        this.checked = checked;
    }

    public String getStrid() {
        return strid;
    }

    public void setStrid(String strid) {
        this.strid = strid;
    }

    public String getStrname() {
        return strname;
    }

    public void setStrname(String strname) {
        this.strname = strname;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCatogry() {
        return catogry;
    }

    public void setCatogry(String catogry) {
        this.catogry = catogry;
    }

    public String getSpeclity() {
        return speclity;
    }

    public void setSpeclity(String speclity) {
        this.speclity = speclity;
    }
}
