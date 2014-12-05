package com.intuso.housemate.extension.android.nfc;

import android.app.Activity;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 25/02/14
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class NfcActivity extends Activity {

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
