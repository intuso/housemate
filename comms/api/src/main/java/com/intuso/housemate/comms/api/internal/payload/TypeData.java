package com.intuso.housemate.comms.api.internal.payload;

import com.intuso.housemate.comms.api.internal.Message;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base data object for a type
 *
 * @param <CHILD_DATA> the type of the child data objects
 */
public abstract class TypeData<CHILD_DATA extends HousemateData<?>>
        extends HousemateData<CHILD_DATA> {

    private int minValues;
    private int maxValues;

    protected TypeData() {}

    /**
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     */
    public TypeData(String id, String name, String description, int minValues, int maxValues) {
        super(id, name, description);
        this.minValues = minValues;
        this.maxValues = maxValues;
    }

    /**
     * Get the minimum number of values an instance of this type should have
     * @return the minimum number of values an instance of this type should have
     */
    public int getMinValues() {
        return minValues;
    }

    public void setMinValues(int minValues) {
        this.minValues = minValues;
    }

    /**
     * Get the maximum number of values an instance of this type should have
     * @return the maximum number of values an instance of this type should have
     */
    public int getMaxValues() {
        return maxValues;
    }

    public void setMaxValues(int maxValues) {
        this.maxValues = maxValues;
    }

    public static class TypeInstancePayload implements Message.Payload {

        private TypeInstance typeInstance;

        public TypeInstancePayload() {}

        public TypeInstancePayload(TypeInstance typeInstance) {
            this.typeInstance = typeInstance;
        }

        public TypeInstance getTypeInstance() {
            return typeInstance;
        }

        public void setTypeInstance(TypeInstance typeInstance) {
            this.typeInstance = typeInstance;
        }

        @Override
        public void ensureSerialisable() {
            ensureSerialisable(typeInstance);
        }

        public static void ensureSerialisable(TypeInstance typeInstance) {
            if(typeInstance.getChildValues() != null)
                TypeInstanceMapPayload.ensureSerialisable(typeInstance.getChildValues());
        }
    }

    public static class TypeInstancesPayload implements Message.Payload {

        private TypeInstances typeInstances;

        public TypeInstancesPayload() {}

        public TypeInstancesPayload(TypeInstances typeInstances) {
            this.typeInstances = typeInstances;
        }

        public TypeInstances getTypeInstances() {
            return typeInstances;
        }

        public void setTypeInstances(TypeInstances typeInstances) {
            this.typeInstances = typeInstances;
        }

        @Override
        public void ensureSerialisable() {
            ensureSerialisable(typeInstances);
        }

        public static void ensureSerialisable(TypeInstances typeInstances) {
            if(typeInstances.getElements() != null && !(typeInstances.getElements() instanceof ArrayList))
                typeInstances.setElements(new ArrayList<>(typeInstances.getElements()));
            if(typeInstances.getElements() != null)
                for(TypeInstance element : typeInstances.getElements())
                    if(element != null)
                        TypeInstancePayload.ensureSerialisable(element);
        }
    }

    public static class TypeInstanceMapPayload implements Message.Payload {

        private TypeInstanceMap typeInstanceMap;

        public TypeInstanceMapPayload() {}

        public TypeInstanceMapPayload(TypeInstanceMap typeInstanceMap) {
            this.typeInstanceMap = typeInstanceMap;
        }

        public TypeInstanceMap getTypeInstanceMap() {
            return typeInstanceMap;
        }

        public void setTypeInstanceMap(TypeInstanceMap typeInstanceMap) {
            this.typeInstanceMap = typeInstanceMap;
        }

        @Override
        public void ensureSerialisable() {
            ensureSerialisable(typeInstanceMap);
        }

        public static void ensureSerialisable(TypeInstanceMap typeInstanceMap) {
            if(typeInstanceMap.getChildren() != null && !(typeInstanceMap.getChildren() instanceof HashMap))
                typeInstanceMap.setChildren(new HashMap<>(typeInstanceMap.getChildren()));
            if(typeInstanceMap.getChildren() != null)
                for(TypeInstances child : typeInstanceMap.getChildren().values())
                    if(child != null)
                        TypeInstancesPayload.ensureSerialisable(child);
        }
    }
}
