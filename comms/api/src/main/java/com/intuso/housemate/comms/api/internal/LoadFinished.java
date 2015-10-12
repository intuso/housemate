package com.intuso.housemate.comms.api.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Message payload for a load response of a remote object
 */
public class LoadFinished implements Message.Payload {

    private static final long serialVersionUID = -1L;

    private String loaderId;
    private List<String> errors;

    public LoadFinished() {}

    public static LoadFinished forSuccess(String loaderName) {
        return new LoadFinished(loaderName, null);
    }

    public static LoadFinished forErrors(String loaderName, String ... errors) {
        return forErrors(loaderName, Arrays.asList(errors));
    }

    public static LoadFinished forErrors(String loaderName, List<String> errors) {
        return new LoadFinished(loaderName, errors);
    }

    public LoadFinished(String loaderId, List<String> errors) {
        this.loaderId = loaderId;
        this.errors = errors;
    }

    /**
     * Get the loader name
     * @return the loader name
     */
    public String getLoaderId() {
        return loaderId;
    }

    public void setLoaderId(String loaderId) {
        this.loaderId = loaderId;
    }

    /**
     * Get the error that occurred
     * @return the error that occurred
     */
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return loaderId + " load finished" + (errors != null ? " and failed because of " + errors.size() + " errors" : "");
    }

    @Override
    public void ensureSerialisable() {
        if(errors != null && !(errors instanceof Serializable))
            errors = new ArrayList<>(errors);
    }
}
