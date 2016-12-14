package com.intuso.housemate.extension.plugin.homeeasyuk.feature;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyCommand;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyHardware;
import com.intuso.housemate.client.v1_0.proxy.simple.SimpleProxyValue;
import com.intuso.housemate.extension.homeeasyuk.api.HomeEasyUKHardwareAPI;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 08/12/16.
 */
public class HomeEasyUKHardwareProxy implements HomeEasyUKHardwareAPI {

    private final Logger logger;
    private final SimpleProxyHardware hardware;
    private final ListenersFactory listenersFactory;

    @Inject
    public HomeEasyUKHardwareProxy(@Assisted Logger logger,
                                   @Assisted SimpleProxyHardware hardware,
                                   ListenersFactory listenersFactory) {
        this.logger = logger;
        this.hardware = hardware;
        this.listenersFactory = listenersFactory;
    }

    @Override
    public Appliance appliance(int houseId, byte unitId) {
        return new Appliance(houseId, unitId);
    }

    private class Appliance implements HomeEasyUKHardwareAPI.Appliance, Value.Listener<SimpleProxyValue> {

        private final int houseId;
        private final byte unitId;

        private final Listeners<Listener> listeners;

        private SimpleProxyCommand onCommand;
        private SimpleProxyCommand offCommand;
        private SimpleProxyValue isOnValue;
        private ListenerRegistration valueListenerRegistration;

        private Appliance(int houseId, byte unitId) {
            this.houseId = houseId;
            this.unitId = unitId;
            listeners = listenersFactory.create();
        }

        private synchronized void ensureOnCommand() {
            if(onCommand == null)
                onCommand = hardware.getCommands().get("homeeasyuk." + houseId + "." + unitId + ".on");
            if(onCommand == null)
                throw new HousemateException("Could not find on command for homeeasyuk appliance " + houseId + "." + unitId);
        }

        private synchronized void ensureOffCommand() {
            if(offCommand == null)
                offCommand = hardware.getCommands().get("homeeasyuk." + houseId + "." + unitId + ".off");
            if(offCommand == null)
                throw new HousemateException("Could not find off command for homeeasyuk appliance " + houseId + "." + unitId);
        }

        private synchronized void ensureIsOnValue() {
            if(isOnValue == null)
                isOnValue = hardware.getValues().get("homeeasyuk." + houseId + "." + unitId + ".ison");
            if(isOnValue == null)
                throw new HousemateException("Could not find is on value for homeeasyuk appliance " + houseId + "." + unitId);
        }

        @Override
        public boolean isOn() {
            return isOnValue.getValue() != null
                    && isOnValue.getValue().getFirstValue() != null
                    && Boolean.parseBoolean(isOnValue.getValue().getFirstValue());
        }

        @Override
        public void turnOn() {
            ensureOnCommand();
            onCommand.perform(new Command.PerformListener<SimpleProxyCommand>() {
                @Override
                public void commandStarted(SimpleProxyCommand command) {
                    logger.debug("Starting on command for HomeEasy UK appliance {}-{}", houseId, unitId);
                }

                @Override
                public void commandFinished(SimpleProxyCommand command) {
                    logger.debug("Finished on command for HomeEasy UK appliance {}-{}", houseId, unitId);
                }

                @Override
                public void commandFailed(SimpleProxyCommand command, String error) {
                    logger.debug("Failed on command for HomeEasy UK appliance {}-{}. {}", houseId, unitId, error);
                }
            });
        }

        @Override
        public void turnOff() {
            ensureOffCommand();
            offCommand.perform(new Command.PerformListener<SimpleProxyCommand>() {
                @Override
                public void commandStarted(SimpleProxyCommand command) {
                    logger.debug("Starting off command for HomeEasy UK appliance {}-{}", houseId, unitId);
                }

                @Override
                public void commandFinished(SimpleProxyCommand command) {
                    logger.debug("Finished off command for HomeEasy UK appliance {}-{}", houseId, unitId);
                }

                @Override
                public void commandFailed(SimpleProxyCommand command, String error) {
                    logger.debug("Failed off command for HomeEasy UK appliance {}-{}. {}", houseId, unitId, error);
                }
            });
        }

        @Override
        public ListenerRegistration listen(Listener listener) {
            if(valueListenerRegistration == null) {
                ensureIsOnValue();
                valueListenerRegistration = isOnValue.addObjectListener(this);
            }
            return listeners.addListener(listener);
        }

        @Override
        public void valueChanging(SimpleProxyValue value) {}

        @Override
        public void valueChanged(SimpleProxyValue value) {
            if(isOn()) {
                logger.debug("HomeEasy UK appliance {}-{} is now on", houseId, unitId);
                for (Listener listener : listeners)
                    listener.on(true);
            } else {
                logger.debug("HomeEasy UK appliance {}-{} is now off", houseId, unitId);
                for (Listener listener : listeners)
                    listener.on(false);
            }
        }
    }

    public interface Factory {
        HomeEasyUKHardwareProxy create(Logger logger, SimpleProxyHardware hardware);
    }
}
