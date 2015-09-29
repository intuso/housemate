package com.intuso.housemate.persistence.api.bridge.v1_0;

import com.google.inject.Inject;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstanceMapMapper;
import com.intuso.housemate.object.api.bridge.v1_0.TypeInstancesMapper;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.internal.Persistence;

import java.util.Set;

/**
 * Created by tomc on 25/09/15.
 */
public class PersistenceV1_0ReverseBridge implements Persistence {

    private final com.intuso.housemate.persistence.v1_0.api.Persistence persistence;
    private final TypeInstancesMapper typeInstancesMapper;
    private final TypeInstanceMapMapper typeInstanceMapMapper;

    @Inject
    public PersistenceV1_0ReverseBridge(com.intuso.housemate.persistence.v1_0.api.Persistence persistence, TypeInstancesMapper typeInstancesMapper, TypeInstanceMapMapper typeInstanceMapMapper) {
        this.persistence = persistence;
        this.typeInstancesMapper = typeInstancesMapper;
        this.typeInstanceMapMapper = typeInstanceMapMapper;
    }

    @Override
    public TypeInstances getTypeInstances(String[] path) throws DetailsNotFoundException {
        try {
            return typeInstancesMapper.map(persistence.getTypeInstances(path));
        } catch(com.intuso.housemate.persistence.v1_0.api.DetailsNotFoundException e) {
            throw new DetailsNotFoundException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void saveTypeInstances(String[] path, TypeInstances instances) {
        persistence.saveTypeInstances(path, typeInstancesMapper.map(instances));
    }

    @Override
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException {
        try {
            return persistence.getValuesKeys(path);
        } catch (com.intuso.housemate.persistence.v1_0.api.DetailsNotFoundException e) {
            throw new DetailsNotFoundException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public TypeInstanceMap getValues(String[] path) throws DetailsNotFoundException {
        try {
            return typeInstanceMapMapper.map(persistence.getValues(path));
        } catch (com.intuso.housemate.persistence.v1_0.api.DetailsNotFoundException e) {
            throw new DetailsNotFoundException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public TypeInstanceMap getOrCreateValues(String[] path) {
        return typeInstanceMapMapper.map(persistence.getOrCreateValues(path));
    }

    @Override
    public void saveValues(String[] path, TypeInstanceMap details) {
        persistence.saveValues(path, typeInstanceMapMapper.map(details));
    }

    @Override
    public void removeValues(String[] path) {
        persistence.removeValues(path);
    }
}
