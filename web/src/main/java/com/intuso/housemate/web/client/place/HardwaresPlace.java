package com.intuso.housemate.web.client.place;

import com.google.common.collect.Sets;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.ui.view.HousemateView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class HardwaresPlace extends HousematePlace {

    protected enum Field implements TokenisableField {
        Selected {
            @Override
            public String getFieldName() {
                return "selected";
            }
        }
    }

    private Set<String> hardwareIds;

    public HardwaresPlace() {
        super();
    }

    public HardwaresPlace(Set<String> hardwareIds) {
        super();
        this.hardwareIds = hardwareIds;
    }

    public Set<String> getHardwareIds() {
        return hardwareIds;
    }

    @Prefix("hardwares")
    public static class Tokeniser implements PlaceTokenizer<HardwaresPlace> {

        @Override
        public HardwaresPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Selected.getFieldName()) != null) {
                Set<String> hardwareNames = Sets.newHashSet(stringToNames(
                        fields.get(Field.Selected.getFieldName())));
                return new HardwaresPlace(hardwareNames);
            } else
                return new HardwaresPlace();
        }

        @Override
        public String getToken(HardwaresPlace hardwaresPlace) {
            Map<TokenisableField, String> fields = new HashMap<>();
            if(hardwaresPlace.getHardwareIds() != null && hardwaresPlace.getHardwareIds().size() > 0)
                fields.put(Field.Selected, namesToString(hardwaresPlace.getHardwareIds()));
            return HousematePlace.getToken(fields);
        }
    }

    @Override
    public HousemateView getView() {
        return Housemate.INJECTOR.getHardwaresView();
    }
}
