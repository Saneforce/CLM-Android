package saneforce.sanclm.activities.Model;

public class ModelDownloadMaster {
    String itemName;
    String itemCount;
    boolean animCount;

    public ModelDownloadMaster(String itemName, String itemCount,boolean animCount) {
        this.itemName = itemName;
        this.itemCount = itemCount;
        this.animCount=animCount;
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
}
