package com.intuso.housemate.platform.android.common;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 22/10/13
 * Time: 08:11
 * To change this template use File | Settings | File Templates.
 */
public class MessageCodes {

    // connection-based messages
    public final static int REGISTER = 1;
    public final static int UNREGISTER = 2;
    public final static int REGISTERED = 3;

    // comms based messages
    public final static int SEND_MESSAGE = 10;

    private MessageCodes() {}
}
