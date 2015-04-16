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
public class ServersPlace extends HousematePlace {

    protected enum Field implements TokenisableField {
        Selected {
            @Override
            public String getFieldName() {
                return "selected";
            }
        }
    }

    private Set<String> serverIds;

    public ServersPlace() {
        super();
    }

    public ServersPlace(Set<String> serverIds) {
        super();
        this.serverIds = serverIds;
    }

    public Set<String> getServerIds() {
        return serverIds;
    }

    @Prefix("servers")
    public static class Tokeniser implements PlaceTokenizer<ServersPlace> {

        @Override
        public ServersPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Selected.getFieldName()) != null) {
                Set<String> applicationIds = Sets.newHashSet(stringToNames(
                        fields.get(Field.Selected.getFieldName())));
                return new ServersPlace(applicationIds);
            } else
                return new ServersPlace();
        }

        @Override
        public String getToken(ServersPlace applicationsPlace) {
            Map<TokenisableField, String> fields = new HashMap<>();
            if(applicationsPlace.getServerIds() != null && applicationsPlace.getServerIds().size() > 0)
                fields.put(Field.Selected, namesToString(applicationsPlace.getServerIds()));
            return HousematePlace.getToken(fields);
        }
    }

    @Override
    public HousemateView getView() {
        return Housemate.INJECTOR.getServersView();
    }
}
