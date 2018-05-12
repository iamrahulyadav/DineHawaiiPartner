package com.dinehawaiipartner.Util;

/**
 * Created by MAX on 25-Jan-17.
 */

public class AppConstants {


    public static String KEY_METHOD="method";
    public static String KEY_USER_ID="user_id";


    public interface BASEURL {
        String URL = "http://take007.co.in/Projects-Work/Hawaii/APP/Partner_App/";
    }

    public interface ENDPOINT {
         String LOGINURL = "Register_Vendor_Log.php";
         String GETALLDRIVERURL = "Vendor_Partner_Driver_Api.php";
    }

    public interface REGISTRATION {

      String USERLOGIN = "Partner_Vendor_login";

    }
    public interface VENDOR_METHODS{
        String ALLDRIVERS = "All_Drivers";
        String ADD_DRIVER = "Add_New_Driver";
        String EDIT_DRIVER = "Edit_New_Driver";
    }


    public class LOGIN_TYPE {
        public static final String VENDOR_USER = "PartnerVendor";
        public static final String DRIVER = "Driver";
    }
}
