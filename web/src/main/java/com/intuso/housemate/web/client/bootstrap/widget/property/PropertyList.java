package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.google.gwt.dom.client.Style;
import com.intuso.housemate.web.client.bootstrap.widget.list.TableList;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCol;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableColGroup;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableHead;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyProperty;

/**
 */
public class PropertyList extends TableList<GWTProxyProperty> {

    public PropertyList() {
        addColGroup();
        addHeader();
    }

    private void addColGroup() {
        TableColGroup colGroup= new TableColGroup();
        TableCol titleCol = new TableCol();
        titleCol.getElement().getStyle().setWidth(100, Style.Unit.PX);
        TableCol valueCol = new TableCol();
        TableCol setButtonCol = new TableCol();
        setButtonCol.getElement().getStyle().setWidth(100, Style.Unit.PX);
        colGroup.add(titleCol);
        colGroup.add(valueCol);
        colGroup.add(setButtonCol);
        add(colGroup);
    }

    private void addHeader() {
        TableRow header = new TableRow();
        header.add(new TableHead("Name"));
        header.add(new TableHead("Value"));
        header.add(new TableHead("Set"));
        add(header);
    }

    @Override
    public TableRow createObjectTableRow(GWTProxyProperty property) {
        return new PropertyTableRow(property);
    }
}
