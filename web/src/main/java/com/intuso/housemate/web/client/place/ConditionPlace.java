package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 05/12/12
 * Time: 22:52
 * To change this template use File | Settings | File Templates.
 */
public class ConditionPlace extends RulePlace {

    protected enum Field implements HousematePlace.TokenisableField {
        ConditionNames {
            @Override
            public String getFieldName() {
                return "conditions";
            }
        }
    }

    public static Breadcrumb getBreadcrumb(String ruleName) {
        return new Breadcrumb("Conditions", PlaceName.Condition.getToken()
                + RulePlace.Field.RuleName.getFieldName() + FIELD_VALUE_SEPARATOR + ruleName);
    }

    public static List<Breadcrumb> getBreadcrumbs(String ruleName, List<String> conditionNames) {
        List<Breadcrumb> result = new ArrayList<Breadcrumb>(conditionNames.size());
        List<String> previousConditionNames = new ArrayList<String>(conditionNames.size());
        for(String conditionName : conditionNames) {
            previousConditionNames.add(conditionName);
            result.add(new Breadcrumb(conditionName, PlaceName.Condition.getToken()
                    + RulePlace.Field.RuleName.getFieldName() + FIELD_VALUE_SEPARATOR + ruleName + FIELD_SEPARATOR
                    + Field.ConditionNames.getFieldName() + FIELD_VALUE_SEPARATOR + namesToString(previousConditionNames)));
        }
        return result;
    }

    private List<String> conditionNames;

    public ConditionPlace(String ruleName) {
        super(ruleName);
        breadcrumbList.add(getBreadcrumb(ruleName));
    }

    public ConditionPlace(String ruleName, List<String> conditionNames) {
        super(ruleName);
        this.conditionNames = conditionNames;
        breadcrumbList.add(getBreadcrumb(ruleName));
        breadcrumbList.addAll(getBreadcrumbs(ruleName, conditionNames));
    }

    public List<String> getConditionNames() {
        return conditionNames;
    }

    @Prefix("condition")
    public static class Tokeniser implements PlaceTokenizer<ConditionPlace> {

        @Override
        public ConditionPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.ConditionNames.getFieldName()) != null
                    && fields.get(RulePlace.Field.RuleName.getFieldName()) != null)
                return new ConditionPlace(
                        fields.get(RulePlace.Field.RuleName.getFieldName()),
                        stringToNames(fields.get(Field.ConditionNames.getFieldName())));
            else if(fields.get(RulePlace.Field.RuleName.getFieldName()) != null)
                return new ConditionPlace(
                        fields.get(RulePlace.Field.RuleName.getFieldName()));
            else
                return null;
        }

        @Override
        public String getToken(ConditionPlace conditionPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(conditionPlace.getRuleName() != null)
                fields.put(RulePlace.Field.RuleName, conditionPlace.getRuleName());
            if(conditionPlace.getConditionNames() != null)
                fields.put(Field.ConditionNames, namesToString(conditionPlace.getConditionNames()));
            return HousematePlace.getToken(fields);
        }
    }
}
