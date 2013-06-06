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
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.argument.ArgumentList;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 * Created by Ravn Systems
 * User: janvh
 * Date: 23/07/11
 * Time: 11:48
 */
public class CommandPopup extends Composite {

    interface CommandPopupUiBinder extends UiBinder<HTMLPanel, CommandPopup> {
    }

    private static CommandPopupUiBinder ourUiBinder = GWT.create(CommandPopupUiBinder.class);

    @UiField
    Modal modal;
    @UiField
    ArgumentList argumentList;

    private GWTProxyCommand command;
    private TypeInstances values;

    public CommandPopup(GWTProxyCommand command) {

        this.command = command;

        initWidget(ourUiBinder.createAndBindUi(this));

        modal.setTitle(command.getDescription());

        values = new TypeInstances();
        argumentList.setTypeValues(values);
        argumentList.setList(command.getArguments());
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