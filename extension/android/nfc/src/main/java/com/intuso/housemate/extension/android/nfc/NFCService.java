package com.intuso.housemate.extension.android.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.intuso.housemate.client.v1_0.api.object.Command;
import com.intuso.housemate.platform.android.app.HousemateService;
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
                    createServer(getLogger()),
                    startId);
        }
        return result;
    }

    private class CommandPerformer implements Command.PerformListener<AndroidProxyCommand> {

        private final int startId;

        private CommandPerformer(String objectPath, AndroidProxyServer server, int startId) {
            this.startId = startId;
            final AndroidProxyCommand command = server.find(objectPath.substring(1).split("/"), false);
            if (command == null) {
                message("Scanned tag's path does not exist", false);
                finished();
            } else
                command.perform(this);
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
