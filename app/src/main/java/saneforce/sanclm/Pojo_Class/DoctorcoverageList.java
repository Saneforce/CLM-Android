package saneforce.sanclm.Pojo_Class;

public class DoctorcoverageList {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getvCount() {
        return vCount;
    }

    public void setvCount(String vCount) {
        this.vCount = vCount;
    }

    public String gettCnt() {
        return tCnt;
    }

    public void settCnt(String tCnt) {
        this.tCnt = tCnt;
    }

    public DoctorcoverageList(String name, String vCount, String tCnt) {
        this.name = name;
        this.vCount = vCount;
        this.tCnt = tCnt;
    }

    String vCount;
    String tCnt;

}
