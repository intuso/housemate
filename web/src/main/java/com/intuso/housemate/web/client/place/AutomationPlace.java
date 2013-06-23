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
public class AutomationPlace extends HomePlace {

    protected enum Field implements HousematePlace.TokenisableField {
        AutomationName {
            @Override
            public String getFieldName() {
                return "automation";
            }
        }
    }

    public static Breadcrumb getBreadcrumb() {
        return new Breadcrumb("Automations", PlaceName.Automation.getToken());
    }

    public static Breadcrumb getBreadcrumb(String automationName) {
        return new Breadcrumb(automationName, PlaceName.Automation.getToken()
                + Field.AutomationName.getFieldName() + FIELD_VALUE_SEPARATOR + automationName);
    }

    private String automationName;

    public AutomationPlace() {
        super();
        breadcrumbList.add(getBreadcrumb());
    }

    public AutomationPlace(String automationName) {
        super();
        this.automationName = automationName;
        breadcrumbList.add(getBreadcrumb());
        breadcrumbList.add(getBreadcrumb(automationName));
    }

    public String getAutomationName() {
        return automationName;
    }

    @Prefix("automation")
    public static class Tokeniser implements PlaceTokenizer<AutomationPlace> {

        @Override
        public AutomationPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.AutomationName.getFieldName()) != null)
                return new AutomationPlace(fields.get(Field.AutomationName.getFieldName()));
            else
                return new AutomationPlace();
        }

        @Override
        public String getToken(AutomationPlace automationPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(automationPlace.getAutomationName() != null)
                fields.put(Field.AutomationName, automationPlace.getAutomationName());
            return HousematePlace.getToken(fields);
        }
    }
}
