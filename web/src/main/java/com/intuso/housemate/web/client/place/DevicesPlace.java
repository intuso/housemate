package com.intuso.housemate.web.client.place;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.ui.view.HousemateView;

import java.util.HashMap;
import java.util.List;
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

    private Set<String> deviceIds;

    public DevicesPlace() {
        super();
    }

    public DevicesPlace(Set<String> deviceIds) {
        super();
        this.deviceIds = deviceIds;
    }

    public Set<String> getDeviceIds() {
        return deviceIds;
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
            if(devicesPlace.getDeviceIds() != null && devicesPlace.getDeviceIds().size() > 0)
                fields.put(Field.Selected, namesToString(devicesPlace.getDeviceIds()));
            return HousematePlace.getToken(fields);
        }
    }

    @Override
    public List<HousemateObject.TreeLoadInfo> createTreeLoadInfos() {
        HousemateObject.TreeLoadInfo listInfo = new HousemateObject.TreeLoadInfo(Root.DEVICES_ID);
        if(deviceIds != null)
            for(String deviceId : deviceIds)
                listInfo.getChildren().put(deviceId, new HousemateObject.TreeLoadInfo(deviceId, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        HousemateObject.TreeLoadInfo addInfo = new HousemateObject.TreeLoadInfo(Root.ADD_DEVICE_ID, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE));
        return Lists.newArrayList(listInfo, addInfo);
    }

    @Override
    protected HousemateView getView() {
        return Housemate.INJECTOR.getDevicesView();
    }
}
