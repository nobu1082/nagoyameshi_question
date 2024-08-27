package com.example.nobukuni2023.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.PaymentMethodCollection;

@Service
public class SubscriptionService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	private final UserRepository userRepository;

	public SubscriptionService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void create(Map<String, String> paymentIntentObject) {
		User user = new User();

		String Subscription = String.valueOf(paymentIntentObject.get("Subscription"));
		user.setSubscription(Subscription);
		userRepository.save(user);
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
