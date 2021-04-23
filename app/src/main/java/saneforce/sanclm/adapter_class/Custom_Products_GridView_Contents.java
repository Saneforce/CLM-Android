package saneforce.sanclm.adapter_class;

import android.graphics.Bitmap;
import android.util.Log;

public class Custom_Products_GridView_Contents {

    String mProductBrdCode,mFileName,mLogoURL,mLocalID,mFiletype,mDoctorCode,mProductBrdName,mPresentName;

    Boolean mSelectionState;

    int mAdapterMode,columnid;
    Bitmap bitmap;
    int brdid;

    public Custom_Products_GridView_Contents(String mFileName,String ProductBrdCode,String ProductBrdName,String mLogoURL,Boolean mSelectionState,int mAdapterMode,String mFiletype,Bitmap bitmap)
    {
        super();
        this.mFileName=mFileName;
        this.mProductBrdCode=ProductBrdCode;
        this.mProductBrdName=ProductBrdName;
        this.mFiletype=mFiletype;
        this.mLogoURL=mLogoURL;
        Log.v("logo_url_fif",this.mLogoURL);
        this.mSelectionState=mSelectionState;
        this.mAdapterMode=mAdapterMode;
        this.bitmap=bitmap;
    }





    public Custom_Products_GridView_Contents(String mFileName,String ProductBrdCode,String ProductBrdName,String mLogoURL,Boolean mSelectionState,int mAdapterMode,String mFiletype,String presentname)
    {
        super();
        this.mFileName=mFileName;
        this.mProductBrdCode=ProductBrdCode;
        this.mProductBrdName=ProductBrdName;
        this.mFiletype=mFiletype;
        this.mLogoURL=mLogoURL;
        Log.v("logo_url_fif",this.mLogoURL);
        this.mSelectionState=mSelectionState;
        this.mAdapterMode=mAdapterMode;
        this.mPresentName=presentname;
    }
    public Custom_Products_GridView_Contents(String mFileName,String ProductBrdCode,String ProductBrdName,String mLogoURL,Boolean mSelectionState,int mAdapterMode,String mFiletype,String presentname,int columnid)
    {
        super();
        this.mFileName=mFileName;
        this.mProductBrdCode=ProductBrdCode;
        this.mProductBrdName=ProductBrdName;
        this.mFiletype=mFiletype;
        this.mLogoURL=mLogoURL;
        Log.v("logo_url_fif",this.mLogoURL);
        this.mSelectionState=mSelectionState;
        this.mAdapterMode=mAdapterMode;
        this.mPresentName=presentname;
        this.columnid=columnid;
    }
    public Custom_Products_GridView_Contents(String mFileName, String ProductBrdCode, String ProductBrdName, String mLogoURL, Boolean mSelectionState, int mAdapterMode, String mFiletype, String presentname, int columnid, Bitmap bitmap,int brdid,String mLocalID)
    {
        super();
        this.mFileName=mFileName;
        this.mProductBrdCode=ProductBrdCode;
        this.mProductBrdName=ProductBrdName;
        this.mFiletype=mFiletype;
        this.mLogoURL=mLogoURL;
        Log.v("logo_url_fif",this.mLogoURL);
        this.mSelectionState=mSelectionState;
        this.mAdapterMode=mAdapterMode;
        this.mPresentName=presentname;
        this.columnid=columnid;
        this.bitmap=bitmap;
        this.brdid=brdid;
        this.mLocalID=mLocalID;
    }

    public Custom_Products_GridView_Contents(String ProductBrdName){
        this.mProductBrdName=ProductBrdName;
    }

    public Custom_Products_GridView_Contents(boolean mSelectionState){
        this.mSelectionState=mSelectionState;
    }

    @Override
    public boolean equals(Object obj) {
        Custom_Products_GridView_Contents dt = (Custom_Products_GridView_Contents)obj;

       /* if(mSelectionState){
            Log.v("product_name_in","empty");

        }
        else{
            Log.v("product_name_in","ntempty");
        }*/
        if(mProductBrdName == null){
            if(mSelectionState){
                if(dt.mSelectionState.equals(mSelectionState)) return true;
            }
            return false;
        }
        if(dt.mProductBrdName.equals(mProductBrdName)) return true;

        return false;
    }

    public Custom_Products_GridView_Contents(String ProductBrdCode,String ProductBrdName,String mLogoURL,Boolean mSelectionState,int mAdapterMode,String mFiletype)
    {
        super();
        this.mProductBrdCode=ProductBrdCode;
        this.mProductBrdName=ProductBrdName;
        this.mFiletype=mFiletype;
        this.mLogoURL=mLogoURL;
        Log.v("logo_url_fif",this.mLogoURL);
        this.mSelectionState=mSelectionState;
        this.mAdapterMode=mAdapterMode;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getColumnid() {
        return columnid;
    }

    public void setColumnid(int columnid) {
        this.columnid = columnid;
    }

    public String getmPresentName() {
        return mPresentName;
    }

    public void setmPresentName(String mPresentName) {
        this.mPresentName = mPresentName;
    }

    public String getmLocalID()
    {
        return mLocalID;
    }
    public int getmAdapterMode()
    {
        return mAdapterMode;
    }
    public String getmDoctorCode()
    {
        return mDoctorCode;
    }
    public String getmFiletype()
    {
        return mFiletype;
    }

    public String getmProductCode()
    {
        return mProductBrdCode;
    }

    public String getmFileName()
    {
        return mFileName;
    }

    public String getmProductName()
    {
        return mProductBrdName;
    }

    public void setmProductName(String mProductBrdName) {
        this.mProductBrdName = mProductBrdName;
    }

    public String getmLogoURL()
    {
        return mLogoURL;
    }

    public Boolean getmSelectionState()
    {
        return mSelectionState;
    }

    public void setmSelectionState(Boolean mSelectionState)
    {
        this.mSelectionState=mSelectionState;
    }

    public int getBrdid() {
        return brdid;
    }

    public void setBrdid(int brdid) {
        this.brdid = brdid;
    }
}
