package com.intuso.housemate.extension.android.nfc;

import android.nfc.NfcAdapter;
import android.widget.TextView;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.simple.ProxyClientHelper;
import com.intuso.housemate.platform.android.app.HousemateActivity;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 25/02/14
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class NfcActivity extends HousemateActivity {

    public final static ApplicationDetails APPLICATION_DETAILS
            = new ApplicationDetails(NfcActivity.class.getName(), "Android NFC", "Android NFC Service");

    private ProxyClientHelper<AndroidProxyRoot> clientHelper;

    @Override
    public void onStart() {
        super.onStart();
        setContentView(R.layout.main);
        setMessage("NfcActivity started");
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            final String objectPath = getIntent().getData().getPath();
            setMessage("Tag scanned, path is "+ getIntent().getData().getPath());
            clientHelper = ProxyClientHelper.newClientHelper(getLog(),
                    new AndroidProxyRoot(getLog(), getListenersFactory(), getProperties(), getRouter()), getRouter());
            clientHelper.applicationDetails(APPLICATION_DETAILS)
                    .load(objectPath.substring(1).split("/"))
                    .callback(new LoadManager.Callback() {
                        @Override
                        public void failed(HousemateObject.TreeLoadInfo path) {
                            setMessage("Failed to load");
                        }

                        @Override
                        public void allLoaded() {
                            final HousemateObject<?, ?, ?, ?> object = clientHelper.getRoot().getObject(objectPath.substring(1).split("/"));
                            if(object == null)
                                setMessage(objectPath + " does not exist");
                            if (!(object instanceof AndroidProxyCommand)) {
                                setMessage(objectPath + " is not a command");
                            } else {
                                AndroidProxyCommand command = (AndroidProxyCommand) object;
                                command.perform(new CommandPerformListener<AndroidProxyCommand>() {
                                    @Override
                                    public void commandStarted(AndroidProxyCommand command) {
                                        setMessage("Performing");
                                    }

                                    @Override
                                    public void commandFinished(AndroidProxyCommand command) {
                                        setMessage("Performed");
                                    }

                                    @Override
                                    public void commandFailed(AndroidProxyCommand command, String error) {
                                        setMessage("Perform failed: " + error);
                                    }
                                });
                            }
                        }
                    })
                    .start();
        }
    }

    @Override
    protected void onStop() {
        if(clientHelper != null) {
            clientHelper.stop();
            clientHelper = null;
        }
        super.onStop();
    }

    private void setMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.onLabel)).setText(message);
            }
        });
    }
}
