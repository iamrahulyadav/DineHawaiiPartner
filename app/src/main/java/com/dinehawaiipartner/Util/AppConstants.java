package com.dinehawaiipartner.Util;

/**
 * Created by MAX on 25-Jan-17.
 */

public class AppConstants {

    public static String KEY_METHOD = "method";
    public static String KEY_USER_ID = "user_id";

    public interface BASEURL {
        String URL = "http://take007.co.in/Projects-Work/Hawaii/APP/Partner_App/";
    }

    public interface ENDPOINT {
        String LOGINURL = "Register_Vendor_Log.php";
        String DRIVERURL = "Vendor_Partner_Driver_Api.php";
        String ORDERSURL = "get_Orders_Api.php";
    }

    public interface REGISTRATION {

        String USERLOGIN = "Partner_Vendor_login";

    }

    public interface VENDOR_METHODS {
        String ALLDRIVERS = "All_Drivers";
        String ADD_DRIVER = "Add_New_Driver";
        String EDIT_DRIVER = "Edit_New_Driver";
        String GETALLPENDINGORDERS = "getDeliveryOrders";
        String ASSIGNORDER = "assignOrderToDriver";
        String LOGOUTVENDOR = "Logout_Vendor_Driver";
        String GETSTARTEDDELIVERY = "getStartedDeliveryOrders";
        String GETCOMPLETEDDELIVERY = "getCompletedDeliveryOrders";
    }

    public interface DRIVER_METHODS {
        String UPDATELOG = "driver_log_update";
        String LOGOUTDRIVER = "Logout_Vendor_Driver";
        String NEWDELIVERIES = "get_new_deliveries";
        String STARTDELIVERY = "start_trip";
        String COMPLETEDELIVERY = "complete_trip";
        String DRIVER_START_DELIVERIES = "driver_start_deliveries";
    }


    public class LOGIN_TYPE {
        public static final String VENDOR_USER = "PartnerVendor";
        public static final String DRIVER = "Driver";
    }

    public class NOTIFICATION_KEY {
        public static final String DEFAULT_MESSAGE = "default_message";
        public static final String DRIVER_NEW_DELIVERY = "driver_new_delivery";
        public static final String MANAGER_NEW_DELIVERY = "manager_new_delivery";
        public static final String MANAGER_DELIVERY_PICKEDUP = "manager_driver_picked";
        public static final String MANAGER_DELIVERY_COMPLETED = "manager_driver_completed";
    }

    public class NOTIFICATION_ID {
        public static final int DEFAULT = 1;
        public static final int DRIVER_NEW_DELIVERY = 2;
        public static final int MANAGER_DELIVERY_PICKEDUP = 3;
        public static final int MANAGER_DELIVERY_COMPLETED = 4;
    }
}
