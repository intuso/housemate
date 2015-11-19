package com.intuso.housemate.comms.api.internal.access;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 04/02/14
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationDetails implements Serializable {

    private static final long serialVersionUID = -1L;

    private String applicationId;
    private String applicationName;
    private String applicationDescription;

    private ApplicationDetails() {}

    public ApplicationDetails(String applicationId, String applicationName, String applicationDescription) {
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.applicationDescription = applicationDescription;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }
}
