package com.vtnq.web.Service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalServiceImplement implements PayPalService {

    @Autowired
    private APIContext apiContext;


    @Override
    public Payment createPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        // Format the total amount
        String formattedTotal;
        if (currency.equalsIgnoreCase("JPY") || currency.equalsIgnoreCase("HUF")) {
            formattedTotal = String.format("%.0f", total); // Không có chữ số thập phân cho JPY và HUF
        } else {
            formattedTotal = String.format("%.2f", total); // Hai chữ số thập phân
        }

        // Check for PayPal limits and round down if needed
        if (formattedTotal.length() > 10) { // 7 chữ số trước dấu thập phân và 2 chữ số thập phân
            formattedTotal = formattedTotal.substring(0, 10); // Cắt bớt để phù hợp giới hạn
        }

        // Construct Amount object
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(formattedTotal);

        // Setup Details (ensure consistency between subtotal and total)
        Details details = new Details();
        details.setSubtotal(formattedTotal); // Subtotal phải khớp với tổng
        details.setShipping("0.00");
        details.setTax("0.00");
        amount.setDetails(details);

        // Create a Transaction
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Setup Payer
        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        // Setup Payment
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // Setup Redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        // Create Payment
        return payment.create(apiContext);
    }


    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }
}
