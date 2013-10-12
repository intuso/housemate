package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeature;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeatureFactory;

import java.util.List;

/**
 * @param <RESOURCES> the type of the resources
 * @param <CHILD_RESOURCES> the type of the child's resources
 * @param <COMMAND> the type of the commands
 * @param <COMMANDS> the type of the commands list
 * @param <VALUE> the type of the values
 * @param <VALUES> the type of the values list
 * @param <PROPERTY> the type of the properties
 * @param <PROPERTIES> the type of the properties list
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDevice<
            RESOURCES extends ProxyResources<
                    ? extends HousemateObjectFactory<CHILD_RESOURCES, HousemateData<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>,
                    ? extends ProxyFeatureFactory<FEATURE, DEVICE>>,
            CHILD_RESOURCES extends ProxyResources<?, ?>,
            COMMAND extends ProxyCommand<?, ?, ?, ?, COMMAND>,
            COMMANDS extends ProxyList<?, ?, CommandData, COMMAND, COMMANDS>,
            VALUE extends ProxyValue<?, ?, VALUE>,
            VALUES extends ProxyList<?, ?, ValueData, VALUE, VALUES>,
            PROPERTY extends ProxyProperty<?, ?, ?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<?, ?, PropertyData, PROPERTY, PROPERTIES>,
            FEATURE extends ProxyFeature<?, DEVICE>,
            DEVICE extends ProxyDevice<RESOURCES, CHILD_RESOURCES, COMMAND, COMMANDS, VALUE, VALUES, PROPERTY, PROPERTIES, FEATURE, DEVICE>>
        extends ProxyPrimaryObject<RESOURCES, CHILD_RESOURCES, DeviceData, COMMAND, VALUE, DEVICE, DeviceListener<? super DEVICE>>
        implements Device<COMMAND, COMMAND, COMMAND, COMMANDS, VALUE, VALUE, VALUE, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICE> {

    /**
     * @param resources {@inheritDoc}
     * @param childResources {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyDevice(RESOURCES resources, CHILD_RESOURCES childResources, DeviceData data) {
        super(resources, childResources, data);
    }

    @Override
    public final COMMANDS getCommands() {
        return (COMMANDS) getChild(COMMANDS_ID);
    }

    @Override
    public final VALUES getValues() {
        return (VALUES) getChild(VALUES_ID);
    }

    @Override
    public final PROPERTIES getProperties() {
        return (PROPERTIES) getChild(PROPERTIES_ID);
    }

    @Override
    public boolean isConnected() {
        VALUE connected = getConnectedValue();
        return connected.getTypeInstances() != null && connected.getTypeInstances().getFirstValue() != null
                ? Boolean.parseBoolean(connected.getTypeInstances().getFirstValue()) : false;
    }

    @Override
    public VALUE getConnectedValue() {
        return (VALUE) getChild(CONNECTED_ID);
    }

    @Override
    public final List<String> getFeatureIds() {
        return getData().getFeatureIds();
    }

    @Override
    public final List<String> getCustomCommandIds() {
        return getData().getCustomCommandIds();
    }

    @Override
    public final List<String> getCustomValueIds() {
        return getData().getCustomValueIds();
    }

    @Override
    public final List<String> getCustomPropertyIds() {
        return getData().getCustomPropertyIds();
    }

    public <F extends FEATURE> F getFeature(String featureId) {
        return getResources().getFeatureFactory().getFeature(featureId, getThis());
    }
}
