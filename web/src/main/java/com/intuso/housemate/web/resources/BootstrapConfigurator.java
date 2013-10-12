package com.intuso.housemate.web.resources;

import com.github.gwtbootstrap.client.ui.config.Configurator;
import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.core.client.GWT;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/10/13
 * Time: 22:44
 * To change this template use File | Settings | File Templates.
 */
public class BootstrapConfigurator implements Configurator {

    public Resources getResources() {
        return GWT.create(BootstrapResources.class);
    }

    public boolean hasResponsiveDesign() {
        return true;
    }
}

