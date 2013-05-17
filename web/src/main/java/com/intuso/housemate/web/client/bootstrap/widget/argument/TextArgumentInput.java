package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.intuso.housemate.core.object.type.RegexTypeWrappable;
import com.intuso.housemate.core.object.type.SimpleTypeWrappable;
import com.intuso.housemate.core.object.type.TypeWrappable;
import com.intuso.housemate.core.object.value.Value;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 18/12/12
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class TextArgumentInput extends TextBox implements ArgumentInput {

    public TextArgumentInput(TypeWrappable typeWrappable) {
        final Validator validator = getValidator(typeWrappable);
        addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if(validator.isValid(getText()))
                    fireEvent(new ArgumentEditedEvent(getText()));
                else {
                    // TODO show invalid input
                }
            }
        });
    }

    @Override
    public void setValue(Value<?, ?> value) {
        setText(value.getValue());
    }

    @Override
    public HandlerRegistration addArgumentEditedHandler(ArgumentEditedHandler handler) {
        return addHandler(handler, ArgumentEditedEvent.TYPE);
    }

    private Validator getValidator(TypeWrappable typeWrappable) {
        if(typeWrappable instanceof SimpleTypeWrappable) {
            switch (((SimpleTypeWrappable)typeWrappable).getType()) {
                case String:
                    return new StringValidator();
                case Integer:
                    return new IntegerValidator();
                case Double:
                    return new DoubleValidator();
                case Boolean:
                    return new BooleanValidator();
            }
        } else if(typeWrappable instanceof RegexTypeWrappable) {
            RegexTypeWrappable regexTypeWrappable = (RegexTypeWrappable)typeWrappable;
            return new RegexValidator(regexTypeWrappable.getRegexPattern(), regexTypeWrappable.getDescription());
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
