package link.webarata3.dro.copypictonas.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class FileSizeResultReceiver extends ResultReceiver {

    private Receiver receiver;

    public FileSizeResultReceiver(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        public void onFileSizeResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onFileSizeResult(resultCode, resultData);
        }
    }
}

