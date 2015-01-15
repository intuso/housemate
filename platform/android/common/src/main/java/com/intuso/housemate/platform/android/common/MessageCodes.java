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
    public final static int CREATE_REGISTRATION = 1;
    public final static int RE_REGISTER = 2;
    public final static int UNREGISTER = 3;
    public final static int REGISTERED = 4;

    // comms based messages
    public final static int SEND_MESSAGE = 10;

    private MessageCodes() {}
}
