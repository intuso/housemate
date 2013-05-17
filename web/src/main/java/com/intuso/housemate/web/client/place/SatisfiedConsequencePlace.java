package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 05/12/12
 * Time: 22:52
 * To change this template use File | Settings | File Templates.
 */
public class SatisfiedConsequencePlace extends RulePlace {

    protected enum Field implements TokenisableField {
        ConsequenceName {
            @Override
            public String getFieldName() {
                return "consequence";
            }
        }
    }

    public static Breadcrumb getBreadcrumb(String ruleName) {
        return new Breadcrumb("Satisfied Consequences", PlaceName.SatisfiedConsequence.getToken()
                + RulePlace.Field.RuleName.getFieldName() + FIELD_VALUE_SEPARATOR + ruleName);
    }

    public static Breadcrumb getBreadcrumb(String ruleName, String consequenceName) {
        return new Breadcrumb(consequenceName, PlaceName.SatisfiedConsequence.getToken()
                + RulePlace.Field.RuleName.getFieldName() + FIELD_VALUE_SEPARATOR + ruleName + FIELD_SEPARATOR
                + Field.ConsequenceName.getFieldName() + FIELD_VALUE_SEPARATOR + consequenceName);
    }

    private String consequenceName;

    public SatisfiedConsequencePlace(String ruleName) {
        super(ruleName);
        breadcrumbList.add(getBreadcrumb(ruleName));
    }

    public SatisfiedConsequencePlace(String ruleName, String consequenceName) {
        super(ruleName);
        this.consequenceName = consequenceName;
        breadcrumbList.add(getBreadcrumb(ruleName));
        breadcrumbList.add(getBreadcrumb(ruleName, consequenceName));
    }

    public String getConsequenceName() {
        return consequenceName;
    }

    @Prefix("satisfiedconsequence")
    public static class Tokeniser implements PlaceTokenizer<SatisfiedConsequencePlace> {

        @Override
        public SatisfiedConsequencePlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.ConsequenceName.getFieldName()) != null
                    && fields.get(RulePlace.Field.RuleName.getFieldName()) != null)
                return new SatisfiedConsequencePlace(
                        fields.get(RulePlace.Field.RuleName.getFieldName()),
                        fields.get(Field.ConsequenceName.getFieldName()));
            else if(fields.get(RulePlace.Field.RuleName.getFieldName()) != null)
                return new SatisfiedConsequencePlace(
                        fields.get(RulePlace.Field.RuleName.getFieldName()));
            else
                return null;
        }

        @Override
        public String getToken(SatisfiedConsequencePlace satisfiedConsequencePlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(satisfiedConsequencePlace.getRuleName() != null)
                fields.put(RulePlace.Field.RuleName, satisfiedConsequencePlace.getRuleName());
            if(satisfiedConsequencePlace.getConsequenceName() != null)
                fields.put(Field.ConsequenceName, satisfiedConsequencePlace.getConsequenceName());
            return HousematePlace.getToken(fields);
        }
    }
}
