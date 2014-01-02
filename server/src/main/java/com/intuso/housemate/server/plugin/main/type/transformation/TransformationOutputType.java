package com.intuso.housemate.server.plugin.main.type.transformation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.RealChoiceType;

/**
 */
@Singleton
public class TransformationOutputType extends RealChoiceType<String> {

    public final static String ID = "output-type";
    public final static String NAME = "Output Type";
    public final static String DESCRIPTION = "The output type of the transformation";

    private final TypeSerialiser<String> serialiser;

    @Inject
    public TransformationOutputType(final RealResources realResources, RealList<TypeData<?>, RealType<?, ?, ?>> types,
                                    TypeSerialiser<String> serialiser) {
        super(realResources, ID, NAME, DESCRIPTION, 1, 1);
        this.serialiser = serialiser;
        types.addObjectListener(new ListListener<RealType<?, ?, ?>>() {
            @Override
            public void elementAdded(RealType<?, ?, ?> type) {
                getOptions().add(new RealOption(realResources, type.getId(), type.getName(), type.getDescription()));
            }

            @Override
            public void elementRemoved(RealType<?, ?, ?> element) {
                getOptions().remove(element.getId());
            }
        });
    }

    @Override
    public TypeInstance serialise(String o) {
        return serialiser.serialise(o);
    }

    @Override
    public String deserialise(TypeInstance instance) {
        return serialiser.deserialise(instance);
    }

    public final static class Serialiser implements TypeSerialiser<String> {
        @Override
        public TypeInstance serialise(String typeId) {
            return typeId != null ? new TypeInstance(typeId) : null;
        }

        @Override
        public String deserialise(TypeInstance instance) {
            return instance != null ? instance.getValue() : null;
        }
    }
}
