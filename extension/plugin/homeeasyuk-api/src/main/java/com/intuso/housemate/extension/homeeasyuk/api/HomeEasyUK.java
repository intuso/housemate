package com.intuso.housemate.extension.homeeasyuk.api;

import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Hardware;
import com.intuso.housemate.client.v1_0.api.object.Value;
import com.intuso.housemate.client.v1_0.real.api.annotations.HardwareAPI;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 08/12/16.
 */
@HardwareAPI("homeeasyuk-1.0")
public interface HomeEasyUK {
    
    Device register(String houseId, String unitId);
    
    interface Device {
        boolean isOn();
        void turnOn();
        void turnOff();
        ListenerRegistration listen(Listener listener);

        interface Listener {
            void turnedOn();
            void turnedOff();
        }
    }
    
    class Proxy implements HomeEasyUK {
        
        private final Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> hardware;

        public Proxy(Hardware<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> hardware) {
            this.hardware = hardware;
        }

        @Override
        public HomeEasyUK.Device register(String houseId, String unitId) {
            return new Device(houseId, unitId);
        }

        private class Device implements HomeEasyUK.Device {
            
            private final String houseId;
            private final String unitId;

            private Command<?, ?, ?, ?> onCommand;
            private Command<?, ?, ?, ?> offCommand;
            private Value<?, ?, ?> onValue;

            private Device(String houseId, String unitId) {
                this.houseId = houseId;
                this.unitId = unitId;
            }

            public synchronized void ensureOnCommand() {
                if(onCommand == null)
                    onCommand = hardware.getCommands().get("homeeasyuk." + houseId + "." + unitId + ".on");
            }

            public synchronized void ensureOffCommand() {
                if(offCommand == null)
                    offCommand = hardware.getCommands().get("homeeasyuk." + houseId + "." + unitId + ".off");
            }

            @Override
            public boolean isOn() {
                return false;
            }

            @Override
            public void turnOn() {
                ensureOnCommand();
                if(onCommand == null)
                    throw new HousemateException("Could not find on command for homeeasyuk device " + houseId + "." + unitId);
//                onCommand.perform();
            }

            @Override
            public void turnOff() {

            }

            @Override
            public ListenerRegistration listen(Listener listener) {
                return null;
            }
        }
    }
}
