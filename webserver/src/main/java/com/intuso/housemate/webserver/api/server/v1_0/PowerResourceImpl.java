package com.intuso.housemate.webserver.api.server.v1_0;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.ability.Power;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.view.*;
import com.intuso.housemate.client.v1_0.api.type.serialiser.BooleanPrimitiveSerialiser;
import com.intuso.housemate.client.v1_0.proxy.object.*;
import com.intuso.housemate.client.v1_0.rest.PowerResource;
import com.intuso.housemate.client.v1_0.rest.model.Page;
import com.intuso.housemate.webserver.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tomc on 23/01/17.
 */
public class PowerResourceImpl implements PowerResource {

    private final static Logger logger = LoggerFactory.getLogger(PowerResourceImpl.class);

    // todo this class is still assuming that devices have abilities rather than their components.

    private final Command.PerformListener<Command<?, ?, ?, ?>> loggerListener = new Command.PerformListener<Command<?, ?, ?, ?>>() {
        @Override
        public void commandStarted(Command<?, ?, ?, ?> command) {
            logger.debug("Started perform of {}", command);
        }

        @Override
        public void commandFinished(Command<?, ?, ?, ?> command) {
            logger.debug("Finished perform of {}", command);
        }

        @Override
        public void commandFailed(Command<?, ?, ?, ?> command, String error) {
            logger.debug("Failed perform of {} because {}", command, error);
        }
    };

    @Context private HttpServletRequest request;

    @Override
    public Page<Object.Data> list(int offset, int limit) {

        logger.debug("Listing power {} to {}", offset, limit);

        ProxyServer.Simple server = SessionUtils.getServer(request.getSession(false));
        if(server == null)
            throw new BadRequestException("No server for user");

        List<Object.Data> devices = Lists.newArrayList();
        server.getDevices().forEach(device -> {
            if(device.get() != null) {
                // need to change the id to that of the reference rather than the actual device, but also want to keep the rest of the device data
                Device.Data data = clone(device.get().getData());
                data.setId(device.getId());
                devices.add(data);
            }
        });

        Stream<Object.Data> stream  = devices.stream();
        if(offset > 0)
            stream = stream.skip(offset);
        if(limit >= 0)
            stream = stream.limit(limit);
        return new Page<>(offset, devices.size(), stream.collect(Collectors.toList()));
    }

    @Override
    public boolean isOn(String id) {
        logger.debug("Is on {}", id);
        ProxyReference.Simple<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>> deviceReference = SessionUtils.getServer(request.getSession(false)).getDevices().get(id);
        if(deviceReference == null || deviceReference.get() == null)
            throw new NotFoundException();
        ProxyDevice<?, ?, DeviceView<?>, ?, ? extends ProxyList<? extends ProxyDeviceComponent<?, ?, ?>, ?>, ?> device = deviceReference.get();
        device.load(new DeviceView().setComponents(new ListView<>(new DeviceComponentView().setValues(new ListView<>(new ValueView(), "on")))));
        for(ProxyDeviceComponent<?, ?, ?> component : device.getDeviceComponents())
            if(component.getAbilities().contains(Power.State.ID))
                return BooleanPrimitiveSerialiser.INSTANCE.deserialise(component.getValues().get("on").getValues().get(0));
        return false;
    }

    @Override
    public void turnOn(String id) {
        logger.debug("Turning on {}", id);
        ProxyReference.Simple<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>> deviceReference = SessionUtils.getServer(request.getSession(false)).getDevices().get(id);
        if(deviceReference == null || deviceReference.get() == null)
            throw new NotFoundException();
        ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?> device = deviceReference.get();
        device.load(new DeviceView().setComponents(new ListView<>(new DeviceComponentView().setCommands(new ListView<>(new CommandView(), "on")))));
        for(ProxyDeviceComponent<?, ?, ?> component : device.getDeviceComponents()) {
            if (component.getAbilities().contains(Power.Control.ID)) {
                component.getCommands().get("on").perform(loggerListener);
                return;
            }
        }
    }

    @Override
    public void turnOff(String id) {
        logger.debug("Turning off {}", id);
        ProxyReference.Simple<DeviceView<?>, ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?>> deviceReference = SessionUtils.getServer(request.getSession(false)).getDevices().get(id);
        if(deviceReference == null || deviceReference.get() == null)
            throw new NotFoundException();
        ProxyDevice<?, ?, DeviceView<?>, ?, ?, ?> device = deviceReference.get();
        device.load(new DeviceView().setComponents(new ListView<>(new DeviceComponentView().setCommands(new ListView<>(new CommandView(), "off")))));
        for(ProxyDeviceComponent<?, ?, ?> component : device.getDeviceComponents()) {
            if (component.getAbilities().contains(Power.Control.ID)) {
                component.getCommands().get("off").perform(loggerListener);
                return;
            }
        }
    }

    public Device.Data clone(Device.Data data) {
        if(data instanceof Device.Group.Data) {
            Device.Group.Data groupData = (Device.Group.Data) data;
            return new Device.Group.Data(groupData.getId(), groupData.getName(), groupData.getDescription());
        } else if(data instanceof Device.Connected.Data) {
            Device.Connected.Data connectedData = (Device.Connected.Data) data;
            return new Device.Connected.Data(connectedData.getId(), connectedData.getName(), connectedData.getDescription());
        } else
            return null;
    }
}
