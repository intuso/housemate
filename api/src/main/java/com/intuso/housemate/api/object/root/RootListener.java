package com.intuso.housemate.api.object.root;

import com.intuso.housemate.api.comms.ConnectionStatus;
import com.intuso.housemate.api.object.ObjectListener;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/03/13
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public interface RootListener<R extends Root> extends ObjectListener {
    public void connectionStatusChanged(R root, ConnectionStatus status);
    void brokerInstanceChanged(R root);
}
