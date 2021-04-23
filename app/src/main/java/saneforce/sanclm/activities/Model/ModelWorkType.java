package saneforce.sanclm.activities.Model;

public class ModelWorkType {

    String name,code,flag;

    public ModelWorkType(String name, String code, String flag) {
        this.name = name;
        this.code = code;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
