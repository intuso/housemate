package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.DevicesPlace;
import com.intuso.housemate.web.client.ui.view.DeviceView;

/**
 */
public class DeviceActivity extends HousemateActivity<DevicesPlace, DeviceView> {

    protected DeviceActivity(DevicesPlace place) {
        super(place);
    }

    @Override
    public DeviceView getView() {
        return Housemate.FACTORY.getDeviceView();
    }
}
