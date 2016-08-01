package id.co.veritrans.sdk.coreflow.core;

import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BankTransferPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.CreditCardPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.KlikBCAPaymentRequest;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author rakawm
 */
public interface SnapRestAPI {

    /*
     * Get snap transaction details using Snap Endpoint.
     */
    @GET("/v1/payment_pages/{snap_token}")
    void getSnapTransaction(@Path("snap_token") String snapToken, Callback<Transaction> transactionCallback);

    /**
     * Charge payment using credit card token.
     *
     * @param creditCardPaymentRequest    Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_credit_card")
    void paymentUsingCreditCard(@Body CreditCardPaymentRequest creditCardPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bank transfer BCA.
     *
     * @param bankTransferPaymentRequest  Payment Request Details.
     * @param transactionResponseCallback Callback
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_bank_transfer_bca")
    void paymentUsingBankTransferBCA(@Body BankTransferPaymentRequest bankTransferPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using bank transfer Permata.
     *
     * @param bankTransferPaymentRequest  Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_permata")
    void paymentUsingBankTransferPermata(@Body BankTransferPaymentRequest bankTransferPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);

    /**
     * Charge payment using KlikBCA.
     *
     * @param klikBCAPaymentRequest       Payment Request Details.
     * @param transactionResponseCallback Callback.
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/v1/pay_with_bca_klikbca")
    void paymentUsingKlikBCA(@Body KlikBCAPaymentRequest klikBCAPaymentRequest, Callback<TransactionResponse> transactionResponseCallback);
}
