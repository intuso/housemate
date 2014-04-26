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
public class UsersPlace extends HousematePlace {

    protected enum Field implements TokenisableField {
        Selected {
            @Override
            public String getFieldName() {
                return "selected";
            }
        }
    }

    private Set<String> userIds;

    public UsersPlace() {
        super();
    }

    public UsersPlace(Set<String> userIds) {
        super();
        this.userIds = userIds;
    }

    public Set<String> getUserIds() {
        return userIds;
    }

    @Prefix("users")
    public static class Tokeniser implements PlaceTokenizer<UsersPlace> {

        @Override
        public UsersPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Selected.getFieldName()) != null) {
                Set<String> usernames = Sets.newHashSet(stringToNames(
                        fields.get(Field.Selected.getFieldName())));
                return new UsersPlace(usernames);
            } else
                return new UsersPlace();
        }

        @Override
        public String getToken(UsersPlace usersPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(usersPlace.getUserIds() != null && usersPlace.getUserIds().size() > 0)
                fields.put(Field.Selected, namesToString(usersPlace.getUserIds()));
            return HousematePlace.getToken(fields);
        }
    }

    @Override
    public List<HousemateObject.TreeLoadInfo> createTreeLoadInfos() {
        HousemateObject.TreeLoadInfo listInfo = new HousemateObject.TreeLoadInfo(Root.USERS_ID);
        if(userIds != null)
            for(String userId : userIds)
                listInfo.getChildren().put(userId, new HousemateObject.TreeLoadInfo(userId, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE)));
        HousemateObject.TreeLoadInfo addInfo = new HousemateObject.TreeLoadInfo(Root.ADD_USER_ID, new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE));
        return Lists.newArrayList(listInfo, addInfo);
    }

    @Override
    protected HousemateView getView() {
        return Housemate.INJECTOR.getUsersView();
    }
}
