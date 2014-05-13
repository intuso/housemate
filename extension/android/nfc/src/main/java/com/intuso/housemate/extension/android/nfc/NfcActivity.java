package com.intuso.housemate.extension.android.nfc;

import android.content.Intent;
import com.intuso.housemate.object.proxy.simple.ProxyClientHelper;
import com.intuso.housemate.platform.android.app.HousemateActivity;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 25/02/14
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class NfcActivity extends HousemateActivity {

    private ProxyClientHelper<AndroidProxyRoot> clientHelper;

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getApplicationContext(), NFCService.class);
        intent.setAction(getIntent().getAction());
        intent.setData(getIntent().getData());
        getApplicationContext().startService(intent);
        finish();
    }
}
