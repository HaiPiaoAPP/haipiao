package com.haipiao.registration.handler;


import com.haipiao.common.enums.SecurityCodeType;
import com.haipiao.common.handler.AbstractHandler;
import com.haipiao.common.service.SessionService;
import com.haipiao.common.util.security.SecurityCodeManager;
import com.haipiao.registration.req.VendSCRequest;
import com.haipiao.registration.resp.VendSCResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.haipiao.common.enums.StatusCode.SUCCESS;
import static com.haipiao.common.enums.StatusCode.THROTTLED;

@Component
public class VendSCHandler extends AbstractHandler<VendSCRequest, VendSCResponse> {

    @Autowired
    private final SecurityCodeManager securityCodeManager;

    public VendSCHandler(SessionService sessionService,
                         SecurityCodeManager securityCodeManager) {
        super(VendSCResponse.class, sessionService);
        this.securityCodeManager = securityCodeManager;
    }

    @Override
    public VendSCResponse execute(VendSCRequest request) {
        final String sc = securityCodeManager.getSecurityCode(request.getCell(),
            SecurityCodeType.findByCode(request.getType()));
        if (sc == null) {
            return new VendSCResponse(THROTTLED);
        }
        VendSCResponse resp = new VendSCResponse(SUCCESS);
        resp.setSecurityCode(sc);
        return resp;
    }
}
