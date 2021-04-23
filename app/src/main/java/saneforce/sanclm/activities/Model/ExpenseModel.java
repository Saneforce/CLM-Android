package saneforce.sanclm.activities.Model;

public class ExpenseModel {
    String date,day,place,fare,distance,allowance,total;

    public ExpenseModel(String date, String day, String place, String fare, String distance, String allowance,String total) {
        this.date = date;
        this.day = day;
        this.place = place;
        this.fare = fare;
        this.distance = distance;
        this.allowance = allowance;
        this.total = total;
    }

    public ExpenseModel(String place,String day,String distance,String fare,String total){
        this.day = day;
        this.place = place;
        this.fare = fare;
        this.distance = distance;
        this.total=total;

    }
    public ExpenseModel(String place,String day,String distance,String fare){
        this.day = day;
        this.place = place;
        this.fare = fare;
        this.distance = distance;

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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAllowance() {
        return allowance;
    }

    public void setAllowance(String allowance) {
        this.allowance = allowance;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
