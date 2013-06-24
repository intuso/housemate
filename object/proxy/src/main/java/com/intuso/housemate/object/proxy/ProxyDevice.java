package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.command.CommandWrappable;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.device.DeviceWrappable;
import com.intuso.housemate.api.object.property.PropertyWrappable;
import com.intuso.housemate.api.object.value.ValueWrappable;

/**
 */
public abstract class ProxyDevice<
            R extends ProxyResources<? extends HousemateObjectFactory<SR, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            SR extends ProxyResources<?>,
            C extends ProxyCommand<?, ?, ?, ?, C>,
            CL extends ProxyList<?, ?, CommandWrappable, C, CL>,
            V extends ProxyValue<?, ?, V>,
            VL extends ProxyList<?, ?, ValueWrappable, V, VL>,
            P extends ProxyProperty<?, ?, ?, ?, P>,
            PL extends ProxyList<?, ?, PropertyWrappable, P, PL>,
            D extends ProxyDevice<R, SR, C, CL, V, VL, P, PL, D>>
        extends ProxyPrimaryObject<R, SR, DeviceWrappable, C, V, D, DeviceListener<? super D>>
        implements Device<C, C, C, CL, V, V, V, V, VL, P, PL, D> {

    private CL commandList;
    private VL valueList;
    private PL propertyList;

    public ProxyDevice(R resources, SR subResources, DeviceWrappable wrappable) {
        super(resources, subResources, wrappable);
    }

    @Override
    protected final void getChildObjects() {
        super.getChildObjects();
        commandList = (CL)getWrapper(COMMANDS_ID);
        valueList = (VL)getWrapper(VALUES_ID);
        propertyList = (PL)getWrapper(PROPERTIES_ID);
    }

    @Override
    public final CL getCommands() {
        return commandList;
    }

    @Override
    public final VL getValues() {
        return valueList;
    }

    @Override
    public final PL getProperties() {
        return propertyList;
    }
}
