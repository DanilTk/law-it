package pl.lawit.web.controller;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Object not found"),
        @ApiResponse(responseCode = "429", description = "Too Many Requests"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
})
public interface BaseController {
}
