package com.example.myapplication.User_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User_Login_Success {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Usersuccess")
    @Expose
    private List<Usersuccess> userSucc = null;

    public Boolean getSuccess() { return success;   }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {  return msg;    }

    public void setMsg(String msg) { this.msg = msg;  }
    public List<Usersuccess> getCustomerMe() {

        return userSucc;
    }

    public void setCustomerMe(List<Usersuccess> userSucc) {
        this.userSucc = userSucc;
    }
}
