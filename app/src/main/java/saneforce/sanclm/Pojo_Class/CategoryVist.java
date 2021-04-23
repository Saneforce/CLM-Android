package saneforce.sanclm.Pojo_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryVist {

    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

}
