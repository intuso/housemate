package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.MultiChoiceTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.object.type.option.HasOptions;
import com.intuso.housemate.api.object.type.option.OptionWrappable;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealMultiChoiceType<O>
        extends RealType<MultiChoiceTypeWrappable, ListWrappable<OptionWrappable>, Set<O>>
        implements HasOptions<RealList<OptionWrappable, RealOption>> {

    public final static String OPTIONS = "options";

    private RealList<OptionWrappable, RealOption> options;

    protected RealMultiChoiceType(RealResources resources, String id, String name, String description, List<RealOption> options) {
        super(resources, new MultiChoiceTypeWrappable(id, name, description));
        this.options = new RealList<OptionWrappable, RealOption>(resources, OPTIONS, OPTIONS, "The options for the choice", options);
        addWrapper(this.options);
    }

    @Override
    public RealList<OptionWrappable, RealOption> getOptions() {
        return options;
    }

    protected static class MultiChoiceSerialiser<O> implements TypeSerialiser<Set<O>> {

        private final TypeSerialiser<O> elementSerialiser;

        protected MultiChoiceSerialiser(TypeSerialiser<O> elementSerialiser) {
            this.elementSerialiser = elementSerialiser;
        }

        @Override
        public TypeInstance serialise(Set<O> os) {
            TypeInstances children = new TypeInstances();
            for(O o : os) {
                TypeInstance typeValue = elementSerialiser.serialise(o);
                children.put(typeValue.getValue(), typeValue);
            }
            return new TypeInstance("", children);
        }

        @Override
        public Set<O> deserialise(TypeInstance value) {
            if(value == null || value.getChildValues() == null)
                return null;
            Set<O> result = new HashSet<O>();
            for(TypeInstance typeValue : value.getChildValues().values())
                result.add(elementSerialiser.deserialise(typeValue));
            return result;
        }
    }
}
