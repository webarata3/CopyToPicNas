package link.webarata3.dro.copypictonas.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FileSizeIntentService extends IntentService {
    private static final String TAG = FileSizeIntentService.class.getSimpleName();

    public FileSizeIntentService() {
        super(FileSizeIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");
        Objects.requireNonNull(intent);

        ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();

        for (int i = 0; i < 100; i++) {
            try {
                bundle.putInt("progress", i + 1);
                receiver.send(1, bundle);

                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Log.d(TAG, "InterruptedException");
            }
        }
        Log.d(TAG, "Service Stopping!");
        stopSelf();
    }
}
