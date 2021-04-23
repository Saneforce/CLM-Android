package saneforce.sanclm.activities.Model;

public class ModelReportList {
    String name,cluster,jw,dt,product,gift,remark,vtime,mtime;

    public ModelReportList(String name, String cluster, String jw, String dt, String product, String gift, String remark, String vtime, String mtime) {
        this.name = name;
        this.cluster = cluster;
        this.jw = jw;
        this.dt = dt;
        this.product = product;
        this.gift = gift;
        this.remark = remark;
        this.vtime = vtime;
        this.mtime = mtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getJw() {
        return jw;
    }

    public void setJw(String jw) {
        this.jw = jw;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVtime() {
        return vtime;
    }

    public void setVtime(String vtime) {
        this.vtime = vtime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }
}
