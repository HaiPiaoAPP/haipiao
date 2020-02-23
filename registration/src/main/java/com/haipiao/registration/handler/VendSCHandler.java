package com.haipiao.registration.handler;

import com.haipiao.common.enums.SecurityCodeType;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.common.util.security.SecurityCodeManager;
import com.haipiao.registration.application.RegistrationController;
import com.haipiao.registration.property.SMSProperties;
import com.haipiao.registration.req.VendSCRequest;
import com.haipiao.registration.resp.VendSCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import static com.haipiao.common.enums.StatusCode.BAD_REQUEST;
import static com.haipiao.common.enums.StatusCode.SUCCESS;
import static com.haipiao.common.enums.StatusCode.THROTTLED;
import static com.haipiao.common.enums.StatusCode.INTERNAL_SERVER_ERROR;

@Component
public class VendSCHandler extends AbstractHandler<VendSCRequest, VendSCResponse> {

    @Autowired
    private final SecurityCodeManager securityCodeManager;

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    SMSProperties smsProperties;

    public VendSCHandler(SessionService sessionService,
                         SecurityCodeManager securityCodeManager) {
        super(VendSCResponse.class, sessionService);
        this.securityCodeManager = securityCodeManager;
    }

    @Override
    public VendSCResponse execute(VendSCRequest request) {
        SecurityCodeType sct = SecurityCodeType.findByCode(request.getType());
        if (sct == null) {
            return new VendSCResponse(BAD_REQUEST);
        }
        final String sc = securityCodeManager.getSecurityCode(request.getCell(),
            SecurityCodeType.findByCode(request.getType()));
        if (sc == null) {
            return new VendSCResponse(THROTTLED);
        }


        if(Boolean.parseBoolean(smsProperties.getSms().getTesting())) { // Testing only
            VendSCResponse resp = new VendSCResponse(SUCCESS);
            resp.setSecurityCode(sc);
            return resp;
        } else if(sendSMS(request.getCell(), sc)){ // Invoke external SMS service
            VendSCResponse resp = new VendSCResponse(SUCCESS);
            return resp;
        } else {
            VendSCResponse resp = new VendSCResponse(INTERNAL_SERVER_ERROR);
            return resp;
        }
    }

    // cell currently assume cell number is in China.
    private boolean sendSMS(String cell, String sc) {
        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getSms().getRegion(),
                smsProperties.getSms().getAccessKeyId(),
                smsProperties.getSms().getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(smsProperties.getSms().getDomain());
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getSms().getRegion());
        request.putQueryParameter("PhoneNumbers", cell);
        request.putQueryParameter("SignName", smsProperties.getSms().getSignature());
        request.putQueryParameter("TemplateCode", smsProperties.getSms().getTemplate());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + sc + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            if(response.getData().contains("OK")) {
                return true;
            } else {
                logger.info("response: {}", response.getData());
                return false;
            }
        } catch (ServerException e) {
            logger.info("server exception: {}", e);
            return false;
        } catch (ClientException e) {
            logger.info("client exception: {}", e);
            return false;
        }
    }
}
