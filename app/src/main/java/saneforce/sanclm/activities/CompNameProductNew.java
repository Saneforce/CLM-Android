package saneforce.sanclm.activities;

import org.json.JSONArray;

import java.util.ArrayList;

public class CompNameProductNew {

    String  Ccode;
    String CName;
    String PCode;
    String PName;
    String invqty;




    String ptp;
    String ptr;
    String sw;
    String rx;
    JSONArray feedback;
    String Fmessage;
    ArrayList<String> image;


    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }


    public String getFmessage() {
        return Fmessage;
    }

    public void setFmessage(String fmessage) {
        Fmessage = fmessage;
    }


    public String getCcode() {
        return Ccode;
    }

    public void setCcode(String ccode) {
        Ccode = ccode;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getPCode() {
        return PCode;
    }

    public void setPCode(String PCode) {
        this.PCode = PCode;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getInvqty() {
        return invqty;
    }

    public void setInvqty(String invqty) {
        this.invqty = invqty;
    }

    public String getPtp() {
        return ptp;
    }

    public void setPtp(String ptp) {
        this.ptp = ptp;
    }

    public String getPtr() {
        return ptr;
    }

    public void setPtr(String ptr) {
        this.ptr = ptr;
    }

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



    public JSONArray getFeedback() {
        return feedback;
    }

    public void setFeedback(JSONArray feedback) {
        this.feedback = feedback;
    }


}
