package com.intuso.housemate.web.client.bootstrap.widget.table;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 */
public class TableRow extends ComplexPanel {

    private TableRowElement tr;

    public TableRow() {
        Document doc = Document.get();
        tr = doc.createTRElement();
        setElement(tr);
    }

    @Override
    public void add(Widget child) {
        add(child, (Element) tr.cast());
    }
}
