package saneforce.sanclm.activities.Model;

public class AcivityModel {
    String sino;
    String name;
    String plan;

    public String getSino() {
        return sino;
    }

    public void setSino(String sino) {
        this.sino = sino;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public AcivityModel(String sino, String name, String plan, String actual) {
        this.sino = sino;
        this.name = name;
        this.plan = plan;
        this.actual = actual;
    }

    String actual;
}
