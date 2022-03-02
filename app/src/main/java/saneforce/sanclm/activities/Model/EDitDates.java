package saneforce.sanclm.activities.Model;

public class EDitDates {
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public EDitDates(String date, String confirmed) {
        this.date = date;
        this.confirmed = confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    String confirmed;

}
