package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;

public class CommonOperators {

    private CommonOperators() {}

    @TypeInfo(id = "add", name = "Add", description = "Adds the values together")
    public interface Add<I, O> extends Operator<I, O> {}

    @TypeInfo(id = "subtract", name = "Subtract", description = "Subtracts the second value fromthe first")
    public interface Subtract<I, O> extends Operator<I, O> {}

    @TypeInfo(id = "multiply", name = "Multiply", description = "Multiplies both values together")
    public interface Multiply<I, O> extends Operator<I, O> {}

    @TypeInfo(id = "divide", name = "Divide", description = "Divides the first number by the second")
    public interface Divide<I, O> extends Operator<I, O> {}

    @TypeInfo(id = "maximum", name = "Maximum", description = "Takes the maximum of the two values")
    public interface Maximum<I, O> extends Operator<I, O> {}

    @TypeInfo(id = "minimum", name = "Minimum", description = "Takes the minimum of the two values")
    public interface Minimum<I, O> extends Operator<I, O> {}
}