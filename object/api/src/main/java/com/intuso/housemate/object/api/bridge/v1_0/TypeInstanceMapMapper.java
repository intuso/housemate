package com.intuso.housemate.object.api.bridge.v1_0;

import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;

/**
 * Created by tomc on 29/09/15.
 */
public interface TypeInstanceMapMapper {

    TypeInstanceMap map(com.intuso.housemate.object.v1_0.api.TypeInstanceMap typeInstanceMap);
    com.intuso.housemate.object.v1_0.api.TypeInstanceMap map(TypeInstanceMap typeInstanceMap);

    class Impl implements TypeInstanceMapMapper {

        private final TypeInstancesMapper typeInstancesMapper;

        @Inject
        public Impl(TypeInstancesMapper typeInstancesMapper) {
            this.typeInstancesMapper = typeInstancesMapper;
        }

        @Override
        public TypeInstanceMap map(com.intuso.housemate.object.v1_0.api.TypeInstanceMap typeInstanceMap) {
            if(typeInstanceMap == null)
                return null;
            TypeInstanceMap result = new TypeInstanceMap();
            for(String key : typeInstanceMap.getChildren().keySet())
                result.getChildren().put(key, typeInstancesMapper.map(typeInstanceMap.getChildren().get(key)));
            return result;
        }

        @Override
        public com.intuso.housemate.object.v1_0.api.TypeInstanceMap map(TypeInstanceMap typeInstanceMap) {
            if(typeInstanceMap == null)
                return null;
            com.intuso.housemate.object.v1_0.api.TypeInstanceMap result = new com.intuso.housemate.object.v1_0.api.TypeInstanceMap();
            for(String key : typeInstanceMap.getChildren().keySet())
                result.getChildren().put(key, typeInstancesMapper.map(typeInstanceMap.getChildren().get(key)));
            return result;
        }
    }
}
