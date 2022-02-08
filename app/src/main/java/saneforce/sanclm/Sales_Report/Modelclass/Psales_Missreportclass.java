package saneforce.sanclm.Sales_Report.Modelclass;

public class Psales_Missreportclass {

    String sf_code;
    String Dcnt;
    String Dmet;
    String Dmis;
    String MRptdate;
    String Name;
    String Cluster;
    String Mdate;

    public Psales_Missreportclass(String sf_code, String dcnt, String dmet, String dmis, String MRptdate, String name, String cluster, String mdate) {
        this.sf_code = sf_code;
        Dcnt = dcnt;
        Dmet = dmet;
        Dmis = dmis;
        this.MRptdate = MRptdate;
        Name = name;
        Cluster = cluster;
        Mdate = mdate;
    }

    public String getSf_code() {
        return sf_code;
    }

    public void setSf_code(String sf_code) {
        this.sf_code = sf_code;
    }

    public String getDcnt() {
        return Dcnt;
    }

    public void setDcnt(String dcnt) {
        Dcnt = dcnt;
    }

    public String getDmet() {
        return Dmet;
    }

    public void setDmet(String dmet) {
        Dmet = dmet;
    }

    public String getDmis() {
        return Dmis;
    }

    public void setDmis(String dmis) {
        Dmis = dmis;
    }

    public String getMRptdate() {
        return MRptdate;
    }

    public void setMRptdate(String MRptdate) {
        this.MRptdate = MRptdate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCluster() {
        return Cluster;
    }

    public void setCluster(String cluster) {
        Cluster = cluster;
    }

    public String getMdate() {
        return Mdate;
    }

    public void setMdate(String mdate) {
        Mdate = mdate;
    }
}
