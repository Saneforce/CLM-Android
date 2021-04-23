package saneforce.sanclm.activities.Model;

import java.util.Comparator;

public class DetailingOfReport {
    String name,fieldperson,type,territory,spec,cls,count,time,totsec;

    public DetailingOfReport(String name, String fieldperson, String type, String territory, String spec, String cls, String count, String time,String totsec) {
        this.name = name;
        this.fieldperson = fieldperson;
        this.type = type;
        this.territory = territory;
        this.spec = spec;
        this.cls = cls;
        this.count = count;
        this.time = time;
        this.totsec = totsec;
    }

    public String getTotsec() {
        return totsec;
    }

    public void setTotsec(String totsec) {
        this.totsec = totsec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldperson() {
        return fieldperson;
    }

    public void setFieldperson(String fieldperson) {
        this.fieldperson = fieldperson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static Comparator<DetailingOfReport> customerNameComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getName().toUpperCase();
            String StudentName2 = s2.getName().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
    public static Comparator<DetailingOfReport> fieldForceComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getFieldperson().toUpperCase();
            String StudentName2 = s2.getFieldperson().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
    public static Comparator<DetailingOfReport> typeComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getType().toUpperCase();
            String StudentName2 = s2.getType().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
    public static Comparator<DetailingOfReport> territoryComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getTerritory().toUpperCase();
            String StudentName2 = s2.getTerritory().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
    public static Comparator<DetailingOfReport> specComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getSpec().toUpperCase();
            String StudentName2 = s2.getSpec().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
    public static Comparator<DetailingOfReport> clsComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getCls().toUpperCase();
            String StudentName2 = s2.getCls().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
    public static Comparator<DetailingOfReport> countComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getCount().toUpperCase();
            String StudentName2 = s2.getCount().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
    public static Comparator<DetailingOfReport> totalsecComparator = new Comparator<DetailingOfReport>() {

        public int compare(DetailingOfReport s1, DetailingOfReport s2) {
            String StudentName1 = s1.getTotsec().toUpperCase();
            String StudentName2 = s2.getTotsec().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
}
