package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class RulePlace extends HomePlace {

    protected enum Field implements HousematePlace.TokenisableField {
        RuleName {
            @Override
            public String getFieldName() {
                return "rule";
            }
        }
    }

    public static Breadcrumb getBreadcrumb() {
        return new Breadcrumb("Rules", PlaceName.Rule.getToken());
    }

    public static Breadcrumb getBreadcrumb(String ruleName) {
        return new Breadcrumb(ruleName, PlaceName.Rule.getToken()
                + Field.RuleName.getFieldName() + FIELD_VALUE_SEPARATOR + ruleName);
    }

    private String ruleName;

    public RulePlace() {
        super();
        breadcrumbList.add(getBreadcrumb());
    }

    public RulePlace(String ruleName) {
        super();
        this.ruleName = ruleName;
        breadcrumbList.add(getBreadcrumb());
        breadcrumbList.add(getBreadcrumb(ruleName));
    }

    public String getRuleName() {
        return ruleName;
    }

    @Prefix("rule")
    public static class Tokeniser implements PlaceTokenizer<RulePlace> {

        @Override
        public RulePlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.RuleName.getFieldName()) != null)
                return new RulePlace(fields.get(Field.RuleName.getFieldName()));
            else
                return new RulePlace();
        }

        @Override
        public String getToken(RulePlace rulePlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(rulePlace.getRuleName() != null)
                fields.put(Field.RuleName, rulePlace.getRuleName());
            return HousematePlace.getToken(fields);
        }
    }
}
