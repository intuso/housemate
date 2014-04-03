package com.intuso.housemate.object.proxy;

import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.device.feature.ProxyFeature;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * @param <COMMAND> the type of the commands
 * @param <COMMANDS> the type of the commands list
 * @param <VALUE> the type of the values
 * @param <VALUES> the type of the values list
 * @param <PROPERTY> the type of the properties
 * @param <PROPERTIES> the type of the properties list
 * @param <DEVICE> the type of the device
 */
public abstract class ProxyDevice<
            COMMAND extends ProxyCommand<?, ?, ?, COMMAND>,
            COMMANDS extends ProxyList<CommandData, COMMAND, COMMANDS>,
            VALUE extends ProxyValue<?, VALUE>,
            VALUES extends ProxyList<ValueData, VALUE, VALUES>,
            PROPERTY extends ProxyProperty<?, ?, PROPERTY>,
            PROPERTIES extends ProxyList<PropertyData, PROPERTY, PROPERTIES>,
            FEATURE extends ProxyFeature<?, DEVICE>,
            DEVICE extends ProxyDevice<COMMAND, COMMANDS, VALUE, VALUES, PROPERTY, PROPERTIES, FEATURE, DEVICE>>
        extends ProxyPrimaryObject<DeviceData, COMMAND, VALUE, DEVICE, DeviceListener<? super DEVICE>>
        implements Device<COMMAND, COMMAND, COMMAND, COMMANDS, VALUE, VALUE, VALUE, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICE> {

    /**
     * @param log {@inheritDoc}
     * @param data {@inheritDoc}
     */
    public ProxyDevice(Log log, ListenersFactory listenersFactory, DeviceData data) {
        super(log, listenersFactory, data);
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
    public List<ListenerRegistration> registerListeners() {
        final java.util.List<ListenerRegistration> result = super.registerListeners();
        addChildLoadedListener(CONNECTED_ID, new ChildLoadedListener<DEVICE, ProxyObject<?, ?, ?, ?, ?>>() {
            @Override
            public void childLoaded(DEVICE object, ProxyObject<?, ?, ?, ?, ?> proxyObject) {
                result.add(getConnectedValue().addObjectListener(new ValueListener<VALUE>() {
                    @Override
                    public void valueChanging(VALUE value) {
                        // do nothing
                    }

                    @Override
                    public void valueChanged(VALUE value) {
                        // call our own listeners
                        boolean connected = isConnected();
                        for(DeviceListener<? super DEVICE> listener : getObjectListeners())
                            listener.deviceConnected(getThis(), connected);
                    }
                }));
            }
        });
        return result;
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

    public abstract <F extends FEATURE> F getFeature(String featureId);
}
