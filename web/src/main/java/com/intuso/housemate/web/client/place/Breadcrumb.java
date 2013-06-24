package com.intuso.housemate.web.client.place;

/**
 */
public class Breadcrumb {

    private String label;
    private String href;

    public Breadcrumb() {
    }

    public Breadcrumb(String label, String href) {
        this.label = label;
        this.href = href;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
