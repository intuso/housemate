package com.intuso.housemate.web.client.place;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.root.ObjectRoot;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.ui.view.HousemateView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class ApplicationsPlace extends HousematePlace {

    protected enum Field implements TokenisableField {
        Selected {
            @Override
            public String getFieldName() {
                return "selected";
            }
        }
    }

    private Set<String> applicationIds;

    public ApplicationsPlace() {
        super();
    }

    public ApplicationsPlace(Set<String> applicationIds) {
        super();
        this.applicationIds = applicationIds;
    }

    public Set<String> getApplicationIds() {
        return applicationIds;
    }

    @Prefix("applications")
    public static class Tokeniser implements PlaceTokenizer<ApplicationsPlace> {

        @Override
        public ApplicationsPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Selected.getFieldName()) != null) {
                Set<String> applicationIds = Sets.newHashSet(stringToNames(
                        fields.get(Field.Selected.getFieldName())));
                return new ApplicationsPlace(applicationIds);
            } else
                return new ApplicationsPlace();
        }

        @Override
        public String getToken(ApplicationsPlace applicationsPlace) {
            Map<TokenisableField, String> fields = new HashMap<>();
            if(applicationsPlace.getApplicationIds() != null && applicationsPlace.getApplicationIds().size() > 0)
                fields.put(Field.Selected, namesToString(applicationsPlace.getApplicationIds()));
            return HousematePlace.getToken(fields);
        }
    }

    @Override
    public List<HousemateObject.TreeLoadInfo> createTreeLoadInfos() {
        HousemateObject.TreeLoadInfo treeLoadInfo = new HousemateObject.TreeLoadInfo(ObjectRoot.APPLICATIONS_ID);
        if(applicationIds != null)
            for(String applicationId : applicationIds)
                treeLoadInfo.getChildren().put(applicationId, new HousemateObject.TreeLoadInfo(applicationId, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        return Lists.newArrayList(treeLoadInfo);
    }

    @Override
    protected HousemateView getView() {
        return Housemate.INJECTOR.getApplicationsView();
    }
}
