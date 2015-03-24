package com.intuso.housemate.realclient.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
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
public class DeviceListWatcher implements ListListener<RealDevice> {

    private final Multimap<RealDevice, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final ValueWatcher valueWatcher;
    private final PropertyListWatcher propertyListWatcher;
    private final DeviceListener deviceListener;

    @Inject
    public DeviceListWatcher(Log log, Persistence persistence, ValueWatcher valueWatcher, PropertyListWatcher propertyListWatcher, DeviceListener deviceListener) {
        this.log = log;
        this.persistence = persistence;
        this.valueWatcher = valueWatcher;
        this.propertyListWatcher = propertyListWatcher;
        this.deviceListener = deviceListener;
    }

    @Override
    public void elementAdded(RealDevice device) {

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(device.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(device.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(device.getDescription())));
        toSave.getChildren().put("type", new TypeInstances(new TypeInstance(device.getType())));
        try {
            persistence.saveValues(device.getPath(), toSave);
        } catch (HousemateException e) {
            log.e("Failed to save new device values", e);
        }

        listeners.put(device, device.addObjectListener(deviceListener));
        listeners.put(device, device.getRunningValue().addObjectListener(valueWatcher));
        listeners.put(device, device.getProperties().addObjectListener(propertyListWatcher, true));
        try {
            TypeInstances instances = persistence.getTypeInstances(device.getRunningValue().getPath());
            if(instances.getElements().size() > 0 && BooleanType.SERIALISER.deserialise(instances.getElements().get(0)))
                device.getStartCommand().perform(new TypeInstanceMap(),
                        new CommandPerformListener(log, "Start device \"" + device.getId() + "\""));
        } catch(DetailsNotFoundException e) {
            log.w("No details found for whether the device was previously running" + Arrays.toString(device.getPath()));
        } catch(HousemateException e) {
            log.e("Failed to check value for whether the device was previously running", e);
        }
    }

    @Override
    public void elementRemoved(RealDevice device) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(device);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            persistence.removeValues(device.getPath());
        } catch(HousemateException e) {
            log.e("Failed to delete device properties", e);
        }
    }
}
