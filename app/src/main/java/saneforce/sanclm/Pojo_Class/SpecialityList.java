package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecialityList {

    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Division_Code")
    @Expose
    private String dv_code;
    @SerializedName("Doc_Special_Name")
    @Expose
    private String username;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDv_code() {
        return dv_code;
    }

    public void setDv_code(String dv_code) {
        this.dv_code = dv_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
