package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.proxy.api.*;
import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.housemate.web.client.bootstrap.widget.NestedTable;
import com.intuso.housemate.web.client.bootstrap.widget.property.Property;
import com.intuso.housemate.web.client.object.*;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class GeneralOptions extends NestedTable {

    public GeneralOptions(GWTProxyList<Type.Data<?>, GWTProxyType> types, ProxyObject<?, ?, ?, ?, ?> object) {
        if(object instanceof ProxyRenameable)
            addRow("Rename", new RenameableWidget(types, (ProxyRenameable<GWTProxyCommand>) object));
        if(object instanceof ProxyUsesDriver)
            addRow("Driver", new Property(types, ((ProxyUsesDriver<GWTProxyProperty, GWTProxyValue>) object).getDriverProperty()));
        if(object instanceof ProxyRunnable)
            addRow("Running", new RunnableWidget((ProxyRunnable<GWTProxyCommand, GWTProxyValue>) object));
        if(object instanceof ProxyRemoveable)
            addRow("Remove", new RemoveableWidget(types, (ProxyRemoveable<GWTProxyCommand>) object));
    }
}
