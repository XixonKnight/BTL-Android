package com.example.booking.constant;

import com.example.booking.model.dbo.Hotel;

public class Constant {
    public static final String PATTERN_EMAIL = "^[a-zA-Z0-9_[^!@#%^&$*()\\s-+=]]+@[ge]mail.com$";
    public static final String PATTERN_PASSWORD = "^[a-zA-Z0-9[^_!@#%^&$*()\\s-+=]]{8,}$";
    public static final String SUBJECT_SEND = "Booking Room";
    public static final String FORMAT_DATE = "dd/MM/yyyy";


    //    id Area
    public static final int ID_HANOI = 1;
    public static final int ID_TP_HCM = 2;
    public static final int ID_QUANG_NINH = 3;
    public static final int ID_DA_NANG = 4;
    public static final int ID_NHA_TRANG = 5;
    public static final int ID_HUE = 7;
    public static final int ID_HAI_PHONG = 8;

    //    Id Categories
    public static final int ID_SINGLE_ROOM = 1;
    public static final int ID_DOUBLE_ROOM = 2;
    public static final int ID_FAMILY_ROOM = 3;

//    Id Hotel


    //    local server
    public static final String URL_CREATE_USER = "https://xixonknight.000webhostapp.com/query_booking_room/CreateUser.php";
    public static final String URL_LOGIN = "https://xixonknight.000webhostapp.com/query_booking_room/CheckLogin.php";
    public static final String URL_SELECT_INFO_CUSTOMER = "https://xixonknight.000webhostapp.com/query_booking_room/SelectInfoCustomer.php";
    public static final String URL_UPDATE_INFO_CUSTOMER = "https://xixonknight.000webhostapp.com/query_booking_room/UpdateInfoCustomer.php";
    public static final String URL_CHECK_GMAIL = "https://xixonknight.000webhostapp.com/query_booking_room/CheckGmail.php";
    public static final String URL_UPDATE_PASSWORD = "https://xixonknight.000webhostapp.com/query_booking_room/UpdatePasswordCustomer.php";
    public static final String URL_SELECT_ALL_AREA = "https://xixonknight.000webhostapp.com/query_booking_room/SelectAllArea.php";
    public static final String URL_SELECT_ALL_CATEGORY = "https://xixonknight.000webhostapp.com/query_booking_room/SelectAllCategory.php";
    public static final String URL_CHECK_CREATE_GMAIL = "https://xixonknight.000webhostapp.com/query_booking_room/CheckGmailCreate.php";
    public static final String URL_SELECT_ALL_DATA_HOTEL = "https://xixonknight.000webhostapp.com/query_booking_room/SelectAllHotel.php";
    public static final String URL_SELECT_IMAGE_HOTEL = "https://xixonknight.000webhostapp.com/query_booking_room/SelectImageHotel.php";
    public static final String URL_SELECT_TYPE_ROOM = "https://xixonknight.000webhostapp.com/query_booking_room/SelectTypeRoom.php";
    public static final String URL_INSERT_CMT = "https://xixonknight.000webhostapp.com/query_booking_room/InsertCmt.php";
    public static final String URL_GET_CMT_BY_HOTEL = "https://xixonknight.000webhostapp.com/query_booking_room/GetAllCommentByHotel.php";
}
