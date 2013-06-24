package com.intuso.housemate.plugin.rfxcom;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.plugin.api.RealDeviceFactory;
import com.rfxcom.rfxtrx.util.HomeEasy;

/**
 */
public class HomeEasyApplianceFactory implements RealDeviceFactory<HomeEasyAppliance> {

    private final HomeEasy homeEasy;

    public HomeEasyApplianceFactory(HomeEasy homeEasy) {
        this.homeEasy = homeEasy;
    }

    @Override
    public String getTypeId() {
        return "home-easy";
    }

    @Override
    public String getTypeName() {
        return "Home Easy";
    }

    @Override
    public String getTypeDescription() {
        return "Remote Home Easy switch";
    }

    @Override
    public HomeEasyAppliance create(RealResources resources, String id, String name, String description) throws HousemateException {
        return new HomeEasyAppliance(resources, homeEasy, id, name, description);
    }
}
