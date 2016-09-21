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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StripeTestMain {

	private Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

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

		stripeTestMain.createToken();

		// stripeTestMain.retriveCustomer("cus_99fg6DfuZ2cJVp");
	}

	private void createCustomer() {
		String tokenStr = createToken().getId();

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("source", tokenStr);
		customerParams.put("email", "pranish.sthatest@gmaill.com");

		Customer customer = new Customer();
		try {

			customer = Customer.create(customerParams);
			LOGGER.debug("Customer : ", customer);

		} catch (Exception e) {

			LOGGER.error("Error while creating customer : ", customer);
			LOGGER.debug("createCustomer().e", e);

		}
	}

	/**
	 * Method to set an existing card as a default card
	 */
	private void setDefaultCard() {

		Customer customer = retriveCustomer("cus_96JpIXIQBUJhCL");

		LOGGER.debug("Customer : ", customer);
		LOGGER.debug("Sources : ", customer.getSources());
		LOGGER.debug("Default sources : ", customer.getDefaultSource());

		customer.setDefaultSource("card_18o7E5ElSMaq70BZWN9oHqIb");

		LOGGER.debug("Updated Customer: ", customer);

	}

	@SuppressWarnings("deprecation")
	private Customer retriveCustomer(String customerId) {
		Customer customer = new Customer();
		try {
			customer = Customer.retrieve(customerId, apiKey);
			System.out.println("Retrived Customer : " + customer);
		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.setDefaultCard() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.setDefaultCard() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.setDefaultCard() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.setDefaultCard() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.setDefaultCard() --> ", e);

		}

		return customer;
	}

	/**
	 * Adding a new card to an existing customer
	 */
	@SuppressWarnings("deprecation")
	private void addCards() {

		try {

			String CUSTOMER_ID = "cus_96JpIXIQBUJhCL";

			Customer customer = Customer.retrieve(CUSTOMER_ID, apiKey);

			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("source", createToken().getId());

			ExternalAccount updatedCustomer = customer.getSources().create(customerParams);

			LOGGER.debug("Customer :  ", customer);
			LOGGER.debug("Updated customer after adding card : ", updatedCustomer);

		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.addCards() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.addCards() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.addCards() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.addCards() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.addCards() --> ", e);

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
				LOGGER.debug("Error while retriving customer and creating plan : " + e.getMessage());

			}

		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.addCards() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.addCards() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.addCards() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.addCards() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.addCards() --> ", e);

		}

	}

	private void createCustomerAndPay() {
		/* Creating a token */
		Token token = createToken();

		// Get the credit card details submitted by the form
		String tokenStr = token.getId();

		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("source", tokenStr);
		customerParams.put("email", "pranish.stha04@gmail.com");

		try {
			Customer customer = Customer.create(customerParams);
			LOGGER.debug("Customer created : ", customer);

			Map<String, Object> chargeMap = new HashMap<String, Object>();
			chargeMap.put("amount", 2150);
			chargeMap.put("currency", "usd");

			String customerId = customer.getId();
			chargeMap.put("customer", customerId);

			RequestOptions requestOptions = (new RequestOptionsBuilder()).setApiKey(apiKey).build();

			Charge createdCharge = new Charge();
			try {
				createdCharge = Charge.create(chargeMap, requestOptions);
				LOGGER.debug("Payment successfull : ", createdCharge);

			} catch (StripeException e) {

				LOGGER.debug("StripeTestMain.createCustomerAndPay()", e);

			}

			/*
			 * try{ Plan plan = createSubscriptionPlan(null); // Set your secret
			 * key: remember to change this to your live secret key // in
			 * production // See your keys here:
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

			LOGGER.debug("StripeTestMain.createCustomerAndPay()", e);

		}

	}

	/**
	 * Make 1 time payment
	 */
	@SuppressWarnings("deprecation")
	private Charge makeCharge() {

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

			LOGGER.debug("Payment successfull : ");
			LOGGER.debug("Created charge : ", createdCharge);
			LOGGER.debug("Card : " + createdCharge.getCard());

		} catch (StripeException e) {

			System.out.println("Error while using customer id for payment : " + e.getMessage());
		}

		return createdCharge;
	}

	/**
	 * Retrive a charge detail
	 */
	private void retriveChargeDetails(String chargeId) {
		try {
			Stripe.apiKey = apiKey;
			LOGGER.debug("Charge : ", Charge.retrieve(chargeId));
		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.retriveChargeDetails() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.retriveChargeDetails() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.retriveChargeDetails() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.retriveChargeDetails() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.retriveChargeDetails() --> ", e);

		}
	}

	/**
	 * Create subscription plan
	 */
	private Plan createSubscriptionPlan(Charge charge) {
		System.out.println("********Create subscription plan**********");

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
			LOGGER.debug("Plan Created : ", plan);
		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.createSubscriptionPlan() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.createSubscriptionPlan() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.createSubscriptionPlan() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.createSubscriptionPlan() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.createSubscriptionPlan() --> ", e);

		}

		return plan;
	}

	/**
	 * Assign the created plan to customer
	 */
	private void assignToCustomer(Plan plan) {
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

			LOGGER.debug("AuthenticationException --> StripeTestMain.assignToCustomer() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.assignToCustomer() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.assignToCustomer() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.assignToCustomer() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.assignToCustomer() --> ", e);

		}
	}

	/**
	 * Create card token
	 */
	private Token createToken() {

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
		cardParams.put("name", "name");
		cardParams.put("address_zip", 1111);
		cardParams.put("address_state", "BG");
		cardParams.put("address_line1", "ason");

		// cardParams.put("id", "card_18nirgElSMaq70BZ0iJnYQVI");

		tokenParams.put("card", cardParams);

		try {
			token = Token.create(tokenParams);
			LOGGER.debug(" Token " + token);
		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.createToken() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.createToken() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.createToken() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.createToken() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.createToken() --> ", e);

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

			LOGGER.debug("Updated Subscription : ", updatedSubscription);

		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.updateExistingsSubscription() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.updateExistingsSubscription() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.updateExistingsSubscription() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.updateExistingsSubscription() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.updateExistingsSubscription() --> ", e);

		}

		return subscription;
	}

	@SuppressWarnings("deprecation")
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
			LOGGER.debug("Card : ", customer.getCards());
		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.getCardDetails() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.getCardDetails() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.getCardDetails() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.getCardDetails() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.getCardDetails() --> ", e);

		}

	}

	private void retriveSubscription() {
		Subscription subscription = new Subscription();
		try {
			Stripe.apiKey = apiKey;
			Subscription retrivedSubscription = subscription.retrieve("sub_95vhEt4tCxp8tQ");
			LOGGER.debug("Retrived subscription :   ", retrivedSubscription);
		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.retriveSubscription() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.retriveSubscription() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.retriveSubscription() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.retriveSubscription() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.retriveSubscription() --> ", e);

		}

	}

	private void cancelExistingsSubscription() {

		Stripe.apiKey = apiKey;

		try {
			Subscription subscription = Subscription.retrieve("sub_95vhEt4tCxp8tQ");
			Subscription cancelledSubscription = subscription.cancel(null);
			System.out.println("Cancelled Subscription : " + cancelledSubscription);
		} catch (AuthenticationException e) {

			LOGGER.debug("AuthenticationException --> StripeTestMain.cancelExistingsSubscription() --> ", e);

		} catch (InvalidRequestException e) {

			LOGGER.debug("InvalidRequestException --> StripeTestMain.cancelExistingsSubscription() --> ", e);

		} catch (APIConnectionException e) {

			LOGGER.debug("APIConnectionException --> StripeTestMain.cancelExistingsSubscription() --> ", e);

		} catch (CardException e) {

			LOGGER.debug("CardException --> StripeTestMain.cancelExistingsSubscription() --> ", e);

		} catch (APIException e) {

			LOGGER.debug("APIException --> StripeTestMain.cancelExistingsSubscription() --> ", e);

		}

	}

}
