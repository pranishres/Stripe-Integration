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
import com.stripe.model.ExternalAccount;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;

public class StripeTestMain {

	String apiKey = "sk_test_Xnqmv0DtbQLd6f8QEPxLvsXv";

	public static void main(String[] args) {

		StripeTestMain stripeTestMain = new StripeTestMain();

		// Charge charge = stripeTestMain.makeCharge();
		// stripeTestMain.retriveChargeDetails(charge.getId());

		// Plan plan = stripeTestMain.createSubscriptionPlan(null);
		// stripeTestMain.assignToCustomer(plan);

		// stripeTestMain.creteCustomerAndPay();
		// stripeTestMain.createCustomer();

		// stripeTestMain.makeCharge();

		// stripeTestMain.createCustomer();
		// stripeTestMain.addCards();

		/* Update an existing subscription */
		// stripeTestMain.updateExistingsSubscription();

		/** Cancelling an existing subscriotion **/
		// stripeTestMain.cancelExistingsSubscription();

		/* Get card details using cardId */
		// stripeTestMain.getCardDetails();

		/* Retrive a subscrition details */
		// stripeTestMain.retriveSubscription();

		// stripeTestMain.createCustomer();

		// stripeTestMain.retriveCustomerAndCreatePlan();

		// stripeTestMain.setDefaultCard();

		// stripeTestMain.assignToCustomer(stripeTestMain.createSubscriptionPlan(null));

//		 stripeTestMain.createToken();
		
		stripeTestMain.addCards();
		
//		stripeTestMain.retriveCustomerAndCreatePlan();
//		stripeTestMain.updateExistingsSubscription();
	}

	private void createCustomer() {
		String tokenStr = createToken().getId();

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("source", tokenStr);
		customerParams.put("email", "pranish.sthatest@gmaill.com");

		Customer customer = new Customer();
		try {
			customer = Customer.create(customerParams);
			System.out.println("Customer : " + customer);
		} catch (Exception e) {
			System.out.println();
		}
	}

	/**
	 * Method to set an existing card as a default card
	 */
	private void setDefaultCard() {

		Customer customer = retriveCustomer("cus_96JpIXIQBUJhCL");
		System.out.println("Customer : " + customer);
		System.out.println("Sources : " + customer.getSources());
		System.out.println("Default sources : " + customer.getDefaultSource());

		customer.setDefaultSource("card_18o7E5ElSMaq70BZWN9oHqIb");
		System.out.println("Updated Customer: " + customer);

	}

