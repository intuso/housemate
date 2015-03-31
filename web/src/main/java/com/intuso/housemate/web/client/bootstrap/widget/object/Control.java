package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.intuso.housemate.api.object.Removeable;
import com.intuso.housemate.api.object.Renameable;
import com.intuso.housemate.api.object.Runnable;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.proxy.ProxyObject;
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
        if(object instanceof Renameable)
            addRow("Rename", new RenameableWidget(types, (Renameable<GWTProxyCommand>) object));
        if(object instanceof Renameable)
            addRow("Running", new RunnableWidget((Runnable<GWTProxyCommand, GWTProxyValue>) object));
        if(object instanceof Renameable)
            addRow("Remove", new RemoveableWidget(types, (Removeable<GWTProxyCommand>) object));
    }
}
