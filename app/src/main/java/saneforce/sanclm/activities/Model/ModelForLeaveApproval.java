package saneforce.sanclm.activities.Model;

public class ModelForLeaveApproval {
    String leaveid,name,from,to,reason,addr_reason,leavetype,available,days;

    public ModelForLeaveApproval(String leaveid, String name, String from, String to, String reason, String addr_reason, String leavetype, String available,String days) {
        this.leaveid = leaveid;
        this.name = name;
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.addr_reason = addr_reason;
        this.leavetype = leavetype;
        this.available = available;
        this.days=days;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getLeaveid() {
        return leaveid;
    }

    public void setLeaveid(String leaveid) {
        this.leaveid = leaveid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAddr_reason() {
        return addr_reason;
    }

    public void setAddr_reason(String addr_reason) {
        this.addr_reason = addr_reason;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
