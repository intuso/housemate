package com.intuso.housemate.object.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.object.api.internal.TypeInstance;

/**
 * Created by tomc on 29/09/15.
 */
public interface TypeInstanceMapper {

    Function<TypeInstance, com.intuso.housemate.object.v1_0.api.TypeInstance> getToV1_0Function();
    Function<com.intuso.housemate.object.v1_0.api.TypeInstance, TypeInstance> getFromV1_0Function();
    TypeInstance map(com.intuso.housemate.object.v1_0.api.TypeInstance typeInstance);
    com.intuso.housemate.object.v1_0.api.TypeInstance map(TypeInstance typeInstance);

    class Impl implements TypeInstanceMapper {

        private final Function<TypeInstance, com.intuso.housemate.object.v1_0.api.TypeInstance> toV1_0Function = new Function<TypeInstance, com.intuso.housemate.object.v1_0.api.TypeInstance>() {
            @Override
            public com.intuso.housemate.object.v1_0.api.TypeInstance apply(TypeInstance typeInstance) {
                return map(typeInstance);
            }
        };

        private final Function<com.intuso.housemate.object.v1_0.api.TypeInstance, TypeInstance> fromV1_0Function = new Function<com.intuso.housemate.object.v1_0.api.TypeInstance, TypeInstance>() {
            @Override
            public TypeInstance apply(com.intuso.housemate.object.v1_0.api.TypeInstance typeInstance) {
                return map(typeInstance);
            }
        };

        private final TypeInstanceMapMapper typeInstanceMapMapper;

        @Inject
        public Impl(TypeInstanceMapMapper typeInstanceMapMapper) {
            this.typeInstanceMapMapper = typeInstanceMapMapper;
        }

        @Override
        public Function<TypeInstance, com.intuso.housemate.object.v1_0.api.TypeInstance> getToV1_0Function() {
            return toV1_0Function;
        }

        @Override
        public Function<com.intuso.housemate.object.v1_0.api.TypeInstance, TypeInstance> getFromV1_0Function() {
            return fromV1_0Function;
        }

        @Override
        public TypeInstance map(com.intuso.housemate.object.v1_0.api.TypeInstance typeInstance) {
            if(typeInstance == null)
                return null;
            return new TypeInstance(typeInstance.getValue(), typeInstanceMapMapper.map(typeInstance.getChildValues()));
        }

        @Override
        public com.intuso.housemate.object.v1_0.api.TypeInstance map(TypeInstance typeInstance) {
            if(typeInstance == null)
                return null;
            return new com.intuso.housemate.object.v1_0.api.TypeInstance(typeInstance.getValue(), typeInstanceMapMapper.map(typeInstance.getChildValues()));
        }
    }
}
