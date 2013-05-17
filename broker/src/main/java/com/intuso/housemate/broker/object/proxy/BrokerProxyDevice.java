package com.intuso.housemate.broker.object.proxy;

import com.intuso.housemate.broker.object.real.BrokerRealCommand;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.object.command.CommandWrappable;
import com.intuso.housemate.core.object.device.Device;
import com.intuso.housemate.core.object.device.DeviceListener;
import com.intuso.housemate.core.object.device.DeviceWrappable;
import com.intuso.housemate.core.object.property.PropertyWrappable;
import com.intuso.housemate.core.object.value.ValueListener;
import com.intuso.housemate.core.object.value.ValueWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 27/05/12
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public class BrokerProxyDevice
        extends BrokerProxyPrimaryObject<DeviceWrappable, BrokerProxyDevice, DeviceListener<? super BrokerProxyDevice>>
        implements Device<BrokerRealCommand, BrokerProxyCommand, BrokerProxyCommand,
            BrokerProxyList<CommandWrappable, BrokerProxyCommand>, BrokerProxyValue, BrokerProxyValue, BrokerProxyValue,
            BrokerProxyList<ValueWrappable, BrokerProxyValue>, BrokerProxyProperty, BrokerProxyProperty,
            BrokerProxyList<PropertyWrappable, BrokerProxyProperty>, BrokerProxyDevice> {

    private BrokerProxyList<CommandWrappable, BrokerProxyCommand> commands;
    private BrokerProxyList<ValueWrappable, BrokerProxyValue> values;
    private BrokerProxyList<PropertyWrappable, BrokerProxyProperty> properties;

    public BrokerProxyDevice(BrokerProxyResources<BrokerProxyFactory.All> resources, DeviceWrappable wrappable) {
        super(resources, wrappable);
    }

    @Override
    protected void getChildObjects() {
        super.getChildObjects();
        commands = (BrokerProxyList<CommandWrappable, BrokerProxyCommand>)getWrapper(COMMANDS);
        values = (BrokerProxyList<ValueWrappable, BrokerProxyValue>)getWrapper(VALUES);
        properties = (BrokerProxyList<PropertyWrappable, BrokerProxyProperty>)getWrapper(PROPERTIES);
        getRunningValue().addObjectListener(new ValueListener<BrokerProxyValue>() {
            @Override
            public void valueChanged(BrokerProxyValue value) {
                try {
                    getResources().getGeneralResources().getStorage().saveValue(value.getPath(), value.getValue());
                } catch(HousemateException e) {
                    getLog().e("Failed to save running value of device");
                    getLog().st(e);
                }
            }
        });
    }

    @Override
    public BrokerProxyList<CommandWrappable, BrokerProxyCommand> getCommands() {
        return commands;
    }

    @Override
    public BrokerProxyList<ValueWrappable, BrokerProxyValue> getValues() {
        return values;
    }

    @Override
    public BrokerProxyList<PropertyWrappable, BrokerProxyProperty> getProperties() {
        return properties;
    }
}
