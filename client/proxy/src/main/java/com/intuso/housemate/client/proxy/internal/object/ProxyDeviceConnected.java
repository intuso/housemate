package com.intuso.housemate.client.proxy.internal.object;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.view.DeviceConnectedView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Base interface for all proxy features
 * @param <DEVICE> the feature type
 */
public abstract class ProxyDeviceConnected<COMMAND extends ProxyCommand<?, ?, ?>,
        DEVICE_COMPONENTS extends ProxyList<? extends ProxyDeviceComponent<?, ?, ?>, ?>,
        DEVICE extends ProxyDeviceConnected<COMMAND, DEVICE_COMPONENTS, DEVICE>>
        extends ProxyDevice<Device.Connected.Data, Device.Connected.Listener<? super DEVICE>, DeviceConnectedView, COMMAND, DEVICE_COMPONENTS, DEVICE>
        implements Device.Connected<COMMAND, DEVICE_COMPONENTS, DEVICE> {

    /**
     * @param logger {@inheritDoc}
     */
    public ProxyDeviceConnected(Logger logger,
                                String path,
                                String name,
                                ManagedCollectionFactory managedCollectionFactory,
                                Receiver.Factory receiverFactory,
                                Factory<COMMAND> commandFactory,
                                Factory<DEVICE_COMPONENTS> componentsFactory) {
        super(logger, path, name, Device.Connected.Data.class, managedCollectionFactory, receiverFactory, commandFactory, componentsFactory);
    }

    @Override
    public DeviceConnectedView createView(View.Mode mode) {
        return new DeviceConnectedView(mode);
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:16
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyDeviceConnected<ProxyCommand.Simple,
            ProxyList.Simple<ProxyDeviceComponent.Simple>,
            Simple> {

        @Inject
        public Simple(@Assisted Logger logger,
                      @Assisted("path") String path,
                      @Assisted("name") String name,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyDeviceComponent.Simple>> componentsFactory) {
            super(logger, path, name, managedCollectionFactory, receiverFactory, commandFactory, componentsFactory);
        }
    }
}
