package com.samyotech.laundry.interfaces;

public interface Consts {

    String APP_NAME = "laundry";
//    String DEV_URL = "http://192.168.1.5/win-laundry/";
   // String DEV_URL = "http://192.168.88.19/win-laundry/";
    String DEV_URL = "http://windigitalkhatulistiwa.com/laundryapp_tes/";
    String PROD_URL = "http://laundryapp.windigitalkhatulistiwa.com/";
    String BASE_URL =PROD_URL;
    String API_URL = BASE_URL + "api/";
    String API_URL_TEST = "http://blcstore.id/dunia-laundry/api/";


    /*-----Apis-----*/
    String FORGOTPASSWORD = "forgotPassword";
    String RESENDOTP = "resendOtpSms";
    String OTPSMS = "otpSms";
    String SIGNUP = "signup";
    String LOGIN = "login";
    String LOGIN_GOOGLE = "loginGoogle";
    String LOGOUT = "logOut";
    String DELETEACCOUNT = "deleteAccount";
    String CHANGEPASSWORD = "changePassword";
    String GETHOMEDATA = "getHomeData";
    String GETALLSERVICE = "getAllService";
    String SEARCH = "search";
    String Count = "count";
    String GETALLOFFER = "getAllOffer";
    String GETOFFERFORLAUNDRYSHOP = "getOfferForLaundryShop";
    String other = "getMenuByCategoryId";
    String GETALLLAUNDRYSHOP = "getAllLaundryShop";
    String GETLAUNDRYSHOP = "getLaundryById";
    String GETBOOKINGLIST = "getBookingList";
    String ADDTIKET = "addTiket";
    String TIKETLIST = "tiketList";
    String GETSHOPSERVICES = "getShopServices";
    String GETITEMBYSHOPID = "getItembyShopId";
    String APPLYPROMOCODE = "applyPromocode";
    String GETMESSAGE = "getMessage";
    String SETMESSAGE = "setMessage";
    String ORDERSUBMIT = "orderSubmit";
    String ORDERCANCEL = "orderCancel";
    String ADDRATING = "addRating";
    String GETRATING = "getRating";
    String GETMESSAGEHISTORY = "getMessageHistory";
    String GETLAUNDRYBYSERVICE = "getLaundryByService";
    String GET_NOTIFICATION = "get_notification";
    String GETCURRENCY = "getCurrency";
    String GETTIKETCOMMENT = "getTiketComment";
    String ADDTIKETCOMMENT = "addTiketComment";
    String USERUPDATE = "userUpdate";
    String ORDERIPAYMU = "orderipaymu";


    /*LOGINSIGNUPPARAMS*/
    String EMAIL = "email";
    String ADDRESS = "address";
    String NAME = "name";
    String PASSWORD = "password";
    String COUNTRY_CODE = "country_code";
    String DEVICE_TYPE = "device_type";
    String MOBILE = "mobile";
    String DEVICE_TOKEN = "device_token";

    String FLAG = "flag";
    String USER_DTO = "userdto";
    String REGISTER_DTO = "registerdto";
    String SHOPDTO = "shopdto";
    String NEARSHOPDTO = "nearshopdto";
    String CURRENCYDTO = "currencyDTO";
    String BOOKINGDTO = "bookingdto";
    String ORDERLISTDTO = "orderlistdto";
    String USER_ID = "user_id";
    String RATING = "rating";
    String TIKET_ID = "tiket_id";
    String IMAGE = "image";
    String S_NO = "s_no";
    String STATUS = "status";
    String CREATED_AT = "created_at";
    String UPDATED_AT = "updated_at";
    String SERVICE_NAME = "service_name";
    String TITLE = "title";
    String MESSAGE = "message";
    String MEDIA = "media";
    String DESCRIPTION = "description";
    String ORDERLINK = "orderlink";

    String CAMERA_ACCEPTED = "camera_accepted";
    String STORAGE_ACCEPTED = "storage_accepted";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String MODIFY_AUDIO_ACCEPTED = "modify_audio_accepted";
    String FINE_LOC = "fine_loc";
    String CORAS_LOC = "coras_loc";
    String CALL_PRIVILAGE = "call_privilage";
    String CALL_PHONE = "call_phone";
    String PROMOCODE = "promocode";
    String TOTAL_PRICE = "total_price";
    String IS_REGISTERED = "is_registered";
    String IS_NOT_FIRST_TIME = "is_first_time";

    String ORDER_ID = "order_id";
    String SHOP_ID = "shop_id";
    String SERVICE_ID = "service_id";
    String SERVICEDTO = "servicedto";
    String SHOPSERVICEDTO = "shopservicedto";
    String ITEM_ID = "item_id";
    String ITEM_NAME = "item_name";
    String TO_USER_ID = "to_user_id";
    String FROM_USER_ID = "from_user_id";
    String SHOP_NAME = "shop_name";
    String PRICE = "price";
    String PICKUP_DATE = "pickup_date";
    String PICKUP_TIME = "pickup_time";
    String DELIVERY_DATE = "delivery_date";
    String DELIVERY_TIME = "delivery_time";
    String DISCOUNT = "discount";
    String FINAL_PRICE = "final_price";
    String CURRENCY_CODE = "currency_code";
    String ITEM_DETAILS = "item_details";
    String SHIPPING_ADDRESS = "shipping_address";
    String QUANTITY = "quantity";
    String LANDMARK = "landmark";
    String NEW_PASSWORD = "new_password";
    String BOOKINGFRAGMENT = "3";
    String SCREEN_TAG = "screen_tag";
    String BROADCAST = "broadcast";
    String TICKET_ID = "icket_id";
    String TYPE = "type";

    String BROADCASTNOTIFICATION = "7001";//both
    String PERSONALNOTIFICATION = "7002";//both
    String CHATNOTIFICATION = "7003";//both
    String ORDERNOTIFICATION = "7004";//both
    String TICKETNOTIFICATION = "7005";//both
    //language
    String LANGUAGE = "language";
    String BACKGROUND = "background";
}
