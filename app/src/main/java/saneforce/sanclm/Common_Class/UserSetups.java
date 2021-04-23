package saneforce.sanclm.Common_Class;

public class UserSetups {
    private static UserSetups userSetups;
    private static int AppType;

    public static int getAppType() {
        return AppType;
    }

    public static void setAppType(int appType) {
        AppType = appType;
    }

    public static synchronized UserSetups getInstance() {
        if (userSetups == null) {
            userSetups = new UserSetups();
        }
        userSetups.AppType=1;

        return userSetups;
    }

    public static void ClearInstance() {
        userSetups = null;
    }
}
