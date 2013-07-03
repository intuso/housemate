package com.intuso.housemate.web.client.bootstrap.widget.type;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.api.object.type.ChoiceTypeWrappable;
import com.intuso.housemate.api.object.type.CompoundTypeWrappable;
import com.intuso.housemate.api.object.type.ObjectTypeWrappable;
import com.intuso.housemate.api.object.type.RegexTypeWrappable;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCell;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.event.TypeInputEditedEvent;
import com.intuso.housemate.web.client.handler.HasTypeInputEditedHandlers;
import com.intuso.housemate.web.client.handler.TypeInputEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class TypeInputTableRow
        extends TableRow
        implements HasTypeInputEditedHandlers, TypeInputEditedHandler {

    private final String id;
    private final TypeInstanceMap instances;

    public TypeInputTableRow(String id, String name, String description, GWTProxyType type, TypeInstanceMap instances) {
        this.id = id;
        this.instances = instances;
        addNameCell(name, description);
        addValueCell(type);
    }

    private void addNameCell(String name, String description) {
        TableCell nameCell = new TableCell();
        nameCell.add(new Label(name));
        nameCell.setTitle(description);
        add(nameCell);
    }

    private void addValueCell(GWTProxyType type) {
        TableCell valueCell = new TableCell();
        TypeInput input = getInput(type);
        if(input != null) {
            input.addTypeInputEditedHandler(this);
            if(instances != null)
                input.setTypeInstances(instances.get(id));
            valueCell.add(input);
        }
        add(valueCell);
    }

    @Override
    public HandlerRegistration addTypeInputEditedHandler(TypeInputEditedHandler handler) {
        return addHandler(handler, TypeInputEditedEvent.TYPE);
    }

    @Override
    public void onTypeInputEdited(TypeInputEditedEvent event) {
        if(instances != null)
            instances.put(id, event.getTypeInstances());
        fireEvent(event);
    }

    public static TypeInput getInput(GWTProxyType type) {
        if(type == null)
            return null;
        TypeWrappable typeWrappable = type.getData();
        if(typeWrappable instanceof SimpleTypeWrappable) {
            if(((SimpleTypeWrappable)typeWrappable).getType() == SimpleTypeWrappable.Type.Boolean)
                return new CheckBoxInput();
            else
                return new TextInput(typeWrappable);
        } else if(typeWrappable instanceof ChoiceTypeWrappable) {
            if(typeWrappable.getMinValues() == 1 && typeWrappable.getMaxValues() == 1)
                return new SingleSelectInput(type);
            else
                return new MultiSelectInput(type);
        } else if(typeWrappable instanceof RegexTypeWrappable)
            return new TextInput(typeWrappable);
        else if(typeWrappable instanceof ObjectTypeWrappable)
            return new ObjectBrowserInput((ObjectTypeWrappable) typeWrappable);
        else if(typeWrappable instanceof CompoundTypeWrappable)
            return new CompoundInput((CompoundTypeWrappable) typeWrappable, type);
        return null;
    }
}
