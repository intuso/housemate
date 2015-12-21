package com.intuso.housemate.extension.android.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.intuso.housemate.client.v1_0.proxy.api.LoadManager;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyClientHelper;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRoot;
import com.intuso.housemate.comms.v1_0.api.access.ApplicationDetails;
import com.intuso.housemate.object.v1_0.api.Application;
import com.intuso.housemate.object.v1_0.api.ApplicationInstance;
import com.intuso.housemate.object.v1_0.api.BaseHousemateObject;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.platform.android.app.HousemateService;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.housemate.platform.android.app.object.AndroidProxyRoot;

import java.util.List;

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
            new CommandPerformer(intent.getData().getPath(),
                    new ProxyClientHelper<>(new AndroidProxyRoot(getLogger(), getListenersFactory(), getProperties(), getRouter()), getRouter())
                            .applicationDetails(APPLICATION_DETAILS)
                            .component(NFCService.class.getName()),
                    startId).start();
        }
        return result;
    }

    private class CommandPerformer implements ProxyRoot.Listener<AndroidProxyRoot>, LoadManager.Callback, Command.PerformListener<AndroidProxyCommand> {

        private final String objectPath;
        private final ProxyClientHelper<AndroidProxyRoot> clientHelper;
        private final int startId;

        private CommandPerformer(String objectPath, ProxyClientHelper<AndroidProxyRoot> clientHelper, int startId) {
            this.objectPath = objectPath;
            this.clientHelper = clientHelper.load(objectPath.substring(1).split("/"))
                    .callback(this);
            clientHelper.getRoot().addObjectListener(this);
            this.startId = startId;
        }

        public void start() {
            clientHelper.start();
        }

        @Override
        public void applicationStatusChanged(AndroidProxyRoot root, Application.Status applicationStatus) {
            // do nothing
        }

        @Override
        public void applicationInstanceStatusChanged(AndroidProxyRoot root, ApplicationInstance.Status applicationInstanceStatus) {
            if(applicationInstanceStatus == ApplicationInstance.Status.Pending) {
                message("Please grant access to the application", false);
                finished();
            }
        }

        @Override
        public void newApplicationInstance(AndroidProxyRoot root, String instanceId) {
            // do nothing
        }

        @Override
        public void failed(List<String> errors) {
            message("Failed to load scanned tag's path", false);
            finished();
        }

        @Override
        public void succeeded() {
            final BaseHousemateObject<?> object = clientHelper.getRoot().findObject(objectPath.substring(1).split("/"));
            if (object == null) {
                message("Scanned tag's path does not exist", false);
                finished();
            } else if (!(object instanceof AndroidProxyCommand)) {
                message("Scanned tag's path is not a command", false);
                finished();
            } else {
                AndroidProxyCommand command = (AndroidProxyCommand) object;
                command.perform(this);
            }
        }

        @Override
         public void commandStarted(AndroidProxyCommand command) {
            // do nothing
        }

        @Override
        public void commandFinished(AndroidProxyCommand command) {
            message("Performed command " + command.getName(), true);
            finished();
        }

        @Override
        public void commandFailed(AndroidProxyCommand command, String error) {
            message("Performed command " + command.getName() + " failed: " + error, false);
            finished();
        }

        protected void message(String message, boolean isShort) {
            Toast.makeText(getApplicationContext(), message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
        }

        protected void finished() {
            clientHelper.stop();
            NFCService.this.stopSelf(startId);
        }
    }
}
