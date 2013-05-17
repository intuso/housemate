package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.DevicePlace;
import com.intuso.housemate.web.client.ui.view.DeviceView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class DeviceActivity extends HousemateActivity<DevicePlace, DeviceView> {

    protected DeviceActivity(DevicePlace place) {
        super(place);
    }

    @Override
    public DeviceView getView() {
        return Housemate.FACTORY.getDeviceView();
    }
}
