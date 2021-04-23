package saneforce.sanclm.activities.Model;

public class QuizOptionModel{
    String value,id,value2;
    boolean isCheck,isCheck2;

    public QuizOptionModel(String value, String id, boolean isCheck,String value2,boolean isCheck2) {
        this.value = value;
        this.value2 = value2;
        this.id = id;
        this.isCheck = isCheck;
        this.isCheck2 = isCheck2;
    }
    public QuizOptionModel(String value, String id, boolean isCheck) {
        this.value = value;
        this.value2 = value2;
        this.id = id;
        this.isCheck = isCheck;
        this.isCheck2 = isCheck2;
    }

    public boolean isCheck2() {
        return isCheck2;
    }

    public void setCheck2(boolean check2) {
        isCheck2 = check2;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
