package saneforce.sanclm.Sales_Report.Modelclass;

public class Designation_List {

    String Designation_Name;
    boolean IsChecked;

    public Designation_List(String designation_Name, boolean IsChecked) {
        Designation_Name = designation_Name;
        this.IsChecked = IsChecked;
    }

    public String getDesignation_Name() {
        return Designation_Name;
    }

    public void setDesignation_Name(String designation_Name) {
        Designation_Name = designation_Name;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }
}
