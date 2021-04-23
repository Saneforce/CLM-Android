package saneforce.sanclm.activities.Model;

public class ModelDayReport {
    String date,name,wrktyp,cluster,holiday,rem,ul,dr,st,ch,code;

    public ModelDayReport(String date, String name, String wrktyp, String cluster, String holiday, String rem, String ul, String dr, String st, String ch,String code) {
        this.date = date;
        this.name = name;
        this.wrktyp = wrktyp;
        this.cluster = cluster;
        this.holiday = holiday;
        this.rem = rem;
        this.ul = ul;
        this.dr = dr;
        this.st = st;
        this.ch = ch;
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWrktyp() {
        return wrktyp;
    }

    public void setWrktyp(String wrktyp) {
        this.wrktyp = wrktyp;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public String getUl() {
        return ul;
    }

    public void setUl(String ul) {
        this.ul = ul;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }
}
