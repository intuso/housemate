package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
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

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@Id(value = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware implements HardwareDriver {

    private final RFXtrx rfxtrx;
    private final Set<Handler> handlers;
    private final Lighting2Handler.AC lighting2ACHandler;

    private String pattern = ".*ttyUSB.*";

    @Inject
    public RFXtrx433Hardware(Injector injector) {
        Injector ourInjector = injector.createChildInjector(new RFXtrx433Module());
        this.rfxtrx = ourInjector.getInstance(RFXtrx.class);
        this.handlers = ourInjector.getInstance(new Key<Set<Handler>>() {});
        this.lighting2ACHandler = ourInjector.getInstance(Lighting2Handler.AC.class);
    }

    @Property
    @Id(value = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names")
    public void setPattern(String pattern) {
        this.pattern = pattern;
        if(rfxtrx != null)
            rfxtrx.setPatterns(Lists.newArrayList(Pattern.compile(pattern)));
    }

    public String getPattern() {
        return pattern;
    }

    @Property
    @Id(value = "autocreate", name = "Auto Create", description = "Automatically create Housemate devices as connected devices are detected")
    public void setAutoCreate(boolean autoCreate) {
        for(Handler handler : handlers)
            handler.autoCreate(autoCreate);
    }

    @Override
    public void init(Logger logger, HardwareDriver.Callback callback, Iterable<String> connectedDeviceIds) {
        // setup the connection to the USB device
        rfxtrx.openPortSafe();
        for(Handler handler : handlers)
            handler.init(callback);
        id: for(String connectedDeviceId : connectedDeviceIds) {
            for(Handler handler : handlers) {
                if (handler.matches(connectedDeviceId)) {
                    handler.parseId(connectedDeviceId);
                    continue id;
                }
            }
        }
    }

    @Override
    public void uninit() {
        for(Handler handler : handlers)
            handler.uninit();
        rfxtrx.closePort();
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
}
