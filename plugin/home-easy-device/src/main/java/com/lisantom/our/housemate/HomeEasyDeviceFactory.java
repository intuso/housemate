package com.lisantom.our.housemate;

import com.intuso.housemate.broker.plugin.RealDeviceFactory;
import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.real.RealResources;
import com.rfxcom.rfxtrx.util.HomeEasy;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 04/03/13
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class HomeEasyDeviceFactory implements RealDeviceFactory<HomeEasyAppliance> {

    private final HomeEasy homeEasy;

    public HomeEasyDeviceFactory(HomeEasy homeEasy) {
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
