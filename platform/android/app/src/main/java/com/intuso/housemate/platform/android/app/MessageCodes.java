package com.intuso.housemate.platform.android.app;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 22/10/13
 * Time: 08:11
 * To change this template use File | Settings | File Templates.
 */
public class MessageCodes {

    // connection state
    public final static int CONNECTED = 0;
    public final static int DISCONNECTED = 1;

    // registration
    public final static int REGISTER = 10;
    public final static int REGISTERED = 11;
    public final static int UNREGISTER = 12;

    // open/close receiver/sender
    public final static int OPEN_RECEIVER = 20;
    public final static int OPENED_RECEIVER = 21;
    public final static int CLOSE_RECEIVER = 22;
    public final static int OPEN_SENDER = 23;
    public final static int OPENED_SENDER = 24;
    public final static int CLOSE_SENDER = 25;

    // messages
    public final static int SEND_MESSAGE = 30;
    public final static int SENT_MESSAGE = 31;
    public final static int GET_MESSAGE = 32;
    public final static int GOT_MESSAGE = 33;
    public final static int GET_MESSAGES = 34;
    public final static int GOT_MESSAGES = 35;
    public final static int LISTEN = 36;
    public final static int ON_MESSAGE_RECEIVED = 37;

    private MessageCodes() {}
}
