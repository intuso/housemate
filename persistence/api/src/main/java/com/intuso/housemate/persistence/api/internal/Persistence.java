package com.intuso.housemate.persistence.api.internal;

import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;

import java.util.Set;

/**
 */
public interface Persistence {
    TypeInstances getTypeInstances(String[] path) throws DetailsNotFoundException;
    void saveTypeInstances(String[] path, TypeInstances instances) ;
    Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException;
    TypeInstanceMap getValues(String[] path) throws DetailsNotFoundException;
    TypeInstanceMap getOrCreateValues(String[] path) ;
    void saveValues(String[] path, TypeInstanceMap details);
    void removeValues(String[] path);
}
