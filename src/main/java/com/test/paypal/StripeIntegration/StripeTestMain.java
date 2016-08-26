package com.test.paypal.StripeIntegration;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;

public class StripeTestMain {
	public static void main(String[] args) {
		StripeTestMain stripeTestMain = new StripeTestMain();
		stripeTestMain.createSubscriptionPlan();
	}

	private void makeCharge() {
		RequestOptions requestOptions = (new RequestOptionsBuilder()).setApiKey("sk_test_Xnqmv0DtbQLd6f8QEPxLvsXv")
				.build();
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", 1000);
		chargeMap.put("currency", "usd");
		Map<String, Object> cardMap = new HashMap<String, Object>();
		cardMap.put("number", "4242424242424242");
		cardMap.put("exp_month", 12);
		cardMap.put("exp_year", 2020);
		chargeMap.put("card", cardMap);

		Customer customer = new Customer();
		customer.setEmail("pranish.stha04@gmail.com");

		try {
			Charge charge = new Charge();
			charge.setCustomer("Pranish Cus");
			;
			charge.setReceiptEmail("pranish.stha04@gmail.com");

			Charge cretedCharge = Charge.create(chargeMap, requestOptions);

			System.out.println("Payment successfull : ");
			System.out.println(charge);
			System.out.println("-----------------");
			System.out.println(cretedCharge);
		} catch (StripeException e) {
			e.printStackTrace();
		}
	}

	private void createSubscriptionPlan() {
		// Set your secret key: remember to change this to your live secret key
		// in production
		// See your keys here: https://dashboard.stripe.com/account/apikeys
		Stripe.apiKey = "sk_test_Xnqmv0DtbQLd6f8QEPxLvsXv";

		Map<String, Object> planParams = new HashMap<String, Object>();
		planParams.put("amount", 9000);
		planParams.put("interval", "day");
		planParams.put("name", "First plan. Better than paypal");
		planParams.put("currency", "usd");
		planParams.put("id", "gold");

		try {
			Plan plan = Plan.create(planParams);
			System.out.println("Plan Created : " + plan);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void assignToCustomer() {
		// Set your secret key: remember to change this to your live secret key
		// in production
		// See your keys here: https://dashboard.stripe.com/account/apikeys
		Stripe.apiKey = "sk_test_Xnqmv0DtbQLd6f8QEPxLvsXv";

		// Get the credit card details submitted by the form
		String token = request.getParameter("stripeToken");

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("source", token);
		customerParams.put("plan", "gold");
		customerParams.put("email", "payinguser@example.com");

		Customer.create(customerParams);
	}
}
