package saneforce.sanclm.activities.Model;

public class ModelForReportList {
    String name,date,cluster,qualify,spec,prev,category,cls;

    public ModelForReportList(String name, String date, String cluster, String qualify, String spec, String prev, String category, String cls) {
        this.name = name;
        this.date = date;
        this.cluster = cluster;
        this.qualify = qualify;
        this.spec = spec;
        this.prev = prev;
        this.category = category;
        this.cls = cls;
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

    public String getQualify() {
        return qualify;
    }

    public void setQualify(String qualify) {
        this.qualify = qualify;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }
}
