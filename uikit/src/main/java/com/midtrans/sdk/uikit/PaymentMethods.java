package com.midtrans.sdk.uikit;

import android.content.Context;
import android.text.TextUtils;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.models.CreditCardType;
import com.midtrans.sdk.uikit.models.UobPayment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

/**
 * Payment method list.
 *
 * @author rakawm
 */
public class PaymentMethods {

    public static PaymentMethodsModel getMethods(Context context, String paymentType, String status, Boolean isTablet) {
        if (paymentType.equals(context.getString(R.string.payment_credit_debit))) {
            return getMethodCreditCards(context, 1, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_bank_transfer))) {
            return getMethodBankTransfer(context, 2, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_bca_click))) {
            return getMethodBCAKlikpay(context, 3, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_klik_bca))) {
            return getMethodKlikBCA(context, 4, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_epay_bri))) {
            return getMethodEpayBRI(context, 5, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_cimb_clicks))) {
            return getMethodCIMBClicks(context, 6, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_mandiri_clickpay))) {
            return getMethodMandiriClickpay(context, 7, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_indomaret))) {
            return getMethodIndomaret(context, 8, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_kioson))) {
            return getMethodKiosan(context, 9, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_telkomsel_cash))) {
            return getMethodTelkomselCash(context, 10, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_mandiri_ecash))) {
            return getMethodMandiriECash(context, 11, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_indosat_dompetku))) {
            return getMethodIndosatDompetku(context, 12, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_xl_tunai))) {
            return getMethodXLTunai(context, 13, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_gci))) {
            return getMethodGCI(context, 14, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_gopay))) {
            return getMethodGopay(context, 15, paymentType, status, isTablet);
        } else if (paymentType.equals(context.getString(R.string.payment_shopeepay))) {
            return getMethodShopeepay(context, 16, paymentType, status, isTablet);
        } else if (paymentType.equals(context.getString(R.string.payment_danamon_online))) {
            return getDanamonOnline(context, 17, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_akulaku))) {
            return getMethodAkulaku(context, 18, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_alfamart))) {
            return getMethodAlfamart(context, 19, paymentType, status);
        } else if (paymentType.equals(context.getString(R.string.payment_uob))) {
            return getMethodUob(context, 4, paymentType, status);
        } else {
            return null;
        }
    }

    private static PaymentMethodsModel getMethodCreditCards(Context context, int priority, String paymentType, String status) {
        int creditCardSupportType = SdkUIFlowUtil.getCreditCardIconType();
        switch (creditCardSupportType) {
            case CreditCardType.TYPE_MASTER_VISA_JCB_AMEX:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card), R.drawable.ic_credit, paymentType, priority, status);
            case CreditCardType.TYPE_MASTER_VISA_JCB:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_3), R.drawable.ic_credit_3, paymentType, priority, status);
            case CreditCardType.TYPE_MASTER_VISA_AMEX:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_4), R.drawable.ic_credit_4, paymentType, priority, status);
            default:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_2), R.drawable.ic_credit_2, paymentType, priority, status);
        }
    }

    private static PaymentMethodsModel getMethodBankTransfer(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bank_transfer), context.getString(R.string.payment_method_description_bank_transfer), R.drawable.ic_atm, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodUob(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_uob), context.getString(R.string.payment_method_description_uob), R.drawable.ic_uob, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodBCAKlikpay(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bca_klikpay), context.getString(R.string.payment_method_description_bca_klikpay), R.drawable.ic_klikpay, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodKlikBCA(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_klik_bca), context.getString(R.string.payment_method_description_klik_bca), R.drawable.ic_klikbca, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodEpayBRI(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bri_epay), context.getString(R.string.payment_method_description_epay_bri), R.drawable.ic_epay, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodCIMBClicks(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_cimb_clicks), context.getString(R.string.payment_method_description_cimb_clicks), R.drawable.ic_cimb, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodMandiriClickpay(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_clickpay), context.getString(R.string.payment_method_description_mandiri_clickpay), R.drawable.ic_mandiri2, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodIndomaret(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indomaret), context.getString(R.string.payment_method_description_indomaret), R.drawable.ic_indomaret, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodKiosan(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_kioson), context.getString(R.string.payment_method_description_kioson), R.drawable.ic_kioson, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodTelkomselCash(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_telkomsel_cash), context.getString(R.string.payment_method_description_telkomsel_cash), R.drawable.ic_telkomsel, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodMandiriECash(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_ecash), context.getString(R.string.payment_method_description_mandiri_ecash), R.drawable.ic_mandiri_e_cash, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodIndosatDompetku(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indosat_dompetku), context.getString(R.string.payment_method_description_indosat_dompetku), R.drawable.ic_indosat, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodXLTunai(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_xl_tunai), context.getString(R.string.payment_method_description_xl_tunai), R.drawable.ic_xl, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodGCI(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_gci), context.getString(R.string.payment_method_description_gci), R.drawable.ic_gci, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodGopay(Context context, int priority, String paymentType, String status, Boolean isTablet) {
        if (isTablet) {
            return new PaymentMethodsModel(context.getString(R.string.payment_method_gopay_qris), context.getString(R.string.payment_method_description_gopay_qris), R.drawable.uikit_ic_gopay_qris, paymentType, priority, status);
        } else {
            return new PaymentMethodsModel(context.getString(R.string.payment_method_gopay), context.getString(R.string.payment_method_description_gopay), R.drawable.ic_gopay, paymentType, priority, status);
        }
    }

    private static PaymentMethodsModel getMethodShopeepay(Context context, int priority, String paymentType, String status, Boolean isTablet) {
        if (isTablet) {
            return new PaymentMethodsModel(context.getString(R.string.payment_method_shopeepay_qris), context.getString(R.string.payment_method_description_shopeepay_qris), R.drawable.uikit_ic_shopeepay_qris, paymentType, priority, status);
        } else {
            return new PaymentMethodsModel(context.getString(R.string.payment_method_shopeepay_deeplink), context.getString(R.string.payment_method_description_shopeepay_deeplink), R.drawable.uikit_ic_shopeepay, paymentType, priority, status);
        }
    }

    private static PaymentMethodsModel getDanamonOnline(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_danamon_online), context.getString(R.string.payment_method_description_danamon_online), R.drawable.ic_danamon_online, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodAkulaku(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_akulaku), context.getString(R.string.payment_method_description_akulaku), R.drawable.ic_akulaku, paymentType, priority, status);
    }

    private static PaymentMethodsModel getMethodAlfamart(Context context, int priority, String paymentType, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_alfamart), context.getString(R.string.payment_method_description_alfamart), R.drawable.ic_alfamart, paymentType, priority, status);
    }

    public static BankTransfer createBankTransferModel(Context context, String type, String status) {
        BankTransfer bankTransfer = null;
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case PaymentType.BCA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, 1, context.getString(R.string.payment_bank_description_bca), status);
                    break;

                case PaymentType.E_CHANNEL:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.mandiri_bill), R.drawable.ic_mandiri_bill_payment2, 2, context.getString(R.string.payment_bank_description_mandiri), status);
                    break;

                case PaymentType.BNI_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bni_bank_transfer), R.drawable.ic_bni, 4, context.getString(R.string.payment_bank_description_bni), status);
                    break;

                case PaymentType.BRI_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bri_bank_transfer), R.drawable.ic_bri, 5, context.getString(R.string.payment_bank_description_bri), status);
                    break;

                case PaymentType.PERMATA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, 3, context.getString(R.string.payment_bank_description_permata), status);
                    break;

                case PaymentType.ALL_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.all_bank_transfer), R.drawable.ic_atm, 6, context.getString(R.string.payment_bank_description_other), status);
                    break;
            }
        }
        return bankTransfer;
    }

    public static UobPayment createUobPaymentModel(Context context, String type, String status) {
        UobPayment uobPayment = null;
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case PaymentType.UOB_WEB:
                    uobPayment = new UobPayment(type, context.getString(R.string.web_uob), R.drawable.ic_uob, 1, context.getString(R.string.payment_uob_description_web), status);
                    break;

                case PaymentType.UOB_APP:
                    uobPayment = new UobPayment(type, context.getString(R.string.app_uob), R.drawable.ic_uob, 2, context.getString(R.string.payment_uob_description_app), status);
                    break;
            }
        }
        return uobPayment;
    }
}
