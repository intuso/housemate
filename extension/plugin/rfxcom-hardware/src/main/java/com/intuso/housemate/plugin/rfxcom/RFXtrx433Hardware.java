package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.annotations.Property;
import com.intuso.housemate.client.v1_0.real.api.annotations.TypeInfo;
import com.intuso.housemate.client.v1_0.real.api.driver.HardwareDriver;
import com.intuso.housemate.client.v1_0.real.api.object.RealDevice;
import com.intuso.housemate.plugin.rfxcom.lighting1.Lighting1ARCHandler;
import com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2ACHandler;
import com.intuso.housemate.plugin.rfxcom.lighting2.Lighting2HomeEasyEUHandler;
import com.intuso.housemate.plugin.rfxcom.temperaturesensor.*;
import com.rfxcom.rfxtrx.RFXtrx;
import org.slf4j.Logger;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by tomc on 16/03/15.
 */
@TypeInfo(id = "rfxtrx433", name = "RFXtr433", description = "RFXCom 433MHz Transceiver")
public class RFXtrx433Hardware implements HardwareDriver {

    private final RFXtrx rfxtrx;

    private final List<Handler> handlers = Lists.newArrayList();

    private String pattern = ".*ttyUSB.*";
    private boolean listen = true;
    private boolean isRunning = false;

    @Inject
    public RFXtrx433Hardware(RealDevice.Container deviceContainer,
                             @Assisted Logger logger,
                             @Assisted Callback callback) {

        // setup the connection to the USB device
        rfxtrx = new RFXtrx(logger, Lists.<Pattern>newArrayList());

        // create all the handlers
        handlers.add(new Lighting1ARCHandler(logger, rfxtrx, deviceContainer));
        handlers.add(new Lighting2ACHandler(logger, rfxtrx, deviceContainer));
        handlers.add(new Lighting2HomeEasyEUHandler(logger, rfxtrx, deviceContainer));
        handlers.add(new Temperature1Handler(logger, rfxtrx, deviceContainer));
        handlers.add(new Temperature2Handler(logger, rfxtrx, deviceContainer));
        handlers.add(new Temperature3Handler(logger, rfxtrx, deviceContainer));
        handlers.add(new Temperature4Handler(logger, rfxtrx, deviceContainer));
        handlers.add(new Temperature5Handler(logger, rfxtrx, deviceContainer));
    }

    @Property(value = "string")
    @TypeInfo(id = "serial-pattern", name = "Serial port pattern", description = "Regex matching acceptable serial port names")
    public void setPattern(String pattern) {
        this.pattern = pattern;
        rfxtrx.setPatterns(Lists.newArrayList(Pattern.compile(pattern)));
    }

    public String getPattern() {
        return pattern;
    }

    @Property(value = "boolean")
    @TypeInfo(id = "listen", name = "Listen for new devices", description = "Create a new device when a command is received for it")
    public void setListen(boolean listen) {
        this.listen = listen;
        if(isRunning)
            for(Handler handler : handlers)
                handler.listen(listen);
    }

    @Override
    public void start() {
        rfxtrx.openPortSafe();
        for(Handler handler : handlers)
            handler.listen(listen);
    }

    @Override
    public void stop() {
        rfxtrx.closePort();
        for(Handler handler : handlers)
            handler.listen(false);
    }
}
