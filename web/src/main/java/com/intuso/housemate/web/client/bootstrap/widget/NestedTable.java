package com.intuso.housemate.web.client.bootstrap.widget;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;

import java.util.List;

/**
 * Created by tomc on 06/03/15.
 */
public class NestedTable extends Composite {

    interface NestedTableUiBinder extends UiBinder<Widget, NestedTable> {}

    private static NestedTableUiBinder ourUiBinder = GWT.create(NestedTableUiBinder.class);

    @UiField
    Heading heading;
    @UiField
    protected Grid grid;

    private final List<RowNum> rowNums = Lists.newArrayList();
    private boolean headerAdded = false;
    private int nextRow = 0;

    public NestedTable() {
        initWidget(ourUiBinder.createAndBindUi(this));
        grid.resizeColumns(2);
    }

    public void setHeader(String header) {
        ensureHeaderSpace();
        if(header != null)
            grid.setWidget(0, 0, new Heading(HeadingSize.H4, header));
    }

    public void setHeaderWidget(IsWidget widget) {
        ensureHeaderSpace();
        if(widget != null) {
            widget.asWidget().addStyleName("pull-right");
            grid.setWidget(0, 1, widget);
        }
    }

    public void setHeader(String header, IsWidget widget) {
        setHeader(header);
        setHeaderWidget(widget);
    }

    private void ensureHeaderSpace() {
        if(!headerAdded) {
            grid.insertRow(0);
            nextRow++;
            for(RowNum rowNum : rowNums)
                rowNum.increment();
            headerAdded = true;
        }
    }

    public RowNum addRow(String name, IsWidget widget) {
        grid.resizeRows(nextRow + 1);
        if(name != null)
            grid.setText(nextRow, 0, name);
        if(widget != null) {
            widget.asWidget().addStyleName("pull-right");
            grid.setWidget(nextRow, 1, widget);
        }
        RowNum result = new RowNum(nextRow++);
        rowNums.add(result);
        return result;
    }

    protected class RowNum {

        private int row;

        private RowNum(int row) {
            this.row = row;
        }

        public int getRow() {
            return row;
        }

        public void increment() {
            row++;
        }

        public void decrement() {
            row--;
        }
    }
}
