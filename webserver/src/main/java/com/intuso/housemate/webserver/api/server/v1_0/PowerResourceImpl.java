package com.intuso.housemate.webserver.api.server.v1_0;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.type.serialiser.BooleanSerialiser;
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

        List<Device.Data> devices = Lists.newArrayList();
        server.getDevices().forEach(device -> devices.add(device.getData()));

        Stream<Device.Data> stream  = devices.stream();
        if(offset > 0)
            stream = stream.skip(offset);
        if(limit >= 0)
            stream = stream.limit(limit);
        return new Page<>(offset, devices.size(), stream.collect(Collectors.toList()));
    }

    @Override
    public boolean isOn(String id) {
        logger.debug("Is on {}", id);
        ProxyDevice<?, ?, ?, ?, ?, ?> device = SessionUtils.getServer(request.getSession(false)).getDevices().get(id);
        if(device == null)
            throw new NotFoundException();
        return BooleanSerialiser.INSTANCE.deserialise(device.getValues().get("on").getValue().getElements().get(0));
    }

    @Override
    public void turnOn(String id) {
        logger.debug("Turning on {}", id);
        ProxyDevice<?, ?, ?, ?, ?, ?> device = SessionUtils.getServer(request.getSession(false)).getDevices().get(id);
        if(device == null)
            throw new NotFoundException();
        device.getCommands().get("on").perform(loggerListener);
    }

    @Override
    public void turnOff(String id) {
        logger.debug("Turning off {}", id);
        ProxyDevice<?, ?, ?, ?, ?, ?> device = SessionUtils.getServer(request.getSession(false)).getDevices().get(id);
        if(device == null)
            throw new NotFoundException();
        device.getCommands().get("off").perform(loggerListener);
    }
}
