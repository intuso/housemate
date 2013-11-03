package com.intuso.housemate.platform.android.app;

import android.app.Activity;
import android.os.Bundle;
import com.intuso.housemate.api.resources.ClientResources;

public class HousemateActivity extends Activity {

    private AndroidAppResources resources;

    public ClientResources getHousemateResources() {
        return resources;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = new AndroidAppResources(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resources.destroy();
    }
}
