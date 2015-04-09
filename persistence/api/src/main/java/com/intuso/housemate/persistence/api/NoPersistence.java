package com.intuso.housemate.persistence.api;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 28/03/14
 * Time: 08:10
 * To change this template use File | Settings | File Templates.
 */
public class NoPersistence implements Persistence {

    @Override
    public TypeInstances getTypeInstances(String[] path) throws DetailsNotFoundException, HousemateException {
        throw new DetailsNotFoundException();
    }

    @Override
    public void saveTypeInstances(String[] path, TypeInstances instances) throws HousemateException {
        // do nothing
    }

    @Override
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException, HousemateException {
        throw new DetailsNotFoundException();
    }

    @Override
    public TypeInstanceMap getValues(String[] path) throws DetailsNotFoundException, HousemateException {
        throw new DetailsNotFoundException();
    }

    @Override
    public TypeInstanceMap getOrCreateValues(String[] path) throws HousemateException {
        return new TypeInstanceMap();
    }

    @Override
    public void saveValues(String[] path, TypeInstanceMap details) throws HousemateException {
        // do nothing
    }

    @Override
    public void removeValues(String[] path) throws HousemateException {
        // do nothing
    }
}
