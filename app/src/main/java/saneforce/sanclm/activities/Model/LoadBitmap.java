package saneforce.sanclm.activities.Model;

import android.graphics.Bitmap;

public class LoadBitmap {
    Bitmap bitmaps;
    int indexVal;
    String timing;
    String dateVal;
    String brdName;
    boolean isCheck;

    public LoadBitmap(String brdName,String timing){
        this.brdName=brdName;
        this.timing=timing;
    }

    public LoadBitmap(String timing){
        this.brdName=brdName;
        this.timing=timing;
        this.dateVal=dateVal;
    }



    public LoadBitmap(Bitmap bitmaps, int indexVal) {
        this.bitmaps = bitmaps;
        this.indexVal = indexVal;
    }

    public LoadBitmap(String timing,int indexVal,String dateVal,String brdName){
        this.timing=timing;
        this.indexVal=indexVal;
        this.dateVal=dateVal;
        this.brdName=brdName;
    }

    public LoadBitmap(String name,boolean isCheck){
        brdName=name;
        this.isCheck=isCheck;
    }

    public String getBrdName() {
        return brdName;
    }

    public void setBrdName(String brdName) {
        this.brdName = brdName;
    }

    public String getDateVal() {
        return dateVal;
    }

    public void setDateVal(String dateVal) {
        this.dateVal = dateVal;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public Bitmap getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(Bitmap bitmaps) {
        this.bitmaps = bitmaps;
    }

    public int getIndexVal() {
        return indexVal;
    }

    public void setIndexVal(int indexVal) {
        this.indexVal = indexVal;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
