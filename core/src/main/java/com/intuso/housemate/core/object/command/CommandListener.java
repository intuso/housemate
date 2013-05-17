package com.intuso.housemate.core.object.command;

import com.intuso.housemate.core.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/07/12
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */
public interface CommandListener<C extends Command<?, ?>>
        extends ObjectListener {
    public void commandStarted(C command);
    public void commandFinished(C command);
    public void commandFailed(C command, String error);
}
