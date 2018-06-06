package com.dinehawaiipartner.Util;

/**
 * Created by MAX on 25-Jan-17.
 */

public class AppConstants {

    public static final String KEY_FCM_ID = "fcm_id";
    public static final String KEY_USER_TYPE = "user_type";
    public static String KEY_METHOD = "method";
    public static String KEY_USER_ID = "user_id";

    public interface BASEURL {
        String URL = "http://take007.co.in/Projects-Work/Hawaii/APP/Partner_App/";
    }

    public interface ENDPOINT {
        String LOGINURL = "Register_Vendor_Log.php";
        String DRIVERURL = "Vendor_Partner_Driver_Api.php";
        String ORDERSURL = "get_Orders_Api.php";
        String GET_ORDERS_DRIVER_API = "get_Orders_Driver_Api.php";

        String OTHER_VENDOR_URL = "normal_user/register_log.php";
    }

    public interface REGISTRATION {

        String USERLOGIN = "Partner_Vendor_login";

    }

    public interface VENDOR_METHODS {
        String ALLDRIVERS = "All_Drivers";
        String ADD_DRIVER = "Add_New_Driver";
        String EDIT_DRIVER = "Edit_New_Driver";
        String GETALLPENDINGORDERS = "getDeliveryOrders";
        String GETPENDINGDELIVERYORDERS = "getPendingDeliveryOrders";
        String ASSIGNORDER = "assignOrderToDriver";
        String LOGOUTVENDOR = "Logout_Vendor_Driver";
        String GETSTARTEDDELIVERY = "getStartedDeliveryOrders";
        String GETCOMPLETEDDELIVERY = "getCompletedDeliveryOrders";
        String VENDOR_ACCEPT_DELIVERY = "vendor_accept_delivery";
        String VENDOR_REJECT_DELIVERY = "vendor_reject_delivery";
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
        public static final String OTHER_VENDOR = "OtherVendor";
    }

    public class NOTIFICATION_KEY {
        public static final String DEFAULT_MESSAGE = "default_message";
        public static final String DRIVER_NEW_DELIVERY = "driver_new_delivery";
        public static final String MANAGER_NEW_DELIVERY = "manager_new_delivery";
        public static final String MANAGER_DELIVERY_PICKEDUP = "manager_driver_picked";
        public static final String MANAGER_DELIVERY_COMPLETED = "manager_driver_completed";
        public static final String MANAGER_DELIVERY_ACCEPTED = "manager_delivery_accepted";
        public static final String DRIVER_DELIVERY_ACCEPTED = "driver_delivery_accepted";
    }

    public class NOTIFICATION_ID {
        public static final int DEFAULT = 1;
        public static final int DRIVER_NEW_DELIVERY = 2;
        public static final int MANAGER_DELIVERY_PICKEDUP = 3;
        public static final int MANAGER_DELIVERY_COMPLETED = 4;
    }

    public class OTHER_VENDOR_API {
        public static final String LOGOUT = "mobile_logout";
    }

    public class COMMON_METHODS {
        public static final String UPDATE_FCM = "update_fcm_token";
    }
}
