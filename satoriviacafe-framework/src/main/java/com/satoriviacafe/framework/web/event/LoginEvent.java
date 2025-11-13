package com.satoriviacafe.framework.web.event;

import com.satoriviacafe.common.core.domain.satoriviacafe.LoginUser;
import com.satoriviacafe.common.event.BaseEvent;

/**
 * @author shy
 * @since 2025年05月28日
 */
public class LoginEvent extends BaseEvent<LoginUser> {


    public LoginEvent(LoginUser source) {
        super(source);
    }
}
