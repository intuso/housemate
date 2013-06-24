package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.google.gwt.dom.client.Style;
import com.intuso.housemate.web.client.bootstrap.widget.list.TableList;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCol;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableColGroup;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableHead;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 */
public class CommandList extends TableList<GWTProxyCommand> {

    public CommandList() {
        addColGroup();
        addHeader();
    }

    private void addColGroup() {
        TableColGroup colGroup= new TableColGroup();
        TableCol titleCol = new TableCol();
        titleCol.getElement().getStyle().setWidth(100, Style.Unit.PX);
        TableCol valueCol = new TableCol();
        colGroup.add(titleCol);
        colGroup.add(valueCol);
        add(colGroup);
    }

    private void addHeader() {
        TableRow header = new TableRow();
        header.add(new TableHead("Name"));
        header.add(new TableHead("Perform"));
        add(header);
    }

    @Override
    public TableRow createObjectTableRow(GWTProxyCommand command) {
        return new CommandTableRow(command);
    }
}
