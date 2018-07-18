package app.application.recharge.titoriya.Utils;

/**
 * Created by user on 2/6/2017.
 */

public class Api {

//  public static String BASE_URL_RECHARGE = "http://rechargerock.com/r_admin/ws/";
//    public static String Base_Url_New =    "http://rechargerock.com/r_admin/ws/";



    public static String BASE_URL_RECHARGE = "https://trubizpay.com/retail-login/web-sr/";
    public static String Base_Url_New =      "https://trubizpay.com/retail-login/web-sr/";

    public static String BASE_URL = "http://dmts.co.in/api/";

    public static String URL_STORE_TOKEN = BASE_URL_RECHARGE + "login_access.php";
    public static String ForgetPwd_Url = "";
    public static String First_Step_Recharge = BASE_URL_RECHARGE + "mobile_recharge.php";
    public static String Dth_Recharge = BASE_URL_RECHARGE + "dth_recharge.php";
    public static String Datacard_Recharge = BASE_URL_RECHARGE + "datacard_recharge.php";
    public static String Gas_Bill_Recharge = BASE_URL_RECHARGE + "gas_billpay.php";
    public static String Electric_Bill_Recharge = BASE_URL_RECHARGE + "electric_billpay.php";
    public static String Insurance_Bill_Recharge = BASE_URL_RECHARGE + "insurance_pay.php";
    public static String Wallet_Balance_Url = BASE_URL_RECHARGE + "wallet_balance.php";
    public static String Operator_List_Api = BASE_URL_RECHARGE+"get_network.php";


    public static String Customer_Validation_Url_new = Base_Url_New+"dmt_customer_validate.php";
    public static String Customer_Registration_Url_new = Base_Url_New+"dmt_add_cust.php";
    public static String Customer_OTP_Url_new = Base_Url_New+"dmt_otc_confirmation.php";
    public static String Customer_Add_Benifi_new = Base_Url_New+"add_beneficiary.php";
    public static String Customer_Add_Benifi_OTP_new = Base_Url_New+"dmt_otc_bene_confirmation.php";
    public static String Customer_Add_Benifi_OTP_For_Delete_new = Base_Url_New+"dmt_otc_delete_beneficiary.php";
    public static String Customer_Remited_new = Base_Url_New+"dmt_remittance.php";
    public static String Customer_Deleted_new = Base_Url_New+"delete_beneficiary.php";
    public static String Customer_Remitance_Hitory_Url_New=Base_Url_New+"wallet_history.php";
    public static String Customer_Remitance_ReIniate_New=Base_Url_New+"dmt_bene_list.php";
    public static String Customer_Remitance_ReIniate_Send_New=Base_Url_New+"dmt_reiniate.php";
    public static String Customer_RechargeHistory=Base_Url_New+"recharge_history.php";
    public static String Customer_paymentHistory=Base_Url_New+"payment_history.php";
    public static String Customer_BillingSummary=Base_Url_New+"billing_summary.php";

    public static String PROFILE_SHOW=Base_Url_New+"agent_profile.php";
    public static String PROFILE_UPDATE=Base_Url_New+"update_profile.php";
    public static String CHANGE_PASSWORD=Base_Url_New+"change_password.php";
    public static String RAISE_DISPUTE=Base_Url_New+"raise_dispute.php";
    public static String SUPPORT_CENTER_DISPUTE=Base_Url_New+"raise_ticket.php";
    public static String RAISE_DISPUTE_DETAILS=Base_Url_New+"view_dispute.php";
    public static String SUPPORT_CENTER_DETAILS=Base_Url_New+"view_ticket.php";
    public static String ACTION_DISPUTE=Base_Url_New+"update_dispute.php";
    public static String ACTION_TICKET_UPDATE=Base_Url_New+"update_ticket.php";
    public static String ACTION_DISPUTE_DETAILS=Base_Url_New+"dispute_list.php";
    public static String ACTION_DTICKET_DETAILS=Base_Url_New+"ticket_list.php";
    public static String UPDATE_PAYMENT=Base_Url_New+"update_payment.php";
    public static String BANK_LIST=Base_Url_New+"bank_list.php";
    public static String Bank_List_Url = Base_Url_New + "ifsc_finder.php";
    public static String CHECK_VERSION = Base_Url_New + "update_version.php";
    public static String LOGIN_PIN = Base_Url_New + "pin_confirm.php";
    public static String OTP_VERIFY = Base_Url_New + "send_otp.php";
    public static String REGISTRATION_OTP_VERIFY = Base_Url_New + "otp.php";
    public static String REGISTRATION_USER = Base_Url_New + "register.php";


