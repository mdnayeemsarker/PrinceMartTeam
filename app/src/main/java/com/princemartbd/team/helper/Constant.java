package com.princemartbd.team.helper;

public class Constant {
    //MODIFICATION PART
    //Admin panel url
    public static final String MainBaseUrl = "https://admin.princemartbd.com/";
    public static String APP_UPDATE_URL = MainBaseUrl + "apps.php";
//    public static final String MainBaseUrl = "https://beta.princemartbd.com/";

    //Admin panel url or ecart multivendor website url (if exist)
    public static final String WebSiteUrl = "https://princemartbd.com/";

    //set your jwt secret key here...key must same in PHP and Android
    public static final String JWT_KEY = "BFbHzx@?6mU}_wv8Q2q~A*&aWp%39=GTVf!;t)e(Ey{<j^LR:,";

    //MODIFICATION PART END
    public static final String BaseUrl = MainBaseUrl + "affiliate/";
    public static final String BaseUrl_API_Firebase = MainBaseUrl + "api-firebase/";

    //**********APIS**********
    public static final String AUTH_LOGIN_URL = BaseUrl + "auth.php";
    public static final String AFFILIATER_ADD = BaseUrl + "manage_affiliaters.php";
    public static final String WITHDRAW_REQUEST = BaseUrl + "withdraw.php";
    public static final String DASHBOARD = BaseUrl + "dashboard.php";
    public static final String ALL_MARKETERS = BaseUrl + "manage_affiliaters.php";
    public static final String ALL_TRANSACTIONS = BaseUrl + "manage_transactions.php";

    public static final String REGISTER_URL = BaseUrl + "user-registration.php";
    public static final String FORGET_PASSWORD_URL = BaseUrl + "auth.php";
    public static final String SMS_SEND_URL = BaseUrl_API_Firebase + "sms.php";
    public static final String SETTING_URL = BaseUrl_API_Firebase + "settings.php";

    //**************parameters***************
    public static final String SETTINGS = "settings";
    public static final String AccessKey = "accesskey";
    public static final String AccessKeyVal = "j-V]![e7f6k8)W&r(v=pQs<Zm.u?^y4dUC;c,qa5RbBLP3}X%h";  // change
    public static final String PROFILE = "profile";
    public static final String GetVal = "1";
    public static final String AUTHORIZATION = "Authorization";
    public static final String IS_USER_LOGIN = "is_user_login";
    public static final String GET_PRIVACY = "get_privacy";
    public static final String GET_TERMS = "get_terms";
    public static final String GET_CONTACT = "get_contact";
    public static final String GET_ABOUT_US = "get_about_us";
    public static final String BALANCE = "balance";
    public static final String COUNTRY_CODE = "country_code";
    public static final String REFERRAL_CODE = "referral_code";
    public static final String FRIEND_CODE = "friends_code";
    public static final String TYPE_TRANSACTION = "transactions";
    public static final String CREDIT = "credit";
    public static final String TYPE = "type";
    public static final String TYPE_ID = "type_id";
    public static final String LAST_UPDATED = "last_updated";
    public static final String AFFILIATER_CODE = "affiliater_code";
    public static final String DATE_CREATED = "date_created";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String ADDED_BY = "added_by";
    public static final String STATUS = "status";
    public static final String REGISTER = "register";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String LOGIN = "login";
    public static final String PAGE = "page";
    public static final String PASSWORD = "password";
    public static final String FCM_ID = "fcm_id";
    public static final String ERROR = "error";
    public static final String USER_ID = "user_id";
    public static final String ALL_AFFILIATERS = "all_affiliaters";
    public static final String ADD_AFFILIATERS = "add_affiliater";
    public static final String FORGOT_PASSWORD_MOBILE = "forgot-password-mobile";
    public static final String DATA = "data";
    public static final String CURRENCY = "currency";
    public static final String AMOUNT = "amount";
    public static final String MESSAGE = "message";
    public static final String FROM = "from";
    public static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghjiklmnopqrstuvwxyz";
    public static final String NUMERIC_STRING = "123456789";
    public static final String MANAGER = "manager";
    public static final String AFFILIATERS = "affiliaters";
    public static final String TYPE_USER = "type_user";
    public static final String MARKETER = "marketer";
    public static final String TRANSACTION = "Transactions";
    public static final String ManProfileActivity = "ManProfileActivity";
    public static final String WebViewActivity = "WebViewActivity";
    public static final String API_KEY = "API_KEY";
    public static final String NOT_APPROVED = "NOT APPROVED";
    public static final String APPROVED = "APPROVED";
    public static final String CANCEL = "CANCEL";
    public static final String DEBIT = "DEBIT";
    public static final String CREDIT_MESSAGE = "CREDIT";
    public static final String GENERATED_OTP = "generated_otp";
    public static final String SEND_SMS = "send_sms";
    public static final String TEXT = "text";
    public static final String TOTAL_BALANCE = "total_balance";
    public static final String MANAGE_TEAM = "manage_team";
    public static final String EARNING = "earning";
    public static final String WITHDRAW = "withdraw";
    public static final String ADD_MARKETER = "add_marketer";
    public static final String MAN_WITHDRAW = "ManWithHistoryActivity";
    public static final String VERIFY_USER = "verify-user";
    public static final String WEB_VIEW = "web_view";
    public static final String ManWithDashboard = "ManWithDashboardActivity";
    public static final String MarWithHistoryActivity = "MarWithHistoryActivity";
    public static final String MarWithdrawActivity = "MarWithdrawActivity";
    public static final String MarTransactionsActivity = "MarTransactionsActivity";
    public static final String MarWithDashActivity = "MarWithDashActivity";

    //**************Constants Values***************
    public static int TOTAL_CART_ITEM = 0;
    public static String MERCHANT_ID = "";
    public static String MERCHANT_KEY = "";
    public static String MERCHANT_SALT = "";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String randomNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}