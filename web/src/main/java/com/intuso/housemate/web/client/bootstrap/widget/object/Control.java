package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.intuso.housemate.client.v1_0.proxy.api.ProxyObject;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRemoveable;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRenameable;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRunnable;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.NestedTable;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class Control extends NestedTable {

    public Control(GWTProxyList<TypeData<?>, GWTProxyType> types, ProxyObject<?, ?, ?, ?, ?> object) {
        if(object instanceof ProxyRenameable)
            addRow("Rename", new RenameableWidget(types, (ProxyRenameable<GWTProxyCommand>) object));
        if(object instanceof ProxyRunnable)
            addRow("Running", new RunnableWidget((ProxyRunnable<GWTProxyCommand, GWTProxyValue>) object));
        if(object instanceof ProxyRemoveable)
            addRow("Remove", new RemoveableWidget(types, (ProxyRemoveable<GWTProxyCommand>) object));
    }
}
