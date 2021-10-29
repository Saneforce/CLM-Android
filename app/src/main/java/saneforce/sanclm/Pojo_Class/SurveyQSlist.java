package saneforce.sanclm.Pojo_Class;

public class SurveyQSlist {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getDrCat() {
        return drCat;
    }

    public void setDrCat(String drCat) {
        this.drCat = drCat;
    }

    public String getDrSpl() {
        return drSpl;
    }

    public void setDrSpl(String drSpl) {
        this.drSpl = drSpl;
    }

    public String getDrCls() {
        return drCls;
    }

    public void setDrCls(String drCls) {
        this.drCls = drCls;
    }

    public String getHosCls() {
        return hosCls;
    }

    public void setHosCls(String hosCls) {
        this.hosCls = hosCls;
    }

    public String getChmCat() {
        return chmCat;
    }

    public void setChmCat(String chmCat) {
        this.chmCat = chmCat;
    }

    public String getStkstate() {
        return stkstate;
    }

    public void setStkstate(String stkstate) {
        this.stkstate = stkstate;
    }

    public String getStkHQ() {
        return stkHQ;
    }

    public void setStkHQ(String stkHQ) {
        this.stkHQ = stkHQ;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getQc_id() {
        return qc_id;
    }

    public void setQc_id(String qc_id) {
        this.qc_id = qc_id;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getQlength() {
        return qlength;
    }

    public void setQlength(String qlength) {
        this.qlength = qlength;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public String getQanswer() {
        return qanswer;
    }

    public void setQanswer(String qanswer) {
        this.qanswer = qanswer;
    }

    public String getActive_flag() {
        return active_flag;
    }

    public void setActive_flag(String active_flag) {
        this.active_flag = active_flag;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public SurveyQSlist(String id, String survey, String drCat, String drSpl, String drCls, String hosCls, String chmCat, String stkstate, String stkHQ, String stype, String qc_id, String qtype, String qlength, String mandatory, String qname, String qanswer, String active_flag, String answer) {
        this.id = id;
        this.survey = survey;
        this.drCat = drCat;
        this.drSpl = drSpl;
        this.drCls = drCls;
        this.hosCls = hosCls;
        this.chmCat = chmCat;
        this.stkstate = stkstate;
        this.stkHQ = stkHQ;
        this.stype = stype;
        this.qc_id = qc_id;
        this.qtype = qtype;
        this.qlength = qlength;
        this.mandatory = mandatory;
        this.qname = qname;
        this.qanswer = qanswer;
        this.active_flag = active_flag;
        this.answer=answer;
    }

    String id,survey, drCat, drSpl,  drCls, hosCls, chmCat,  stkstate,  stkHQ,  stype,  qc_id,  qtype,  qlength,  mandatory,  qname,  qanswer,  active_flag,answer;


}
