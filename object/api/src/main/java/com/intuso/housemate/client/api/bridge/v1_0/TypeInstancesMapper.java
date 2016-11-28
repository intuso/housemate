package com.intuso.housemate.client.api.bridge.v1_0;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Type;

/**
 * Created by tomc on 29/09/15.
 */
public interface TypeInstancesMapper {

    com.intuso.housemate.client.api.internal.object.Type.Instances map(Type.Instances typeInstances);
    Type.Instances map(com.intuso.housemate.client.api.internal.object.Type.Instances typeInstances);

    class Impl implements TypeInstancesMapper {

        private final TypeInstanceMapper typeInstanceMapper;

        @Inject
        public Impl(TypeInstanceMapper typeInstanceMapper) {
            this.typeInstanceMapper = typeInstanceMapper;
        }

        @Override
        public com.intuso.housemate.client.api.internal.object.Type.Instances map(Type.Instances typeInstances) {
            if(typeInstances == null)
                return null;
            return new com.intuso.housemate.client.api.internal.object.Type.Instances(Lists.newArrayList(Lists.transform(typeInstances.getElements(), typeInstanceMapper.getFromV1_0Function())));
        }

        @Override
        public Type.Instances map(com.intuso.housemate.client.api.internal.object.Type.Instances typeInstances) {
            if(typeInstances == null)
                return null;
            return new Type.Instances(Lists.newArrayList(Lists.transform(typeInstances.getElements(), typeInstanceMapper.getToV1_0Function())));
        }
    }
}
