package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.v1_0.api.annotation.Command;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.Property;
import com.intuso.housemate.client.v1_0.api.driver.HardwareDriver;
import com.intuso.housemate.plugin.rfxcom.ioc.RFXtrx433Module;
import com.rfxcom.rfxtrx.RFXtrx;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@Id(value = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware implements HardwareDriver {

    private final static String DEFAULT_PATTERNS = ".*ttyUSB.*,COM.*,.*tty\\.usbserial-.*";

    private static List<RFXtrx433Hardware> CREATED_INSTANCES = Lists.newCopyOnWriteArrayList();
    private static RFXtrx433Hardware RUNNING_INSTANCE = null;

    private final static RFXtrx RFXTRX = new RFXtrx();

    private final Set<Handler> handlers;
    private final Lighting2Handler.AC lighting2ACHandler;

    private String patterns = DEFAULT_PATTERNS;
    private boolean autoCreate = true;

    @Inject
    public RFXtrx433Hardware(Injector injector) {
        Injector ourInjector = injector.createChildInjector(new RFXtrx433Module(RFXTRX));
        this.handlers = ourInjector.getInstance(new Key<Set<Handler>>() {});
        this.lighting2ACHandler = ourInjector.getInstance(Lighting2Handler.AC.class);
        CREATED_INSTANCES.add(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        CREATED_INSTANCES.remove(this);
    }

    public String getPatterns() {
        return patterns;
    }

    @Property
    @Id(value = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names")
    public void setPatterns(String patterns) {
        this.patterns = patterns;
        RFXTRX.setPatterns(asPatterns(patterns));
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    @Property
    @Id(value = "autocreate", name = "Auto Create", description = "Automatically create devices as new messages are received")
    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
        for(Handler handler : handlers)
            handler.autoCreate(autoCreate);
    }

    @Override
    public void init(Logger logger, HardwareDriver.Callback callback) {
        synchronized (RFXtrx433Hardware.class) {
            if(RUNNING_INSTANCE != null)
                throw new HardwareDriver.HardwareException("Another device is using the hardware");
            RUNNING_INSTANCE = this;
        }
        // setup the connection to the USB device
        RFXTRX.setPatterns(asPatterns(patterns));
        RFXTRX.openPortSafe();
        for (Handler handler : handlers) {
            handler.autoCreate(autoCreate);
            handler.init(callback);
        }
    }

    @Override
    public void foundDeviceId(String deviceId) {
        for(Handler handler : handlers) {
            if (handler.matches(deviceId)) {
                handler.parseId(deviceId);
                return;
            }
        }
    }

    @Override
    public void uninit() {
        for(Handler handler : handlers)
            handler.uninit();
        RFXTRX.closePort();
        synchronized (RFXtrx433Hardware.class) {
            // this should always be true, but let's check just in case
            if(RUNNING_INSTANCE != null && RUNNING_INSTANCE.equals(this))
                RUNNING_INSTANCE = null;
        }
    }

    @Command
    @Id("homeeasyuk-appliance-add")
    public void addHomeEasyUKAppliance(@Id("house-id") int houseId, @Id("unit-code") byte unitCode) {
        lighting2ACHandler.addDevice(houseId, unitCode);
    }

    @Command
    @Id("homeeasyuk-appliance-remove")
    public void removeHomeEasyUKAppliance(@Id("house-id") int houseId, @Id("unit-code") byte unitCode) {
        lighting2ACHandler.removeDevice(houseId, unitCode);
    }

    private static List<Pattern> asPatterns(String patterns) {
        List<Pattern> result = Lists.newArrayList();
        for(String pattern : patterns.split(","))
            result.add(Pattern.compile(pattern));
        return result;
    }

    public static class Detector implements HardwareDriver.Detector {

        @Override
        public void detect(Callback callback) {

            // check if a hardware of this driver already exists
            if(CREATED_INSTANCES.size() > 0)
                return;

            // if not, try and open the port. Should succeed if one is attached
            try {
                RFXTRX.setPatterns(asPatterns(DEFAULT_PATTERNS));
                RFXTRX.openPort();
                RFXTRX.closePort();

                // if we get here, there are no current drivers created but there is an RFXtrx433 attached.
                Map<String, List<Object>> properties = Maps.newHashMap();
                properties.put("autocreate", Lists.newArrayList(true));
                callback.create("rfxtrx433", "RFXtr433", "RFXCom 433MHz Transceiver", properties);
            } catch (IOException e) {
                // do nothing, just testing if we can open one
            }
        }
    }
}
