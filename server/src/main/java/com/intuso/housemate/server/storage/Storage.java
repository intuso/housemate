package com.intuso.housemate.server.storage;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;

import java.util.Set;

/**
 */
public interface Storage {
    public TypeInstances getTypeInstances(String[] path) throws DetailsNotFoundException, HousemateException;
    public void saveTypeInstances(String[] path, TypeInstances instances) throws HousemateException;
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException, HousemateException;
    public TypeInstanceMap getValues(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException;
    public void saveValues(String[] path, String detailsKey, TypeInstanceMap details) throws HousemateException;
    public void removeValues(String[] path) throws HousemateException;

    void saveValues(String[] path, TypeInstanceMap details) throws HousemateException;
}
