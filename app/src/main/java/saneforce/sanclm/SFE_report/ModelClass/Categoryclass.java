package saneforce.sanclm.SFE_report.ModelClass;

public class Categoryclass {

    String Division_code;
    String No_of_visit;
    String Cat_Name;
    Double Vst_1;
    Double Vst_Avg1;
    Double Vst_2;
    Double Vst_Avg2;
    Double Vst_3;
    Double Vst_Avg3;
    Double Vst_4;
    Double Vst_Avg4;
    String tot_drs;
    String Doc_Cat_Code;

    public Categoryclass(){

    }

    public void setDivision_code(String division_code) {
        Division_code = division_code;
    }
    public String getDivision_code() {
        return Division_code;
    }
    public String getNo_of_visit() {
        return No_of_visit;
    }
    public void setNo_of_visit(String no_of_visit) {
        No_of_visit = no_of_visit;
    }

    public void setCat_Name(String cat_Name) {
        Cat_Name = cat_Name;
    }

    public String getCat_Name() {
        return Cat_Name;
    }

    public void setVst_1(Double vst_1) {
        Vst_1 = vst_1;
    }

    public Double getVst_1() {
        return Vst_1;
    }

    public void setVst_2(Double vst_2) {
        Vst_2 = vst_2;
    }

    public Double getVst_2() {
        return Vst_2;
    }

    public void setVst_3(Double vst_3) {
        Vst_3 = vst_3;
    }

    public Double getVst_3() {
        return Vst_3;
    }

    public void setVst_4(Double vst_4) {
        Vst_4 = vst_4;
    }

    public Double getVst_4() {
        return Vst_4;
    }

    public Double getVst_Avg1() {
        return Vst_Avg1;
    }

    public Double getVst_Avg2() {
        return Vst_Avg2;
    }

    public Double getVst_Avg3() {
        return Vst_Avg3;
    }

    public Double getVst_Avg4() {
        return Vst_Avg4;
    }

    public void setVst_Avg1(Double vst_Avg1) {
        Vst_Avg1 = vst_Avg1;
    }

    public void setVst_Avg2(Double vst_Avg2) {
        Vst_Avg2 = vst_Avg2;
    }

    public void setVst_Avg3(Double vst_Avg3) {
        Vst_Avg3 = vst_Avg3;
    }

    public void setVst_Avg4(Double vst_Avg4) {
        Vst_Avg4 = vst_Avg4;
    }
    public String getTot_drs() {
        return tot_drs;
    }
    public void setTot_drs(String tot_drs) {
        this.tot_drs = tot_drs;
    }

    public String getDoc_Cat_Code() {
        return Doc_Cat_Code;
    }

    public void setDoc_Cat_Code(String doc_Cat_Code) {
        Doc_Cat_Code = doc_Cat_Code;
    }
}