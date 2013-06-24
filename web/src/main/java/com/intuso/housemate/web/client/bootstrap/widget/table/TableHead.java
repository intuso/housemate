package com.intuso.housemate.web.client.bootstrap.widget.table;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 */
public class TableHead extends ComplexPanel {

    private TableCellElement th;

    public TableHead() {
        Document doc = Document.get();
        th = doc.createTHElement();
        setElement(th);
    }

    public TableHead(String title) {
        this();
        getElement().setInnerText(title);
    }

    @Override
    public void add(Widget child) {
        add(child, (Element) th.cast());
    }
}
