package saneforce.sanclm.adapter_class;

import java.util.Comparator;

public class Custom_DCR_GV_Dr_Contents {

    private String mDoctorName;
    private String mDoctorcode;
    private String  mDocotrSpeciality;
    private String  mDocotrCategory;
    private String mDoctorTown;
    private String mDoctorTownCd;
    private String max,tag,specCode;

    public Custom_DCR_GV_Dr_Contents(String mDoctorName, String mDoctorcode, String mDocotrSpeciality, String mDocotrCategory, String mDocotorTown, String mDocotorTownCd,String max,String tag) {
        this.mDoctorName = mDoctorName;
        this.mDoctorcode = mDoctorcode;
        this.mDocotrSpeciality = mDocotrSpeciality;
        this.mDocotrCategory = mDocotrCategory;
        this.mDoctorTown = mDocotorTown;
        this.mDoctorTownCd = mDocotorTownCd;
        this.max=max;
        this.tag=tag;

    }
    public Custom_DCR_GV_Dr_Contents(String mDoctorName, String mDoctorcode, String mDocotrSpeciality, String mDocotrCategory, String mDocotorTown, String mDocotorTownCd,String max,String tag,String specCode) {
        this.mDoctorName = mDoctorName;
        this.mDoctorcode = mDoctorcode;
        this.mDocotrSpeciality = mDocotrSpeciality;
        this.mDocotrCategory = mDocotrCategory;
        this.mDoctorTown = mDocotorTown;
        this.mDoctorTownCd = mDocotorTownCd;
        this.max=max;
        this.tag=tag;
        this.specCode=specCode;

    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getmDoctorName() {
        return mDoctorName;
    }

    public void setmDoctorName(String mDoctorName) {
        this.mDoctorName = mDoctorName;
    }

    public String getmDoctorcode() {
        return mDoctorcode;
    }

    public void setmDoctorcode(String mDoctorcode) {
        this.mDoctorcode = mDoctorcode;
    }

    public String getmDocotrSpeciality() {
        return mDocotrSpeciality;
    }

    public void setmDocotrSpeciality(String mDocotrSpeciality) {
        this.mDocotrSpeciality = mDocotrSpeciality;
    }

    public String getmDocotrCategory() {
        return mDocotrCategory;
    }

    public void setmDocotrCategory(String mDocotrCategory) {
        this.mDocotrCategory = mDocotrCategory;
    }

    public String getmDoctorTown() {
        return mDoctorTown;
    }

    public void setmDoctorTown(String mDoctorTown) {
        this.mDoctorTown = mDoctorTown;
    }

    public String getmDoctorTownCd() {
        return mDoctorTownCd;
    }

    public void setmDoctorTownCd(String mDoctorTownCd) {
        this.mDoctorTownCd = mDoctorTownCd;
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<Custom_DCR_GV_Dr_Contents> StuNameComparator = new Comparator<Custom_DCR_GV_Dr_Contents>() {

        @Override
        public int compare(Custom_DCR_GV_Dr_Contents o1, Custom_DCR_GV_Dr_Contents o2) {
            String drname1=o1.getmDoctorName().toUpperCase();
            String drname2=o2.getmDoctorName().toUpperCase();

            return drname1.compareTo(drname2);
        }

    };
}
