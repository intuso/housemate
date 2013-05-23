package com.intuso.housemate.api.object.type;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.option.OptionWrappable;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public final class MultiChoiceTypeWrappable extends TypeWrappable<ListWrappable<OptionWrappable>> {

    public final static Joiner JOINER = Joiner.on(",");
    public final static Splitter SPLITTER = Splitter.on(",");

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

    private MultiChoiceTypeWrappable() {}

    public MultiChoiceTypeWrappable(String id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new MultiChoiceTypeWrappable(getId(), getName(), getDescription());
    }
}
