package com.intuso.housemate.extension.android.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.intuso.housemate.api.comms.access.ApplicationDetails;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.object.proxy.simple.ProxyClientHelper;
import com.intuso.housemate.platform.android.app.HousemateService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;

/**
 * Created by tomc on 09/05/14.
 */
public class NFCService extends HousemateService {

    public final static ApplicationDetails APPLICATION_DETAILS
            = new ApplicationDetails(NfcActivity.class.getName(), "Android NFC", "Android NFC Service");

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            final String objectPath = intent.getData().getPath();
            final ProxyClientHelper<AndroidProxyRoot> clientHelper = ProxyClientHelper.newClientHelper(getLog(),
                    new AndroidProxyRoot(getLog(), getListenersFactory(), getProperties(), getRouter()), getRouter());
            clientHelper.applicationDetails(APPLICATION_DETAILS)
                    .load(objectPath.substring(1).split("/"))
                    .callback(new LoadManager.Callback() {
                        @Override
                        public void failed(HousemateObject.TreeLoadInfo path) {
                            message("Failed to load scanned tag's path", false);
                            finished(clientHelper, startId);
                        }

                        @Override
                        public void allLoaded() {
                            final HousemateObject<?, ?, ?, ?> object = clientHelper.getRoot().getObject(objectPath.substring(1).split("/"));
                            if(object == null) {
                                message("Scanned tag's path does not exist", false);
                                finished(clientHelper, startId);
                            } else if (!(object instanceof AndroidProxyCommand)) {
                                message("Scanned tag's path is not a command", false);
                                finished(clientHelper, startId);
                            } else {
                                AndroidProxyCommand command = (AndroidProxyCommand) object;
                                command.perform(new CommandPerformListener<AndroidProxyCommand>() {
                                    @Override
                                    public void commandStarted(AndroidProxyCommand command) {
                                        // do nothing
                                    }

                                    @Override
                                    public void commandFinished(AndroidProxyCommand command) {
                                        message("Performed command " + command.getName(), true);
                                        finished(clientHelper, startId);
                                    }

                                    @Override
                                    public void commandFailed(AndroidProxyCommand command, String error) {
                                        message("Performed command " + command.getName() + " failed: " + error, false);
                                        finished(clientHelper, startId);
                                    }
                                });
                            }
                        }
                    })
                    .start();
        }
        return result;
    }

    protected void message(String message, boolean isShort) {
        Toast.makeText(getApplicationContext(), message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
    }

    protected void finished(ProxyClientHelper<AndroidProxyRoot> clientHelper, int startId) {
        if(clientHelper != null)
            clientHelper.stop();
        stopSelf(startId);
    }
}
