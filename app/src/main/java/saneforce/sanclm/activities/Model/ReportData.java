package saneforce.sanclm.activities.Model;

public class ReportData {
    String name;
    String mn;
    String yr;
    String seen;
    String meet;
    String missed;
    String total_dr;

    public ReportData(String name, String mn, String yr, String seen, String meet, String missed, String total_dr) {
        this.name = name;
        this.mn = mn;
        this.yr = yr;
        this.seen = seen;
        this.meet = meet;
        this.missed = missed;
        this.total_dr = total_dr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getYr() {
        return yr;
    }

    public void setYr(String yr) {
        this.yr = yr;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getMeet() {
        return meet;
    }

    public void setMeet(String meet) {
        this.meet = meet;
    }

    public String getMissed() {
        return missed;
    }

    public void setMissed(String missed) {
        this.missed = missed;
    }

    public String getTotal_dr() {
        return total_dr;
    }

    public void setTotal_dr(String total_dr) {
        this.total_dr = total_dr;
    }
}
