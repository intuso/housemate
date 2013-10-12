package com.intuso.housemate.web.resources;

import com.github.gwtbootstrap.client.ui.resources.Resources;
import com.google.gwt.resources.client.TextResource;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 08/10/13
 * Time: 23:46
 * To change this template use File | Settings | File Templates.
 */
public interface BootstrapMinResources extends Resources {

    @Source({"css/intuso-bootstrap.min.css"})
    TextResource bootstrapCss();

    @Source({"css/intuso-bootstrap.min.css"})
    TextResource bootstrapResponsiveCss();
}
