package com.intuso.housemate.web.client.bootstrap.widget.table;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 */
public class TableCell extends ComplexPanel {

    private TableCellElement td;

    public TableCell() {
        Document doc = Document.get();
        td = doc.createTDElement();
        setElement(td);
    }

    @Override
    public void add(Widget child) {
        add(child, (Element) td.cast());
    }
}
