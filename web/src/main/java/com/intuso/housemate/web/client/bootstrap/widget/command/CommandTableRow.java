package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.google.gwt.user.client.ui.Label;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCell;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class CommandTableRow extends TableRow {

    public CommandTableRow(GWTProxyCommand command) {
        addNameCell(command.getId(), command.getDescription());
        addPerformCell(command);
    }

    private void addNameCell(String name, String description) {
        TableCell nameCell = new TableCell();
        nameCell.add(new Label(name));
        nameCell.setTitle(description);
        add(nameCell);
    }

    private void addPerformCell(GWTProxyCommand command) {
        TableCell commandCell = new TableCell();
        commandCell.add(new PerformButton(command, "Perform"));
        add(commandCell);
    }
}
