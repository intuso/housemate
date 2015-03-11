package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.intuso.housemate.api.object.Removeable;
import com.intuso.housemate.api.object.Renameable;
import com.intuso.housemate.api.object.Runnable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.NestedTable;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class Control extends NestedTable {

    public Control(ProxyObject<?, ?, ?, ?, ?> object) {
        if(object instanceof Renameable)
            addRow("Rename", new RenameableWidget((Renameable<GWTProxyCommand>) object));
        if(object instanceof Renameable)
            addRow("Running", new RunnableWidget((Runnable<GWTProxyCommand, GWTProxyValue>) object));
        if(object instanceof Renameable)
            addRow("Remove", new RemoveableWidget((Removeable<GWTProxyCommand>) object));
    }
}
