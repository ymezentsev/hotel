package com.robot.hotel.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ResponseEntity<ErrorDto> getError(WebRequest webRequest) {

        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE)
        );

        return ResponseEntity
                .status((Integer) attributes.get("status"))
                .body(ErrorDto
                        .builder()
                        .errorCode((Integer) attributes.get("status"))
                        .error((String) attributes.get("error"))
                        .timestamp(((Date) attributes.get("timestamp")).toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime())
                        .errorDescription((String) attributes.get("message"))
                        .build()
                );
    }
}
