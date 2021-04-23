package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MontlyVistDetail
{
    @SerializedName("mn")
    @Expose
    private String mn;
    @SerializedName("Mon")
    @Expose
    private String mon;
    @SerializedName("Yr")
    @Expose
    private String yr;
    @SerializedName("cnt")
    @Expose
    private String cnt;
    @SerializedName("Cnt")
    @Expose
    private String Count;


    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getYr() {
        return yr;
    }

    public void setYr(String yr) {
        this.yr = yr;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }
}
