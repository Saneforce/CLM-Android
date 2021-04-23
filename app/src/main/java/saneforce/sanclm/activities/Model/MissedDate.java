package saneforce.sanclm.activities.Model;

public class MissedDate {
    String mnthYr,date,day,td_date;

    public MissedDate(String mnthYr, String date, String day,String td_date) {
        this.mnthYr = mnthYr;
        this.date = date;
        this.day = day;
        this.td_date = td_date;
    }

    public String getTd_date() {
        return td_date;
    }

    public void setTd_date(String td_date) {
        this.td_date = td_date;
    }

    public String getMnthYr() {
        return mnthYr;
    }

    public void setMnthYr(String mnthYr) {
        this.mnthYr = mnthYr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
