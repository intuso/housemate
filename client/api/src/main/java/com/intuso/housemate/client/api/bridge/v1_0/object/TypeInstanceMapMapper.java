package com.intuso.housemate.client.api.bridge.v1_0.object;

import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Type;

/**
 * Created by tomc on 29/09/15.
 *
 * An interface because they type instance(s)(map) all inject each other -> circular dependency
 */
public interface TypeInstanceMapMapper {

    com.intuso.housemate.client.api.internal.object.Type.InstanceMap map(Type.InstanceMap typeInstanceMap);
    Type.InstanceMap map(com.intuso.housemate.client.api.internal.object.Type.InstanceMap typeInstanceMap);

    class Impl implements TypeInstanceMapMapper {

        private final TypeInstancesMapper typeInstancesMapper;

        @Inject
        public Impl(TypeInstancesMapper typeInstancesMapper) {
            this.typeInstancesMapper = typeInstancesMapper;
        }

        @Override
        public com.intuso.housemate.client.api.internal.object.Type.InstanceMap map(Type.InstanceMap typeInstanceMap) {
            if(typeInstanceMap == null)
                return null;
            com.intuso.housemate.client.api.internal.object.Type.InstanceMap result = new com.intuso.housemate.client.api.internal.object.Type.InstanceMap();
            for(String key : typeInstanceMap.getChildren().keySet())
                result.getChildren().put(key, typeInstancesMapper.map(typeInstanceMap.getChildren().get(key)));
            return result;
        }

        @Override
        public Type.InstanceMap map(com.intuso.housemate.client.api.internal.object.Type.InstanceMap typeInstanceMap) {
            if(typeInstanceMap == null)
                return null;
            Type.InstanceMap result = new Type.InstanceMap();
            for(String key : typeInstanceMap.getChildren().keySet())
                result.getChildren().put(key, typeInstancesMapper.map(typeInstanceMap.getChildren().get(key)));
            return result;
        }
    }
}
