package saneforce.sanclm.activities.Model;

public class ChatModel {

    String msg;
    boolean fromWhere;
    String date;

    public ChatModel(String msg, boolean fromWhere,String date) {
        this.msg = msg;
        this.fromWhere = fromWhere;
        this.date=date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(boolean fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
