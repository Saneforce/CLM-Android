package saneforce.sanclm.activities.Model;

public class ModelVisitControl {
    String name,date,cluster,tdr,miss,dr_met,dr_seen,fw_day,avg,cover;

    public ModelVisitControl(String name, String date, String cluster, String tdr, String miss, String dr_met, String dr_seen, String fw_day, String avg, String cover) {
        this.name = name;
        this.date = date;
        this.cluster = cluster;
        this.tdr = tdr;
        this.miss = miss;
        this.dr_met = dr_met;
        this.dr_seen = dr_seen;
        this.fw_day = fw_day;
        this.avg = avg;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getTdr() {
        return tdr;
    }

    public void setTdr(String tdr) {
        this.tdr = tdr;
    }

    public String getMiss() {
        return miss;
    }

    public void setMiss(String miss) {
        this.miss = miss;
    }

    public String getDr_met() {
        return dr_met;
    }

    public void setDr_met(String dr_met) {
        this.dr_met = dr_met;
    }

    public String getDr_seen() {
        return dr_seen;
    }

    public void setDr_seen(String dr_seen) {
        this.dr_seen = dr_seen;
    }

    public String getFw_day() {
        return fw_day;
    }

    public void setFw_day(String fw_day) {
        this.fw_day = fw_day;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
