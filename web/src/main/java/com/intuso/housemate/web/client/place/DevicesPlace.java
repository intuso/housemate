package com.intuso.housemate.web.client.place;

import com.google.common.collect.Sets;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class DevicesPlace extends HousematePlace {

    protected enum Field implements HousematePlace.TokenisableField {
        Selected {
            @Override
            public String getFieldName() {
                return "selected";
            }
        }
    }

    private Set<String> deviceNames;

    public DevicesPlace() {
        super();
    }

    public DevicesPlace(Set<String> deviceNames) {
        super();
        this.deviceNames = deviceNames;
    }

    public Set<String> getDeviceNames() {
        return deviceNames;
    }

    @Prefix("devices")
    public static class Tokeniser implements PlaceTokenizer<DevicesPlace> {

        @Override
        public DevicesPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Selected.getFieldName()) != null) {
                Set<String> deviceNames = Sets.newHashSet(stringToNames(
                        fields.get(Field.Selected.getFieldName())));
                return new DevicesPlace(deviceNames);
            } else
                return new DevicesPlace();
        }

        @Override
        public String getToken(DevicesPlace devicesPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(devicesPlace.getDeviceNames() != null && devicesPlace.getDeviceNames().size() > 0)
                fields.put(Field.Selected, namesToString(devicesPlace.getDeviceNames()));
            return HousematePlace.getToken(fields);
        }
    }
}
