package com.midtrans.sdk.uikit.view.method.creditcard.point;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.CurrencyHelper;
import com.midtrans.sdk.uikit.utilities.Helper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;
import com.midtrans.sdk.uikit.widget.SemiBoldTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class BankPointsActivity extends BasePaymentActivity {
    public static final String EXTRA_POINT = "point.balance";
    public static final String EXTRA_BANK = "point.bank";

    public static final String EXTRA_DATA_POINT = "point.redeemed";
    private static final String TAG = BankPointsActivity.class.getSimpleName();

    private EditText fieldRedeemedPoint;

    private TextView textTotalPoints;
    private TextView textAmountToPay;
    private SemiBoldTextView textTitle;

    private ImageView imageBankPointLogo;

    private FancyButton buttonRedeemPoint;
    private FancyButton buttonPayWithoutPoint;
    private FancyButton containerAmount;
    private FancyButton containerTotalPoint;

    private BankPointsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        setContentView(R.layout.activity_payment_credit_card_point);
        initRedeemedPointsField();
        bindValues();
        updateAmountToPayText();
        initButtons();
        initBankPointPage();
    }

    private void initPresenter() {
        String pointBank = getIntent().getStringExtra(EXTRA_BANK);
        float pointBalance = getIntent().getFloatExtra(EXTRA_POINT, 0f);
        presenter = new BankPointsPresenter(pointBalance, pointBank);
    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        fieldRedeemedPoint = (AppCompatEditText) findViewById(R.id.redeemed_point_field);
        textTotalPoints = findViewById(R.id.text_total_point);
        textAmountToPay = findViewById(R.id.text_amount_to_pay);

        imageBankPointLogo = findViewById(R.id.bank_point_logo);
        buttonRedeemPoint = findViewById(R.id.button_primary);
        buttonPayWithoutPoint = findViewById(R.id.button_pay_without_point);
        containerAmount = findViewById(R.id.container_amount);
        containerTotalPoint = findViewById(R.id.container_total_point);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonRedeemPoint);
        setSecondaryBackgroundColor(containerAmount);
        containerAmount.setAlpha(0.5f);
        setSecondaryBackgroundColor(containerTotalPoint);
        containerTotalPoint.setAlpha(0.5f);
        setTextColor(buttonPayWithoutPoint);
        setIconColorFilter(buttonPayWithoutPoint);
    }

    private void initRedeemedPointsField() {
        fieldRedeemedPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputString = editable.toString();
                float inputtedPoint = 0f;

                try {
                    if (editable.length() == 0) {
                        editable.insert(0, "0");
                    } else if (inputString.length() > 1 && inputString.charAt(0) == '0') {
                        editable.delete(0, 1);
                    }

                    inputtedPoint = Float.parseFloat(inputString);

                } catch (RuntimeException e) {
                    Logger.error(TAG, "fieldRedeemedPoint:" + e.getMessage());
                }

                if (inputtedPoint <= presenter.getTotalAmount() && presenter.isValidInputPoint(inputtedPoint)) {
                    presenter.calculateAmount(inputtedPoint);
                    presenter.setLatestValidPoint((long) inputtedPoint);
                } else {
                    fieldRedeemedPoint.setText(String.valueOf(presenter.getLatestValidPoint()));
                    fieldRedeemedPoint.setSelection(fieldRedeemedPoint.getText().length());
                }
                updateAmountToPayText();
            }
        });

    }

    private void bindValues() {
        float initialPointBalance = presenter.getPointBalance();
        if (initialPointBalance > presenter.getTotalAmount()) {
            initialPointBalance = (float) presenter.getTotalAmount();
        }
        if (presenter.isValidInputPoint(initialPointBalance)) {
            presenter.calculateAmount(initialPointBalance);
            presenter.setLatestValidPoint((long) initialPointBalance);
            fieldRedeemedPoint.setText(String.valueOf((long) initialPointBalance));
        }
    }

    private void initBankPointPage() {
        String bank = presenter.getPointBank();
        String itemDetailsName = "";

        switch (bank) {
            case BankType.BNI:
                itemDetailsName = getString(R.string.redeem_bni_title);
                setTitle(itemDetailsName);
                imageBankPointLogo.setImageResource(R.drawable.bni_badge);
                textTotalPoints.setText(getString(R.string.total_bni_reward_point, CurrencyHelper.getFormattedAmount(presenter.getPointBalance())));
                findViewById(R.id.container_redeemed_point).setVisibility(View.VISIBLE);
                findViewById(R.id.container_fiestapoin).setVisibility(View.GONE);
                setFocusForBniPoint();
                buttonRedeemPoint.setText(getString(R.string.pay_with_bni_point));
                buttonRedeemPoint.setTextBold();
                break;
            case BankType.MANDIRI:
                itemDetailsName = getString(R.string.redeem_mandiri_title);
                setTitle(itemDetailsName);
                imageBankPointLogo.setImageResource(R.drawable.mandiri_badge);
                textTotalPoints.setText(getString(R.string.total_mandiri_fiestapoint, CurrencyHelper.getFormattedAmount(presenter.getPointBalance())));
                findViewById(R.id.container_redeemed_point).setVisibility(View.GONE);
                findViewById(R.id.container_fiestapoin).setVisibility(View.VISIBLE);
                setFiestapoinDiscount();
                buttonRedeemPoint.setText(getString(R.string.pay_with_mandiri_point));
                buttonRedeemPoint.setTextBold();
                buttonPayWithoutPoint.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        presenter.setItemDetailsName(itemDetailsName);
        updateAmountToPayText();
    }

    private void setFiestapoinDiscount() {
        float fiestaDiscount = presenter.getPointBalance();
        presenter.calculateAmount(fiestaDiscount);
        ((DefaultTextView) findViewById(R.id.text_fiestapoin_discount)).setText(CurrencyHelper.getFormattedNegativeAmount(this, fiestaDiscount, presenter.getCurrency()));
    }

    private void setFocusForBniPoint() {
        new Handler().postDelayed(() -> {
            // Request focus for edit text
            fieldRedeemedPoint.requestFocus();
            fieldRedeemedPoint.setSelection(fieldRedeemedPoint.getText().toString().length());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(fieldRedeemedPoint, InputMethodManager.SHOW_IMPLICIT);
        }, 500);
    }

    private void initButtons() {
        buttonRedeemPoint.setOnClickListener(view -> redeemPoint(true));

        buttonPayWithoutPoint.setOnClickListener(v -> redeemPoint(false));
    }

    private void updateAmountToPayText() {
        String amountToPay = CurrencyHelper.formatAmount(this, presenter.getAmountToPay(), presenter.getCurrency());
        textAmountToPay.setText(amountToPay);
        if (textTotalAmount != null) {
            textTotalAmount.setText(amountToPay);
        }
        addNewItemDetails(presenter.createBankPointItemDetails());
    }

    private void redeemPoint(boolean withPoint) {
        Helper.hideKeyboard(this);

        String strPoint = "0";
        if (withPoint) {
            strPoint = fieldRedeemedPoint.getText().toString().trim();
        }
        float redeemedPoint = Float.valueOf(strPoint);
        finishBankPoint(redeemedPoint);
    }

    private void finishBankPoint(float redeemedPoint) {
        Intent data = new Intent();
        data.putExtra(EXTRA_DATA_POINT, redeemedPoint);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}