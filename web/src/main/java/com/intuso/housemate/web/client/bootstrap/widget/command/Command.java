package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.object.v1_0.api.TypeInstances;
import com.intuso.housemate.web.client.bootstrap.widget.type.ParameterInputList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;

/**
 */
public class Command extends Composite implements com.intuso.housemate.object.v1_0.api.Command.PerformListener<GWTProxyCommand> {

    interface CommandUiBinder extends UiBinder<FlowPanel, Command> {}

    private static CommandUiBinder ourUiBinder = GWT.create(CommandUiBinder.class);

    @UiField
    Alert errorAlert;
    @UiField
    ParameterInputList parameterList;
    @UiField
    Button performButton;

    private GWTProxyCommand command;
    private com.intuso.housemate.object.v1_0.api.Command.PerformListener<GWTProxyCommand> listener;
    private TypeInstances values;

    public Command(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command) {
        this(types, command, null);
    }

    public Command(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, com.intuso.housemate.object.v1_0.api.Command.PerformListener<GWTProxyCommand> listener) {

        this.command = command;
        this.listener = listener;

        values = new TypeInstances();

        initWidget(ourUiBinder.createAndBindUi(this));

        parameterList.setTypes(types);
        parameterList.setTypeInstances(values);
        parameterList.setList(command.getParameters());
    }

    @UiFactory
    protected Command createDialog() {
        return this;
    }

    @UiHandler("performButton")
    protected void onPerform(ClickEvent event) {
        performButton.setEnabled(false);
        command.perform(values.getElements().get(0).getChildValues(), this);
    }

    @Override
    public void commandStarted(GWTProxyCommand command) {
        errorAlert.setVisible(false);
        if(listener != null)
            listener.commandStarted(command);
    }

    @Override
    public void commandFinished(GWTProxyCommand command) {
        if(listener != null)
            listener.commandFinished(command);
    }

    @Override
    public void commandFailed(GWTProxyCommand command, String error) {
        errorAlert.setText(error);
        errorAlert.setVisible(true);
        performButton.setEnabled(true);
        if(listener != null)
            listener.commandFailed(command, error);
    }
}