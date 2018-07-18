package app.application.recharge.titoriya.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by user on 2/7/2017.
 */

public class MyPrefrences {
    static SharedPreferences loginPreferences;
    static SharedPreferences usertypePreferences;
    static SharedPreferences User_Id_Preferences;
    static SharedPreferences User_Pin_Preferences;
    static SharedPreferences User_Mobile_Preferences;
    static SharedPreferences User_Token_Preferences;
    static SharedPreferences loginOTPPreferences;
    static SharedPreferences MOBILE_NO_NEW;


    public static String USER_LOGIN = "userlogin";
    public static String USER_TYPE = "usertype";
    public static String USER_ID = "userid";
    public static String USER_PIN = "user_pin";
    public static String USER_Mobile = "USER_Mobile";
    public static String USER_Token = "USER_Token";
    public static String USER_Login_OTP = "USER_Login_OTP";
    public static String MOBILE_NEW = "MOBILE_NEW";


    public static void resetPrefrences(Context context) {
        setUserLogin(context, false);
        setUserType(context, "");
        setUserMobile(context, "");
    }

    public static void setUserLogin(Context context, boolean is) {
        loginPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.putBoolean(USER_LOGIN, is);
        editor.commit();
    }


    public static void setLoginOTP(Context context, boolean is) {
        loginOTPPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = loginOTPPreferences.edit();
        editor.putBoolean(USER_Login_OTP, is);
        editor.commit();
    }

    public static boolean getLogin_OTP(Context context) {
        loginOTPPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return loginOTPPreferences.getBoolean(USER_Login_OTP,true);
    }

    public static boolean getUserLogin(Context context) {
        loginPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return loginPreferences.getBoolean(USER_LOGIN, false);
    }

    public static void setUserType(Context context, String is) {
        usertypePreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = usertypePreferences.edit();
        editor.putString(USER_TYPE, is);
        editor.commit();
    }

    public static String getUserType(Context context) {
        usertypePreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return usertypePreferences.getString(USER_TYPE, "");
    }

    public static void setUserId(Context context, String is) {
        User_Id_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = User_Id_Preferences.edit();
        editor.putString(USER_ID, is);
        editor.commit();
    }

    public static String getUserId(Context context) {
        User_Id_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return User_Id_Preferences.getString(USER_ID, "242584fdcf14b1a4");
    }

    public static void setUserPin(Context context, String is) {
        User_Pin_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = User_Pin_Preferences.edit();
        editor.putString(USER_PIN, is);
        editor.commit();
    }

    public static String getUserPin(Context context) {
        User_Pin_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return User_Pin_Preferences.getString(USER_PIN, "555584fdcf14b1de");

    }

    public static void setUserMobile(Context context, String is) {
        User_Mobile_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = User_Mobile_Preferences.edit();
        editor.putString(USER_Mobile, is);
        editor.commit();
    }

    public static String getUserMobile(Context context) {
        User_Mobile_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return User_Mobile_Preferences.getString(USER_Mobile, "0123456789");

    }

    public static void setToken(Context context, String is) {
        User_Token_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = User_Token_Preferences.edit();
        editor.putString(USER_Token, is);
        editor.commit();
    }

    public static String getToken(Context context) {
        User_Token_Preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return User_Token_Preferences.getString(USER_Token, "0");

    }

    public static void setMobileNoNew(Context context, String is) {
        MOBILE_NO_NEW = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = MOBILE_NO_NEW.edit();
        editor.putString(MOBILE_NEW, is);
        editor.commit();
    }

    public static String getMobileNoNew(Context context) {
        MOBILE_NO_NEW = PreferenceManager.getDefaultSharedPreferences(context);
        return MOBILE_NO_NEW.getString(MOBILE_NEW, "0123456789");

    }

}
