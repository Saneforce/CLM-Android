package saneforce.sanclm.activities.Model;

public class ModelCalendarRow {
    String  day,schedule,member;

    public ModelCalendarRow(String day, String schedule, String member) {
        this.day = day;
        this.schedule = schedule;
        this.member = member;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
