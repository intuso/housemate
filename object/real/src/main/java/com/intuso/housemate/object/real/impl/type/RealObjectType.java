package com.intuso.housemate.object.real.impl.type;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.BaseObject;
import com.intuso.housemate.api.object.NoChildrenWrappable;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.type.ObjectTypeWrappable;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 22/04/13
 * Time: 08:43
 * To change this template use File | Settings | File Templates.
 */
public class RealObjectType<O extends BaseObject<?>>
        extends RealType<ObjectTypeWrappable, NoChildrenWrappable, RealObjectType.Reference<O>> {

    public final static String ID = "object";
    public final static String NAME = "Object";

    public RealObjectType(RealResources resources, Root<?, ?> root) {
        super(resources, new ObjectTypeWrappable(ID, NAME, "Path to an object"), new Serialiser<O>(root));
    }

    public static class Reference<O extends BaseObject<?>> {

        private final String[] path;
        private final O object;

        public Reference(String[] path) {
            this(path, null);
        }

        public Reference(O object) {
            this(object == null ? null : object.getPath(), object);
        }

        public Reference(String[] path, O object) {
            this.path = path;
            this.object = object;
        }

        public String[] getPath() {
            return path;
        }

        public O getObject() {
            return object;
        }
    }

    private static class Serialiser<O extends BaseObject<?>> implements TypeSerialiser<Reference<O>> {

        private final Joiner joiner = Joiner.on("/");
        private final Splitter splitter = Splitter.on("/");
        
        private final Root<?, ?> root;

        private Serialiser(Root<?, ?> root) {
            this.root = root;
        }

        @Override
        public String serialise(Reference<O> o) {
            if(o == null)
                return null;
            return joiner.join(o.getPath());
        }

        @Override
        public Reference<O> deserialise(String value) {
            if(value == null)
                return new Reference<O>(null, null);
            List<String> pathList = Lists.newArrayList(splitter.split(value));
            String[] path = pathList.toArray(new String[pathList.size()]);
            return new Reference<O>(path, (O) root.getWrapper(path));
        }
    }
}
