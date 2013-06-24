package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.DevicePlace;
import com.intuso.housemate.web.client.ui.view.DeviceView;

/**
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
