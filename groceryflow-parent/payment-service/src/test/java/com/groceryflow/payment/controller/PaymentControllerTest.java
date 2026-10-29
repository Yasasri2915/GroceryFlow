package com.groceryflow.payment.controller;

import com.groceryflow.payment.dto.PaymentResponseDto;
import com.groceryflow.payment.dto.ProcessPaymentRequest;
import com.groceryflow.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void shouldRejectUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/api/payments/order/1"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "alice@groceryflow.com", roles = {"CUSTOMER"})
    void shouldGetPaymentByOrderIdWhenAuthenticated() throws Exception {
        Mockito.when(paymentService.getPaymentByOrderId(1L))
               .thenReturn(new PaymentResponseDto());

        mockMvc.perform(get("/api/payments/order/1")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "alice@groceryflow.com", roles = {"CUSTOMER"})
    void shouldProcessPaymentWhenAuthenticated() throws Exception {
        Mockito.when(paymentService.processPayment(any(ProcessPaymentRequest.class)))
               .thenReturn(new PaymentResponseDto());

        String jsonPayload = "{"
                + "\"orderId\": 4,"
                + "\"customerId\": 1,"
                + "\"amount\": 10.98,"
                + "\"paymentMethod\": \"CREDIT_CARD\""
                + "}";

        mockMvc.perform(post("/api/payments/process")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk());
    }
}