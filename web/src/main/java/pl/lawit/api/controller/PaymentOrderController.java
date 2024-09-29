package pl.lawit.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lawit.web.handler.PaymentHandler;

import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/payments", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class PaymentOrderController {

	private final PaymentHandler handler;

	@Operation(summary = "Gateway callback")
	@PostMapping
	public void callback(@RequestBody String token) {
		handler.process(token);
	}

}
