package com.intuso.housemate.web.client.ioc;

import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tomc on 09/12/15.
 */
public class LoggerProvider implements Provider<Logger> {
    @Override
    public Logger get() {
        return LoggerFactory.getLogger("com.intuso.housemate");
    }
}
