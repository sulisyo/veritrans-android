package com.midtrans.sdk.uikit.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.adapters.CardPagerAdapter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.CirclePageIndicator;

import java.util.ArrayList;

public class SavedCardFragment extends Fragment {
    int installmentCurrentPosition, installmentTotalPositions;
    private ViewPager savedCardPager;
    private CirclePageIndicator circlePageIndicator;
    private FloatingActionButton addCardBt;
    private ArrayList<SaveCardRequest> creditCards;
    private CardPagerAdapter cardPagerAdapter;
    private TextView emptyCardsTextView;
    private TextView textInstallmentTerm;
    //private MorphingButton btnMorph;
    private LinearLayout creditCardLayout;
    private RelativeLayout newCardButtonLayout;
    private int creditCardLayoutHeight;
    private String cardNumber;
    private Button buttonIncrease;
    private Button buttonDecrease;
    private LinearLayout layoutInstallment;

    public SavedCardFragment() {

    }

    public static SavedCardFragment newInstance() {
        SavedCardFragment fragment = new SavedCardFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bindViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        showLayouts();


    }

    private void bindViews(final View view) {
        emptyCardsTextView = (TextView) view.findViewById(R.id.text_empty_saved_cards);
        savedCardPager = (ViewPager) view.findViewById(R.id.saved_card_pager);
        creditCardLayout = (LinearLayout) view.findViewById(R.id.credit_card_holder);
        newCardButtonLayout = (RelativeLayout) view.findViewById(R.id.new_card_button_layout);
        textInstallmentTerm = (TextView) view.findViewById(R.id.text_installment_term);
        buttonDecrease = (Button) view.findViewById(R.id.button_installment_decrease);
        buttonIncrease = (Button) view.findViewById(R.id.button_installment_increase);
        layoutInstallment = (LinearLayout) view.findViewById(R.id.layout_installment);
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIncraseTerm();
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDecreaseTerm();
            }
        });
        addCardBt = (FloatingActionButton) view.findViewById(R.id.btn_add_card);
        addCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideLayouts();

            }
        });

        float cardWidth = ((CreditDebitCardFlowActivity) getActivity()).getScreenWidth();
        float cardHeight = cardWidth * Constants.CARD_ASPECT_RATIO;
        Logger.i("card width:" + cardWidth + ",height:" + cardHeight);
        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, (int) cardHeight);
        savedCardPager.setLayoutParams(parms);
        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        creditCards = ((CreditDebitCardFlowActivity) getActivity()).getCreditCardList();
        setViewPagerValues();

    }


    private void setViewPagerValues() {
        if (creditCards != null) {
            if (getActivity() != null) {
                cardPagerAdapter = new CardPagerAdapter(this, getChildFragmentManager(),
                        creditCards, getActivity());
                savedCardPager.setAdapter(cardPagerAdapter);
                savedCardPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int
                            positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        setupCardInstallment();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                circlePageIndicator.setViewPager(savedCardPager);
                ((CreditDebitCardFlowActivity) getActivity()).setAdapterViews(cardPagerAdapter, circlePageIndicator, emptyCardsTextView);
                showHideNoCardMessage();
                setupCardInstallment();
            }

        }
    }

    private void setupCardInstallment() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK.getCreditCard() != null
                && midtransSDK.getCreditCard().getBank() != null
                && midtransSDK.getCreditCard().getBank().equalsIgnoreCase(BankType.MAYBANK)) {
            showInstallmentLayout(false);
        } else {
            SaveCardRequest currentCard = cardPagerAdapter.getCurrentItem(savedCardPager.getCurrentItem());
            ArrayList<Integer> installmentTerms = ((CreditDebitCardFlowActivity) getActivity()).getInstallmentTerms(currentCard.getMaskedCard().substring(0, 6));
            if (installmentTerms != null && installmentTerms.size() > 1) {
                installmentCurrentPosition = 0;
                installmentTotalPositions = installmentTerms.size() - 1;
                setInstallmentTerm();
                showInstallmentLayout(true);
                setPaymentInstallment();
            } else {
                showInstallmentLayout(false);
            }
        }
    }

    private void setInstallmentTerm() {
        String installmentTerm;
        int term = ((CreditDebitCardFlowActivity) getActivity()).getInstallmentTerm(installmentCurrentPosition);
        if (term < 1) {
            installmentTerm = getString(R.string.no_installment);
        } else {
            installmentTerm = getString(R.string.formatted_installment_month, term + "");
        }
        textInstallmentTerm.setText(installmentTerm);
    }

    private void showInstallmentLayout(boolean show) {
        if (show) {
            if (layoutInstallment.getVisibility() == View.GONE) {
                layoutInstallment.setVisibility(View.VISIBLE);
            }
            buttonDecrease.setEnabled(false);
            buttonIncrease.setEnabled(true);
        } else {
            if (layoutInstallment.getVisibility() == View.VISIBLE) {
                layoutInstallment.setVisibility(View.GONE);
            }
            ((CreditDebitCardFlowActivity) getActivity()).setInstallment(0);
        }
    }

    private void showHideNoCardMessage() {
        if (creditCards.isEmpty()) {
            emptyCardsTextView.setVisibility(View.VISIBLE);
        } else {
            emptyCardsTextView.setVisibility(View.GONE);
        }
    }

    public void deleteCreditCard(String saveTokenId) {
        SdkUIFlowUtil.showProgressDialog(getActivity(), getString(R.string.processing_delete), false);
        showHideNoCardMessage();
        deleteCards(saveTokenId);

    }

    public void deleteCards(final String tokenId) {
        ArrayList<SaveCardRequest> cardList = new ArrayList<>();
        this.cardNumber = tokenId;
        if (creditCards != null && !creditCards.isEmpty()) {
            cardList.addAll(creditCards);
            for (int i = 0; i < cardList.size(); i++) {
                if (cardList.get(i).getSavedTokenId().equalsIgnoreCase(tokenId)) {
                    cardList.remove(cardList.get(i));
                }
            }
        }
        ((CreditDebitCardFlowActivity) getActivity()).saveCreditCards(cardList, true);
    }

    public void hideLayouts() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment
                    .newInstance();
            ((CreditDebitCardFlowActivity) getActivity()).replaceFragment
                    (addCardDetailsFragment, R.id.card_container, true, false);
            return;
        }
        ((CreditDebitCardFlowActivity) (getActivity())).morphingAnimation();
        creditCardLayoutHeight = SavedCardFragment.this.creditCardLayout.getMeasuredHeight();
        Logger.i("creditCardLayoutHeight:" + creditCardLayoutHeight);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(newCardButtonLayout, "alpha", 1f, 0f);
        fadeOut.setDuration(Constants.CARD_ANIMATION_TIME);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(creditCardLayout, "translationY",
                -creditCardLayoutHeight);
        translateY.setDuration(Constants.CARD_ANIMATION_TIME);
        translateY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //payNowBtn.setVisibility(View.VISIBLE);
                addCardBt.setVisibility(View.GONE);
                AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment
                        .newInstance();
                ((CreditDebitCardFlowActivity) getActivity()).replaceFragment
                        (addCardDetailsFragment, R.id.card_container, true, false);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        translateY.start();
        fadeOut.start();

    }

    public void showLayouts() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return;
        }
        ((CreditDebitCardFlowActivity) getActivity()).getBtnMorph().setVisibility(View.VISIBLE);
        ((CreditDebitCardFlowActivity) getActivity()).morphToCircle((int) Constants.CARD_ANIMATION_TIME);
        //creditCardLayoutHeight = SavedCardFragment.this.creditCardLayout.getMeasuredHeight();
        Logger.i("creditCardLayoutHeight:" + creditCardLayoutHeight);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(newCardButtonLayout, "alpha", 0f, 1f);
        fadeIn.setDuration(Constants.CARD_ANIMATION_TIME);
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(creditCardLayout, "translationY",
                -creditCardLayoutHeight);
        slideOut.setDuration(0);
        slideOut.start();
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(creditCardLayout, "translationY",
                0);
        slideIn.setDuration(Constants.CARD_ANIMATION_TIME);
        slideIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        slideIn.start();
        fadeIn.start();
        addCardBt.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((CreditDebitCardFlowActivity) getActivity()).getBtnMorph().setVisibility(View.GONE);
                ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.saved_card);
            }
        }, Constants.CARD_ANIMATION_TIME);
    }

    public void onDeleteCardSuccess() {
        SdkUIFlowUtil.hideProgressDialog();
        int position = -1;
        for (int i = 0; i < creditCards.size(); i++) {
            if (creditCards.get(i).getSavedTokenId().equalsIgnoreCase(cardNumber)) {
                position = i;
            }
        }
        if (creditCards != null && !creditCards.isEmpty()) {
            Logger.i("position to delete:" + position + "," + creditCards.size());
            if (!creditCards.isEmpty()) {
                for (int i = 0; i < creditCards.size(); i++) {
                    Logger.i("cards before:" + creditCards.get(i).getSavedTokenId());
                }
            }

            creditCards.remove(position);

            if (!creditCards.isEmpty()) {
                for (int i = 0; i < creditCards.size(); i++) {

                    Logger.i("cards after:" + creditCards.get(i).getSavedTokenId());
                }
            }

            Logger.i("setting view pager value");
            if (cardPagerAdapter != null && circlePageIndicator != null) {
                Logger.i("notifying data");
                cardPagerAdapter.notifyChangeInPosition(1);
                cardPagerAdapter.notifyDataSetChanged();
                circlePageIndicator.notifyDataSetChanged();
                if (creditCards.isEmpty()) {
                    AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
                    ((CreditDebitCardFlowActivity) getActivity()).replaceFragment(addCardDetailsFragment, R.id.card_container, false, false);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(getString(R.string.card_details));
                } else {
                    emptyCardsTextView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void onDecreaseTerm() {
        if (installmentCurrentPosition > 0 && installmentCurrentPosition <= installmentTotalPositions) {
            installmentCurrentPosition -= 1;
            setPaymentInstallment();
            setInstallmentTerm();
        }
        disableEnableInstallmentButton();
    }

    private void onIncraseTerm() {
        if (installmentCurrentPosition >= 0 && installmentCurrentPosition < installmentTotalPositions) {
            installmentCurrentPosition += 1;
            setInstallmentTerm();
            setPaymentInstallment();
        }
        disableEnableInstallmentButton();
    }

    private void disableEnableInstallmentButton() {

        if (installmentCurrentPosition == 0 && installmentTotalPositions == 0) {
            buttonDecrease.setEnabled(false);
            buttonIncrease.setEnabled(false);
        } else if (installmentCurrentPosition > 0 && installmentCurrentPosition < installmentTotalPositions) {
            buttonDecrease.setEnabled(true);
            buttonIncrease.setEnabled(true);
        } else if (installmentCurrentPosition > 0 && installmentCurrentPosition == installmentTotalPositions) {
            buttonDecrease.setEnabled(true);
            buttonIncrease.setEnabled(false);
        } else if (installmentCurrentPosition == 0 && installmentCurrentPosition < installmentTotalPositions) {
            buttonDecrease.setEnabled(false);
            buttonIncrease.setEnabled(true);
        }
    }


    private void setPaymentInstallment() {
        if (layoutInstallment.getVisibility() == View.VISIBLE) {
            ((CreditDebitCardFlowActivity) getActivity()).setInstallment(installmentCurrentPosition);
        }
    }
}