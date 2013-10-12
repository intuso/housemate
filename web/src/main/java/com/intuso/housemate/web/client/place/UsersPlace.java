package com.intuso.housemate.web.client.place;

import com.google.common.collect.Sets;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
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

    private Set<String> usernames;

    public UsersPlace() {
        super();
    }

    public UsersPlace(Set<String> usernames) {
        super();
        this.usernames = usernames;
    }

    public Set<String> getUsernames() {
        return usernames;
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
            if(usersPlace.getUsernames() != null && usersPlace.getUsernames().size() > 0)
                fields.put(Field.Selected, namesToString(usersPlace.getUsernames()));
            return HousematePlace.getToken(fields);
        }
    }
}
