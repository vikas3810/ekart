//package com.ekart.model;
//
//import com.paypal.api.payments.Payment;
//import com.paypal.api.payments.PaymentExecution;
//import com.paypal.base.rest.PayPalRESTException;
//
//public class PayPalPaymentService {
//
//    private final String clientId = "YOUR_CLIENT_ID";
//    private final String clientSecret = "YOUR_CLIENT_SECRET";
//    private final PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
//    private final PayPalHttpClient client = new PayPalHttpClient(environment);
//
//    public String createPaymentOrder(double amount, String currencyCode) {
//        Payment payment = new Payment();
//
//        // Set payment details (e.g., amount, currency, etc.)
//        // Note: You need to create and set the Payment object according to your requirements.
//
//        try {
//            Payment createdPayment = payment.create(client);
//            String approvalLink = createdPayment.getLinks()
//                    .stream()
//                    .filter(link -> "approval_url".equals(link.getRel().toLowerCase()))
//                    .findFirst()
//                    .map(link -> link.getHref())
//                    .orElse(null);
//
//            return approvalLink;
//        } catch (PayPalRESTException e) {
//            // Handle PayPal SDK exceptions here
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public boolean executePayment(String paymentId, String payerId) {
//        PaymentExecution paymentExecution = new PaymentExecution();
//        paymentExecution.setPayerId(payerId);
//
//        try {
//            Payment payment = Payment.get(client, paymentId);
//            Payment executedPayment = payment.execute(client, paymentExecution);
//            return "approved".equalsIgnoreCase(executedPayment.getState());
//        } catch (PayPalRESTException e) {
//            // Handle PayPal SDK exceptions here
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
//
