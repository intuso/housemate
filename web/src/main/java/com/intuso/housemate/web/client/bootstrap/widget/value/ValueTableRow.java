package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCell;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 */
public class ValueTableRow extends TableRow {

    public ValueTableRow(GWTProxyValue value) {
        addNameCell(value.getName(), value.getDescription());
        addValueCell(value);
    }

    private void addNameCell(String name, String description) {
        TableCell nameCell = new TableCell();
        nameCell.add(new Label(name));
        nameCell.setTitle(description);
        add(nameCell);
    }

    private void addValueCell(GWTProxyValue value) {
        TableCell valueCell = new TableCell();
        valueCell.add(Value.getWidget(value));
        add(valueCell);
    }
}
