package com.intuso.housemate.comms.serialiser.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.comms.serialiser.json.mixins.MessageMixin;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 22/01/14
 * Time: 08:47
 * To change this template use File | Settings | File Templates.
 */
public class JacksonModule extends SimpleModule {
    public JacksonModule() {
        setMixInAnnotation(Message.class, MessageMixin.class);
    }
}
