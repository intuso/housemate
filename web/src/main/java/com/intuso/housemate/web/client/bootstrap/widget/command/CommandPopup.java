package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.type.ParameterInputList;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 */
public class CommandPopup extends Composite {

    interface CommandPopupUiBinder extends UiBinder<HTMLPanel, CommandPopup> {
    }

    private static CommandPopupUiBinder ourUiBinder = GWT.create(CommandPopupUiBinder.class);

    @UiField
    Modal modal;
    @UiField
    ParameterInputList parameterList;

    private GWTProxyCommand command;
    private TypeInstanceMap values;

    public CommandPopup(GWTProxyCommand command) {

        this.command = command;

        initWidget(ourUiBinder.createAndBindUi(this));

        modal.setTitle(command.getDescription());

        values = new TypeInstanceMap();
        parameterList.setTypeInstances(values);
        parameterList.setList(command.getParameters());
    }

    @UiFactory
    protected CommandPopup createDialog() {
        return this;
    }

    @UiHandler("performButton")
    protected void onPerform(ClickEvent event) {
        Housemate.FACTORY.getEventBus().fireEvent(new PerformCommandEvent(command, values));
        hide();
    }

    public void show() {
        modal.show();
    }

    public void hide() {
        modal.hide();
    }
}