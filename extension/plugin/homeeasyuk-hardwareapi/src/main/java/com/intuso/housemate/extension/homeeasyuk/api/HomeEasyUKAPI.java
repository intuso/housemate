package com.intuso.housemate.extension.homeeasyuk.api;

import com.intuso.housemate.client.v1_0.api.annotation.Command;
import com.intuso.housemate.client.v1_0.api.annotation.ConnectedDevice;
import com.intuso.housemate.client.v1_0.api.annotation.Id;
import com.intuso.housemate.client.v1_0.api.annotation.IdFormatter;
import com.intuso.housemate.client.v1_0.api.feature.PowerControl;

import java.util.Map;

/**
 * Created by tomc on 08/12/16.
 */
public interface HomeEasyUKAPI {

    String ID_PREFIX = "homeeasyuk-appliance-";

    @Command
    @Id("init-homeeasy-appliance")
    void initAppliance(@Id("house-id") int houseId, @Id("unit-code") byte unitCode);

    @Command
    @Id("uninit-homeeasy-appliance")
    void uninitAppliance(@Id("house-id") int houseId, @Id("unit-code") byte unitCode);

    @ConnectedDevice(IdFormatterImpl.class)
    Appliance getAppliance(@Id("house-id") int houseId, @Id("unit-code") byte unitCode);

    interface Appliance extends PowerControl {}

    class IdFormatterImpl implements IdFormatter {

        @Override
        public String format(Map<String, Object> args) {
            return ID_PREFIX + args.get("house-id") + "-" + args.get("unit-code");
        }
    }
}
