package com.intuso.housemate.core.object.consequence;

import com.intuso.housemate.core.object.list.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 01/06/12
 * Time: 01:08
 * To change this template use File | Settings | File Templates.
 */
public interface HasConsequences<L extends List<? extends Consequence<?, ?, ?, ?, ?>>> {
    public L getConsequences();
}
