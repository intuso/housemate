package com.intuso.housemate.client.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.intuso.housemate.client.v1_0.api.object.Type;

/**
 * Created by tomc on 29/09/15.
 */
public interface TypeInstanceMapper {

    Function<com.intuso.housemate.client.api.internal.object.Type.Instance, Type.Instance> getToV1_0Function();
    Function<Type.Instance, com.intuso.housemate.client.api.internal.object.Type.Instance> getFromV1_0Function();
    com.intuso.housemate.client.api.internal.object.Type.Instance map(Type.Instance typeInstance);
    Type.Instance map(com.intuso.housemate.client.api.internal.object.Type.Instance typeInstance);

    class Impl implements TypeInstanceMapper {

        private final Function<com.intuso.housemate.client.api.internal.object.Type.Instance, Type.Instance> toV1_0Function = new Function<com.intuso.housemate.client.api.internal.object.Type.Instance, Type.Instance>() {
            @Override
            public Type.Instance apply(com.intuso.housemate.client.api.internal.object.Type.Instance typeInstance) {
                return map(typeInstance);
            }
        };

        private final Function<Type.Instance, com.intuso.housemate.client.api.internal.object.Type.Instance> fromV1_0Function = new Function<Type.Instance, com.intuso.housemate.client.api.internal.object.Type.Instance>() {
            @Override
            public com.intuso.housemate.client.api.internal.object.Type.Instance apply(Type.Instance typeInstance) {
                return map(typeInstance);
            }
        };

        private final TypeInstanceMapMapper typeInstanceMapMapper;

        @Inject
        public Impl(TypeInstanceMapMapper typeInstanceMapMapper) {
            this.typeInstanceMapMapper = typeInstanceMapMapper;
        }

        @Override
        public Function<com.intuso.housemate.client.api.internal.object.Type.Instance, Type.Instance> getToV1_0Function() {
            return toV1_0Function;
        }

        @Override
        public Function<Type.Instance, com.intuso.housemate.client.api.internal.object.Type.Instance> getFromV1_0Function() {
            return fromV1_0Function;
        }

        @Override
        public com.intuso.housemate.client.api.internal.object.Type.Instance map(Type.Instance typeInstance) {
            if(typeInstance == null)
                return null;
            return new com.intuso.housemate.client.api.internal.object.Type.Instance(typeInstance.getValue(), typeInstanceMapMapper.map(typeInstance.getChildValues()));
        }

        @Override
        public Type.Instance map(com.intuso.housemate.client.api.internal.object.Type.Instance typeInstance) {
            if(typeInstance == null)
                return null;
            return new Type.Instance(typeInstance.getValue(), typeInstanceMapMapper.map(typeInstance.getChildValues()));
        }
    }
}
