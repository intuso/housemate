package com.intuso.housemate.object.server.real;

import com.intuso.housemate.api.object.NoChildrenData;
import com.intuso.housemate.api.object.ObjectListener;
import com.intuso.utilities.log.Log;

public class NoChildrenServerRealObject extends ServerRealObject<NoChildrenData, NoChildrenData,
        NoChildrenServerRealObject, ObjectListener> {
    private NoChildrenServerRealObject(Log log, NoChildrenData data) {
        super(log, data);
    }
}
