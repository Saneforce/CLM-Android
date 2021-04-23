package saneforce.sanclm.activities.Model;

public class ModelMissedReport {
    String name,code,display_date,req_date,cluster,dr,met,missed;

    public ModelMissedReport(String name, String code, String display_date, String req_date, String cluster, String dr, String met, String missed) {
        this.name = name;
        this.code = code;
        this.display_date = display_date;
        this.req_date = req_date;
        this.cluster = cluster;
        this.dr = dr;
        this.met = met;
        this.missed = missed;
    }

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

    public String getDisplay_date() {
        return display_date;
    }

    public void setDisplay_date(String display_date) {
        this.display_date = display_date;
    }

    public String getReq_date() {
        return req_date;
    }

    public void setReq_date(String req_date) {
        this.req_date = req_date;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getMet() {
        return met;
    }

    public void setMet(String met) {
        this.met = met;
    }

    public String getMissed() {
        return missed;
    }

    public void setMissed(String missed) {
        this.missed = missed;
    }
}
