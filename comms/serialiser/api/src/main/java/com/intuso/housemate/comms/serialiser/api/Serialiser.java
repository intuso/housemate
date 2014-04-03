package com.intuso.housemate.comms.serialiser.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.comms.Message;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 21/01/14
 * Time: 08:17
 * To change this template use File | Settings | File Templates.
 */
public interface Serialiser {

    public final static String DETAILS_KEY = "serialisation";

    public void write(Message<?> message) throws IOException;
    public Message<?> read() throws InterruptedException, IOException, HousemateException;
}
