package com.intuso.housemate.web.client.bootstrap.widget.argument;

import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.core.object.type.MultiChoiceTypeWrappable;
import com.intuso.housemate.core.object.type.ObjectTypeWrappable;
import com.intuso.housemate.core.object.type.RegexTypeWrappable;
import com.intuso.housemate.core.object.type.SimpleTypeWrappable;
import com.intuso.housemate.core.object.type.SingleChoiceTypeWrappable;
import com.intuso.housemate.core.object.type.TypeWrappable;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCell;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.event.ArgumentEditedEvent;
import com.intuso.housemate.web.client.handler.ArgumentEditedHandler;
import com.intuso.housemate.web.client.object.GWTProxyArgument;
import com.intuso.housemate.web.client.object.GWTProxyType;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class ArgumentTableRow extends TableRow implements ArgumentEditedHandler {

    private String argumentId;
    private Map<String, String> valuesMap;

    public ArgumentTableRow(GWTProxyArgument argument, Map<String, String> valuesMap) {
        this.argumentId = argument.getId();
        this.valuesMap = valuesMap;
        addNameCell(argument.getId(), argument.getDescription());
        addValueCell(argument);
    }

    private void addNameCell(String name, String description) {
        TableCell nameCell = new TableCell();
        nameCell.add(new Label(name));
        nameCell.setTitle(description);
        add(nameCell);
    }

    private void addValueCell(GWTProxyArgument argument) {
        TableCell valueCell = new TableCell();
        ArgumentInput argumentInput = getArgumentInput(argument.getType());
        if(argumentInput != null) {
            argumentInput.addArgumentEditedHandler(this);
            valueCell.add(argumentInput);
        }
        add(valueCell);
    }

    @Override
    public void onArgumentEdited(ArgumentEditedEvent event) {
        valuesMap.put(argumentId, event.getNewValue());
    }

    public static ArgumentInput getArgumentInput(GWTProxyType type) {
        TypeWrappable typeWrappable = type.getWrappable();
        if(typeWrappable instanceof SimpleTypeWrappable) {
            if(((SimpleTypeWrappable)typeWrappable).getType() == SimpleTypeWrappable.Type.Boolean)
                return new CheckBoxArgumentInput();
            else
                return new TextArgumentInput(typeWrappable);
        } else if(typeWrappable instanceof SingleChoiceTypeWrappable)
            return new SingleSelectArgumentInput(type);
        else if(typeWrappable instanceof MultiChoiceTypeWrappable)
            return new MultiSelectArgumentInput(type);
        else if(typeWrappable instanceof RegexTypeWrappable)
            return new TextArgumentInput(typeWrappable);
        else if(typeWrappable instanceof ObjectTypeWrappable)
            return new ObjectBrowserInput((ObjectTypeWrappable) typeWrappable);
        return null;
    }
}
