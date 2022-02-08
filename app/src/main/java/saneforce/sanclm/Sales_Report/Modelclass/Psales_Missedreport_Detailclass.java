package saneforce.sanclm.Sales_Report.Modelclass;

public class Psales_Missedreport_Detailclass {

    String ListedDrCode;
    String ListedDr_Name;
    String Doc_Cat_SName;
    String Doc_Special_SName;
    String Doc_ClsSName;
    String Doc_QuaName;
    String territory_Name;
    String Msdt;

    public Psales_Missedreport_Detailclass(String listedDrCode, String listedDr_Name, String doc_Cat_SName, String doc_Special_SName, String doc_ClsSName, String doc_QuaName, String territory_Name, String msdt) {
        ListedDrCode = listedDrCode;
        ListedDr_Name = listedDr_Name;
        Doc_Cat_SName = doc_Cat_SName;
        Doc_Special_SName = doc_Special_SName;
        Doc_ClsSName = doc_ClsSName;
        Doc_QuaName = doc_QuaName;
        this.territory_Name = territory_Name;
        Msdt = msdt;
    }

    public String getListedDrCode() {
        return ListedDrCode;
    }

    public void setListedDrCode(String listedDrCode) {
        ListedDrCode = listedDrCode;
    }

    public String getListedDr_Name() {
        return ListedDr_Name;
    }

    public void setListedDr_Name(String listedDr_Name) {
        ListedDr_Name = listedDr_Name;
    }

    public String getDoc_Cat_SName() {
        return Doc_Cat_SName;
    }

    public void setDoc_Cat_SName(String doc_Cat_SName) {
        Doc_Cat_SName = doc_Cat_SName;
    }

    public String getDoc_Special_SName() {
        return Doc_Special_SName;
    }

    public void setDoc_Special_SName(String doc_Special_SName) {
        Doc_Special_SName = doc_Special_SName;
    }

    public String getDoc_ClsSName() {
        return Doc_ClsSName;
    }

    public void setDoc_ClsSName(String doc_ClsSName) {
        Doc_ClsSName = doc_ClsSName;
    }

    public String getDoc_QuaName() {
        return Doc_QuaName;
    }

    public void setDoc_QuaName(String doc_QuaName) {
        Doc_QuaName = doc_QuaName;
    }

    public String getTerritory_Name() {
        return territory_Name;
    }

    public void setTerritory_Name(String territory_Name) {
        this.territory_Name = territory_Name;
    }

    public String getMsdt() {
        return Msdt;
    }

    public void setMsdt(String msdt) {
        Msdt = msdt;
    }
}
