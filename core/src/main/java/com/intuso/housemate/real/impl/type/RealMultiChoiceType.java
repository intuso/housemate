package com.intuso.housemate.real.impl.type;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.intuso.housemate.core.object.list.ListWrappable;
import com.intuso.housemate.core.object.type.MultiChoiceTypeWrappable;
import com.intuso.housemate.core.object.type.TypeSerialiser;
import com.intuso.housemate.core.object.type.option.HasOptions;
import com.intuso.housemate.core.object.type.option.OptionWrappable;
import com.intuso.housemate.real.RealList;
import com.intuso.housemate.real.RealOption;
import com.intuso.housemate.real.RealResources;
import com.intuso.housemate.real.RealType;

import javax.annotation.Nullable;
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

    protected RealMultiChoiceType(RealResources resources, String id, String name, String description, List<RealOption> options, TypeSerialiser<O> elementSerialiser) {
        super(resources, new MultiChoiceTypeWrappable(id, name, description), new Serialiser<O>(elementSerialiser));
        this.options = new RealList<OptionWrappable, RealOption>(resources, OPTIONS, OPTIONS, "The options for the choice", options);
        addWrapper(this.options);
    }

    @Override
    public RealList<OptionWrappable, RealOption> getOptions() {
        return options;
    }

    private final static Joiner JOINER = Joiner.on(",");
    private final static Splitter SPLITTER = Splitter.on(",");

    public final static TypeSerialiser<Set<String>> SERIALISER = new TypeSerialiser<Set<String>>() {

        @Override
        public String serialise(Set<String> os) {
            return JOINER.join(os);
        }

        @Override
        public Set<String> deserialise(String value) {
            return Sets.newHashSet(SPLITTER.split(value));
        }
    };

    private static class Serialiser<O> implements TypeSerialiser<Set<O>> {

        private final TypeSerialiser<O> elementSerialiser;

        private final Function<O, String> serialiseTransformer = new Function<O, String>() {
            @Override
            public String apply(@Nullable O o) {
                return elementSerialiser.serialise(o);
            }
        };

        private final Function<String, O> deserialiseTransformer = new Function<String, O>() {
            @Override
            public O apply(@Nullable String value) {
                return elementSerialiser.deserialise(value);
            }
        };

        private Serialiser(TypeSerialiser<O> elementSerialiser) {
            this.elementSerialiser = elementSerialiser;
        }

        @Override
        public String serialise(Set<O> os) {
            return JOINER.join(Iterables.transform(os, serialiseTransformer));
        }

        @Override
        public Set<O> deserialise(String value) {
            return Sets.newHashSet(Iterables.transform(SPLITTER.split(value), deserialiseTransformer));
        }
    }
}
