package com.example.nobukuni2023.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionRetrieveParams;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	private SubscriptionService subscriptionService = null;
	public StripeService(SubscriptionService subscritonService) {
		this.subscriptionService = subscriptionService;
	}
	
	public void processSessionCompleted(Event event) {
        Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
        optionalStripeObject.ifPresentOrElse(stripeObject -> {
            Session session = (Session)stripeObject;
            SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();

            try {
                session = Session.retrieve(session.getId(), params, null);
                Map<String, String> paymentIntentObject = session.getPaymentIntentObject().getMetadata();
                subscriptionService.create(paymentIntentObject);
            } catch (StripeException e) {
                e.printStackTrace();
            }
            System.out.println("有料会員登録処理が成功しました。");
            System.out.println("Stripe API Version: " + event.getApiVersion());
            System.out.println("stripe-java Version: " + Stripe.VERSION);
        },
        () -> {
            System.out.println("有料会員の登録処理が失敗しました。");
            System.out.println("Stripe API Version: " + event.getApiVersion());
            System.out.println("stripe-java Version: " + Stripe.VERSION);
        });
    
	}

	public PaymentMethod getDefaultPaymentMethod(String customerId) {
		try {
			Stripe.apiKey = stripeApiKey;
			Map<String, Object> params = new HashMap<>();
			params.put("customer", customerId);
			params.put("type", "card");
			PaymentMethodCollection paymentMethods = PaymentMethod.list(params);

			// 顧客が支払い方法を追加しているか確認する
			if (!paymentMethods.getData().isEmpty()) {
				return paymentMethods.getData().get(0);
			}
		} catch (StripeException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	
}

