package saneforce.sanclm.activities.Model;

import org.json.JSONArray;

public class ModelBrandAuditList {
    String prName;
    String comName;
    String comPrdName;
    String qty,rate,val,compCode,compPcode,sw,rx;
    String jsonChem;
    String prcode;

    public ModelBrandAuditList() {
    }

    public JSONArray getFeedback() {
        return feedback;
    }

    public void setFeedback(JSONArray feedback) {
        this.feedback = feedback;
    }

    JSONArray feedback;



    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getRx() {
        return rx;
    }

    public void setRx(String rx) {
        this.rx = rx;
    }



    public ModelBrandAuditList(String prName, String comName, String comPrdName, String qty, String rate, String val, String sw, String rx, String compCode, String compPcode,JSONArray feedback,String prcode) {
        this.prName = prName;
        this.comName = comName;
        this.comPrdName = comPrdName;
        this.qty = qty;
        this.rate = rate;
        this.val = val;
        this.compCode=compCode;
        this.compPcode=compPcode;
        this.rx=rx;
        this.sw=sw;
        this.feedback=feedback;
        this.prcode=prcode;
    }
    public ModelBrandAuditList(String prName, String comName, String comPrdName, String qty, String rate, String val, String compCode, String compPcode) {

        this.prName = prName;
        this.comName = comName;
        this.comPrdName = comPrdName;
        this.qty = qty;
        this.rate = rate;
        this.val = val;
        this.compCode=compCode;
        this.compPcode=compPcode;

    }
    public ModelBrandAuditList(String prName, String comName, String qty, String rate, String val, String compCode,String jsonChem) {

        this.prName = prName;
        this.comName = comName;
        this.qty = qty;
        this.rate = rate;
        this.val = val;
        this.compCode = compCode;
        this.jsonChem=jsonChem;
    }

    public String getPrcode() {
        return prcode;
    }

    public void setPrcode(String prcode) {
        this.prcode = prcode;
    }

    public ModelBrandAuditList(String prName, String comName, String qty, String rate, String val, String sw, String rx, String compCode, String jsonChem,String prcode) {

        this.prName = prName;
        this.comName = comName;
        this.qty = qty;
        this.rate = rate;
        this.val = val;
        this.compCode = compCode;
        this.jsonChem=jsonChem;
        this.sw=sw;
        this.rx=rx;
        this.prcode=prcode;
    }
    public String getPrName() {
        return prName;
    }

    public void setPrName(String prName) {
        this.prName = prName;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getComPrdName() {
        return comPrdName;
    }

    public void setComPrdName(String comPrdName) {
        this.comPrdName = comPrdName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompPcode() {
        return compPcode;
    }

    public void setCompPcode(String compPcode) {
        this.compPcode = compPcode;
    }

    public String getJsonChem() {
        return jsonChem;
    }

    public void setJsonChem(String jsonChem) {
        this.jsonChem = jsonChem;
    }
}
