package com.intuso.housemate.broker.storage;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;

import java.util.Set;

/**
 */
public interface Storage {
    public TypeInstance getValue(String[] path) throws DetailsNotFoundException, HousemateException;
    public void saveValue(String[] path, TypeInstance value) throws HousemateException;
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException, HousemateException;
    public TypeInstances getValues(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException;
    public void saveValues(String[] path, String detailsKey, TypeInstances details) throws HousemateException;
    public void removeValues(String[] path) throws HousemateException;
}
