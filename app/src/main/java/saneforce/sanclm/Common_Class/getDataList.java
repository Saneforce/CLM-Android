package saneforce.sanclm.Common_Class;

public class getDataList {

    private String name;
    private String url;
    private String item;
    private String tableName;
    private String coloumns;
    private String today;
    private String join;
    private String where;
    private String or;
    private String wt;
    private String sfCode;
    private String orderBy;
    private String mastersync_key;

    int progress;
    public getDataList(String name,String key,String url, String item,String tableName,String today,String join,String where,String or,String wt,String sfCode,String orderBy) {
        this.mastersync_key=key;
        this.name=name;
        this.url=url;
        this.item=item;
        this.tableName=tableName;
        if(tableName=="vwDoctor_Master_APP")
            this.coloumns= "[\"doctor_code as id\", \"doctor_name as name\",\"hospital_code\",\"hospital_name\",\"town_code\",\"town_name\",\"lat\",\"long\",\"addrs\",\"doctor_category\",\"Doc_Cat_Code\",\"doctor_speciality\",\"Doc_Class_ShortName as dr_class\",\"Doc_ClsCode\",\"ListedDr_DOB as DOB\",\"ListedDr_DOW as DOW\",\"Tlvst\",\"Drvst_month\",\"Product_Code\",\"Product_Brd_Code\",\"Doc_Special_Code\",\"idsl\",\"Gcount\",\"Geototal\",\"addr\",\"img_name\",\"cus_phone\"]";
        else if(tableName=="vwChemists_Master_APP")
            this.coloumns= "[\"chemists_code as id\", \"chemists_name as name\",\"lat\",\"long\",\"Chm_cat\",\"town_code\",\"town_name\",\"addr\",\"img_name\",\"cus_phone\"]";
        else if(tableName=="vwstockiest_Master_APP")
            this.coloumns= "[\"stockiest_code as id\", \"stockiest_name as name\",\"lat\",\"long\",\"town_code\",\"town_name\",\"addr\",\"img_name\",\"cus_phone\"]";
        else if(tableName=="vwunlisted_doctor_master_APP")
            this.coloumns= "[\"unlisted_doctor_code as id\", \"unlisted_doctor_name as name\",\"lat\",\"long\",\"town_code\",\"town_name\",\"addr\",\"img_name\",\"cus_phone\"]";
        else if(tableName=="vwCIP_APP")
            this.coloumns= "[\"id\",\"name\",\"hospital_code\",\"hospital_name\",\"Town_code as town_code\",\"Town_Name as town_name\"]";
        else if(tableName=="vwHosp_Master_App")
            this.coloumns= "[\"Hospital_Code as id\",\"Hospital_Name as name\",\"lat\",\"long\",\"town_code\",\"town_name\"]";
        else if(tableName=="mas_worktype")
            this.coloumns= "[\"type_code as id\", \"Wtype as name\"]";
        else if(tableName=="product_master")
            this.coloumns= "[\"product_code as id\", \"product_name as name\", \"Product_Sl_No as pSlNo\", \"Product_Category cateid\"]'";
        else if(tableName=="gift_master")
            this.coloumns= "[\"gift_code as id\", \"gift_name as name\" ]'";
        else if(tableName=="salesforce_master")
            this.coloumns= "[\"sf_code as id\", \"sf_name as name\"]";
        else if(tableName=="vwTown_Master_APP")
            this.coloumns= "[\"town_code as id\", \"town_name as name\",\"Tcodes\"]";
        else if(tableName=="vwFeedTemplate")
            this.coloumns= "[\"id as id\", \"content as name\"]";
        else if(tableName=="vwRmksTemplate")
            this.coloumns= "[\"id as id\", \"content as name\"]";
        else if(tableName=="mas_worktype")
            this.coloumns= "[\"type_code as id\", \"Wtype as name\",\"Hlfdy_flag as Hlfdy_flag\"]";
        else if(tableName=="vwMyDayPlan")
            this.coloumns= "[\"worktype\",\"FWFlg\",\"sf_member_code as subordinateid\",\"cluster as clusterid\",\"ClstrName\",\"remarks\"]";
        else if(tableName=="vwTourPlan")
            this.coloumns="[\"date\",\"remarks\",\"worktype_code\",\"worktype_name\",\"RouteCode\",\"RouteName\",\"RouteCode2\",\"RouteName2\",\"RouteCode3\",\"RouteName3\",\"Worked_with_Code\",\"Worked_with_Name\",\"worktype_code2\",\"worktype_name2\",\"worktype_code3\",\"worktype_name3\"]";
        this.today=today;
        this.join=join;
        this.where=where;
        this.or=or;
        this.wt=wt;
        this.sfCode=sfCode;
        this.orderBy=orderBy;

    }

    public String getMastersyncKey() {
        return mastersync_key;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getToday() {
        return today;
    }
    public String getItem() {
        return item;
    }
    public String getTablename() {
        return tableName;
    }
    public String getColoumns() {
        return coloumns;
    }
    public String getJoin() {
        return join;
    }
    public String getWhere() {
        return where;
    }
    public String getSfcode() {
        return sfCode;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getWt() {
        return wt;
    }

    public String getOr() {
        return or;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
