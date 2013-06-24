package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class SatisfiedTaskPlace extends AutomationPlace {

    protected enum Field implements TokenisableField {
        TaskName {
            @Override
            public String getFieldName() {
                return "task";
            }
        }
    }

    public static Breadcrumb getBreadcrumb(String automationName) {
        return new Breadcrumb("Satisfied Tasks", PlaceName.SatisfiedTask.getToken()
                + AutomationPlace.Field.AutomationName.getFieldName() + FIELD_VALUE_SEPARATOR + automationName);
    }

    public static Breadcrumb getBreadcrumb(String automationName, String taskName) {
        return new Breadcrumb(taskName, PlaceName.SatisfiedTask.getToken()
                + AutomationPlace.Field.AutomationName.getFieldName() + FIELD_VALUE_SEPARATOR + automationName + FIELD_SEPARATOR
                + Field.TaskName.getFieldName() + FIELD_VALUE_SEPARATOR + taskName);
    }

    private String taskName;

    public SatisfiedTaskPlace(String automationName) {
        super(automationName);
        breadcrumbList.add(getBreadcrumb(automationName));
    }

    public SatisfiedTaskPlace(String automationName, String taskName) {
        super(automationName);
        this.taskName = taskName;
        breadcrumbList.add(getBreadcrumb(automationName));
        breadcrumbList.add(getBreadcrumb(automationName, taskName));
    }

    public String getTaskName() {
        return taskName;
    }

    @Prefix("satisfiedtask")
    public static class Tokeniser implements PlaceTokenizer<SatisfiedTaskPlace> {

        @Override
        public SatisfiedTaskPlace getPlace(String token) {
            Map<String, String> fields = HousematePlace.getFields(token);
            if(fields.get(Field.TaskName.getFieldName()) != null
                    && fields.get(AutomationPlace.Field.AutomationName.getFieldName()) != null)
                return new SatisfiedTaskPlace(
                        fields.get(AutomationPlace.Field.AutomationName.getFieldName()),
                        fields.get(Field.TaskName.getFieldName()));
            else if(fields.get(AutomationPlace.Field.AutomationName.getFieldName()) != null)
                return new SatisfiedTaskPlace(
                        fields.get(AutomationPlace.Field.AutomationName.getFieldName()));
            else
                return null;
        }

        @Override
        public String getToken(SatisfiedTaskPlace satisfiedTaskPlace) {
            Map<TokenisableField, String> fields = new HashMap<TokenisableField, String>();
            if(satisfiedTaskPlace.getAutomationName() != null)
                fields.put(AutomationPlace.Field.AutomationName, satisfiedTaskPlace.getAutomationName());
            if(satisfiedTaskPlace.getTaskName() != null)
                fields.put(Field.TaskName, satisfiedTaskPlace.getTaskName());
            return HousematePlace.getToken(fields);
        }
    }
}
