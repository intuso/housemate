package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class ConditionPlace extends AutomationPlace {

    protected enum Field implements HousematePlace.TokenisableField {
        ConditionNames {
            @Override
            public String getFieldName() {
                return "conditions";
            }
        },
        Depth {
            @Override
            public String getFieldName() {
                return "depth";
            }
        }
    }

    public static Breadcrumb getBreadcrumb(String automationName) {
        return new Breadcrumb("Conditions", PlaceName.Condition.getToken()
                + AutomationPlace.Field.AutomationName.getFieldName() + FIELD_VALUE_SEPARATOR + automationName);
    }

    public static List<Breadcrumb> getBreadcrumbs(String automationName, List<String> conditionNames) {
        List<Breadcrumb> result = new ArrayList<Breadcrumb>(conditionNames.size());
        List<String> previousConditionNames = new ArrayList<String>(conditionNames.size());
        for(int depth = 0; depth < conditionNames.size(); depth++) {
            String conditionName = conditionNames.get(depth);
            previousConditionNames.add(conditionName);
            result.add(new Breadcrumb(conditionName, PlaceName.Condition.getToken()
                    + AutomationPlace.Field.AutomationName.getFieldName() + FIELD_VALUE_SEPARATOR + automationName + FIELD_SEPARATOR
                    + Field.Depth.getFieldName() + FIELD_VALUE_SEPARATOR + depth + FIELD_SEPARATOR
                    + Field.ConditionNames.getFieldName() + FIELD_VALUE_SEPARATOR + namesToString(previousConditionNames)));
        }
        return result;
    }

    private int depth = 0;
    private List<String> conditionNames;

    public ConditionPlace(String automationName) {
        super(automationName);
        breadcrumbList.add(getBreadcrumb(automationName));
    }

    public ConditionPlace(String automationName, int depth, List<String> conditionNames) {
        super(automationName);
        this.depth = depth;
        this.conditionNames = conditionNames;
        breadcrumbList.add(getBreadcrumb(automationName));
        breadcrumbList.addAll(getBreadcrumbs(automationName, conditionNames));
    }

    public int getDepth() {
        return depth;
    }

    public List<String> getConditionNames() {
        return conditionNames;
    }

    @Prefix("condition")
    public static class Tokeniser implements PlaceTokenizer<ConditionPlace> {

        @Override
        public ConditionPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.Depth.getFieldName()) != null
                    && fields.get(Field.ConditionNames.getFieldName()) != null
                    && fields.get(AutomationPlace.Field.AutomationName.getFieldName()) != null)
                return new ConditionPlace(
                        fields.get(AutomationPlace.Field.AutomationName.getFieldName()),
                        Integer.parseInt(fields.get(Field.Depth.getFieldName())),
                        stringToNames(fields.get(Field.ConditionNames.getFieldName())));
            else if(fields.get(AutomationPlace.Field.AutomationName.getFieldName()) != null)
                return new ConditionPlace(
                        fields.get(AutomationPlace.Field.AutomationName.getFieldName()));
            else
                return null;
        }

        @Override
        public String getToken(ConditionPlace conditionPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(conditionPlace.getAutomationName() != null)
                fields.put(AutomationPlace.Field.AutomationName, conditionPlace.getAutomationName());
            fields.put(Field.Depth, Integer.toString(conditionPlace.getDepth()));
            if(conditionPlace.getConditionNames() != null) {
                fields.put(Field.ConditionNames, namesToString(conditionPlace.getConditionNames()));
            }
            return HousematePlace.getToken(fields);
        }
    }
}