	private Customer retriveCustomer(String customerId) {
		Customer customer = new Customer();
		try {
			customer = Customer.retrieve(customerId, apiKey);
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

		return customer;
	}

	private void addCards() {
		try {
			Customer customer = Customer.retrieve("cus_96JpIXIQBUJhCL", apiKey);

			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("source", createToken().getId());

			ExternalAccount updatedCustomer =  customer.getSources().create(customerParams);

			System.out.println("Customer :  " + customer);
			System.out.println("Updated customer after adding card : " + updatedCustomer);

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

	/**
	 * There are 2 ways to update a plan :-
	 * 
	 * First way is by setting plan to customer as is done in this method.
	 * 
	 * The second way is retrving subscription and setting plan there. The
	 * second method is useful if 1 subscription is set to multiple customers
	 */
	private void retriveCustomerAndCreatePlan() {
		Stripe.apiKey = apiKey;

		try {
			Customer customer = Customer.retrieve("cus_97OOfWOGmfeCvE");
			Plan plan = createSubscriptionPlan(null);
			Map<String, Object> customerParams2 = new HashMap<String, Object>();
			customerParams2.put("plan", plan.getId());

			try {
				Customer customer2 = Customer.retrieve(customer.getId());
				customer2.update(customerParams2);
			} catch (Exception e) {
				System.out.println("Error while retriving customer and creating plan : " + e.getMessage());

			}

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

	private void creteCustomerAndPay() {
		/* Creating a token */
		Token token = createToken();

		// Get the credit card details submitted by the form
		String tokenStr = token.getId();

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("source", tokenStr);
		customerParams.put("email", "pranish.stha04@gmail.com");

		try {
			Customer customer = Customer.create(customerParams);
			System.out.println("Customer created : " + customer);

			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("amount", 2150);
			chargeMap.put("currency", "usd");

			String customerId = customer.getId();
			chargeMap.put("customer", customerId);

			RequestOptions requestOptions = (new RequestOptionsBuilder()).setApiKey(apiKey).build();

			Charge createdCharge = new Charge();
			try {
				createdCharge = Charge.create(chargeMap, requestOptions);
				System.out.println("Payment successfull : ");
				System.out.println(createdCharge);

			} catch (StripeException e) {
				System.out.println("Error while making payment using customer Id : " + e.getMessage());
			}

			/*
			 * try{ Plan plan = createSubscriptionPlan(null);
			 * System.out.println("******* Assign plan to customer **********");
			 * // Set your secret key: remember to change this to your live
			 * secret key // in production // See your keys here:
			 * https://dashboard.stripe.com/account/apikeys Stripe.apiKey =
			 * apiKey;
			 * 
			 * 
			 * Map<String, Object> customerParams2 = new HashMap<String,
			 * Object>(); customerParams2.put("plan", plan.getId());
			 * 
			 * try { Customer customer2 = Customer.retrieve(customerId);
			 * customer2.update(customerParams2);
			 * System.out.println("Customer for plan : " + customer); } catch
			 * (AuthenticationException e) { System.out.println(
			 * "AuthenticationException while assigning plan: " +
			 * e.getMessage()); } catch (InvalidRequestException e) {
			 * System.out.println(
			 * "InvalidRequestException while assigning plan: " +
			 * e.getMessage()); } catch (APIConnectionException e) {
			 * System.out.println(
			 * "APIConnectionException while assigning plan: " +
			 * e.getMessage()); } catch (CardException e) {
			 * System.out.println("CardException while assigning plan: " +
			 * e.getMessage()); } catch (APIException e) {
			 * System.out.println("APIException while assigning plan: " +
			 * e.getMessage()); }
			 * 
			 * }catch(Exception e){
			 * 
			 * }
			 */

		} catch (Exception e) {
			System.out.println("Error while creating customer : " + e.getMessage());
		}

	}

	/**
	 * Make 1 time payment
	 */
	private Charge makeCharge() {
		System.out.println("********* Make payment *********");

		RequestOptions requestOptions = (new RequestOptionsBuilder()).setApiKey(apiKey).build();
		Map<String, Object> chargeMap = new HashMap<String, Object>();
		chargeMap.put("amount", 2400);
		chargeMap.put("currency", "usd");

		// Making payment using credit card

		Map<String, Object> cardMap = new HashMap<String, Object>();
		cardMap.put("number", "4242424242424242");
		cardMap.put("exp_month", 12);
		cardMap.put("exp_year", 2020);

		chargeMap.put("card", cardMap);

		/* Making payment using customer id */

		/*
		 * String customerId = token.getId(); chargeMap.put("customer",
		 * customerId);
		 */

		Charge createdCharge = new Charge();

		try {
			createdCharge = Charge.create(chargeMap, requestOptions);

			System.out.println("Payment successfull : ");
			System.out.println(createdCharge);
			System.out.println("Card : " + createdCharge.getCard());

		} catch (StripeException e) {
			System.out.println("Error while using customer id for payment : " + e.getMessage());
		}

		return createdCharge;
	}

	/**
	 * Retrive a charge detail
	 */
	private void retriveChargeDetails(String chargeId) {
		System.out.println("************Retrive Charge Details********************");
		System.out.println("Charge details of : " + chargeId);
		try {
			Stripe.apiKey = apiKey;
			System.out.println(Charge.retrieve(chargeId));
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

	/**
	 * Create subscription plan
	 */
	private Plan createSubscriptionPlan(Charge charge) {
		System.out.println("********Create subscription plan**********");

		// Set your secret key: remember to change this to your live secret key
		// in production
		// See your keys here: https://dashboard.stripe.com/account/apikeys
		Stripe.apiKey = apiKey;

		Map<String, Object> planParams = new HashMap<String, Object>();
		planParams.put("amount", 2750);
		planParams.put("interval", "month");
		planParams.put("name", "Sept 02 plan. Better than paypal");
		planParams.put("currency", "usd");
		planParams.put("id", "Sept 02 02");
		// planParams.put("trial_period_days", 2);

		Plan plan = new Plan();

		try {
			plan = Plan.create(planParams);
			System.out.println("Plan Created : " + plan);
		} catch (AuthenticationException e) {

			System.out.println("Authentication Exception while creting plan : " + e.getMessage());
		} catch (InvalidRequestException e) {

			System.out.println("InvalidRequestException while creting plan : " + e.getMessage());

		} catch (APIConnectionException e) {

			System.out.println("ApiConnectionException while creating plan : " + e.getMessage());
		} catch (CardException e) {

			System.out.println("Card Exception while creating plan : " + e.getMessage());

		} catch (APIException e) {

			System.out.println("ApiExceotion while creting plan : " + e.getMessage());
		}

		return plan;
	}

	/**
	 * Assign the created plan to customer
	 */
	private void assignToCustomer(Plan plan) {
		System.out.println("******* Assign plan to customer **********");
		// Set your secret key: remember to change this to your live secret key
		// in production
		// See your keys here: https://dashboard.stripe.com/account/apikeys
		Stripe.apiKey = apiKey;

		Token token = createToken();

		// Get the credit card details submitted by the form
		String tokenStr = token.getId();

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("source", tokenStr);
		customerParams.put("plan", plan.getId());
		customerParams.put("email", "pranish.stha003@gmail.com");

		try {
			Customer customer = Customer.create(customerParams);
			System.out.println("Customer for plan : " + customer);
		} catch (AuthenticationException e) {
			System.out.println("AuthenticationException while assigning plan: " + e.getMessage());
		} catch (InvalidRequestException e) {
			System.out.println("InvalidRequestException while assigning plan: " + e.getMessage());
		} catch (APIConnectionException e) {
			System.out.println("APIConnectionException while assigning plan: " + e.getMessage());
		} catch (CardException e) {
			System.out.println("CardException while assigning plan: " + e.getMessage());
		} catch (APIException e) {
			System.out.println("APIException while assigning plan: " + e.getMessage());
		}
	}

	/**
	 * Create card token
	 */
	private Token createToken() {

		System.out.println("Crete token");

		Token token = new Token();
		Stripe.apiKey = apiKey;

		Map<String, Object> tokenParams = new HashMap<String, Object>();
		Map<String, Object> cardParams = new HashMap<String, Object>();
		cardParams.put("number", "4012888888881881");
		cardParams.put("exp_month", 8);
		cardParams.put("exp_year", 2017);
		cardParams.put("cvc", "314");
		cardParams.put("address_city", "Kathmandu");
		cardParams.put("address_country", "NP");
		// cardParams.put("brand", "")
		cardParams.put("name", "name");
		cardParams.put("address_zip", 1111);
		cardParams.put("address_state", "BG");
		cardParams.put("address_line1", "ason");

		// cardParams.put("id", "card_18nirgElSMaq70BZ0iJnYQVI");

		tokenParams.put("card", cardParams);

		try {
			token = Token.create(tokenParams);
			System.out.println(token);
		} catch (AuthenticationException e) {
			System.out.println("AuthenticationException while creating token :  " + e.getMessage());
		} catch (InvalidRequestException e) {
			System.out.println("InvalidRequestException while creating token :  " + e.getMessage());
		} catch (APIConnectionException e) {
			System.out.println("APIConnectionException while creating token :  " + e.getMessage());
		} catch (CardException e) {
			System.out.println("CardException while creating token :  " + e.getMessage());
		} catch (APIException e) {
			System.out.println("APIException while creating token :  " + e.getMessage());
		}

		return token;
	}

	/**
	 * Cancel existing subscription
	 */
	private Subscription updateExistingsSubscription() {

		Stripe.apiKey = apiKey;
		Subscription subscription = new Subscription();

		try {
			subscription = Subscription.retrieve("sub_97OOA9JZjrFwJy");
			Map<String, Object> updateParams = new HashMap<String, Object>();
			Plan plan = createSubscriptionPlan(null);
			updateParams.put("plan", plan.getId());

			Subscription updatedSubscription = subscription.update(updateParams);

			System.out.println("Updated Subscription : " + updatedSubscription);

		} catch (AuthenticationException e) {
			System.out.println("AuthenticationException while cancelling subscription : " + e.getMessage());
		} catch (InvalidRequestException e) {
			System.out.println("InvalidRequestException while cancelling subscription : " + e.getMessage());
		} catch (APIConnectionException e) {
			System.out.println("APIConnectionException while cancelling subscription : " + e.getMessage());
		} catch (CardException e) {
			System.out.println("CardException while cancelling subscription : " + e.getMessage());
		} catch (APIException e) {
			System.out.println("APIException while cancelling subscription : " + e.getMessage());
		}

		return subscription;
	}

	private void getCardDetails() {
		Stripe.apiKey = apiKey;

		Customer customer;
		try {
			customer = Customer.retrieve("cus_95vgxMFz8O2ehu");
			Map<String, Object> cardParams = new HashMap<String, Object>();
			cardParams.put("number", "4242424242424242");
			cardParams.put("exp_month", 8);
			cardParams.put("exp_year", 2017);
			cardParams.put("cvc", "314");

			System.out.println("Customer : " + customer);

			String card = customer.getDefaultCard();
			System.out.println("Card : " + customer.getCards());
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

	private void retriveSubscription() {
		Subscription subscription = new Subscription();
		try {
			Stripe.apiKey = apiKey;
			Subscription retrivedSubscription = subscription.retrieve("sub_95vhEt4tCxp8tQ");
			System.out.println("Retrived subscription :   " + retrivedSubscription);
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

	private void cancelExistingsSubscription() {
		System.out.println("******** Cancelling Subscription **********");

		Stripe.apiKey = apiKey;

		try {
			Subscription subscription = Subscription.retrieve("sub_95vhEt4tCxp8tQ");
			Subscription cancelledSubscription = subscription.cancel(null);
			System.out.println("Cancelled Subscription : " + cancelledSubscription);
		} catch (AuthenticationException e) {
			System.out.println("AuthenticationException while cancelling subscription");
		} catch (InvalidRequestException e) {
			System.out.println("InvalidRequestException while cancelling subscription");
		} catch (APIConnectionException e) {
			System.out.println("APIConnectionException while cancelling subscription");
		} catch (CardException e) {
			System.out.println("CardException while cancelling subscription");
		} catch (APIException e) {
			System.out.println("APIException while cancelling subscription");
		}

	}

}
