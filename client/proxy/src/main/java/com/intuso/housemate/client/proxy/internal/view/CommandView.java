package com.intuso.housemate.client.proxy.internal.view;

/**
 * Created by tomc on 19/06/17.
 */
public class CommandView extends View<CommandView> {

    private ValueView enabledView;
    private ListView<ParameterView> parametersView;

    public CommandView() {}

    public CommandView(Mode mode) {
        super(mode);
    }

    public CommandView(ValueView enabledView, ListView<ParameterView> parametersView) {
        super(Mode.SELECTION);
        this.enabledView = enabledView;
        this.parametersView = parametersView;
    }

    public ValueView getEnabledView() {
        return enabledView;
    }

    public CommandView setEnabledView(ValueView enabledView) {
        this.enabledView = enabledView;
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
