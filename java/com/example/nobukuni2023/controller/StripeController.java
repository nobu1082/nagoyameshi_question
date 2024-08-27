package com.example.nobukuni2023.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.nobukuni2023.entity.User;
import com.example.nobukuni2023.repository.UserRepository;
import com.example.nobukuni2023.security.UserDetailsImpl;
import com.example.nobukuni2023.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerSearchParams;

@Controller
public class StripeController {

	@Value("${stripe.api-key}")
	private String stripeApiKey;
	private final UserRepository userRepository;
	private final StripeService stripeService;
	public StripeController(UserRepository userRepository ,StripeService stripeService) {
		this.userRepository = userRepository;
		this.stripeService = stripeService;
	}
	@GetMapping("stripeEdit")
	@ResponseBody
		public Map<String , Object> stripeEdit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
		Stripe.apiKey = stripeApiKey;
		Map<String , Object> response = new HashMap<>();
		try {
			//String id = userDetailsImpl.getUser().getId().toString();
			String email = userDetailsImpl.getUser().getEmail();
			CustomerSearchParams params = CustomerSearchParams.builder()
					.setQuery("email:'" + email +"'")
					//.setQuery("metadata['userId']:'" + id +"'")
					//.setQuery("metadata['email']:'" + email + "'")
					.build();
			List<Customer> customers = Customer.search(params).getData();
			
			if(customers.size() > 0 ) {
			//	Customer customer = customers.get(0);
			//	response.put("customerId", customer.getS);
				
			//	PaymentMethodListParams listParams=
			//			PaymentMethodListParams.builder()
			//			.setCustomer(customer.getId())
			//			.setType(PaymentMethodListParams.Type.CARD)
			//			.build();
			//	
			//	   List<PaymentMethod> paymentMethods = PaymentMethod.list(listParams).getData();
			//	   
			//		if(!paymentMethods.isEmpty()) {
			//			response.put("paymentMethod", paymentMethods.get(0));
			//		}else {
			//			response.put("paymentMethod",null);
			//	    }
					
					response.put("success", true);
			}else {
				response.put("success", false);
				response.put("error", false);
				response.put("error", "66行目のメッセージがでる　No matching customer found.");
			}
			
		}catch(StripeException e) {
			response.put("success", false);
			response.put("error", e.getMessage());
		}
			return response;
		}

	private void build() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@GetMapping("edit-cards")
	public String editCard(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {  
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		PaymentMethod paymentMethod = stripeService.getDefaultPaymentMethod(user.getSubscription());        
		model.addAttribute("card", paymentMethod.getCard());
		model.addAttribute("cardHolderName", paymentMethod.getBillingDetails().getName());
	        
	    return "/subscriptions/edit";
	}
	
	public class CustomerResponse {
	    private String customerId;
	    private String customerEmail;
	    private String paymentMethodId;
	    private boolean success;
	    private String error;

	    // Getters and Setters
	    public String getCustomerId() { return customerId; }
	    public void setCustomerId(String customerId) { this.customerId = customerId; }
	    public String getCustomerEmail() { return customerEmail; }
	    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
	    public String getPaymentMethodId() { return paymentMethodId; }
	    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
	    public boolean isSuccess() { return success; }
	    public void setSuccess(boolean success) { this.success = success; }
	    public String getError() { return error; }
	    public void setError(String error) { this.error = error; }
	}
	
	
	
	
	}

