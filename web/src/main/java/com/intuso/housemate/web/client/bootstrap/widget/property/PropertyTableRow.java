package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.type.TypeInput;
import com.intuso.housemate.web.client.bootstrap.widget.type.TypeInputTableRow;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCell;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyProperty;

/**
 */
public class PropertyTableRow extends TableRow implements TypeInputEditedHandler {

    private final GWTProxyCommand setCommand;
    private final TypeInstanceMap values = new TypeInstanceMap();

    public PropertyTableRow(GWTProxyProperty property) {
        this.setCommand = property.getSetCommand();
        values.put(Property.VALUE_ID, property.getTypeInstances());
        addNameCell(property.getName(), property.getDescription());
        addValueCell(property);
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
        TypeInput input = TypeInputTableRow.getInput(value.getType());
        if(input != null) {
            input.setTypeInstances(value.getTypeInstances());
            input.addTypeInputEditedHandler(this);
            valueCell.add(input);
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
    public void onTypeInputEdited(TypeInputEditedEvent event) {
        values.put(Property.VALUE_ID, event.getTypeInstances());
    }
}
