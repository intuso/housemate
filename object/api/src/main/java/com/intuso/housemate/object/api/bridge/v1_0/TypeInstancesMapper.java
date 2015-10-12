package com.intuso.housemate.object.api.bridge.v1_0;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.TypeInstances;

/**
 * Created by tomc on 29/09/15.
 */
public interface TypeInstancesMapper {

    TypeInstances map(com.intuso.housemate.object.v1_0.api.TypeInstances typeInstances);
    com.intuso.housemate.object.v1_0.api.TypeInstances map(TypeInstances typeInstances);

    class Impl implements TypeInstancesMapper {

        private final TypeInstanceMapper typeInstanceMapper;

        @Inject
        public Impl(TypeInstanceMapper typeInstanceMapper) {
            this.typeInstanceMapper = typeInstanceMapper;
        }

        @Override
        public TypeInstances map(com.intuso.housemate.object.v1_0.api.TypeInstances typeInstances) {
            if(typeInstances == null)
                return null;
            return new TypeInstances(Lists.newArrayList(Lists.transform(typeInstances.getElements(), typeInstanceMapper.getFromV1_0Function())));
        }

        @Override
        public com.intuso.housemate.object.v1_0.api.TypeInstances map(TypeInstances typeInstances) {
            if(typeInstances == null)
                return null;
            return new com.intuso.housemate.object.v1_0.api.TypeInstances(Lists.newArrayList(Lists.transform(typeInstances.getElements(), typeInstanceMapper.getToV1_0Function())));
        }
    }
}
