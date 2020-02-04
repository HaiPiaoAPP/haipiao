package com.haipiao.imageservice.application;

import com.google.common.base.Preconditions;
import com.haipiao.common.controller.HealthzController;
import com.haipiao.imageservice.dto.GetSecurityTokenRequest;
import com.haipiao.imageservice.dto.GetSecurityTokenResponse;
import com.haipiao.imageservice.handler.SecurityTokenHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ImageController extends HealthzController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private SecurityTokenHandler securityTokenHandler;

    @GetMapping("/image/securitytoken")
    public ResponseEntity<GetSecurityTokenResponse> getSecurityToken(@CookieValue("session-token") String sessionToken)  {
        Preconditions.checkArgument(StringUtils.isNotEmpty(sessionToken));
        GetSecurityTokenRequest req = new GetSecurityTokenRequest();
        return securityTokenHandler.handle(sessionToken, req);
    }
}
