package com.intuso.housemate.api.object.root.real;

import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.HasDevices;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.api.object.root.RootListener;
import com.intuso.housemate.api.object.type.HasTypes;
import com.intuso.housemate.api.object.type.Type;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
public interface RealRoot<T extends Type,
            TL extends List<T>,
            D extends Device<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>,
            DL extends List<D>,
            R extends RealRoot<T, TL, D, DL, R>>
        extends Root<R, RootListener<? super R>>, HasTypes<TL>, HasDevices<DL> {

    public void addType(T type);
    public void removeType(String name);
    public void addDevice(D device);
    public void removeDevice(String name);

}
