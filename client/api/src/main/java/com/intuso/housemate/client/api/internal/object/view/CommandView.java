package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class CommandView extends View {

    private ValueView enabledValue;
    private ListView<ParameterView> parameters;

    public CommandView() {}

    public CommandView(Mode mode) {
        super(mode);
    }

    public CommandView(ValueView enabledValue, ListView<ParameterView> parameters) {
        super(Mode.SELECTION);
        this.enabledValue = enabledValue;
        this.parameters = parameters;
    }

    public ValueView getEnabledValue() {
        return enabledValue;
    }

    public CommandView setEnabledValue(ValueView enabledValue) {
        this.enabledValue = enabledValue;
        return this;
    }

    public ListView<ParameterView> getParameters() {
        return parameters;
    }

    public CommandView setParameters(ListView<ParameterView> parameters) {
        this.parameters = parameters;
        return this;
    }
}
