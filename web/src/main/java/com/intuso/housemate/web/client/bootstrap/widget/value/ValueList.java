package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.dom.client.Style;
import com.intuso.housemate.web.client.bootstrap.widget.list.TableList;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableCol;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableColGroup;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableHead;
import com.intuso.housemate.web.client.bootstrap.widget.table.TableRow;
import com.intuso.housemate.web.client.object.GWTProxyValue;

/**
 */
public class ValueList extends TableList<GWTProxyValue> {

    public ValueList() {
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
        header.add(new TableHead("Value"));
        add(header);
    }

    @Override
    public TableRow createObjectTableRow(GWTProxyValue value) {
        return new ValueTableRow(value);
    }
}
