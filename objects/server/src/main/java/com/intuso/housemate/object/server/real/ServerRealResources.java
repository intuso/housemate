package com.intuso.housemate.object.server.real;

import com.intuso.housemate.object.server.ServerResources;
import com.intuso.housemate.object.real.RealResources;

public interface ServerRealResources extends ServerResources<ServerRealRootObject> {
    RealResources getRealResources();
}
