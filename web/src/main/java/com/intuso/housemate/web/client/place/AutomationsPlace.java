package com.intuso.housemate.web.client.place;

import com.google.common.collect.Sets;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 */
public class AutomationsPlace extends HousematePlace {

    protected enum Field implements HousematePlace.TokenisableField {
        Selected {
            @Override
            public String getFieldName() {
                return "selected";
            }
        }
    }

    private Set<String> automationNames;

    public AutomationsPlace() {
        super();
    }

    public AutomationsPlace(Set<String> automationNames) {
        super();
        this.automationNames = automationNames;
    }

    public Set<String> getAutomationNames() {
        return automationNames;
    }

    @Prefix("automations")
    public static class Tokeniser implements PlaceTokenizer<AutomationsPlace> {

        @Override
        public AutomationsPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Selected.getFieldName()) != null) {
                Set<String> automationNames = Sets.newHashSet(stringToNames(
                        fields.get(Field.Selected.getFieldName())));
                return new AutomationsPlace(automationNames);
            } else
                return new AutomationsPlace();
        }

        @Override
        public String getToken(AutomationsPlace automationPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(automationPlace.getAutomationNames() != null && automationPlace.getAutomationNames().size() > 0)
                fields.put(Field.Selected, namesToString(automationPlace.getAutomationNames()));
            return HousematePlace.getToken(fields);
        }
    }
}
