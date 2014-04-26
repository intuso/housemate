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
public class AutomationsPlace extends HousematePlace {

    protected enum Field implements HousematePlace.TokenisableField {
        Selected {
            @Override
            public String getFieldName() {
                return "selected";
            }
        }
    }

    private Set<String> automationIds;

    public AutomationsPlace() {
        super();
    }

    public AutomationsPlace(Set<String> automationIds) {
        super();
        this.automationIds = automationIds;
    }

    public Set<String> getAutomationIds() {
        return automationIds;
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
            if(automationPlace.getAutomationIds() != null && automationPlace.getAutomationIds().size() > 0)
                fields.put(Field.Selected, namesToString(automationPlace.getAutomationIds()));
            return HousematePlace.getToken(fields);
        }
    }

    @Override
    public List<HousemateObject.TreeLoadInfo> createTreeLoadInfos() {
        HousemateObject.TreeLoadInfo listInfo = new HousemateObject.TreeLoadInfo(Root.AUTOMATIONS_ID);
        if(automationIds != null)
            for(String automationId : automationIds)
                listInfo.getChildren().put(automationId, new HousemateObject.TreeLoadInfo(automationId, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        HousemateObject.TreeLoadInfo addInfo = new HousemateObject.TreeLoadInfo(Root.ADD_AUTOMATION_ID, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE));
        return Lists.newArrayList(listInfo, addInfo);
    }

    @Override
    protected HousemateView getView() {
        return Housemate.INJECTOR.getAutomationsView();
    }
}
