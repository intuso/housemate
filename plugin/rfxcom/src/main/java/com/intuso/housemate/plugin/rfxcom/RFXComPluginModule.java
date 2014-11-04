package com.intuso.housemate.plugin.rfxcom;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.plugin.api.Devices;
import com.intuso.housemate.plugin.api.PluginModule;
import com.intuso.housemate.plugin.api.TypeInfo;
import com.intuso.utilities.log.Log;
import com.rfxcom.rfxtrx.RFXtrx;
import com.rfxcom.rfxtrx.homeeasy.HomeEasy;

import java.util.List;
import java.util.regex.Pattern;

@TypeInfo(id = "com.intuso.housemate.plugin.rfxcom", name = "RFXCom plugin", description = "Plugin for devices that work using a RFXCom Transceiver")
@Devices(HomeEasyAppliance.class)
public class RFXComPluginModule extends PluginModule {

    private final static List<Pattern> PATTERNS = Lists.newArrayList(
            Pattern.compile(".*ttyUSB.*")
    );

    @Inject
    public RFXComPluginModule(Log log) {
        super(log);
    }

    @Provides
    @Singleton
    public HomeEasy getHomeEasy(Log log) throws HousemateException {
        log.d("Initialising RFXCom plugin");
        return HomeEasy.forUK(new RFXtrx(log, PATTERNS));
    }
}
