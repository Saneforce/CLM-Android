package saneforce.sanclm.activities.Model;

public class ModelTpSave {
    String wrk,hq,cluster,joint,dr,chem,rmrk,hosp;
    boolean clusterneed;
    public ModelTpSave(String wrk, String hq, String cluster, String joint, String dr, String chem,String rmrk) {
        this.wrk = wrk;
        this.hq = hq;
        this.cluster = cluster;
        this.joint = joint;
        this.dr = dr;
        this.chem = chem;
        this.rmrk=rmrk;
    }
    public ModelTpSave(String wrk, String hq, String cluster, String joint, String dr, String chem,String rmrk,String   hosp) {
        this.wrk = wrk;
        this.hq = hq;
        this.cluster = cluster;
        this.joint = joint;
        this.dr = dr;
        this.chem = chem;
        this.rmrk=rmrk;
        this.hosp=hosp;
    }

    public ModelTpSave(boolean clusterneed) {
        this.clusterneed = clusterneed;
    }

    public boolean isClusterneed() {
        return clusterneed;
    }

    public void setClusterneed(boolean clusterneed) {
        this.clusterneed = clusterneed;
    }

    public String getRmrk() {
        return rmrk;
    }

    public void setRmrk(String rmrk) {
        this.rmrk = rmrk;
    }

    public String getWrk() {
        return wrk;
    }

    public void setWrk(String wrk) {
        this.wrk = wrk;
    }

    public String getHq() {
        return hq;
    }

    public void setHq(String hq) {
        this.hq = hq;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getJoint() {
        return joint;
    }

    public void setJoint(String joint) {
        this.joint = joint;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getChem() {
        return chem;
    }

    public void setChem(String chem) {
        this.chem = chem;
    }

    public String getHosp() {
        return hosp;
    }

    public void setHosp(String hosp) {
        this.hosp = hosp;
    }
}
