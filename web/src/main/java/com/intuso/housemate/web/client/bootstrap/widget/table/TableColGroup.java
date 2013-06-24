package com.intuso.housemate.web.client.bootstrap.widget.table;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableColElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 */
public class TableColGroup extends ComplexPanel {

    private TableColElement colGroup;

    public TableColGroup() {
        Document doc = Document.get();
        colGroup = doc.createColGroupElement();
        setElement(colGroup);
    }

    @Override
    public void add(Widget child) {
        add(child, (Element) colGroup.cast());
    }
}
