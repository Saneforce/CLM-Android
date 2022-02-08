package saneforce.sanclm.Sales_Report.Modelclass;

public class MultiCheckDivModel {
    String StringID, StringName;
    boolean IsChecked;

    public MultiCheckDivModel(String StringID, String StringName, boolean IsChecked) {
        this.StringID = StringID;
        this.StringName = StringName;
        this.IsChecked = IsChecked;
    }

    public MultiCheckDivModel() {
    }

    public String getStringID() {
        return StringID;
    }

    public void setStringID(String stringID) {
        StringID = stringID;
    }

    public String getStringName() {
        return StringName;
    }

    public void setStringName(String stringName) {
        StringName = stringName;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }
}

