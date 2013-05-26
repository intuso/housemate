package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.argument.ArgumentInput;
import com.intuso.housemate.web.client.bootstrap.widget.argument.ArgumentTableRow;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCell;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class PropertyTableRow extends TableRow implements ArgumentEditedHandler {

    private GWTProxyCommand setCommand;
    private Map<String, String> values = new HashMap<String, String>();

    public PropertyTableRow(GWTProxyProperty property) {
        addNameCell(property.getName(), property.getDescription());
        addValueCell(property);
        this.setCommand = property.getSetCommand();
        addSetButtonCell();
    }

    private void addNameCell(String name, String description) {
        TableCell nameCell = new TableCell();
        nameCell.add(new Label(name));
        nameCell.setTitle(description);
        add(nameCell);
    }

    private void addValueCell(GWTProxyProperty value) {
        TableCell valueCell = new TableCell();
        ArgumentInput argumentInput = ArgumentTableRow.getArgumentInput(value.getType());
        if(argumentInput != null) {
            argumentInput.setValue(value);
            argumentInput.addArgumentEditedHandler(this);
            valueCell.add(argumentInput);
        }
        add(valueCell);
    }

    public void addSetButtonCell() {
        TableCell setButtonCell = new TableCell();
        Button setButton = new Button("Set");
        setButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Housemate.FACTORY.getEventBus().fireEvent(new PerformCommandEvent(setCommand, values));
            }
        });
        setButtonCell.add(setButton);
        add(setButtonCell);
    }

    @Override
    public void onArgumentEdited(ArgumentEditedEvent event) {
        values.put(Property.VALUE, event.getNewValue());
    }
}
