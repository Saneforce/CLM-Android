package saneforce.sanclm.User_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Customer_Me_Success {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("CustomerMe")
    @Expose
    private List<CustomerMe> customerMe = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CustomerMe> getCustomerMe() {
        return customerMe;
    }

    public void setCustomerMe(List<CustomerMe> customerMe) {
        this.customerMe = customerMe;
    }

}



