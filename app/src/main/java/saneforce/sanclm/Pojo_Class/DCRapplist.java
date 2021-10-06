package saneforce.sanclm.Pojo_Class;

public class DCRapplist {
    String trans_slNo;
    String sf_name;
    String activity_date;
    String plan_name;
    String workType_name;
    String reporting_to_sf;
    String fieldWork_indicator;

    public String getTrans_slNo() {
        return trans_slNo;
    }

    public void setTrans_slNo(String trans_slNo) {
        this.trans_slNo = trans_slNo;
    }

    public String getSf_name() {
        return sf_name;
    }

    public void setSf_name(String sf_name) {
        this.sf_name = sf_name;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getWorkType_name() {
        return workType_name;
    }

    public void setWorkType_name(String workType_name) {
        this.workType_name = workType_name;
    }

    public String getReporting_to_sf() {
        return reporting_to_sf;
    }

    public void setReporting_to_sf(String reporting_to_sf) {
        this.reporting_to_sf = reporting_to_sf;
    }

    public String getFieldWork_indicator() {
        return fieldWork_indicator;
    }

    public void setFieldWork_indicator(String fieldWork_indicator) {
        this.fieldWork_indicator = fieldWork_indicator;
    }

    public DCRapplist(String trans_slNo, String sf_name, String activity_date, String plan_name, String workType_name, String reporting_to_sf, String fieldWork_indicator) {
     this.trans_slNo=trans_slNo;
     this.sf_name=sf_name;
     this.activity_date=activity_date;
     this.plan_name=plan_name;
     this.workType_name=workType_name;
     this.reporting_to_sf=reporting_to_sf;
     this.fieldWork_indicator=fieldWork_indicator;
    }
}