    /////// NOT IN USE BEGIN
    public static String Second_Step_Recharge = BASE_URL + "second_step_recharge.php";
    public static String Customer_Validation_Url = BASE_URL + "customer_validation_spay_api.php";
    public static String Customer_Registration_Url = BASE_URL + "customer_registration_spay_api.php";
    public static String Customer_OTP_Confirm = BASE_URL + "customer_otc_spay_api.php";
    public static String Add_Beni_Url = BASE_URL + "customer_add_bene_spay_api.php";
    public static String Customer_Remitance_Url = BASE_URL + "customer_remit_spay_api.php";
    public static String Delete_Beni_Url = BASE_URL + "customer_delete_spay_api.php";
    public static String Cus_Wallet_Bal_Url = BASE_URL + "customer_balance_spay_api.php";
    public static String Bank_List_Url0 = BASE_URL + "customer_ifsc_spay_api.php";
    public static String Customer_Remitance_Hitory_Url=BASE_URL+"customer_wallet_history_dmt_api.php";
    public static String Customer_Acc_Validation_Url=BASE_URL+"customer_account_dmt_api.php";
    public static String Customer_Reinitiate_Transaction_Url=BASE_URL+"customer_reinitiate_dmt_api.php";

/// NOT IN USE END



//    public static String BASE_URL_RECHARGE = "http://netpaisa.com/r_admin/ws/";
//    public static String Base_Url_New =      "http://netpaisa.com/r_admin/ws/";
//
//    public static String BASE_URL = "http://dmts.co.in/api/";
//
//    public static String URL_STORE_TOKEN = BASE_URL_RECHARGE + "login_access.php";
//    public static String ForgetPwd_Url = "";
//    public static String First_Step_Recharge = BASE_URL_RECHARGE + "mobile_recharge.php";
//    public static String Dth_Recharge = BASE_URL_RECHARGE + "dth_recharge.php";
//    public static String Datacard_Recharge = BASE_URL_RECHARGE + "datacard_recharge.php";
//    public static String Gas_Bill_Recharge = BASE_URL_RECHARGE + "gas_billpay.php";
//    public static String Electric_Bill_Recharge = BASE_URL_RECHARGE + "electric_billpay.php";
//    public static String Insurance_Bill_Recharge = BASE_URL_RECHARGE + "insurance_pay.php";
//    public static String Wallet_Balance_Url = BASE_URL_RECHARGE + "wallet_balance.php";
//    public static String Operator_List_Api = BASE_URL_RECHARGE+"get_network.php";
//
///////// NOT IN USE BEGIN
//    public static String Second_Step_Recharge = BASE_URL + "second_step_recharge.php";
//    public static String Customer_Validation_Url = BASE_URL + "customer_validation_spay_api.php";
//    public static String Customer_Registration_Url = BASE_URL + "customer_registration_spay_api.php";
//    public static String Customer_OTP_Confirm = BASE_URL + "customer_otc_spay_api.php";
//    public static String Add_Beni_Url = BASE_URL + "customer_add_bene_spay_api.php";
//    public static String Customer_Remitance_Url = BASE_URL + "customer_remit_spay_api.php";
//    public static String Delete_Beni_Url = BASE_URL + "customer_delete_spay_api.php";
//    public static String Cus_Wallet_Bal_Url = BASE_URL + "customer_balance_spay_api.php";
//    public static String Bank_List_Url = BASE_URL + "customer_ifsc_spay_api.php";
//    public static String Customer_Remitance_Hitory_Url=BASE_URL+"customer_wallet_history_dmt_api.php";
//    public static String Customer_Acc_Validation_Url=BASE_URL+"customer_account_dmt_api.php";
//    public static String Customer_Reinitiate_Transaction_Url=BASE_URL+"customer_reinitiate_dmt_api.php";
//
///// NOT IN USE END
//
//
//    public static String Customer_Validation_Url_new = Base_Url_New+"dmt_customer_validate.php";
//    public static String Customer_Registration_Url_new = Base_Url_New+"dmt_add_cust.php";
//    public static String Customer_OTP_Url_new = Base_Url_New+"dmt_otc_confirmation.php";
//    public static String Customer_Add_Benifi_new = Base_Url_New+"add_beneficiary.php";
//    public static String Customer_Add_Benifi_OTP_new = Base_Url_New+"dmt_otc_bene_confirmation.php";
//    public static String Customer_Add_Benifi_OTP_For_Delete_new = Base_Url_New+"dmt_otc_delete_beneficiary.php";
//    public static String Customer_Remited_new = Base_Url_New+"dmt_remittance.php";
//    public static String Customer_Deleted_new = Base_Url_New+"delete_beneficiary.php";
//    public static String Customer_Remitance_Hitory_Url_New=Base_Url_New+"wallet_history.php";
//    public static String Customer_Remitance_ReIniate_New=Base_Url_New+"dmt_bene_list.php";
//    public static String Customer_Remitance_ReIniate_Send_New=Base_Url_New+"dmt_reiniate.php";
//
//    public static String Customer_RechargeHistory=Base_Url_New+"recharge_history.php";
//    public static String Customer_paymentHistory=Base_Url_New+"payment_history.php";
//    public static String Customer_BillingSummary=Base_Url_New+"billing_summary.php";
}
