package com.example.juspay.controller;

import com.example.juspay.model.OrderRequest;
import com.example.juspay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment")
    public String paymentPage(Model model) {
        model.addAttribute("orderRequest", new OrderRequest());
        return "payment";
    }


    @PostMapping("/payment/initiate")
    public String initiatePayment(@ModelAttribute OrderRequest orderRequest, Model model) {
        String paymentUrl = paymentService.createOrder(orderRequest);
        if (paymentUrl != null) {
            return "redirect:" + paymentUrl; // ✅ Now this works properly
        }
        model.addAttribute("error", "Failed to create payment.");

        return "payment";
    }


    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("order_id") String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "success";
    }
}
