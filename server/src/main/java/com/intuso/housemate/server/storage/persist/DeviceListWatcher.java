package com.intuso.housemate.server.storage.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.server.storage.DetailsNotFoundException;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:24
* To change this template use File | Settings | File Templates.
*/
public class DeviceListWatcher implements ListListener<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?>> {

    private final Multimap<Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Storage storage;
    private final ValueWatcher valueWatcher;
    private final PropertyListWatcher propertyListWatcher;

    @Inject
    public DeviceListWatcher(Log log, Storage storage, ValueWatcher valueWatcher, PropertyListWatcher propertyListWatcher) {
        this.log = log;
        this.storage = storage;
        this.valueWatcher = valueWatcher;
        this.propertyListWatcher = propertyListWatcher;
    }

    @Override
    public void elementAdded(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
        listeners.put(device, device.getRunningValue().addObjectListener(valueWatcher));
        listeners.put(device, device.getProperties().addObjectListener(propertyListWatcher, true));
        try {
            TypeInstances instances = storage.getTypeInstances(device.getRunningValue().getPath());
            if(instances.size() > 0 && BooleanType.SERIALISER.deserialise(instances.get(0)))
                device.getStartCommand().perform(new TypeInstanceMap(),
                        new CommandPerformListener(log, "Start device \"" + device.getId() + "\""));
        } catch(DetailsNotFoundException e) {
            log.w("No details found for whether the device was previously running" + Arrays.toString(device.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to check value for whether the device was previously running", e);
        }
    }

    @Override
    public void elementRemoved(Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ? extends Property<?, ?, ?>, ?, ?> device) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(device);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            storage.removeValues(device.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete device properties", e);
        }
    }
}
