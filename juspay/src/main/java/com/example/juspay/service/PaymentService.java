package com.example.juspay.service;

import com.example.juspay.model.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PaymentService {

    @Value("${juspay.api.key}")
    private String apiKey;

    @Value("${juspay.merchant.id}")
    private String merchantId;

    @Value("${juspay.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String createOrder(OrderRequest orderRequest) {
        String url = baseUrl + "/order/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(apiKey, "");

        Map<String, Object> request = new HashMap<>();
        request.put("order_id", orderRequest.getOrderId());
        request.put("amount", orderRequest.getAmount());
        request.put("customer_id", orderRequest.getCustomerId());
        request.put("merchant_id", merchantId);
        request.put("return_url", "http://localhost:8080/payment/success?order_id=" + orderRequest.getOrderId());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, String> paymentLinks = (Map<String, String>) response.getBody().get("payment_links");
            return paymentLinks.get("web"); // ✅ Get the actual URL
        }
        System.out.println("Juspay response: " + response.getBody());

        return null;
    }

}
