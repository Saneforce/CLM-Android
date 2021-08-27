package saneforce.sanclm.activities.Model;

public class ModelDownloadMaster {
    String itemName;
    String itemCount;
    String refname;


    boolean animCount;

    public ModelDownloadMaster(String itemName, String itemCount,boolean animCount,String refname) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.animCount=animCount;
        this.refname = refname;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public boolean isAnimCount() {
        return animCount;
    }

    public void setAnimCount(boolean animCount) {
        this.animCount = animCount;
    }

    public String getRefname() {
        return refname;
    }

    public void setRefname(String refname) {
        this.refname = refname;
    }

}
