package saneforce.sanclm.api_Interface;

import android.content.Context;
import android.database.Cursor;




import org.json.JSONObject;

import saneforce.sanclm.Common_Class.sqlLite;

public class AppConfig {
    private static AppConfig appConfig;

    private static String weburl = "";
    private static String baseurl = "";
    private static String vCardUrl = "";
    private static String reportPath;
    private static String PDpath = "";

    public static String getPDpath() {
        return PDpath;
    }

    public static void setPDpath(String PDpath) {
        AppConfig.PDpath = PDpath;
    }

    public static String getWeburl() {
        return weburl;
    }

    public static void setWeburl(String weburl) {
        AppConfig.weburl = weburl;
    }

    public static String getBaseurl() {
        return baseurl;
    }

    public static void setBaseurl(String baseurl) {
        AppConfig.baseurl = baseurl.replace("?axn=", "");
    }

    public static String getContext() {
        return vCardUrl;
    }

    public static void setContext(String vCardUrl) {
        AppConfig.vCardUrl = vCardUrl;
    }

    public static String getvCardUrl() {
        return vCardUrl;
    }

    public static void setvCardUrl(String vCardUrl) {
        AppConfig.vCardUrl = vCardUrl;
    }

    public static String getVisiting_Card() {
        return Visiting_Card;
    }

    public static void setVisiting_Card(String visiting_Card) {
        Visiting_Card = visiting_Card;
    }

    public static String getMailPath() {
        return mailPath;
    }

    public static void setMailPath(String mailPath) {
        AppConfig.mailPath = mailPath;
    }

    public static String getUploadPath() {
        return uploadPath;
    }

    public static void setUploadPath(String uploadPath) {
        AppConfig.uploadPath = uploadPath;
    }

    public static String getDivision() {
        return division;
    }

    public static void setDivision(String division) {
        AppConfig.division = division;
    }

    public static String getLogoimg() {
        return logoimg;
    }

    public static void setLogoimg(String logoimg) {
        AppConfig.logoimg = logoimg;
    }

    public static String getOptionFiles() {
        return optionFiles;
    }

    public static void setOptionFiles(String optionFiles) {
        AppConfig.optionFiles = optionFiles;
    }

    public static String getMediaPath() {
        return mediaPath;
    }

    public static String getReportPath() {
        return reportPath;
    }

    public static void setReportPath(String reportPath) {
        AppConfig.reportPath = reportPath;
    }

    public static void setMediaPath(String mediaPath) {
        AppConfig.mediaPath = mediaPath;
    }

    private static String Visiting_Card = "";
    private static String mailPath = "";
    private static String uploadPath = "";
    private static String division = "";
    private static String logoimg = "";
    private static String optionFiles = "";
    private static String mediaPath = "";


    public static synchronized AppConfig getInstance(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();

            sqlLite sqlLite1;
            sqlLite1 = new sqlLite(context);
            Cursor cursor1 = sqlLite1.getAllMasterSyncData("AppSettings");
            if (cursor1.getCount() < 1) {

            } else {
                try {
                    String curval = "";
                    if (cursor1.moveToFirst()) {
                        curval = cursor1.getString(cursor1.getColumnIndex("Master_Sync_Values"));
                    }
                    cursor1.close();
                    JSONObject jsonObject = new JSONObject(curval);
                    JSONObject webdet = jsonObject.getJSONObject("config");

                    appConfig.setWeburl(webdet.getString("weburl"));
                    appConfig.setBaseurl(webdet.getString("baseurl"));
                    appConfig.setvCardUrl(webdet.getString("vCardUrl"));
                    appConfig.setMailPath(webdet.getString("mailPath"));
                    appConfig.setUploadPath(webdet.getString("uploadPath"));
                    appConfig.setDivision(webdet.getString("division"));
                    appConfig.setLogoimg(webdet.getString("logoimg"));
                    appConfig.setReportPath(webdet.getString("reportPath"));
                } catch (Exception e) {

                }

            }
        }
            return appConfig;
    }

        public static void ClearInstance () {
            appConfig = null;
        }

    }
