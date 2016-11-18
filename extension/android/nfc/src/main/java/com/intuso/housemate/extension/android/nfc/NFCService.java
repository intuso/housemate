package com.intuso.housemate.extension.android.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.platform.android.app.HousemateService;
import com.intuso.housemate.platform.android.app.object.AndroidObjectFactories;
import com.intuso.housemate.platform.android.app.object.AndroidProxyCommand;
import com.intuso.housemate.platform.android.app.object.AndroidProxyServer;

/**
 * Created by tomc on 09/05/14.
 */
public class NFCService extends HousemateService {

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            new CommandPerformer(intent.getData().getPath(),
                    new AndroidProxyServer(getLogger(), getListenersFactory(), new AndroidObjectFactories(getListenersFactory()), getConnection()),
                    startId);
        }
        return result;
    }

    private class CommandPerformer implements Command.PerformListener<AndroidProxyCommand> {

        private final String objectPath;
        private final AndroidProxyServer server;
        private final int startId;

        private CommandPerformer(String objectPath, AndroidProxyServer server, int startId) {
            this.objectPath = objectPath;
            this.server = server;
            this.startId = startId;
            final Object<?> object = null;// todo server.findObject(objectPath.substring(1).split("/"));
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
            NFCService.this.stopSelf(startId);
        }
    }
}
