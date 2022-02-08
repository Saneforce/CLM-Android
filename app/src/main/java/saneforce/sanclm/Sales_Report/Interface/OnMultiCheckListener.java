package saneforce.sanclm.Sales_Report.Interface;


import saneforce.sanclm.Sales_Report.Modelclass.MultiCheckDivModel;

public interface OnMultiCheckListener {
    public void OnMultiCheckDivListener_Add(MultiCheckDivModel classGroup);
    public void OnMultiCheckDivListener_Remove(MultiCheckDivModel classGroup);
}
