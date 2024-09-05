package pl.lawit.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static pl.lawit.web.util.ApiVersioning.LI_WEB_API_JSON_V1;

@RestController
@RequestMapping(value = "/lawyers", produces = LI_WEB_API_JSON_V1)
@RequiredArgsConstructor
public class LawyerController {

}
