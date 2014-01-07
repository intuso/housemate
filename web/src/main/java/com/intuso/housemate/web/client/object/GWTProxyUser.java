package com.intuso.housemate.web.client.object;

import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.api.object.user.UserData;
import com.intuso.housemate.object.proxy.ProxyUser;
import com.intuso.utilities.log.Log;

/**
 */
public class GWTProxyUser extends ProxyUser<
        GWTProxyCommand,
        GWTProxyUser> {
    public GWTProxyUser(Log log,
                        Injector injector,
                        @Assisted UserData data) {
        super(log, injector, data);
    }
}
