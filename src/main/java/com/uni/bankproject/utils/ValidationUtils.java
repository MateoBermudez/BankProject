package com.uni.bankproject.utils;

public class ValidationUtils {

    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean isUserIDValid(String userID) {
        return userID.matches("^[0-9]{10}$");
    }

    public static boolean isUserNameValid(String userName) {
        return userName.matches("^[a-zA-Z0-9]{3,}$");
    }

    public static boolean isUserKeyValid(String userKey) {
        return userKey.matches("^[0-9]{4}$");
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^[0-9]{10}$");
    }

    public static boolean isAddressValid(String address) {
        return address.matches("^[a-zA-Z0-9\\s,.-]+$");
    }
}
