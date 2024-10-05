package pl.lawit.web.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(value = {
	@ApiResponse(responseCode = "401", description = "Unauthorized"),
	@ApiResponse(responseCode = "500", description = "Internal Server Error")
})
public interface BaseController {
}
