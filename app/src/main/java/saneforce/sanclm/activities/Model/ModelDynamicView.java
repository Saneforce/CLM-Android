package saneforce.sanclm.activities.Model;

import java.util.ArrayList;

public class ModelDynamicView {
    String  viewid;
    String value;
    String fieldname;
    String tvalue;
    String ctrl_para;
    String creation_id;
    String slno;
    String upload_sv;
    String mandatory;
    String latValue;
    String lngValue;
    ArrayList<PopFeed>  a_list=new ArrayList<>();

    public ModelDynamicView(String viewid, String value, String fieldname,String    tvalue,ArrayList<PopFeed>  a_list,
                            String ctrl_para,String creation_id,String slno,String upload_sv,String mandatory) {
        this.viewid = viewid;
        this.value = value;
        this.tvalue = tvalue;
        this.fieldname = fieldname;
        this.a_list=a_list;
        this.ctrl_para=ctrl_para;
        this.creation_id=creation_id;
        this.slno=slno;
        this.upload_sv=upload_sv;
        this.mandatory=mandatory;
    }

    public ModelDynamicView(String viewid, String value, String fieldname,String    tvalue,ArrayList<PopFeed>  a_list,
                            String ctrl_para,String creation_id,String slno,String upload_sv,String mandatory,String latValue,String lngValue) {
        this.viewid = viewid;
        this.value = value;
        this.tvalue = tvalue;
        this.fieldname = fieldname;
        this.a_list=a_list;
        this.ctrl_para=ctrl_para;
        this.creation_id=creation_id;
        this.slno=slno;
        this.upload_sv=upload_sv;
        this.mandatory=mandatory;
        this.latValue=latValue;
        this.lngValue=lngValue;
    }



    public String getLatValue() {
        return latValue;
    }

    public void setLatValue(String latValue) {
        this.latValue = latValue;
    }

    public String getLngValue() {
        return lngValue;
    }

    public void setLngValue(String lngValue) {
        this.lngValue = lngValue;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getUpload_sv() {
        return upload_sv;
    }

    public void setUpload_sv(String upload_sv) {
        this.upload_sv = upload_sv;
    }

    public String getCtrl_para() {
        return ctrl_para;
    }

    public void setCtrl_para(String ctrl_para) {
        this.ctrl_para = ctrl_para;
    }

    public String getTvalue() {
        return tvalue;
    }

    public void setTvalue(String tvalue) {
        this.tvalue = tvalue;
    }

    public String getViewid() {
        return viewid;
    }

    public void setViewid(String viewid) {
        this.viewid = viewid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public ArrayList<PopFeed> getA_list() {
        return a_list;
    }

    public void setA_list(ArrayList<PopFeed> a_list) {
        this.a_list = a_list;
    }

    public String getCreation_id() {
        return creation_id;
    }

    public void setCreation_id(String creation_id) {
        this.creation_id = creation_id;
    }

    public ModelDynamicView(String  viewid){
        this.viewid=viewid;
    }

    @Override
    public boolean equals(Object obj) {
        ModelDynamicView dt = (ModelDynamicView)obj;

        if(String.valueOf(dt.viewid).equals(String.valueOf(viewid))) return true;

        return false;
    }
}
