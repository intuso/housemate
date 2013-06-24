package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class DevicePlace extends HomePlace {

    protected enum Field implements HousematePlace.TokenisableField {
        DeviceName {
            @Override
            public String getFieldName() {
                return "device";
            }
        }
    }

    public static Breadcrumb getBreadcrumb() {
        return new Breadcrumb("Devices", PlaceName.Device.getToken());
    }

    public static Breadcrumb getBreadcrumb(String deviceName) {
        return new Breadcrumb(deviceName, PlaceName.Device.getToken()
                + Field.DeviceName.getFieldName() + FIELD_VALUE_SEPARATOR + deviceName);
    }

    private String deviceName;

    public DevicePlace() {
        super();
        breadcrumbList.add(getBreadcrumb());
    }

    public DevicePlace(String deviceName) {
        super();
        this.deviceName = deviceName;
        breadcrumbList.add(getBreadcrumb());
        breadcrumbList.add(getBreadcrumb(deviceName));
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Prefix("device")
    public static class Tokeniser implements PlaceTokenizer<DevicePlace> {

        @Override
        public DevicePlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.DeviceName.getFieldName()) != null)
                return new DevicePlace(fields.get(Field.DeviceName.getFieldName()));
            else
                return new DevicePlace();
        }

        @Override
        public String getToken(DevicePlace devicePlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(devicePlace.getDeviceName() != null)
                fields.put(Field.DeviceName, devicePlace.getDeviceName());
            return HousematePlace.getToken(fields);
        }
    }
}
