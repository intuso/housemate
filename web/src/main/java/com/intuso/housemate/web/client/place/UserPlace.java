package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class UserPlace extends HomePlace {

    protected enum Field implements TokenisableField {
        Username {
            @Override
            public String getFieldName() {
                return "username";
            }
        }
    }

    public static Breadcrumb getBreadcrumb() {
        return new Breadcrumb("Users", PlaceName.User.getToken());
    }

    public static Breadcrumb getBreadcrumb(String username) {
        return new Breadcrumb(username, PlaceName.User.getToken()
                + Field.Username.getFieldName() + FIELD_VALUE_SEPARATOR + username);
    }

    private String username;

    public UserPlace() {
        super();
        breadcrumbList.add(getBreadcrumb());
    }

    public UserPlace(String username) {
        super();
        this.username = username;
        breadcrumbList.add(getBreadcrumb());
        breadcrumbList.add(getBreadcrumb(username));
    }

    public String getUsername() {
        return username;
    }

    @Prefix("user")
    public static class Tokeniser implements PlaceTokenizer<UserPlace> {

        @Override
        public UserPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Username.getFieldName()) != null)
                return new UserPlace(fields.get(Field.Username.getFieldName()));
            else
                return new UserPlace();
        }

        @Override
        public String getToken(UserPlace userPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(userPlace.getUsername() != null)
                fields.put(Field.Username, userPlace.getUsername());
            return HousematePlace.getToken(fields);
        }
    }
}
