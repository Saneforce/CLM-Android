package saneforce.sanclm.Sales_Report.Modelclass;

public class pSalesFieldModel {
    String sno, Division_Code,Division_Name,sf_code, sf_name,sf_Designation_Short_Name, sf_hq, sf_type, SF_Cat_Code, sf_TP_Active_Flag,
            Reporting_To, Reporting_To_Name, Reporting_To_Desig,Reporting_To_HQ, Sl_Primary, Sl_Target, Sl_growth, Sl_Achieve, selectedValue,
            Sl_PCPM;

    public pSalesFieldModel(String sno, String division_Code, String division_Name, String sf_code, String sf_name, String sf_Designation_Short_Name,
                            String sf_hq, String sf_type, String SF_Cat_Code, String sf_TP_Active_Flag, String reporting_To, String reporting_To_Name,
                            String reporting_To_Desig, String reporting_To_HQ, String sl_Primary, String sl_Target, String sl_growth, String sl_Achieve,
                            String selectedValue, String sl_PCPM) {
        this.sno = sno;
        Division_Code = division_Code;
        Division_Name = division_Name;
        this.sf_code = sf_code;
        this.sf_name = sf_name;
        this.sf_Designation_Short_Name = sf_Designation_Short_Name;
        this.sf_hq = sf_hq;
        this.sf_type = sf_type;
        this.SF_Cat_Code = SF_Cat_Code;
        this.sf_TP_Active_Flag = sf_TP_Active_Flag;
        Reporting_To = reporting_To;
        Reporting_To_Name = reporting_To_Name;
        Reporting_To_Desig = reporting_To_Desig;
        Reporting_To_HQ = reporting_To_HQ;
        Sl_Primary = sl_Primary;
        Sl_Target = sl_Target;
        Sl_growth = sl_growth;
        Sl_Achieve = sl_Achieve;
        this.selectedValue = selectedValue;
        Sl_PCPM = sl_PCPM;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getDivision_Code() {
        return Division_Code;
    }

    public void setDivision_Code(String division_Code) {
        Division_Code = division_Code;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getSf_code() {
        return sf_code;
    }

    public void setSf_code(String sf_code) {
        this.sf_code = sf_code;
    }

    public String getSf_name() {
        return sf_name;
    }

    public void setSf_name(String sf_name) {
        this.sf_name = sf_name;
    }

    public String getSf_Designation_Short_Name() {
        return sf_Designation_Short_Name;
    }

    public void setSf_Designation_Short_Name(String sf_Designation_Short_Name) {
        this.sf_Designation_Short_Name = sf_Designation_Short_Name;
    }

    public String getSf_hq() {
        return sf_hq;
    }

    public void setSf_hq(String sf_hq) {
        this.sf_hq = sf_hq;
    }

    public String getSf_type() {
        return sf_type;
    }

    public void setSf_type(String sf_type) {
        this.sf_type = sf_type;
    }

    public String getSF_Cat_Code() {
        return SF_Cat_Code;
    }

    public void setSF_Cat_Code(String SF_Cat_Code) {
        this.SF_Cat_Code = SF_Cat_Code;
    }

    public String getSf_TP_Active_Flag() {
        return sf_TP_Active_Flag;
    }

    public void setSf_TP_Active_Flag(String sf_TP_Active_Flag) {
        this.sf_TP_Active_Flag = sf_TP_Active_Flag;
    }

    public String getReporting_To() {
        return Reporting_To;
    }

    public void setReporting_To(String reporting_To) {
        Reporting_To = reporting_To;
    }

    public String getReporting_To_Name() {
        return Reporting_To_Name;
    }

    public void setReporting_To_Name(String reporting_To_Name) {
        Reporting_To_Name = reporting_To_Name;
    }

    public String getReporting_To_Desig() {
        return Reporting_To_Desig;
    }

    public void setReporting_To_Desig(String reporting_To_Desig) {
        Reporting_To_Desig = reporting_To_Desig;
    }

    public String getReporting_To_HQ() {
        return Reporting_To_HQ;
    }

    public void setReporting_To_HQ(String reporting_To_HQ) {
        Reporting_To_HQ = reporting_To_HQ;
    }

    public String getSl_Primary() {
        return Sl_Primary;
    }

    public void setSl_Primary(String sl_Primary) {
        Sl_Primary = sl_Primary;
    }

    public String getSl_Target() {
        return Sl_Target;
    }

    public void setSl_Target(String sl_Target) {
        Sl_Target = sl_Target;
    }

    public String getSl_growth() {
        return Sl_growth;
    }

    public void setSl_growth(String sl_growth) {
        Sl_growth = sl_growth;
    }

    public String getSl_Achieve() {
        return Sl_Achieve;
    }

    public void setSl_Achieve(String sl_Achieve) {
        Sl_Achieve = sl_Achieve;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public String getSl_PCPM() {
        return Sl_PCPM;
    }

    public void setSl_PCPM(String sl_PCPM) {
        Sl_PCPM = sl_PCPM;
    }
}
