package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitorsList {
    @SerializedName("Comp_Sl_No")
    @Expose
    private String compSlNo;
    @SerializedName("Comp_Name")
    @Expose
    private String compName;
    @SerializedName("Comp_Prd_Sl_No")
    @Expose
    private String compPrdSlNo;
    @SerializedName("Comp_Prd_name")
    @Expose
    private String compPrdName;

    public String getCompSlNo() {
        return compSlNo;
    }

    public void setCompSlNo(String compSlNo) {
        this.compSlNo = compSlNo;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompPrdSlNo() {
        return compPrdSlNo;
    }

    public void setCompPrdSlNo(String compPrdSlNo) {
        this.compPrdSlNo = compPrdSlNo;
    }

    public String getCompPrdName() {
        return compPrdName;
    }

    public void setCompPrdName(String compPrdName) {
        this.compPrdName = compPrdName;
    }
}
