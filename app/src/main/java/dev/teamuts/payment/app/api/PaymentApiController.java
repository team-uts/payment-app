package dev.teamuts.payment.app.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment API", description = "Payment Domain")
@RestController
@RequestMapping("/v1/payments")
public class PaymentApiController {}
