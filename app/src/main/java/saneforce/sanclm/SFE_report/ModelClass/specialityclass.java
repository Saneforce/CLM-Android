package saneforce.sanclm.SFE_report.ModelClass;

public class specialityclass {
    String Doc_Special_Code,
            Spec_Name,
            Avrg,tot_drs;
    int cnt;

    public specialityclass(){

    }

    public String getDoc_Special_Code() {
        return Doc_Special_Code;
    }

    public void setDoc_Special_Code(String doc_Special_Code) {
        Doc_Special_Code = doc_Special_Code;
    }

    public String getSpec_Name() {
        return Spec_Name;
    }

    public void setSpec_Name(String spec_Name) {
        Spec_Name = spec_Name;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public String getAvrg() {
        return Avrg;
    }

    public void setAvrg(String avrg) {
        Avrg = avrg;
    }

    public void setTot_drs(String tot_drs) {
        this.tot_drs = tot_drs;
    }

    public String getTot_drs() {
        return tot_drs;
    }
}
