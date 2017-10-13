package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class CommandView extends View {

    private ValueView enabledValueView;
    private ListView<ParameterView> parametersView;

    public CommandView() {}

    public CommandView(Mode mode) {
        super(mode);
    }

    public CommandView(ValueView enabledValueView, ListView<ParameterView> parametersView) {
        super(Mode.SELECTION);
        this.enabledValueView = enabledValueView;
        this.parametersView = parametersView;
    }

    public ValueView getEnabledValueView() {
        return enabledValueView;
    }

    public CommandView setEnabledValueView(ValueView enabledValueView) {
        this.enabledValueView = enabledValueView;
        return this;
    }

    public ListView<ParameterView> getParametersView() {
        return parametersView;
    }

    public CommandView setParametersView(ListView<ParameterView> parametersView) {
        this.parametersView = parametersView;
        return this;
    }
}
