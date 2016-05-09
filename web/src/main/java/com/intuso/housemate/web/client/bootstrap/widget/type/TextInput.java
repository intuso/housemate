package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.event.UserInputEvent;
import com.intuso.housemate.web.client.handler.UserInputHandler;
import org.gwtbootstrap3.client.ui.TextBox;

/**
 */
public class TextInput extends TextBox implements TypeInput, ChangeHandler {

    private final Type.Instances typeInstances;
    private final Validator validator;

    public TextInput(TypeData typeData, final Type.Instances typeInstances) {

        setWidth("100%");

        this.typeInstances = typeInstances;

        if(typeInstances.getElements().size() == 0)
            typeInstances.getElements().add(new Type.Instance());

        if(typeInstances.getElements().get(0) == null) {
            typeInstances.getElements().remove(0);
            typeInstances.getElements().add(0, new Type.Instance());
        }

        if(typeInstances.getFirstValue() == null)
            setText("");
        else
            setText(typeInstances.getFirstValue());

        validator = getValidator(typeData);
        addChangeHandler(this);
    }

    @Override
    public void onChange(ChangeEvent event) {
        if(validator.isValid(getText())) {
            typeInstances.getElements().get(0).setValue(getText());
        } else {
            // TODO show invalid input
        }
        fireEvent(new UserInputEvent());
    }

    @Override
    public Type.Instances getTypeInstances() {
        return typeInstances;
    }

    @Override
    public HandlerRegistration addUserInputHandler(UserInputHandler handler) {
        return addHandler(handler, UserInputEvent.TYPE);
    }

    private Validator getValidator(TypeData typeData) {
        if(typeData instanceof Type.SimpleData) {
            switch (((Type.SimpleData)typeData).getType()) {
                case String:
                    return new StringValidator();
                case Integer:
                    return new IntegerValidator();
                case Double:
                    return new DoubleValidator();
                case Boolean:
                    return new BooleanValidator();
            }
        } else if(typeData instanceof Type.RegexData) {
            Type.RegexData regexTypeData = (Type.RegexData)typeData;
            return new RegexValidator(regexTypeData.getRegexPattern(), regexTypeData.getDescription());
        }
        return new StringValidator();
    }

    private interface Validator {
        public boolean isValid(String input);
        public String getInputFormatDescription();
    }

    private class StringValidator implements Validator {
        @Override
        public boolean isValid(String input) {
            return true;
        }

        @Override
        public String getInputFormatDescription() {
            return "Any text you like";
        }
    }

    private class IntegerValidator implements Validator {
        @Override
        public boolean isValid(String input) {
            try {
                Integer.parseInt(input);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }

        @Override
        public String getInputFormatDescription() {
            return "Any whole number";
        }
    }

    private class DoubleValidator implements Validator {
        @Override
        public boolean isValid(String input) {
            try {
                Double.parseDouble(input);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }

        @Override
        public String getInputFormatDescription() {
            return "Any number";
        }
    }

    private class BooleanValidator implements Validator {
        @Override
        public boolean isValid(String input) {
            return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
        }

        @Override
        public String getInputFormatDescription() {
            return "\"True\" or \"False\"";
        }
    }

    private class RegexValidator implements Validator {

        private RegExp pattern;
        private String formatDescription;

        public RegexValidator(String regexPattern, String formatDescription) {
            pattern = RegExp.compile(regexPattern);
            this.formatDescription = formatDescription;
        }

        @Override
        public boolean isValid(String input) {
            return pattern.test(input);
        }

        @Override
        public String getInputFormatDescription() {
            return formatDescription;
        }
    }
}
